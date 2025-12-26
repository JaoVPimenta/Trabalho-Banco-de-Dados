package Estudo;

import Estudo.model.Contato;
import Estudo.model.Pessoa;
import Estudo.repository.ContatoRepository;
import Estudo.repository.PessoaRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainFrame extends JFrame {

    // Repositories
    private final PessoaRepository pessoaRep = new PessoaRepository();
    private final ContatoRepository contatoRep = new ContatoRepository();

    // Estado Atual
    private Pessoa pessoaSelecionada = null; // Se null, estamos criando uma nova

    // Componentes de Texto (Pessoa)
    private JTextField txtNome;
    private JFormattedTextField txtCpf;
    private JFormattedTextField txtDataNascimento;
    private JComboBox<String> cbSexo;
    private JLabel lblIdPessoa;

    // Componentes de Contato
    private JTextField txtContatoValor;
    private JComboBox<String> cbTipoContato;
    private JTable tabelaContatos;
    private DefaultTableModel modeloContatos;
    private JButton btnAddContato;
    private JButton btnRemoveContato;

    // Componentes de Listagem (Pessoa)
    private JTable tabelaPessoas;
    private DefaultTableModel modeloPessoas;

    public MainFrame() {
        super("Cadastro de Pessoas e Contatos");
        setSize(900, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        carregarTabelaPessoas();
        limparFormulario(); // Inicia em modo de cadastro
    }

    private void initComponents() {
        // --- PAINEL SUPERIOR: DADOS DA PESSOA ---
        JPanel panelPessoa = new JPanel(new GridBagLayout());
        panelPessoa.setBorder(BorderFactory.createTitledBorder("Dados Pessoais"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID (Apenas Visualização)
        gbc.gridx = 0; gbc.gridy = 0;
        panelPessoa.add(new JLabel("ID:"), gbc);
        lblIdPessoa = new JLabel("-");
        gbc.gridx = 1;
        panelPessoa.add(lblIdPessoa, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        panelPessoa.add(new JLabel("Nome Completo:"), gbc);
        txtNome = new JTextField(20);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelPessoa.add(txtNome, gbc);

        // CPF
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
        panelPessoa.add(new JLabel("CPF:"), gbc);
        try {
            MaskFormatter maskCpf = new MaskFormatter("###.###.###-##");
            maskCpf.setPlaceholderCharacter('_');
            txtCpf = new JFormattedTextField(maskCpf);
        } catch (ParseException e) { txtCpf = new JFormattedTextField(); }
        gbc.gridx = 1;
        panelPessoa.add(txtCpf, gbc);

        // Data Nascimento
        gbc.gridx = 2; gbc.gridy = 2;
        panelPessoa.add(new JLabel("Data Nasc (dd/MM/yyyy):"), gbc);
        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            txtDataNascimento = new JFormattedTextField(maskData);
        } catch (ParseException e) { txtDataNascimento = new JFormattedTextField(); }
        gbc.gridx = 3;
        panelPessoa.add(txtDataNascimento, gbc);

        // Sexo
        gbc.gridx = 0; gbc.gridy = 3;
        panelPessoa.add(new JLabel("Gênero:"), gbc);
        cbSexo = new JComboBox<>(new String[]{"M", "F"});
        gbc.gridx = 1;
        panelPessoa.add(cbSexo, gbc);

        // Botões de Ação Principal (Salvar/Excluir/Novo)
        JPanel panelBotoesPessoa = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnNovo = new JButton("Limpar / Novo");
        JButton btnSalvar = new JButton("Salvar Pessoa");
        JButton btnExcluir = new JButton("Excluir Pessoa");

        // Estilização leve
        btnSalvar.setBackground(new Color(100, 200, 100)); // Verde claro
        btnExcluir.setBackground(new Color(255, 100, 100)); // Vermelho claro

        panelBotoesPessoa.add(btnNovo);
        panelBotoesPessoa.add(btnSalvar);
        panelBotoesPessoa.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 4;
        panelPessoa.add(panelBotoesPessoa, gbc);

        // --- PAINEL DO MEIO: CONTATOS ---
        JPanel panelContatos = new JPanel(new BorderLayout());
        panelContatos.setBorder(BorderFactory.createTitledBorder("Contatos da Pessoa"));

        // Inputs de Contato
        JPanel panelInputContato = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtContatoValor = new JTextField(20);
        cbTipoContato = new JComboBox<>(new String[]{"EMAIL", "TELEFONE", "CELULAR"});
        btnAddContato = new JButton("+ Adicionar");
        btnRemoveContato = new JButton("- Remover Selecionado");
        
        panelInputContato.add(new JLabel("Contato:"));
        panelInputContato.add(txtContatoValor);
        panelInputContato.add(new JLabel("Tipo:"));
        panelInputContato.add(cbTipoContato);
        panelInputContato.add(btnAddContato);
        panelInputContato.add(btnRemoveContato);

        // Tabela de Contatos
        String[] colunasContatos = {"ID", "Contato", "Tipo"};
        modeloContatos = new DefaultTableModel(colunasContatos, 0) {
            @Override // Bloquear edição direta na célula
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaContatos = new JTable(modeloContatos);
        tabelaContatos.getTableHeader().setBackground(new Color(200, 240, 200)); // Verde estilo Excel
        JScrollPane scrollContatos = new JScrollPane(tabelaContatos);
        scrollContatos.setPreferredSize(new Dimension(800, 150));

        panelContatos.add(panelInputContato, BorderLayout.NORTH);
        panelContatos.add(scrollContatos, BorderLayout.CENTER);

        // --- PAINEL INFERIOR: LISTA DE PESSOAS (BD) ---
        JPanel panelLista = new JPanel(new BorderLayout());
        panelLista.setBorder(BorderFactory.createTitledBorder("Pessoas Cadastradas (Clique para Editar)"));

        String[] colunasPessoas = {"ID", "Nome", "CPF", "Nascimento", "Sexo"};
        modeloPessoas = new DefaultTableModel(colunasPessoas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaPessoas = new JTable(modeloPessoas);
        JScrollPane scrollPessoas = new JScrollPane(tabelaPessoas);

        panelLista.add(scrollPessoas, BorderLayout.CENTER);

        // --- MONTAGEM FINAL NO FRAME ---
        JPanel panelCentral = new JPanel();
        panelCentral.setLayout(new BoxLayout(panelCentral, BoxLayout.Y_AXIS));
        
        panelCentral.add(panelPessoa);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(panelContatos);
        panelCentral.add(Box.createVerticalStrut(10));
        panelCentral.add(panelLista);

        add(panelCentral, BorderLayout.CENTER);

        // --- ACTION LISTENERS (Eventos) ---

        // 1. Selecionar Pessoa na Tabela Inferior
        tabelaPessoas.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = tabelaPessoas.getSelectedRow();
                    if (row != -1) {
                        int id = (int) modeloPessoas.getValueAt(row, 0);
                        carregarPessoaNoFormulario(id);
                    }
                }
            }
        });

        // 2. Botão Novo (Limpar)
        btnNovo.addActionListener(e -> limparFormulario());

        // 3. Botão Salvar
        btnSalvar.addActionListener(e -> salvarPessoa());

        // 4. Botão Excluir Pessoa
        btnExcluir.addActionListener(e -> excluirPessoa());

        // 5. Adicionar Contato
        btnAddContato.addActionListener(e -> adicionarContato());

        // 6. Remover Contato
        btnRemoveContato.addActionListener(e -> removerContato());
    }

    // --- MÉTODOS DE LÓGICA E BANCO DE DADOS ---

    private void carregarTabelaPessoas() {
        modeloPessoas.setRowCount(0);
        List<Pessoa> lista = pessoaRep.findAll();
        for (Pessoa p : lista) {
            modeloPessoas.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                p.getCpf(),
                formatDateToString(p.getData_nascimento()),
                p.getSexo()
            });
        }
    }

    private void carregarPessoaNoFormulario(int id) {
        pessoaRep.findById(id).ifPresent(p -> {
            this.pessoaSelecionada = p;
            lblIdPessoa.setText(String.valueOf(p.getId()));
            txtNome.setText(p.getNome());
            txtCpf.setText(p.getCpf());
            txtDataNascimento.setText(formatDateToString(p.getData_nascimento()));
            cbSexo.setSelectedItem(p.getSexo());
            
            // Carregar contatos desta pessoa
            carregarContatos(p.getId());
            
            // Habilitar seção de contatos
            habilitarContatos(true);
        });
    }

    private void carregarContatos(int pessoaId) {
        modeloContatos.setRowCount(0);
        List<Contato> contatos = contatoRep.findByPessoa_id(pessoaId);
        for (Contato c : contatos) {
            modeloContatos.addRow(new Object[]{c.getId(), c.getContato(), c.getTipo()});
        }
    }

    private void salvarPessoa() {
        try {
            String nome = txtNome.getText();
            String cpf = txtCpf.getText(); // A máscara retorna caracteres como . e -
            String dataStr = txtDataNascimento.getText();
            String sexo = (String) cbSexo.getSelectedItem();

            Date dataSql = parseDate(dataStr);

            if (pessoaSelecionada == null) {
                // CREATE
                Pessoa nova = new Pessoa(cpf, dataSql, nome, sexo);
                Pessoa criada = pessoaRep.create(nova);
                if (criada != null) {
                    JOptionPane.showMessageDialog(this, "Pessoa cadastrada com ID: " + criada.getId());
                    carregarPessoaNoFormulario(criada.getId()); // Carrega para permitir add contatos
                }
            } else {
                // UPDATE
                pessoaSelecionada.setNome(nome);
                pessoaSelecionada.setCpf(cpf);
                pessoaSelecionada.setData_nascimento(dataSql);
                pessoaSelecionada.setSexo(sexo);
                
                if (pessoaRep.update(pessoaSelecionada)) {
                    JOptionPane.showMessageDialog(this, "Pessoa atualizada!");
                }
            }
            carregarTabelaPessoas();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void excluirPessoa() {
        if (pessoaSelecionada != null) {
            int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir " + pessoaSelecionada.getNome() + "?", "Confirmação", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                pessoaRep.delete(pessoaSelecionada.getId());
                limparFormulario();
                carregarTabelaPessoas();
                JOptionPane.showMessageDialog(this, "Excluído com sucesso.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione uma pessoa primeiro.");
        }
    }

    private void adicionarContato() {
        if (pessoaSelecionada == null) {
            JOptionPane.showMessageDialog(this, "Salve a pessoa antes de adicionar contatos.");
            return;
        }

        String valor = txtContatoValor.getText();
        String tipo = (String) cbTipoContato.getSelectedItem();

        if (valor.trim().isEmpty()) return;

        Contato novo = new Contato(valor, tipo, pessoaSelecionada.getId());
        contatoRep.create(novo);
        
        txtContatoValor.setText("");
        carregarContatos(pessoaSelecionada.getId());
    }

    private void removerContato() {
        int row = tabelaContatos.getSelectedRow();
        if (row != -1) {
            int idContato = (int) modeloContatos.getValueAt(row, 0);
            contatoRep.delete(idContato);
            carregarContatos(pessoaSelecionada.getId());
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um contato na tabela para remover.");
        }
    }

    private void limparFormulario() {
        pessoaSelecionada = null;
        lblIdPessoa.setText("Novo Registro");
        txtNome.setText("");
        txtCpf.setValue(null);
        txtDataNascimento.setValue(null);
        cbSexo.setSelectedIndex(0);
        
        modeloContatos.setRowCount(0);
        habilitarContatos(false);
    }
    
    private void habilitarContatos(boolean enable) {
        txtContatoValor.setEnabled(enable);
        cbTipoContato.setEnabled(enable);
        btnAddContato.setEnabled(enable);
        btnRemoveContato.setEnabled(enable);
        tabelaContatos.setEnabled(enable);
    }

    // Utilitários de Data
    private Date parseDate(String dataStr) throws Exception {
        // Formato da Máscara: dd/MM/yyyy -> Formato SQL: yyyy-MM-dd
        SimpleDateFormat sdfInput = new SimpleDateFormat("dd/MM/yyyy");
        java.util.Date dateUtil = sdfInput.parse(dataStr);
        return new Date(dateUtil.getTime());
    }

    private String formatDateToString(Date dataSql) {
        if (dataSql == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataSql);
    }
}