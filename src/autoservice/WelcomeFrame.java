package autoservice;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class WelcomeFrame extends JFrame {
    private final ServiceCenter center;
    private final ServiceCenterRepository repository;

    public WelcomeFrame(ServiceCenter center, ServiceCenterRepository repository) {
        this.center = center;
        this.repository = repository;
        setTitle("Automobile Service Center");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(760, 540));
        setSize(900, 620);
        setLocationRelativeTo(null);
        AppTheme.styleFrame(this);
        showRoleSelection();
    }

    private void showRoleSelection() {
        JPanel page = new JPanel(new GridBagLayout());
        page.setBackground(AppTheme.BACKGROUND);
        page.setBorder(new EmptyBorder(40, 50, 40, 50));

        JPanel content = new JPanel();
        content.setBackground(AppTheme.BACKGROUND);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        JLabel title = AppTheme.title("Automobile Service Center");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        JLabel subtitle = AppTheme.subtitle("Choose how you would like to continue");
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(10));
        content.add(subtitle);
        content.add(Box.createVerticalStrut(36));

        JButton customerButton = roleButton("I'm a Customer", "Register or sign in to view your service history", true);
        JButton ownerButton = roleButton("Owner / Admin", "Sign in to manage the service center", false);
        customerButton.addActionListener(e -> showCustomerAccess());
        ownerButton.addActionListener(e -> showOwnerLogin());
        content.add(customerButton);
        content.add(Box.createVerticalStrut(18));
        content.add(ownerButton);

        page.add(content);
        setContentPane(page);
        revalidate();
        repaint();
    }

    private JButton roleButton(String title, String detail, boolean primary) {
        JButton button = new JButton("<html><div style='text-align:center'>" + title
                + "<br><span style='font-size:13px;font-weight:normal'>" + detail + "</span></div></html>");
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(520, 104));
        button.setMinimumSize(new Dimension(520, 104));
        button.setMaximumSize(new Dimension(520, 104));
        AppTheme.styleButton(button, primary);
        return button;
    }

    private void showCustomerAccess() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.setBackground(AppTheme.BACKGROUND);
        tabs.setForeground(AppTheme.FOREGROUND);
        tabs.addTab("Customer Login", loginPanel());
        tabs.addTab("Create Account", registerPanel());

        JPanel page = new JPanel(new BorderLayout(12, 12));
        page.setBackground(AppTheme.BACKGROUND);
        page.setBorder(new EmptyBorder(20, 24, 24, 24));
        JButton back = new JButton("Back");
        AppTheme.styleButton(back, false);
        back.addActionListener(e -> showRoleSelection());
        page.add(back, BorderLayout.NORTH);
        page.add(tabs, BorderLayout.CENTER);
        setContentPane(page);
        revalidate();
        repaint();
    }

    private JPanel loginPanel() {
        JPanel panel = authPanel();
        JTextField email = field();
        JPasswordField password = new JPasswordField();
        styleInput(password);
        JButton login = new JButton("Sign In");
        AppTheme.styleButton(login, true);
        login.addActionListener(e -> customerLogin(email.getText(), password));
        addField(panel, 0, "Email address", email);
        addField(panel, 1, "Password", password);
        addAction(panel, 2, login);
        return panel;
    }

    private JPanel registerPanel() {
        JPanel panel = authPanel();
        JTextField name = field();
        JTextField phone = field();
        JTextField email = field();
        JPasswordField password = new JPasswordField();
        JPasswordField confirm = new JPasswordField();
        styleInput(password);
        styleInput(confirm);
        JButton register = new JButton("Create Customer Account");
        AppTheme.styleButton(register, true);
        register.addActionListener(e -> registerCustomer(name.getText(), phone.getText(),
                email.getText(), password, confirm));
        addField(panel, 0, "Full name", name);
        addField(panel, 1, "Phone number", phone);
        addField(panel, 2, "Email address", email);
        addField(panel, 3, "Password (at least 8 characters)", password);
        addField(panel, 4, "Confirm password", confirm);
        addAction(panel, 5, register);
        return panel;
    }

    private JPanel authPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(AppTheme.BACKGROUND);
        panel.setBorder(new EmptyBorder(24, 80, 24, 80));
        return panel;
    }

    private void addField(JPanel panel, int row, String label, JComponent input) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = row;
        c.weightx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(8, 8, 8, 16);
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setForeground(AppTheme.FOREGROUND);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        panel.add(fieldLabel, c);
        c.gridx = 1;
        c.weightx = 1;
        c.insets = new Insets(8, 8, 8, 8);
        input.setPreferredSize(new Dimension(300, 42));
        panel.add(input, c);
    }

    private void addAction(JPanel panel, int row, JButton button) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = row;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(18, 8, 8, 8);
        button.setPreferredSize(new Dimension(300, 54));
        panel.add(button, c);
    }

    private JTextField field() {
        JTextField field = new JTextField();
        styleInput(field);
        return field;
    }

    private void styleInput(JComponent field) {
        field.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        field.setBackground(AppTheme.INPUT_BACKGROUND);
        field.setForeground(AppTheme.INPUT_FOREGROUND);
        if (field instanceof javax.swing.text.JTextComponent) {
            ((javax.swing.text.JTextComponent) field).setCaretColor(AppTheme.INPUT_FOREGROUND);
        }
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)));
    }

    private void customerLogin(String email, JPasswordField passwordField) {
        char[] password = passwordField.getPassword();
        try {
            Customer customer = center.findCustomerByEmail(email);
            if (customer == null || !customer.verifyPassword(password)) {
                showError("Email or password is incorrect. Existing customers without an account can create one using their registered name and email.");
                return;
            }
            openCustomerPortal(customer);
        } finally {
            java.util.Arrays.fill(password, '\0');
            passwordField.setText("");
        }
    }

    private void registerCustomer(String name, String phone, String email,
                                  JPasswordField passwordField, JPasswordField confirmField) {
        char[] password = passwordField.getPassword();
        char[] confirmation = confirmField.getPassword();
        try {
            String normalizedEmail = email.trim();
            if (name.trim().isEmpty() || !normalizedEmail.contains("@")) {
                showError("Enter your name and a valid email address.");
                return;
            }
            if (password.length < 8) {
                showError("Choose a password with at least 8 characters.");
                return;
            }
            if (!MessageDigest.isEqual(new String(password).getBytes(StandardCharsets.UTF_8),
                    new String(confirmation).getBytes(StandardCharsets.UTF_8))) {
                showError("The passwords do not match.");
                return;
            }

            Customer customer = center.findCustomerByEmail(normalizedEmail);
            boolean newCustomer = customer == null;
            if (customer != null && (customer.hasPassword() || !customer.getName().equalsIgnoreCase(name.trim()))) {
                showError("That email already has an account or belongs to a different customer.");
                return;
            }
            if (newCustomer) {
                customer = new Customer(center.nextCustomerId(), name.trim(), phone.trim(), normalizedEmail);
                center.getCustomers().add(customer);
            }
            customer.setPassword(password);
            if (newCustomer) customer.setPhone(phone.trim());
            try {
                repository.save(center);
            } catch (IOException e) {
                if (newCustomer) center.getCustomers().remove(customer);
                customer.restorePassword("", "");
                showError("Could not save your account: " + e.getMessage());
                return;
            }
            openCustomerPortal(customer);
        } finally {
            java.util.Arrays.fill(password, '\0');
            java.util.Arrays.fill(confirmation, '\0');
            passwordField.setText("");
            confirmField.setText("");
        }
    }

    private void showOwnerLogin() {
        JTextField email = field();
        JPasswordField password = new JPasswordField();
        styleInput(password);
        JPanel fields = new JPanel(new GridLayout(0, 1, 8, 8));
        fields.setBackground(AppTheme.PANEL);
        fields.add(label("Use the owner email and password configured for this app."));
        fields.add(label("Owner email"));
        fields.add(email);
        fields.add(label("Owner password"));
        fields.add(password);
        int choice = JOptionPane.showConfirmDialog(this, fields, "Owner / Admin Sign In",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return;

        String configuredEmail = System.getenv("AUTOSERVICE_OWNER_EMAIL");
        String configuredPassword = System.getenv("AUTOSERVICE_OWNER_PASSWORD");
        if (configuredEmail == null || configuredEmail.isBlank()
                || configuredPassword == null || configuredPassword.isEmpty()) {
            showError("Owner login is not configured. Set AUTOSERVICE_OWNER_EMAIL and AUTOSERVICE_OWNER_PASSWORD before starting the application.");
            return;
        }

        char[] enteredPassword = password.getPassword();
        try {
            boolean emailMatches = configuredEmail.trim().equalsIgnoreCase(email.getText().trim());
            boolean passwordMatches = MessageDigest.isEqual(
                    configuredPassword.getBytes(StandardCharsets.UTF_8),
                    new String(enteredPassword).getBytes(StandardCharsets.UTF_8));
            if (!emailMatches || !passwordMatches) {
                showError("Owner email or password is incorrect.");
                return;
            }
            openOwnerDashboard();
        } finally {
            java.util.Arrays.fill(enteredPassword, '\0');
            password.setText("");
        }
    }

    private JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(AppTheme.FOREGROUND);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return label;
    }

    private void openOwnerDashboard() {
        setVisible(false);
        ServiceCenterFrame dashboard = new ServiceCenterFrame(center, () -> setVisible(true));
        dashboard.setVisible(true);
    }

    private void openCustomerPortal(Customer customer) {
        setVisible(false);
        CustomerPortalFrame portal = new CustomerPortalFrame(center, customer, () -> setVisible(true));
        portal.setVisible(true);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Sign In", JOptionPane.ERROR_MESSAGE);
    }
}
