package vetcare;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Aplicar look and feel del sistema operativo
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Si falla, usa el look and feel por defecto de Java
        }

        // Iniciar la ventana principal en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal ventana = new VentanaPrincipal();
            ventana.setVisible(true);
        });
    }
}
