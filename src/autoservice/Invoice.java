package autoservice;

public class Invoice {
    private final double laborCost, partsCost, discount;

    public Invoice(double laborCost, double partsCost, double discount) {
        this.laborCost = laborCost;
        this.partsCost = partsCost;
        this.discount = discount;
    }

    public double getSubtotal() { return laborCost + partsCost; }
    public double getDiscount() { return discount; }
    public double getTotal() { return Math.max(0, getSubtotal() - discount); }

    public void printInvoice(int recordId, String customerName, String vehicleName) {
        System.out.println("\n=============== SERVICE INVOICE ===============");
        System.out.println("Invoice number : " + recordId);
        System.out.println("Customer       : " + customerName);
        System.out.println("Vehicle        : " + vehicleName);
        System.out.printf("Labor cost     : Tk %.2f%n", laborCost);
        System.out.printf("Spare parts    : Tk %.2f%n", partsCost);
        System.out.printf("Subtotal       : Tk %.2f%n", getSubtotal());
        System.out.printf("Loyalty discount: Tk %.2f%n", discount);
        System.out.printf("AMOUNT DUE     : Tk %.2f%n", getTotal());
        System.out.println("================================================\n");
    }
}
