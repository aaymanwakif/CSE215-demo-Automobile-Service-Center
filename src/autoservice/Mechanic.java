package autoservice;

import java.io.Serializable;

public class Mechanic implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id, completedJobs;
    private String name, phone, specialization;
    private double basicSalary, bonusPerJob;

    public Mechanic(int id, String name, String phone, String specialization,
                    double basicSalary, double bonusPerJob) {
        this(id, name, phone, specialization, basicSalary, bonusPerJob, 0);
    }

    public Mechanic(int id, String name, String phone, String specialization,
                    double basicSalary, double bonusPerJob, int completedJobs) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.specialization = specialization;
        this.basicSalary = basicSalary;
        this.bonusPerJob = bonusPerJob;
        this.completedJobs = completedJobs;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getPhone() { return phone; }
    public String getSpecialization() { return specialization; }
    public int getCompletedJobs() { return completedJobs; }
    public double getBasicSalary() { return basicSalary; }
    public double getBonusPerJob() { return bonusPerJob; }
    public void completeJob() { completedJobs++; }
    public double calculateMonthlySalary() { return basicSalary + completedJobs * bonusPerJob; }

    @Override
    public String toString() {
        return String.format("ID: %d | %s | %s | %s | Jobs: %d | Basic salary: Tk %.2f",
                id, name, phone, specialization, completedJobs, basicSalary);
    }
}
