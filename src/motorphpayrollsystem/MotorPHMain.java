package motorphpayrollsystem;

import java.util.ArrayList;
import java.util.List;

/**
 * Console demo of the OOP backend. Reads the real employee CSV, so every value
 * printed here matches the Employee Records screen. No GUI code, no payroll math.
 *
 * Usage (from the repo root):
 *   java -cp "out;src;lib/*" motorphpayrollsystem.MotorPHMain
 *   java -cp "out;src;lib/*" motorphpayrollsystem.MotorPHMain "path/to/MotorPH_Employee Data.csv"
 */
public class MotorPHMain {

    private static final String DEFAULT_CSV = "MotorPH_Employee Data.csv";
    private static final double DEMO_HOURS = 160.0; // assumed hours for the demo payslip

    public static void main(String[] args) {
        String csvPath = args.length > 0 ? args[0] : DEFAULT_CSV;

        List<Employee> all = new EmployeeDao(csvPath).loadEmployees();
        if (all.isEmpty()) {
            System.err.println("No employees loaded from: " + csvPath);
            System.err.println("Run from the repo root, or pass the CSV path as the first argument.");
            return;
        }

        int plain = 0, hr = 0, payroll = 0;
        Employee firstPlain = null, firstHr = null, firstPayroll = null;
        for (Employee e : all) {
            if (e instanceof HRStaff) {
                hr++;
                if (firstHr == null) firstHr = e;
            } else if (e instanceof PayrollStaff) {
                payroll++;
                if (firstPayroll == null) firstPayroll = e;
            } else {
                plain++;
                if (firstPlain == null) firstPlain = e;
            }
        }

        System.out.println("=== CSV loader (EmployeeDao) ===");
        System.out.println("Loaded " + all.size() + " employees: "
            + hr + " HRStaff, " + payroll + " PayrollStaff, " + plain + " Employee");

        // Payroll pipeline on the first real employee.
        Employee first = all.get(0);
        PayrollProcessor processor = new PayrollProcessor();
        PayrollProcessor.PayrollResult result = processor.process(first, DEMO_HOURS);

        System.out.println();
        System.out.println("First row payslip (" + DEMO_HOURS + " hrs assumed):");
        if (result != null) System.out.println(result.toPayslip());
        System.out.println();
        System.out.println("Hourly rate: " + first.getHourlyRate());
        System.out.println("Semi-monthly rate: " + first.getSemiMonthlyRate());

        // Invalid-input test: should print a rejection, not crash.
        System.out.println();
        System.out.println("Testing invalid hours:");
        PayrollProcessor.PayrollResult badResult = processor.process(first, -5.0);
        System.out.println("Result for -5 hours: " + badResult); // expect: null

        // Inheritance + polymorphism: one real employee of each runtime type.
        // Two separate permission checks, as agreed after MS1 feedback:
        // HRStaff -> true/false, PayrollStaff -> false/true, Employee -> false/false.
        System.out.println();
        System.out.println("=== Role-based access test (Inheritance + Polymorphism) ===");
        List<Employee> samples = new ArrayList<>();
        if (firstPlain != null) samples.add(firstPlain);
        if (firstHr != null) samples.add(firstHr);
        if (firstPayroll != null) samples.add(firstPayroll);

        for (Employee e : samples) {
            System.out.println(e.getEmpNo() + " " + e.getFirstName() + " " + e.getLastName()
                + " (" + e.getClass().getSimpleName() + ")"
                + " - canAccessHRAdmin: " + e.canAccessHRAdmin()
                + " - canAccessPayrollAdmin: " + e.canAccessPayrollAdmin()
                + " - modules: " + e.getAvailableModules());
        }
    }
}
