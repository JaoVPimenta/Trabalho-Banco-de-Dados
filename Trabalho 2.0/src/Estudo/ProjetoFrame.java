package Estudo;

import Estudo.model.Projeto;
import Estudo.repository.ProjetoRepository;

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

public class ProjetoFrame extends JFrame {

    private final ProjetoRepository projetoRep = new ProjetoRepository();
    private Projeto projetoSelecionado = null;

    private JTextField txtNome;
    private JFormattedTextField txtDataInicial;
    private JFormattedTextField txtDataFinal;
    private JLabel lblIdProjeto;

    private JTable tabelaProjetos;
    private DefaultTableModel modeloProjetos;

    public ProjetoFrame() {
        super("Gerenciamento de Projetos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Fecha só a janela, não o App
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initComponents();
        carregarTabela();
        limparFormulario();
    }

    private void initComponents() {
        // --- FORMULÁRIO ---
        JPanel panelForm = new JPanel(new GridBagLayout());
        panelForm.setBorder(BorderFactory.createTitledBorder("Dados do Projeto"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ID
        gbc.gridx = 0; gbc.gridy = 0;
        panelForm.add(new JLabel("ID:"), gbc);
        lblIdProjeto = new JLabel("-");
        gbc.gridx = 1;
        panelForm.add(lblIdProjeto, gbc);

        // Nome
        gbc.gridx = 0; gbc.gridy = 1;
        panelForm.add(new JLabel("Nome do Projeto:"), gbc);
        txtNome = new JTextField(30);
        gbc.gridx = 1; gbc.gridwidth = 3;
        panelForm.add(txtNome, gbc);

        // Datas
        try {
            MaskFormatter maskData = new MaskFormatter("##/##/####");
            maskData.setPlaceholderCharacter('_');
            
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1;
            panelForm.add(new JLabel("Data Inicial:"), gbc);
            txtDataInicial = new JFormattedTextField(maskData);
            gbc.gridx = 1;
            panelForm.add(txtDataInicial, gbc);

            gbc.gridx = 2; gbc.gridy = 2;
            panelForm.add(new JLabel("Data Final:"), gbc);
            txtDataFinal = new JFormattedTextField(maskData);
            gbc.gridx = 3;
            panelForm.add(txtDataFinal, gbc);

        } catch (ParseException e) { e.printStackTrace(); }

        // Botões
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton btnNovo = new JButton("Limpar / Novo");
        JButton btnSalvar = new JButton("Salvar Projeto");
        JButton btnExcluir = new JButton("Excluir Projeto");

        btnSalvar.setBackground(new Color(100, 200, 100));
        btnExcluir.setBackground(new Color(255, 100, 100));

        panelBotoes.add(btnNovo);
        panelBotoes.add(btnSalvar);
        panelBotoes.add(btnExcluir);

        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 4;
        panelForm.add(panelBotoes, gbc);

        add(panelForm, BorderLayout.NORTH);

        // --- TABELA ---
        String[] colunas = {"ID", "Nome", "Data Início", "Data Fim"};
        modeloProjetos = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaProjetos = new JTable(modeloProjetos);
        add(new JScrollPane(tabelaProjetos), BorderLayout.CENTER);

        // --- EVENTOS ---
        tabelaProjetos.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    int row = tabelaProjetos.getSelectedRow();
                    if (row != -1) {
                        int id = (int) modeloProjetos.getValueAt(row, 0);
                        carregarProjeto(id);
                    }
                }
            }
        });

        btnNovo.addActionListener(e -> limparFormulario());
        btnSalvar.addActionListener(e -> salvarProjeto());
        btnExcluir.addActionListener(e -> excluirProjeto());
    }

    private void carregarTabela() {
        modeloProjetos.setRowCount(0);
        List<Projeto> lista = projetoRep.findAll();
        for (Projeto p : lista) {
            modeloProjetos.addRow(new Object[]{
                p.getId(),
                p.getNome(),
                formatDate(p.getData_inicial()),
                formatDate(p.getData_final())
            });
        }
    }

    private void carregarProjeto(int id) {
        projetoRep.findById(id).ifPresent(p -> {
            projetoSelecionado = p;
            lblIdProjeto.setText(String.valueOf(p.getId()));
            txtNome.setText(p.getNome());
            txtDataInicial.setText(formatDate(p.getData_inicial()));
            txtDataFinal.setText(formatDate(p.getData_final()));
        });
    }

    private void salvarProjeto() {
        try {
            String nome = txtNome.getText();
            Date dtIni = parseDate(txtDataInicial.getText());
            Date dtFim = parseDate(txtDataFinal.getText());

            if (projetoSelecionado == null) {
                Projeto novo = new Projeto(dtIni, dtFim, nome);
                projetoRep.create(novo);
                JOptionPane.showMessageDialog(this, "Projeto criado!");
            } else {
                projetoSelecionado.setNome(nome);
                projetoSelecionado.setData_inicial(dtIni);
                projetoSelecionado.setData_final(dtFim);
                projetoRep.update(projetoSelecionado);
                JOptionPane.showMessageDialog(this, "Projeto atualizado!");
            }
            limparFormulario();
            carregarTabela();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar: " + e.getMessage());
        }
    }

    private void excluirProjeto() {
        if (projetoSelecionado != null) {
            if(JOptionPane.showConfirmDialog(this, "Excluir projeto?", "Confirma", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                projetoRep.delete(projetoSelecionado.getId());
                limparFormulario();
                carregarTabela();
            }
        }
    }

    private void limparFormulario() {
        projetoSelecionado = null;
        lblIdProjeto.setText("Novo");
        txtNome.setText("");
        txtDataInicial.setValue(null);
        txtDataFinal.setValue(null);
    }

    // Utilitários de Data
    private Date parseDate(String dataStr) throws Exception {
        if (dataStr == null || dataStr.trim().equals("/  /")) return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return new Date(sdf.parse(dataStr).getTime());
    }

    private String formatDate(Date dataSql) {
        if (dataSql == null) return "";
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return sdf.format(dataSql);
    }
}