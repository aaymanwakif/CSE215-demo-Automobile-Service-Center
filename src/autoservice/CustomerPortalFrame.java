package autoservice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerPortalFrame extends JFrame {
    public CustomerPortalFrame(ServiceCenter center, Customer customer, Runnable onLogout) {
        setTitle("Customer Portal - Automobile Service Center");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(980, 650);
        setMinimumSize(new Dimension(760, 500));
        setLocationRelativeTo(null);
        AppTheme.styleFrame(this);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { logout(onLogout); }
        });

        JPanel page = new JPanel(new BorderLayout(18, 18));
        page.setBackground(AppTheme.BACKGROUND);
        page.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        JPanel heading = new JPanel(new BorderLayout(12, 12));
        heading.setBackground(AppTheme.BACKGROUND);
        JPanel welcome = new JPanel();
        welcome.setBackground(AppTheme.BACKGROUND);
        welcome.setLayout(new BoxLayout(welcome, BoxLayout.Y_AXIS));
        welcome.add(AppTheme.title("Welcome, " + customer.getName()));
        welcome.add(Box.createVerticalStrut(6));
        welcome.add(AppTheme.subtitle(customer.getEmail() + "  |  "
                + new LoyaltyManager().getTier(customer) + " member"));
        heading.add(welcome, BorderLayout.CENTER);

        JButton logout = new JButton("Log Out");
        AppTheme.styleButton(logout, false);
        logout.addActionListener(e -> logout(onLogout));
        heading.add(logout, BorderLayout.EAST);
        page.add(heading, BorderLayout.NORTH);

        JPanel details = new JPanel(new GridLayout(1, 3, 12, 12));
        details.setBackground(AppTheme.BACKGROUND);
        details.add(infoCard("Phone", customer.getPhone()));
        details.add(infoCard("Completed services", String.valueOf(customer.getCompletedServices())));
        details.add(infoCard("Customer ID", String.valueOf(customer.getId())));

        DefaultTableModel history = new DefaultTableModel(
                new Object[]{"Date", "Vehicle", "Service", "Mechanic", "Amount (Tk)", "Notes"}, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        for (ServiceRecord record : center.getRecords()) {
            if (record.getCustomerId() != customer.getId()) continue;
            Vehicle vehicle = center.findVehicle(record.getVehicleId());
            Mechanic mechanic = center.findMechanic(record.getMechanicId());
            ServicePackage servicePackage = center.findPackage(record.getPackageId());
            history.addRow(new Object[]{
                    record.getDate(),
                    vehicle == null ? "" : vehicle.getBrand() + " " + vehicle.getModel()
                            + " (" + vehicle.getRegistrationNumber() + ")",
                    servicePackage == null ? "" : servicePackage.getName(),
                    mechanic == null ? "" : mechanic.getName(),
                    String.format("Tk %.2f", record.getTotal()),
                    record.getNotes()});
        }
        JTable table = new JTable(history);
        table.setRowHeight(28);
        table.setFillsViewportHeight(true);
        JScrollPane tableScroll = new JScrollPane(table);
        tableScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER), "Your service history",
                0, 0, new Font("Segoe UI", Font.BOLD, 16), AppTheme.FOREGROUND));

        JPanel centerPanel = new JPanel(new BorderLayout(14, 14));
        centerPanel.setBackground(AppTheme.BACKGROUND);
        centerPanel.add(details, BorderLayout.NORTH);
        centerPanel.add(tableScroll, BorderLayout.CENTER);
        page.add(centerPanel, BorderLayout.CENTER);
        setContentPane(page);
    }

    private JPanel infoCard(String title, String value) {
        JPanel card = new JPanel();
        card.setBackground(AppTheme.PANEL);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppTheme.BORDER),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)));
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        JLabel heading = AppTheme.subtitle(title);
        JLabel detail = new JLabel(value == null || value.isBlank() ? "Not provided" : value);
        detail.setForeground(AppTheme.FOREGROUND);
        detail.setFont(new Font("Segoe UI", Font.BOLD, 20));
        card.add(heading);
        card.add(Box.createVerticalStrut(8));
        card.add(detail);
        return card;
    }

    private void logout(Runnable onLogout) {
        dispose();
        onLogout.run();
    }
}
