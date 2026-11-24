package autoservice;

import java.io.Serializable;

public class SparePart implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id, quantity;
    private String name;
    private double unitPrice;

    public SparePart(int id, String name, int quantity, double unitPrice) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public int getQuantity() { return quantity; }
    public double getUnitPrice() { return unitPrice; }
    public void addStock(int amount) { quantity += amount; }
    public boolean useStock(int amount) {
        if (amount <= 0 || amount > quantity) return false;
        quantity -= amount;
        return true;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | Stock: %d | Unit price: Tk %.2f",
                id, name, quantity, unitPrice);
    }
}
