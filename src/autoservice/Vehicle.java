package autoservice;

import java.io.Serializable;

public class Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id, customerId, year;
    private String registrationNumber, brand, model, vehicleType;

    public Vehicle(int id, int customerId, String registrationNumber, String brand,
                   String model, int year, String vehicleType) {
        this.id = id;
        this.customerId = customerId;
        this.registrationNumber = registrationNumber;
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.vehicleType = vehicleType;
    }

    public int getId() { return id; }
    public int getCustomerId() { return customerId; }
    public String getRegistrationNumber() { return registrationNumber; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getVehicleType() { return vehicleType; }

    @Override
    public String toString() {
        return String.format("Vehicle ID: %d | Owner ID: %d | %s %s | Reg: %s | Year: %d | Type: %s",
                id, customerId, brand, model, registrationNumber, year, vehicleType);
    }
}
