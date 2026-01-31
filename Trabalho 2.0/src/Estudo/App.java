package Estudo;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            
            MenuPrincipal menu = new MenuPrincipal();
            menu.setVisible(true);
        });
    }
}