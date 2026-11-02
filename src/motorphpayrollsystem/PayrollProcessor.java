package motorphpayrollsystem;

public class PayrollProcessor {

    /**
     * Runs the payroll computation sequence for one employee.
     * Returns null if hoursWorked is invalid (negative); throws
     * IllegalArgumentException if emp is null (programming error).
     */
    public PayrollResult process(Employee emp, double hoursWorked) {
        if (emp == null) {
            throw new IllegalArgumentException("Employee must not be null.");
        }
        if (!emp.validateHoursWorked(hoursWorked)) {
            return null;
        }

        PayrollResult r = new PayrollResult();
        r.empNo = emp.getEmpNo();
        r.firstName = emp.getFirstName();
        r.lastName = emp.getLastName();
        r.position = emp.getPosition();

        r.hoursWorked = hoursWorked;
        r.basicSalary = emp.getBasicSalary();
        r.hourlyRate = emp.getHourlyRate();

        r.riceSubsidy = emp.getRiceSubsidy();
        r.phoneAllowance = emp.getPhoneAllowance();
        r.clothingAllowance = emp.getClothingAllowance();
        r.totalAllowances = r.riceSubsidy + r.phoneAllowance + r.clothingAllowance;

        r.grossPay = emp.computeGrossPay(hoursWorked);
        r.sss = emp.computeSSS();
        r.philHealth = emp.computePhilHealth();
        r.pagIbig = emp.computePagIBIG();
        r.tax = emp.computeTax(r.grossPay);
        r.totalDeductions = r.sss + r.philHealth + r.pagIbig + r.tax;
        r.netPay = emp.computeNetPay(hoursWorked);

        return r;
    }

    /** Holds computed values only. The GUI reads fields and displays them. */
    public static class PayrollResult {
        public String empNo, firstName, lastName, position;
        public double hoursWorked, basicSalary, hourlyRate;
        public double riceSubsidy, phoneAllowance, clothingAllowance, totalAllowances;
        public double grossPay, sss, philHealth, pagIbig, tax, totalDeductions, netPay;

        public String toPayslip() {
            String ls = System.lineSeparator();
            return String.format(
                "╔══════════════════════════════════════════╗" + ls +
                "║         MOTORPH PAYROLL SYSTEM           ║" + ls +
                "║              PAYSLIP                     ║" + ls +
                "╠══════════════════════════════════════════╣" + ls +
                "  Employee No : %s" + ls +
                "  Name        : %s, %s" + ls +
                "  Position    : %s" + ls +
                "──────────────────────────────────────────" + ls +
                "  Hours Worked        : %.2f hrs" + ls +
                "  GROSS PAY           : P%,.2f" + ls +
                "──────────────────────────────────────────" + ls +
                "  SSS                 : P%,.2f" + ls +
                "  PhilHealth          : P%,.2f" + ls +
                "  Pag-IBIG            : P%,.2f" + ls +
                "  Withholding Tax     : P%,.2f" + ls +
                "  TOTAL DEDUCTIONS    : P%,.2f" + ls +
                "══════════════════════════════════════════" + ls +
                "  NET PAY             : P%,.2f" + ls +
                "╚══════════════════════════════════════════╝",
                empNo, lastName, firstName, position,
                hoursWorked, grossPay,
                sss, philHealth, pagIbig, tax, totalDeductions, netPay);
        }
    }
}
