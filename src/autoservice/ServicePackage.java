package autoservice;

import java.io.Serializable;

public class ServicePackage implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name, description;
    private double laborHours, hourlyRate;

    public ServicePackage(int id, String name, String description, double laborHours, double hourlyRate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.laborHours = laborHours;
        this.hourlyRate = hourlyRate;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getLaborHours() { return laborHours; }
    public double getHourlyRate() { return hourlyRate; }
    public double getLaborCost() { return laborHours * hourlyRate; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | %.1f labor hours | Tk %.2f labor",
                id, name, description, laborHours, getLaborCost());
    }
}
