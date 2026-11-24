package autoservice;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceCenter implements Serializable {
    private static final long serialVersionUID = 1L;
    private ArrayList<Customer> customers = new ArrayList<>();
    private ArrayList<Vehicle> vehicles = new ArrayList<>();
    private ArrayList<Mechanic> mechanics = new ArrayList<>();
    private ArrayList<ServicePackage> packages = new ArrayList<>();
    private ArrayList<SparePart> spareParts = new ArrayList<>();
    private ArrayList<ServiceRecord> records = new ArrayList<>();

    public ArrayList<Customer> getCustomers() { return customers; }
    public ArrayList<Vehicle> getVehicles() { return vehicles; }
    public ArrayList<Mechanic> getMechanics() { return mechanics; }
    public ArrayList<ServicePackage> getPackages() { return packages; }
    public ArrayList<SparePart> getSpareParts() { return spareParts; }
    public ArrayList<ServiceRecord> getRecords() { return records; }

    public Customer findCustomer(int id) {
        for (Customer c : customers) if (c.getId() == id) return c;
        return null;
    }
    public Customer findCustomerByEmail(String email) {
        for (Customer customer : customers)
            if (customer.getEmail().equalsIgnoreCase(email.trim())) return customer;
        return null;
    }
    public Vehicle findVehicle(int id) {
        for (Vehicle v : vehicles) if (v.getId() == id) return v;
        return null;
    }
    public Mechanic findMechanic(int id) {
        for (Mechanic m : mechanics) if (m.getId() == id) return m;
        return null;
    }
    public ServicePackage findPackage(int id) {
        for (ServicePackage p : packages) if (p.getId() == id) return p;
        return null;
    }
    public SparePart findSparePart(int id) {
        for (SparePart p : spareParts) if (p.getId() == id) return p;
        return null;
    }
    public int nextCustomerId() { return nextId(customers.size(), customers.stream().mapToInt(Customer::getId).max().orElse(0)); }
    public int nextVehicleId() { return nextId(vehicles.size(), vehicles.stream().mapToInt(Vehicle::getId).max().orElse(0)); }
    public int nextMechanicId() { return nextId(mechanics.size(), mechanics.stream().mapToInt(Mechanic::getId).max().orElse(0)); }
    public int nextPackageId() { return nextId(packages.size(), packages.stream().mapToInt(ServicePackage::getId).max().orElse(0)); }
    public int nextPartId() { return nextId(spareParts.size(), spareParts.stream().mapToInt(SparePart::getId).max().orElse(0)); }
    public int nextRecordId() { return nextId(records.size(), records.stream().mapToInt(ServiceRecord::getId).max().orElse(0)); }
    private int nextId(int size, int max) { return max + 1; }

    public boolean registrationExists(String registration) {
        for (Vehicle v : vehicles)
            if (v.getRegistrationNumber().equalsIgnoreCase(registration)) return true;
        return false;
    }
    public void addSamplePackages() {
        if (packages.isEmpty()) {
            packages.add(new ServicePackage(1, "Basic Service", "Oil and filter check, general inspection", 1.5, 600));
            packages.add(new ServicePackage(2, "Full Service", "Complete inspection and maintenance", 3.0, 700));
            packages.add(new ServicePackage(3, "Engine Service", "Engine diagnostics and tune-up", 2.5, 800));
        }
    }
}
