package motorphpayrollsystem;

public class PayrollStaff extends Employee {

    public PayrollStaff(String empNo, String firstName, String lastName,
                        String position, String department,
                        double basicSalary, double hourlyRate, double semiMonthlyRate,
                        double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        super(empNo, firstName, lastName, position, department,
              basicSalary, hourlyRate, semiMonthlyRate,
              riceSubsidy, phoneAllowance, clothingAllowance);
    }

    // Payroll staff get payroll administration privileges only — NOT HR admin.
    // canAccessHRAdmin() is inherited unchanged from Employee (false).
    @Override
    public boolean canAccessPayrollAdmin() {
        return true;
    }

    @Override
    public java.util.List<String> getAvailableModules() {
        java.util.List<String> modules = new java.util.ArrayList<>(super.getAvailableModules());
        modules.add("Bulk Payroll");
        return modules;
    }
}
