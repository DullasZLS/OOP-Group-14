// =============================================================================
// MotorPHMain.java — Console test harness for Employee + PayrollProcessor
//
// Week 8 requirement: "Perform console testing before touching the GUI."
// This uses a sample employee so you (and Vic) can verify the OOP backend
// produces sane output before anyone wires it to the real CSV data or GUI.
//
// To verify against CP2 exactly: swap these sample values for a real row
// from your employees.csv and compare this output to what CP2's
// Payroll.compute() prints for the same employee + hours.
// =============================================================================

public class MotorPHMain {
    public static void main(String[] args) {

        // Sample employee — replace with a real CSV row to verify against CP2.
        Employee emp = new Employee(
            "10001", "Juan", "Dela Cruz",
            "Rank and File", "IT",
            20000.0,   // basicSalary
            0,         // hourlyRate — 0 means "derive from basic" (CP2 behavior)
            0,         // semiMonthlyRate — same
            1500.0,    // riceSubsidy
            1000.0,    // phoneAllowance
            1000.0     // clothingAllowance
        );

        PayrollProcessor processor = new PayrollProcessor();
        PayrollProcessor.PayrollResult result = processor.process(emp, 88.0); // 88 hrs = half month

        System.out.println(result.toPayslip());

        // Sanity checks worth confirming by hand before trusting this:
        System.out.println();
        System.out.println("Derived hourly rate: " + emp.getHourlyRate());       // should be 20000/176 = 113.64
        System.out.println("Derived semi-monthly: " + emp.getSemiMonthlyRate()); // should be 20000/2 = 10000

        // Invalid-input test — should print a rejection, not crash
        System.out.println();
        System.out.println("Testing invalid hours:");
        PayrollProcessor.PayrollResult badResult = processor.process(emp, -5.0);
        System.out.println("Result for -5 hours: " + badResult); // expect: null

        // =====================================================================
        // Week 5 requirement: console proof of inheritance + polymorphism.
        // Three Employee-typed references, three different runtime types.
        // Each call below dispatches to the right override with no
        // isHR()/isPayroll() branching anywhere in this method.
        //
        // Two SEPARATE permission checks are tested (canAccessHRAdmin and
        // canAccessPayrollAdmin) per mentor feedback on MS1: HRStaff must
        // show true/false and PayrollStaff must show false/true — proving
        // the two roles are NOT interchangeable the way a single
        // canAccessAdminModule() boolean would have implied.
        // =====================================================================
        System.out.println();
        System.out.println("=== Role-based access test (Inheritance + Polymorphism) ===");

        Employee plainEmp = emp; // runtime type: Employee
        Employee hrEmp = new HRStaff(
            "10002", "Maria", "Santos",
            "HR Rank and File", "HR",
            18000.0, 0, 0,
            1500.0, 1000.0, 1000.0
        ); // runtime type: HRStaff
        Employee payrollEmp = new PayrollStaff(
            "10003", "Jose", "Reyes",
            "Payroll Rank and File", "Payroll",
            18000.0, 0, 0,
            1500.0, 1000.0, 1000.0
        ); // runtime type: PayrollStaff

        for (Employee e : new Employee[]{plainEmp, hrEmp, payrollEmp}) {
            System.out.println(e.getFirstName() + " " + e.getLastName()
                + " (" + e.getClass().getSimpleName() + ")"
                + " — canAccessHRAdmin: " + e.canAccessHRAdmin()
                + " — canAccessPayrollAdmin: " + e.canAccessPayrollAdmin()
                + " — modules: " + e.getAvailableModules());
        }

        // =====================================================================
        // CSV loader test. Optional: pass the CSV path as the first argument,
        //   java MotorPHMain "path/to/MotorPH_Employee Data.csv"
        // Loads real rows, counts how many became each runtime type, and runs
        // the payroll pipeline on the first employee as a real-data check.
        // =====================================================================
        if (args.length > 0) {
            System.out.println();
            System.out.println("=== CSV loader test (EmployeeDao) ===");

            java.util.List<Employee> all = new EmployeeDao(args[0]).loadEmployees();
            int plain = 0, hr = 0, payroll = 0;
            for (Employee e : all) {
                if (e instanceof HRStaff) hr++;
                else if (e instanceof PayrollStaff) payroll++;
                else plain++;
            }
            System.out.println("Loaded " + all.size() + " employees: "
                + hr + " HRStaff, " + payroll + " PayrollStaff, " + plain + " Employee");

            if (!all.isEmpty()) {
                Employee first = all.get(0);
                System.out.println();
                System.out.println("First row payslip (160 hrs):");
                PayrollProcessor.PayrollResult r = processor.process(first, 160.0);
                if (r != null) System.out.println(r.toPayslip());
            }
        }
    }
}
