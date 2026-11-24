package autoservice;

public class LoyaltyManager {
    public double getDiscountRate(Customer customer) {
        int visits = customer.getCompletedServices();
        if (visits >= 10) return 0.10;
        if (visits >= 5) return 0.05;
        return 0.0;
    }

    public String getTier(Customer customer) {
        int visits = customer.getCompletedServices();
        if (visits >= 10) return "VIP (10%)";
        if (visits >= 5) return "Returning (5%)";
        return "Regular (0%)";
    }
}
