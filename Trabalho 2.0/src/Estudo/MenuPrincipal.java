package Estudo;

import javax.swing.*;
import java.awt.*;

public class MenuPrincipal extends JFrame {

    public MenuPrincipal() {
        super("Sistema de Gestão - Menu Principal");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(3, 1, 10, 10)); // 3 botões empilhados

        // Estilização básica
        JPanel panelPadding = new JPanel(new GridLayout(3, 1, 10, 10));
        panelPadding.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnPessoas = new JButton("Gerenciar Pessoas e Contatos");
        JButton btnProjetos = new JButton("Gerenciar Projetos");
        JButton btnAssociacao = new JButton("Gerenciar Equipes (Associações)");

        btnPessoas.setFont(new Font("Arial", Font.BOLD, 14));
        btnProjetos.setFont(new Font("Arial", Font.BOLD, 14));
        btnAssociacao.setFont(new Font("Arial", Font.BOLD, 14));

        btnPessoas.addActionListener(e -> {
            new MainFrame().setVisible(true); // Abre a tela de Pessoas
        });

        btnProjetos.addActionListener(e -> {
            new ProjetoFrame().setVisible(true); // Abre a tela de Projetos
        });

        btnAssociacao.addActionListener(e -> {
            new AssociacaoFrame().setVisible(true); // Abre a tela de Associação
        });

        panelPadding.add(btnPessoas);
        panelPadding.add(btnProjetos);
        panelPadding.add(btnAssociacao);

        add(panelPadding);
    }
}