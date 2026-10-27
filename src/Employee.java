// =============================================================================
// Employee.java — MotorPH OOP Backend, Milestone 2
//
// Parent class holding shared payroll attributes and computation methods.
// All formulas below are copied as-is from the CP2 procedural implementation
// (Payroll.java, static methods computeSSS/computePhilHealth/computePagIbig/
// computeWithholdingTax and the full compute() pipeline) so that this OOP
// version produces IDENTICAL output to CP2, per the Week 7 reading's
// requirement: "Reuse the behavior, not the code."
//
// No GUI code, no CSV logic, no role-specific branching here — those belong
// elsewhere (GUI layer, EmployeeData/CSV loader, and subclasses respectively).
// =============================================================================

public class Employee {

    // ── Standard working time constants (from CP2 Payroll.java) ────────────
    public static final double WORKING_DAYS_PER_MONTH  = 22.0;
    public static final double WORKING_HOURS_PER_DAY   = 8.0;
    public static final double WORKING_HOURS_PER_MONTH =
        WORKING_DAYS_PER_MONTH * WORKING_HOURS_PER_DAY; // 176

    // ── Shared attributes ───────────────────────────────────────────────────
    private String empNo;
    private String firstName;
    private String lastName;
    private String position;
    private String department;

    private double basicSalary;
    private double hourlyRate;
    private double semiMonthlyRate;

    private double riceSubsidy;
    private double phoneAllowance;
    private double clothingAllowance;

    // ── Constructor ──────────────────────────────────────────────────────
    public Employee(String empNo, String firstName, String lastName,
                     String position, String department,
                     double basicSalary, double hourlyRate, double semiMonthlyRate,
                     double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        this.empNo = empNo;
        this.firstName = firstName;
        this.lastName = lastName;
        this.position = position;
        this.department = department;

        // Enforce the same arithmetic relationship CP2 enforces:
        // if hourly/semiMonthly are missing, derive them from basic.
        this.basicSalary = basicSalary;
        this.hourlyRate = (hourlyRate > 0) ? hourlyRate : basicToHourly(basicSalary);
        this.semiMonthlyRate = (semiMonthlyRate > 0) ? semiMonthlyRate : basicToSemiMonthly(basicSalary);

        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
    }

    // ── Getters / setters ───────────────────────────────────────────────
    public String getEmpNo() { return empNo; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPosition() { return position; }
    public String getDepartment() { return department; }
    public double getBasicSalary() { return basicSalary; }
    public double getHourlyRate() { return hourlyRate; }
    public double getSemiMonthlyRate() { return semiMonthlyRate; }
    public double getRiceSubsidy() { return riceSubsidy; }
    public double getPhoneAllowance() { return phoneAllowance; }
    public double getClothingAllowance() { return clothingAllowance; }

    public void setHourlyRate(double hourlyRate) {
        if (hourlyRate < 0) {
            System.out.println("Rejected: hourly rate cannot be negative.");
            return;
        }
        this.hourlyRate = hourlyRate;
    }

    // ── Arithmetic relationships (identical to CP2 Payroll.java) ───────────
    public static double basicToSemiMonthly(double basicSalary) {
        return basicSalary / 2.0;
    }

    public static double basicToHourly(double basicSalary) {
        return basicSalary / WORKING_HOURS_PER_MONTH;
    }

    public static double hourlyToBasic(double hourlyRate) {
        return hourlyRate * WORKING_HOURS_PER_MONTH;
    }

    public static double semiMonthlyToBasic(double semiMonthly) {
        return semiMonthly * 2.0;
    }

    // ── Shared computation methods ──────────────────────────────────────
    // These are "parent-type methods": called the same way for every
    // employee, with no per-subclass override needed, because CP2 itself
    // has no type-specific variation in these formulas.

    /** Validates hours worked. Returns true if valid (non-negative). */
    public boolean validateHoursWorked(double hoursWorked) {
        if (hoursWorked < 0) {
            System.out.println("Rejected: hours worked cannot be negative.");
            return false;
        }
        return true;
    }

    public double computeGrossPay(double hoursWorked) {
        double allowances = riceSubsidy + phoneAllowance + clothingAllowance;
        return (hoursWorked * hourlyRate) + allowances;
    }

    // NOTE on deduction amounts: CP2's Payroll.compute() calls the monthly
    // table functions and then divides by 2, because payroll is run per
    // semi-monthly period. The three public computeSSS / computePhilHealth /
    // computePagIBIG methods below return that per-period (semi-monthly)
    // share, so the PayrollProcessor result matches CP2 exactly.

    /** Semi-monthly SSS share: monthly table lookup (CP2 computeSSS) divided by 2. */
    public double computeSSS() {
        return sssMonthly() / 2;
    }

    /** SSS monthly table lookup — copied verbatim from CP2 Payroll.computeSSS(). */
    private double sssMonthly() {
        double monthly = semiMonthlyRate * 2;
        if (monthly <  3250) return  135.00;
        if (monthly <  3750) return  157.50;
        if (monthly <  4250) return  180.00;
        if (monthly <  4750) return  202.50;
        if (monthly <  5250) return  225.00;
        if (monthly <  5750) return  247.50;
        if (monthly <  6250) return  270.00;
        if (monthly <  6750) return  292.50;
        if (monthly <  7250) return  315.00;
        if (monthly <  7750) return  337.50;
        if (monthly <  8250) return  360.00;
        if (monthly <  8750) return  382.50;
        if (monthly <  9250) return  405.00;
        if (monthly <  9750) return  427.50;
        if (monthly < 10250) return  450.00;
        if (monthly < 10750) return  472.50;
        if (monthly < 11250) return  495.00;
        if (monthly < 11750) return  517.50;
        if (monthly < 12250) return  540.00;
        if (monthly < 12750) return  562.50;
        if (monthly < 13250) return  585.00;
        if (monthly < 13750) return  607.50;
        if (monthly < 14250) return  630.00;
        if (monthly < 14750) return  652.50;
        if (monthly < 15250) return  675.00;
        if (monthly < 15750) return  697.50;
        if (monthly < 16250) return  720.00;
        if (monthly < 16750) return  742.50;
        if (monthly < 17250) return  765.00;
        if (monthly < 17750) return  787.50;
        if (monthly < 18250) return  810.00;
        if (monthly < 18750) return  832.50;
        if (monthly < 19250) return  855.00;
        if (monthly < 19750) return  877.50;
        if (monthly < 20250) return  900.00;
        if (monthly < 20750) return  922.50;
        if (monthly < 21250) return  945.00;
        if (monthly < 21750) return  967.50;
        if (monthly < 22250) return  990.00;
        if (monthly < 22750) return 1012.50;
        if (monthly < 23250) return 1035.00;
        if (monthly < 23750) return 1057.50;
        if (monthly < 24250) return 1080.00;
        if (monthly < 24750) return 1102.50;
        return 1125.00;
    }

    /** Semi-monthly PhilHealth share: 1.5% of monthly salary, capped at P900 monthly, then / 2. */
    public double computePhilHealth() {
        double monthly = semiMonthlyRate * 2;
        return Math.min(monthly * 0.015, 900.00) / 2;
    }

    /** Semi-monthly Pag-IBIG share: 1% if monthly <= 1500, else 2% capped at P100 monthly, then / 2. */
    public double computePagIBIG() {
        double monthly = semiMonthlyRate * 2;
        if (monthly <= 1500) return (monthly * 0.01) / 2;
        return Math.min(monthly * 0.02, 100.00) / 2;
    }

    /** BIR withholding tax on taxable income (semi-monthly basis). */
    public double computeTax(double grossPay) {
        double taxableMonthly = Math.max(0,
            (grossPay - computeSSS() - computePhilHealth() - computePagIBIG()) * 2);

        double taxMonthly;
        if (taxableMonthly <=  20833) taxMonthly = 0;
        else if (taxableMonthly <=  33333) taxMonthly = (taxableMonthly - 20833)   * 0.20;
        else if (taxableMonthly <=  66667) taxMonthly = 2500  + (taxableMonthly - 33333)  * 0.25;
        else if (taxableMonthly <= 166667) taxMonthly = 10833 + (taxableMonthly - 66667)  * 0.30;
        else if (taxableMonthly <= 666667) taxMonthly = 40833 + (taxableMonthly - 166667) * 0.32;
        else taxMonthly = 200833 + (taxableMonthly - 666667) * 0.35;

        return taxMonthly / 2;
    }

    public double computeNetPay(double hoursWorked) {
        double grossPay = computeGrossPay(hoursWorked);
        double totalDeductions = computeSSS() + computePhilHealth()
                                + computePagIBIG() + computeTax(grossPay);
        return Math.max(0, grossPay - totalDeductions);
    }

    // ── Role-based access (Week 4/5 — Inheritance & Polymorphism) ──────────
    // Default behavior for a plain Employee: no admin access of any kind,
    // core modules only. HRStaff and PayrollStaff each override the ONE
    // permission check that applies to them, leaving the other inherited
    // as false — replacing the scattered EmployeeData.isHR(emp)/isPayroll(emp)
    // checks that currently live in DashboardUI, AttendanceUI, ProfileUI,
    // PayrollAdminUI, editProfileUI.
    //
    // Mentor feedback on MS1 (Week 4/5 CRC review) flagged that a single
    // canAccessAdminModule() boolean, returned true by both HRStaff and
    // PayrollStaff, conflates two different administrative scopes: HR
    // administration (employee records, leave approval) is not the same
    // privilege as payroll administration (deduction computation, bulk
    // payroll runs). This is split into two named checks instead of one
    // generic flag so each subclass only grants the permission it actually
    // has.

    public boolean canAccessHRAdmin() {
        return false;
    }

    public boolean canAccessPayrollAdmin() {
        return false;
    }

    public java.util.List<String> getAvailableModules() {
        return java.util.Arrays.asList("Profile", "Payroll", "Attendance");
    }
}
