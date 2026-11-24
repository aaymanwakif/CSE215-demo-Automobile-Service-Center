package autoservice;

import java.io.Serializable;
import java.time.LocalDate;

public class ServiceRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id, customerId, vehicleId, mechanicId, packageId;
    private String notes;
    private LocalDate date;
    private double laborCost, partsCost, discount, total;

    public ServiceRecord(int id, int customerId, int vehicleId, int mechanicId, int packageId,
                         String notes, double laborCost, double partsCost, double discount) {
        this(id, customerId, vehicleId, mechanicId, packageId, notes, LocalDate.now(),
                laborCost, partsCost, discount);
    }

    public ServiceRecord(int id, int customerId, int vehicleId, int mechanicId, int packageId,
                         String notes, LocalDate date, double laborCost, double partsCost, double discount) {
        this.id = id;
        this.customerId = customerId;
        this.vehicleId = vehicleId;
        this.mechanicId = mechanicId;
        this.packageId = packageId;
        this.notes = notes;
        this.date = date;
        this.laborCost = laborCost;
        this.partsCost = partsCost;
        this.discount = discount;
        this.total = laborCost + partsCost - discount;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public int getVehicleId() { return vehicleId; }
    public int getMechanicId() { return mechanicId; }
    public int getPackageId() { return packageId; }
    public String getNotes() { return notes; }
    public java.time.LocalDate getDate() { return date; }
    public double getLaborCost() { return laborCost; }
    public double getPartsCost() { return partsCost; }
    public double getDiscount() { return discount; }
    public double getTotal() { return total; }

    @Override
    public String toString() {
        return String.format("Record #%d | %s | Customer: %d | Vehicle: %d | Mechanic: %d%n" +
                        "  Labor: Tk %.2f | Parts: Tk %.2f | Discount: Tk %.2f | TOTAL: Tk %.2f%n  Notes: %s",
                id, date, customerId, vehicleId, mechanicId, laborCost, partsCost, discount, total, notes);
    }
}
