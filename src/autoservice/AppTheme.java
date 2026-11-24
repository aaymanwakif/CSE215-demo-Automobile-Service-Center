package autoservice;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

final class AppTheme {
    static final Color BACKGROUND = new Color(22, 27, 35);
    static final Color PANEL = new Color(31, 38, 49);
    static final Color CONTROL = new Color(40, 49, 63);
    static final Color INPUT_BACKGROUND = new Color(245, 247, 250);
    static final Color INPUT_FOREGROUND = new Color(20, 25, 32);
    static final Color FOREGROUND = new Color(232, 237, 244);
    static final Color MUTED = new Color(166, 178, 193);
    static final Color ACCENT = new Color(45, 136, 218);
    static final Color BORDER = new Color(66, 78, 96);

    private AppTheme() { }

    static void apply() {
        UIManager.put("Panel.background", BACKGROUND);
        UIManager.put("OptionPane.background", PANEL);
        UIManager.put("OptionPane.messageForeground", FOREGROUND);
        UIManager.put("Label.foreground", FOREGROUND);
        UIManager.put("Button.background", CONTROL);
        UIManager.put("Button.foreground", FOREGROUND);
        UIManager.put("TextField.background", INPUT_BACKGROUND);
        UIManager.put("TextField.foreground", INPUT_FOREGROUND);
        UIManager.put("TextField.caretForeground", INPUT_FOREGROUND);
        UIManager.put("PasswordField.background", INPUT_BACKGROUND);
        UIManager.put("PasswordField.foreground", INPUT_FOREGROUND);
        UIManager.put("PasswordField.caretForeground", INPUT_FOREGROUND);
        UIManager.put("TextArea.background", INPUT_BACKGROUND);
        UIManager.put("TextArea.foreground", INPUT_FOREGROUND);
        UIManager.put("TextArea.caretForeground", INPUT_FOREGROUND);
        UIManager.put("ComboBox.background", CONTROL);
        UIManager.put("ComboBox.foreground", FOREGROUND);
        UIManager.put("TabbedPane.background", BACKGROUND);
        UIManager.put("TabbedPane.foreground", FOREGROUND);
        UIManager.put("TabbedPane.selected", PANEL);
        UIManager.put("Table.background", PANEL);
        UIManager.put("Table.foreground", FOREGROUND);
        UIManager.put("Table.selectionBackground", ACCENT);
        UIManager.put("Table.selectionForeground", Color.WHITE);
        UIManager.put("TableHeader.background", CONTROL);
        UIManager.put("TableHeader.foreground", FOREGROUND);
        UIManager.put("ScrollPane.background", PANEL);
        UIManager.put("Viewport.background", PANEL);
        UIManager.put("List.background", CONTROL);
        UIManager.put("List.foreground", FOREGROUND);
    }

    static void styleButton(JButton button, boolean primary) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 17));
        button.setForeground(Color.WHITE);
        button.setBackground(primary ? ACCENT : CONTROL);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(buttonBorder());
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    static Border buttonBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                BorderFactory.createEmptyBorder(12, 18, 12, 18));
    }

    static void styleFrame(JFrame frame) {
        frame.getContentPane().setBackground(BACKGROUND);
    }

    static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(FOREGROUND);
        label.setFont(new Font("Segoe UI", Font.BOLD, 30));
        return label;
    }

    static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(MUTED);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        return label;
    }
}
