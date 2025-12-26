package Estudo;

import javax.swing.SwingUtilities;

public class App {
    public static void main(String[] args) {
        // Iniciar a interface gráfica na Thread do Swing
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}