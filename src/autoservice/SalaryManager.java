package autoservice;

public class SalaryManager {
    public double calculateSalary(Mechanic mechanic) {
        return mechanic.calculateMonthlySalary();
    }

    public void printSalary(Mechanic mechanic) {
        System.out.println("\n========== MECHANIC PAYSLIP ==========");
        System.out.println("Mechanic       : " + mechanic.getName());
        System.out.println("Completed jobs : " + mechanic.getCompletedJobs());
        System.out.printf("Basic salary   : Tk %.2f%n", mechanic.getBasicSalary());
        System.out.printf("Performance bonus: Tk %.2f%n",
                mechanic.getCompletedJobs() * mechanic.getBonusPerJob());
        System.out.printf("TOTAL SALARY   : Tk %.2f%n", calculateSalary(mechanic));
        System.out.println("======================================\n");
    }
}
