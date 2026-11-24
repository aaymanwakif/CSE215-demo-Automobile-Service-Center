package autoservice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;

public class ServiceCenterFrame extends JFrame {
    private final ServiceCenter center;
    private final ServiceCenterRepository repository = new ServiceCenterRepository();
    private final Runnable onLogout;
    private final LoyaltyManager loyaltyManager = new LoyaltyManager();
    private final SalaryManager salaryManager = new SalaryManager();

    private final DefaultTableModel customerModel = tableModel("ID", "Name", "Phone", "Email", "Services", "Loyalty");
    private final DefaultTableModel vehicleModel = tableModel("ID", "Owner ID", "Registration", "Brand", "Model", "Year", "Type");
    private final DefaultTableModel mechanicModel = tableModel("ID", "Name", "Phone", "Specialization", "Jobs", "Basic Salary", "Bonus", "Total Salary");
    private final DefaultTableModel packageModel = tableModel("ID", "Name", "Description", "Labor Cost");
    private final DefaultTableModel partsModel = tableModel("ID", "Name", "Stock", "Unit Price");
    private final DefaultTableModel recordModel = tableModel("Record", "Date", "Customer", "Vehicle", "Mechanic", "Package", "Labor", "Parts", "Discount", "Total", "Notes");
    private final JTextArea summaryArea = new JTextArea();
    private JComboBox<Mechanic> payrollMechanics;

    public ServiceCenterFrame(ServiceCenter center, Runnable onLogout) {
        this.center = center;
        this.onLogout = onLogout;
        center.addSamplePackages();

        setTitle("Automobile Service Center");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        AppTheme.styleFrame(this);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { exitApplication(); }
        });

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Dashboard", dashboardPanel());
        tabs.addTab("Customers", listPanel(customerModel, this::showCustomerDialog));
        tabs.addTab("Vehicles", listPanel(vehicleModel, this::showVehicleDialog));
        tabs.addTab("Mechanics", listPanel(mechanicModel, this::showMechanicDialog));
        tabs.addTab("Packages", listPanel(packageModel, null));
        tabs.addTab("Spare Parts", listPanel(partsModel, this::showSparePartDialog));
        tabs.addTab("Service Records", listPanel(recordModel, this::showServiceDialog));
        tabs.addTab("Payroll", payrollPanel());
        setContentPane(tabs);

        refreshAll();
    }

    private DefaultTableModel tableModel(String... columns) {
        return new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private JPanel dashboardPanel() {
        JPanel panel = new JPanel(new BorderLayout(12, 12));
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));

        summaryArea.setEditable(false);
        summaryArea.setFont(new Font(Font.MONOSPACED, Font.BOLD, 16));
        panel.add(summaryArea, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridLayout(2, 3, 10, 10));
        actions.add(actionButton("Register Customer", this::showCustomerDialog));
        actions.add(actionButton("Register Vehicle", this::showVehicleDialog));
        actions.add(actionButton("Add Mechanic", this::showMechanicDialog));
        actions.add(actionButton("Add Spare Part", this::showSparePartDialog));
        actions.add(actionButton("Create Service", this::showServiceDialog));
        actions.add(actionButton("Save Data", this::saveData));
        panel.add(actions, BorderLayout.SOUTH);
        return panel;
    }

    private JButton actionButton(String text, Runnable action) {
        JButton button = new JButton(text);
        AppTheme.styleButton(button, true);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setPreferredSize(new Dimension(220, 92));
        button.addActionListener(e -> action.run());
        return button;
    }

    private JPanel listPanel(DefaultTableModel model, Runnable addAction) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        if (addAction != null) {
            JButton add = new JButton("Add New");
            AppTheme.styleButton(add, true);
            add.setPreferredSize(new Dimension(180, 52));
            add.addActionListener(e -> addAction.run());
            JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            bottom.add(add);
            panel.add(bottom, BorderLayout.SOUTH);
        }
        return panel;
    }

    private JPanel payrollPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        JComboBox<Mechanic> mechanics = new JComboBox<>();
        payrollMechanics = mechanics;
        JTextArea payslip = new JTextArea();
        payslip.setEditable(false);
        payslip.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JButton view = new JButton("View Payslip");
        AppTheme.styleButton(view, true);
        view.setPreferredSize(new Dimension(180, 48));

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.add(new JLabel("Mechanic"));
        top.add(mechanics);
        top.add(view);
        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(payslip), BorderLayout.CENTER);

        Runnable refresh = () -> {
            mechanics.removeAllItems();
            for (Mechanic mechanic : center.getMechanics()) mechanics.addItem(mechanic);
        };
        view.addActionListener(e -> {
            Mechanic mechanic = (Mechanic) mechanics.getSelectedItem();
            if (mechanic == null) {
                payslip.setText("No mechanic selected.");
                return;
            }
            payslip.setText(String.format(
                    "============ MECHANIC PAYSLIP ============%nMechanic       : %s%n" +
                    "Completed jobs : %d%nBasic salary   : Tk %.2f%nPerformance bonus: Tk %.2f%n" +
                    "TOTAL SALARY   : Tk %.2f%n==========================================",
                    mechanic.getName(), mechanic.getCompletedJobs(), mechanic.getBasicSalary(),
                    mechanic.getCompletedJobs() * mechanic.getBonusPerJob(),
                    salaryManager.calculateSalary(mechanic)));
        });
        refresh.run();
        return panel;
    }

    private void showCustomerDialog() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField email = new JTextField();
        Object[] fields = {"Name:", name, "Phone:", phone, "Email:", email};
        if (JOptionPane.showConfirmDialog(this, fields, "Register Customer", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            if (name.getText().trim().isEmpty()) { error("Customer name is required."); return; }
            Customer customer = new Customer(center.nextCustomerId(), name.getText().trim(),
                    phone.getText().trim(), email.getText().trim());
            center.getCustomers().add(customer);
            saveData();
            refreshAll();
            info("Customer registered. ID: " + customer.getId());
        }
    }

    private void showVehicleDialog() {
        if (center.getCustomers().isEmpty()) { error("Register a customer first."); return; }
        JComboBox<Customer> customers = new JComboBox<>(center.getCustomers().toArray(new Customer[0]));
        JTextField registration = new JTextField();
        JTextField brand = new JTextField();
        JTextField model = new JTextField();
        JTextField year = new JTextField();
        JTextField type = new JTextField();
        Object[] fields = {"Owner:", customers, "Registration:", registration, "Brand:", brand,
                "Model:", model, "Manufacturing year:", year, "Vehicle type:", type};
        if (JOptionPane.showConfirmDialog(this, fields, "Register Vehicle", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int manufacturingYear = Integer.parseInt(year.getText().trim());
                String reg = registration.getText().trim().toUpperCase();
                if (reg.isEmpty() || center.registrationExists(reg)) {
                    error("Registration is required and must be unique.");
                    return;
                }
                Customer owner = (Customer) customers.getSelectedItem();
                Vehicle vehicle = new Vehicle(center.nextVehicleId(), owner.getId(), reg,
                        brand.getText().trim(), model.getText().trim(), manufacturingYear, type.getText().trim());
                center.getVehicles().add(vehicle);
                saveData();
                refreshAll();
                info("Vehicle registered. ID: " + vehicle.getId());
            } catch (NumberFormatException ex) {
                error("Manufacturing year must be a whole number.");
            }
        }
    }

    private void showMechanicDialog() {
        JTextField name = new JTextField();
        JTextField phone = new JTextField();
        JTextField skill = new JTextField();
        JTextField salary = new JTextField();
        JTextField bonus = new JTextField();
        Object[] fields = {"Name:", name, "Phone:", phone, "Specialization:", skill,
                "Basic salary (Tk):", salary, "Bonus per job (Tk):", bonus};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Mechanic", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                double basic = Double.parseDouble(salary.getText().trim());
                double jobBonus = Double.parseDouble(bonus.getText().trim());
                if (name.getText().trim().isEmpty() || basic < 0 || jobBonus < 0) {
                    error("Name is required and salaries cannot be negative.");
                    return;
                }
                Mechanic mechanic = new Mechanic(center.nextMechanicId(), name.getText().trim(),
                        phone.getText().trim(), skill.getText().trim(), basic, jobBonus);
                center.getMechanics().add(mechanic);
                saveData();
                refreshAll();
                info("Mechanic added. ID: " + mechanic.getId());
            } catch (NumberFormatException ex) {
                error("Salary and bonus must be valid numbers.");
            }
        }
    }

    private void showSparePartDialog() {
        JTextField name = new JTextField();
        JTextField quantity = new JTextField();
        JTextField price = new JTextField();
        Object[] fields = {"Part name:", name, "Quantity to add:", quantity, "Unit price (Tk):", price};
        if (JOptionPane.showConfirmDialog(this, fields, "Add Spare Part", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            try {
                int amount = Integer.parseInt(quantity.getText().trim());
                double unitPrice = Double.parseDouble(price.getText().trim());
                if (name.getText().trim().isEmpty() || amount <= 0 || unitPrice < 0) {
                    error("Name is required; quantity must be positive and price non-negative.");
                    return;
                }
                SparePart existing = null;
                for (SparePart part : center.getSpareParts()) {
                    if (part.getName().equalsIgnoreCase(name.getText().trim())) { existing = part; break; }
                }
                if (existing != null) {
                    existing.addStock(amount);
                    info("Stock updated. Current stock: " + existing.getQuantity());
                } else {
                    SparePart part = new SparePart(center.nextPartId(), name.getText().trim(), amount, unitPrice);
                    center.getSpareParts().add(part);
                    info("Spare part added. ID: " + part.getId());
                }
                saveData();
                refreshAll();
            } catch (NumberFormatException ex) {
                error("Quantity must be a whole number and price must be numeric.");
            }
        }
    }

    private void showServiceDialog() {
        if (center.getCustomers().isEmpty() || center.getVehicles().isEmpty() || center.getMechanics().isEmpty()) {
            error("You need at least one customer, vehicle, and mechanic first.");
            return;
        }
        JComboBox<Customer> customers = new JComboBox<>(center.getCustomers().toArray(new Customer[0]));
        JComboBox<Vehicle> vehicles = new JComboBox<>();
        JComboBox<Mechanic> mechanics = new JComboBox<>(center.getMechanics().toArray(new Mechanic[0]));
        JComboBox<ServicePackage> packages = new JComboBox<>(center.getPackages().toArray(new ServicePackage[0]));
        JTextArea notes = new JTextArea(3, 20);
        Runnable updateVehicles = () -> {
            vehicles.removeAllItems();
            Customer customer = (Customer) customers.getSelectedItem();
            if (customer != null) for (Vehicle vehicle : center.getVehicles()) {
                if (vehicle.getCustomerId() == customer.getId()) vehicles.addItem(vehicle);
            }
        };
        customers.addActionListener(e -> updateVehicles.run());
        updateVehicles.run();

        JPanel fields = new JPanel(new GridLayout(0, 2, 8, 8));
        fields.add(new JLabel("Customer")); fields.add(customers);
        fields.add(new JLabel("Vehicle")); fields.add(vehicles);
        fields.add(new JLabel("Mechanic")); fields.add(mechanics);
        fields.add(new JLabel("Service Package")); fields.add(packages);
        fields.add(new JLabel("Notes")); fields.add(new JScrollPane(notes));

        if (JOptionPane.showConfirmDialog(this, fields, "Create Service Record", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;

        Customer customer = (Customer) customers.getSelectedItem();
        Vehicle vehicle = (Vehicle) vehicles.getSelectedItem();
        Mechanic mechanic = (Mechanic) mechanics.getSelectedItem();
        ServicePackage servicePackage = (ServicePackage) packages.getSelectedItem();
        if (customer == null || vehicle == null || mechanic == null || servicePackage == null) {
            error("Complete all service selections.");
            return;
        }

        double partsCost = 0;
        if (!center.getSpareParts().isEmpty()) {
            DefaultTableModel parts = new DefaultTableModel(new Object[]{"Use", "Part", "Available", "Quantity", "Unit Price"}, 0) {
                @Override public boolean isCellEditable(int row, int column) { return column == 0 || column == 3; }
                @Override public Class<?> getColumnClass(int column) { return column == 0 ? Boolean.class : String.class; }
            };
            for (SparePart part : center.getSpareParts()) parts.addRow(new Object[]{false, part.getName(), part.getQuantity(), 0, part.getUnitPrice()});
            JTable table = new JTable(parts);
            table.setRowHeight(24);
            if (JOptionPane.showConfirmDialog(this, new JScrollPane(table), "Select Spare Parts",
                    JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                for (int i = 0; i < parts.getRowCount(); i++) {
                    Boolean use = (Boolean) parts.getValueAt(i, 0);
                    if (!use) continue;
                    SparePart part = center.getSpareParts().get(i);
                    int quantity;
                    try { quantity = Integer.parseInt(parts.getValueAt(i, 3).toString()); }
                    catch (Exception ex) { error("Part quantities must be whole numbers."); return; }
                    if (!part.useStock(quantity)) { error("Not enough stock for " + part.getName() + "."); return; }
                    partsCost += quantity * part.getUnitPrice();
                }
            }
        }

        double laborCost = servicePackage.getLaborCost();
        double discount = (laborCost + partsCost) * loyaltyManager.getDiscountRate(customer);
        ServiceRecord record = new ServiceRecord(center.nextRecordId(), customer.getId(), vehicle.getId(),
                mechanic.getId(), servicePackage.getId(), notes.getText().trim(),
                laborCost, partsCost, discount);
        center.getRecords().add(record);
        customer.addCompletedService();
        mechanic.completeJob();
        saveData();
        refreshAll();

        Invoice invoice = new Invoice(laborCost, partsCost, discount);
        String invoiceText = String.format(
                "SERVICE INVOICE%n%nInvoice number : %d%nCustomer       : %s%nVehicle        : %s%n" +
                "Labor cost     : Tk %.2f%nSpare parts    : Tk %.2f%nSubtotal       : Tk %.2f%n" +
                "Loyalty discount: Tk %.2f%nAMOUNT DUE     : Tk %.2f",
                record.getId(), customer.getName(),
                vehicle.getBrand() + " " + vehicle.getModel() + " (" + vehicle.getRegistrationNumber() + ")",
                laborCost, partsCost, invoice.getSubtotal(), discount, invoice.getTotal());
        JTextArea invoiceArea = new JTextArea(invoiceText);
        invoiceArea.setEditable(false);
        invoiceArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 14));
        JOptionPane.showMessageDialog(this, new JScrollPane(invoiceArea), "Invoice", JOptionPane.INFORMATION_MESSAGE);
    }

    private void refreshAll() {
        if (payrollMechanics != null) {
            payrollMechanics.removeAllItems();
            for (Mechanic mechanic : center.getMechanics()) payrollMechanics.addItem(mechanic);
        }
        customerModel.setRowCount(0);
        for (Customer customer : center.getCustomers()) customerModel.addRow(new Object[]{
                customer.getId(), customer.getName(), customer.getPhone(), customer.getEmail(),
                customer.getCompletedServices(), loyaltyManager.getTier(customer)});

        vehicleModel.setRowCount(0);
        for (Vehicle vehicle : center.getVehicles()) vehicleModel.addRow(new Object[]{
                vehicle.getId(), vehicle.getCustomerId(), vehicle.getRegistrationNumber(),
                vehicle.getBrand(), vehicle.getModel(), vehicle.getYear(), vehicle.getVehicleType()});

        mechanicModel.setRowCount(0);
        for (Mechanic mechanic : center.getMechanics()) mechanicModel.addRow(new Object[]{
                mechanic.getId(), mechanic.getName(), mechanic.getPhone(),
                mechanic.getSpecialization(), mechanic.getCompletedJobs(), mechanic.getBasicSalary(),
                mechanic.getBonusPerJob(), mechanic.calculateMonthlySalary()});

        packageModel.setRowCount(0);
        for (ServicePackage servicePackage : center.getPackages()) packageModel.addRow(new Object[]{
                servicePackage.getId(), servicePackage.getName(), servicePackage.getDescription(),
                String.format("Tk %.2f", servicePackage.getLaborCost())});

        partsModel.setRowCount(0);
        for (SparePart part : center.getSpareParts()) partsModel.addRow(new Object[]{
                part.getId(), part.getName(), part.getQuantity(),
                String.format("Tk %.2f", part.getUnitPrice())});

        recordModel.setRowCount(0);
        for (ServiceRecord record : center.getRecords()) {
            Customer customer = center.findCustomer(record.getCustomerId());
            Vehicle vehicle = center.findVehicle(record.getVehicleId());
            Mechanic mechanic = center.findMechanic(record.getMechanicId());
            recordModel.addRow(new Object[]{
                    record.getId(), record.getDate(), customer == null ? "" : customer.getName(),
                    vehicle == null ? "" : vehicle.getRegistrationNumber(),
                    mechanic == null ? "" : mechanic.getName(), recordIdToPackage(record),
                    record.getLaborCost(), record.getPartsCost(), record.getDiscount(),
                    record.getTotal(), record.getNotes()});
        }

        refreshSummary();
    }

    private String recordIdToPackage(ServiceRecord record) {
        ServicePackage servicePackage = center.findPackage(record.getPackageId());
        return servicePackage == null ? "" : servicePackage.getName();
    }

    private void refreshSummary() {
        double revenue = 0;
        for (ServiceRecord record : center.getRecords()) revenue += record.getTotal();
        summaryArea.setText(String.format(
                "AUTOMOBILE SERVICE CENTER%n%nCustomers       : %d%nVehicles        : %d%nMechanics       : %d%n" +
                "Service records : %d%nTotal revenue   : Tk %.2f",
                center.getCustomers().size(), center.getVehicles().size(), center.getMechanics().size(),
                center.getRecords().size(), revenue));
    }

    private void exitApplication() {
        saveData();
        dispose();
        onLogout.run();
    }

    private void info(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void error(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void saveData() {
        try {
            repository.save(center);
        } catch (IOException e) {
            error("Could not save data: " + e.getMessage());
        }
    }
}
