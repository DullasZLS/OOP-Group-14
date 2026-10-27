public class PayrollProcessor {

    /**
     * Runs the full payroll computation for one employee and returns a
     * PayrollResult (used by Main for console testing, and later by the
     * GUI for display — the GUI never computes anything itself).
     */
    public PayrollResult process(Employee emp, double hoursWorked) {
        if (!emp.validateHoursWorked(hoursWorked)) {
            return null; // caller decides how to report invalid input
        }

        PayrollResult r = new PayrollResult();
        r.empNo = emp.getEmpNo();
        r.firstName = emp.getFirstName();
        r.lastName = emp.getLastName();
        r.position = emp.getPosition();

        r.hoursWorked = hoursWorked;
        r.basicSalary = emp.getBasicSalary();
        r.hourlyRate = emp.getHourlyRate();

        r.grossPay = emp.computeGrossPay(hoursWorked);
        r.sss = emp.computeSSS();
        r.philHealth = emp.computePhilHealth();
        r.pagIbig = emp.computePagIBIG();
        r.tax = emp.computeTax(r.grossPay);
        r.totalDeductions = r.sss + r.philHealth + r.pagIbig + r.tax;
        r.netPay = emp.computeNetPay(hoursWorked);

        return r;
    }

    // ── Result holder (keeps the GUI/console layer dumb — it only reads
    //    fields and displays them, it never computes) ───────────────────
    public static class PayrollResult {
        public String empNo, firstName, lastName, position;
        public double hoursWorked, basicSalary, hourlyRate;
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
