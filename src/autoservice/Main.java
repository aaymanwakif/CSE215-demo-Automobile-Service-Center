package autoservice;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ignored) {
            }
            AppTheme.apply();
            try {
                ServiceCenterRepository repository = new ServiceCenterRepository();
                ServiceCenter center = repository.load();
                center.addSamplePackages();
                new WelcomeFrame(center, repository).setVisible(true);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Unable to load saved data",
                        JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
