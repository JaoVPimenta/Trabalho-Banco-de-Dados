package Estudo;

import Estudo.model.Integrante;
import Estudo.model.Pessoa;
import Estudo.model.Projeto;
import Estudo.repository.IntegranteRepository;
import Estudo.repository.PessoaRepository;
import Estudo.repository.ProjetoRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AssociacaoFrame extends JFrame {

    private final IntegranteRepository integranteRep = new IntegranteRepository();
    private final PessoaRepository pessoaRep = new PessoaRepository();
    private final ProjetoRepository projetoRep = new ProjetoRepository();

    private JComboBox<ComboItem> cbPessoa;
    private JComboBox<ComboItem> cbProjeto;
    private JTextField txtCargo;
    private JTable tabelaIntegrantes;
    private DefaultTableModel modeloIntegrantes;

    public AssociacaoFrame() {
        super("Gerenciar Equipes (Integrantes)");
        setSize(850, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        carregarCombos();
        carregarTabela();
    }

    private void initComponents() {
        // --- FORMULÁRIO DE ASSOCIAÇÃO ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Associar Pessoa a Projeto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Combo Pessoa
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("Selecione a Pessoa:"), gbc);
        cbPessoa = new JComboBox<>();
        gbc.gridx = 1; gbc.weightx = 1.0;
        panelForm.add(cbPessoa, gbc);

        // Combo Projeto
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0;
        panelForm.add(new JLabel("Selecione o Projeto:"), gbc);
        cbProjeto = new JComboBox<>();
        gbc.gridx = 1;
        panelForm.add(cbProjeto, gbc);

        // Campo Cargo
        gbc.gridx = 0; gbc.gridy = 2;
        panelForm.add(new JLabel("Cargo / Função:"), gbc);
        txtCargo = new JTextField(20);
        gbc.gridx = 1;
        panelForm.add(txtCargo, gbc);

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnAssociar = new JButton("Adicionar Integrante");
        JButton btnDesassociar = new JButton("Remover Selecionado");

        btnAssociar.setBackground(new Color(100, 200, 100));
        
        panelBotoes.add(btnAssociar);
        panelBotoes.add(btnDesassociar);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        panelForm.add(panelBotoes, gbc);

        add(panelForm, BorderLayout.NORTH);

        // --- TABELA ---
        // Colunas escondidas para guardar os IDs
        String[] colunas = {"ID Pessoa", "Pessoa", "ID Projeto", "Projeto", "Cargo"};
        modeloIntegrantes = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaIntegrantes = new JTable(modeloIntegrantes);
        
        // ocultar: tabelaIntegrantes.removeColumn(tabelaIntegrantes.getColumnModel().getColumn(0));

        add(new JScrollPane(tabelaIntegrantes), BorderLayout.CENTER);

        // --- AÇÕES ---
        btnAssociar.addActionListener(e -> associar());
        btnDesassociar.addActionListener(e -> desassociar());
    }

    private void carregarCombos() {
        cbPessoa.removeAllItems();
        List<Pessoa> pessoas = pessoaRep.findAll();
        for (Pessoa p : pessoas) {
            cbPessoa.addItem(new ComboItem(p.getId(), p.getNome()));
        }

        cbProjeto.removeAllItems();
        List<Projeto> projetos = projetoRep.findAll();
        for (Projeto p : projetos) {
            cbProjeto.addItem(new ComboItem(p.getId(), p.getNome()));
        }
    }

    private void carregarTabela() {
        modeloIntegrantes.setRowCount(0);
        List<Integrante> integrantes = integranteRep.findAll();
        
        for (Integrante i : integrantes) {
            String nomePessoa = buscarNomePessoa(i.getPessoa_id());
            String nomeProjeto = buscarNomeProjeto(i.getProjeto_id());

            modeloIntegrantes.addRow(new Object[]{
                i.getPessoa_id(),
                nomePessoa,
                i.getProjeto_id(),
                nomeProjeto,
                i.getCargo()
            });
        }
    }

    // pegar o Nome baseado no ID (lookup simples)
    private String buscarNomePessoa(int id) {
        for (int k = 0; k < cbPessoa.getItemCount(); k++) {
            ComboItem item = cbPessoa.getItemAt(k);
            if (item.getKey() == id) return item.getValue();
        }
        return "ID: " + id;
    }

    private String buscarNomeProjeto(int id) {
        for (int k = 0; k < cbProjeto.getItemCount(); k++) {
            ComboItem item = cbProjeto.getItemAt(k);
            if (item.getKey() == id) return item.getValue();
        }
        return "ID: " + id;
    }

    private void associar() {
        ComboItem itemPessoa = (ComboItem) cbPessoa.getSelectedItem();
        ComboItem itemProjeto = (ComboItem) cbProjeto.getSelectedItem();
        String cargo = txtCargo.getText();

        if (itemPessoa == null || itemProjeto == null || cargo.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos.");
            return;
        }

        Integrante novo = new Integrante(cargo, itemPessoa.getKey(), itemProjeto.getKey());
        Integrante criado = integranteRep.create(novo);
        
        if (criado != null) {
            JOptionPane.showMessageDialog(this, "Associado com sucesso!");
            carregarTabela();
            txtCargo.setText("");
        } else {
            JOptionPane.showMessageDialog(this, "Erro. Talvez já exista essa associação?");
        }
    }

    private void desassociar() {
        int row = tabelaIntegrantes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Selecione uma linha para remover.");
            return;
        }

        int idPessoa = (int) modeloIntegrantes.getValueAt(row, 0);
        int idProjeto = (int) modeloIntegrantes.getValueAt(row, 2);

        if (integranteRep.delete(idPessoa, idProjeto)) {
            JOptionPane.showMessageDialog(this, "Associação removida.");
            carregarTabela();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao remover.");
        }
    }

    // Classe auxiliar para o ComboBox (Chave = ID, Valor = Nome)
    class ComboItem {
        private int key;
        private String value;

        public ComboItem(int key, String value) {
            this.key = key;
            this.value = value;
        }
        public int getKey() { return key; }
        public String getValue() { return value; }

        @Override
        public String toString() {
            return value; // O que aparece no combobox
        }
    }
}