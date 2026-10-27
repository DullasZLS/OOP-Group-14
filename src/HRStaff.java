public class HRStaff extends Employee {

    public HRStaff(String empNo, String firstName, String lastName,
                   String position, String department,
                   double basicSalary, double hourlyRate, double semiMonthlyRate,
                   double riceSubsidy, double phoneAllowance, double clothingAllowance) {
        super(empNo, firstName, lastName, position, department,
              basicSalary, hourlyRate, semiMonthlyRate,
              riceSubsidy, phoneAllowance, clothingAllowance);
    }

    // HR staff get HR administration privileges only — NOT payroll admin.
    // canAccessPayrollAdmin() is inherited unchanged from Employee (false).
    @Override
    public boolean canAccessHRAdmin() {
        return true;
    }

    @Override
    public java.util.List<String> getAvailableModules() {
        java.util.List<String> modules = new java.util.ArrayList<>(super.getAvailableModules());
        modules.add("Admin");
        return modules;
    }
}
