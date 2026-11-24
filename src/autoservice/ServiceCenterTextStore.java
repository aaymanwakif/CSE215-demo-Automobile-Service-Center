package autoservice;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ServiceCenterTextStore {
    public ServiceCenter load(Path file) throws IOException {
        ServiceCenter center = new ServiceCenter();
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (line.trim().isEmpty() || line.startsWith("#")) continue;
            try {
                loadRow(center, parseRow(line));
            } catch (RuntimeException e) {
                throw new IOException("Invalid data at line " + (i + 1) + " in " + file + ": " + e.getMessage(), e);
            }
        }
        return center;
    }

    public void save(ServiceCenter center, Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add("# Automobile Service Center data, version 1");
        for (Customer customer : center.getCustomers()) {
            lines.add(row("CUSTOMER", customer.getId(), customer.getName(), customer.getPhone(),
                    customer.getEmail(), customer.getCompletedServices(),
                    customer.getPasswordSalt(), customer.getPasswordHash()));
        }
        for (Vehicle vehicle : center.getVehicles()) {
            lines.add(row("VEHICLE", vehicle.getId(), vehicle.getCustomerId(),
                    vehicle.getRegistrationNumber(), vehicle.getBrand(), vehicle.getModel(),
                    vehicle.getYear(), vehicle.getVehicleType()));
        }
        for (Mechanic mechanic : center.getMechanics()) {
            lines.add(row("MECHANIC", mechanic.getId(), mechanic.getName(), mechanic.getPhone(),
                    mechanic.getSpecialization(), mechanic.getBasicSalary(),
                    mechanic.getBonusPerJob(), mechanic.getCompletedJobs()));
        }
        for (ServicePackage servicePackage : center.getPackages()) {
            lines.add(row("PACKAGE", servicePackage.getId(), servicePackage.getName(),
                    servicePackage.getDescription(), servicePackage.getLaborHours(),
                    servicePackage.getHourlyRate()));
        }
        for (SparePart part : center.getSpareParts()) {
            lines.add(row("PART", part.getId(), part.getName(), part.getQuantity(), part.getUnitPrice()));
        }
        for (ServiceRecord record : center.getRecords()) {
            lines.add(row("RECORD", record.getId(), record.getCustomerId(), record.getVehicleId(),
                    record.getMechanicId(), record.getPackageId(), record.getNotes(), record.getDate(),
                    record.getLaborCost(), record.getPartsCost(), record.getDiscount()));
        }

        Path absoluteFile = file.toAbsolutePath();
        Path parent = absoluteFile.getParent();
        if (parent != null) Files.createDirectories(parent);
        Path temporaryFile = Files.createTempFile(parent, absoluteFile.getFileName().toString(), ".tmp");
        try {
            Files.write(temporaryFile, lines, StandardCharsets.UTF_8);
            try {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporaryFile, absoluteFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private void loadRow(ServiceCenter center, List<String> fields) {
        String type = fields.get(0);
        switch (type) {
            case "CUSTOMER":
                if (fields.size() != 6 && fields.size() != 8) {
                    throw new IllegalArgumentException("Expected 6 or 8 columns but found " + fields.size());
                }
                Customer customer = new Customer(integer(fields, 1), fields.get(2), fields.get(3),
                        fields.get(4), integer(fields, 5));
                if (fields.size() == 8) customer.restorePassword(fields.get(6), fields.get(7));
                center.getCustomers().add(customer);
                break;
            case "VEHICLE":
                requireSize(fields, 8);
                center.getVehicles().add(new Vehicle(integer(fields, 1), integer(fields, 2), fields.get(3),
                        fields.get(4), fields.get(5), integer(fields, 6), fields.get(7)));
                break;
            case "MECHANIC":
                requireSize(fields, 8);
                center.getMechanics().add(new Mechanic(integer(fields, 1), fields.get(2), fields.get(3),
                        fields.get(4), decimal(fields, 5), decimal(fields, 6), integer(fields, 7)));
                break;
            case "PACKAGE":
                requireSize(fields, 6);
                center.getPackages().add(new ServicePackage(integer(fields, 1), fields.get(2),
                        fields.get(3), decimal(fields, 4), decimal(fields, 5)));
                break;
            case "PART":
                requireSize(fields, 5);
                center.getSpareParts().add(new SparePart(integer(fields, 1), fields.get(2),
                        integer(fields, 3), decimal(fields, 4)));
                break;
            case "RECORD":
                requireSize(fields, 11);
                center.getRecords().add(new ServiceRecord(integer(fields, 1), integer(fields, 2),
                        integer(fields, 3), integer(fields, 4), integer(fields, 5), fields.get(6),
                        LocalDate.parse(fields.get(7)), decimal(fields, 8), decimal(fields, 9), decimal(fields, 10)));
                break;
            default:
                throw new IllegalArgumentException("Unknown row type: " + type);
        }
    }

    private List<String> parseRow(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder field = new StringBuilder();
        boolean quoted = false;
        boolean closedQuote = false;

        for (int i = 0; i < line.length(); i++) {
            char current = line.charAt(i);
            if (quoted) {
                if (current == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        field.append('"');
                        i++;
                    } else {
                        quoted = false;
                        closedQuote = true;
                    }
                } else {
                    field.append(current);
                }
            } else if (current == ',') {
                fields.add(unescape(field.toString()));
                field.setLength(0);
                closedQuote = false;
            } else if (current == '"' && field.length() == 0 && !closedQuote) {
                quoted = true;
            } else if (closedQuote) {
                throw new IllegalArgumentException("Unexpected character after quoted value");
            } else {
                field.append(current);
            }
        }
        if (quoted) throw new IllegalArgumentException("Unclosed quoted value");
        fields.add(unescape(field.toString()));
        return fields;
    }

    private String row(Object... values) {
        StringBuilder line = new StringBuilder();
        for (Object value : values) {
            if (line.length() > 0) line.append(',');
            String text = escape(String.valueOf(value));
            line.append('"').append(text.replace("\"", "\"\"")).append('"');
        }
        return line.toString();
    }

    private String escape(String value) {
        return value.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n");
    }

    private String unescape(String value) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if (current == '\\' && i + 1 < value.length()) {
                char next = value.charAt(++i);
                if (next == 'n') result.append('\n');
                else if (next == 'r') result.append('\r');
                else result.append(next);
            } else {
                result.append(current);
            }
        }
        return result.toString();
    }

    private void requireSize(List<String> fields, int expected) {
        if (fields.size() != expected) {
            throw new IllegalArgumentException("Expected " + expected + " columns but found " + fields.size());
        }
    }

    private int integer(List<String> fields, int index) {
        return Integer.parseInt(fields.get(index));
    }

    private double decimal(List<String> fields, int index) {
        return Double.parseDouble(fields.get(index));
    }
}
