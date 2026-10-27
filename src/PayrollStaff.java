// =============================================================================
// PayrollStaff.java — MotorPH OOP Backend, Milestone 2 (Week 4/5)
//
// Child class of Employee. Inherits all shared employee fields and payroll
// computation methods unchanged — Payroll staff are paid exactly like any
// other employee (CP2's Payroll.compute() has no role-specific pay
// branching).
//
// What this class adds is role-based system ACCESS, per the Week 4/5 CRC
// cards and Method Dictionary: it overrides canAccessAdminModule() and
// getAvailableModules() to replace the scattered
// EmployeeData.isPayroll(emp) checks currently in PayrollAdminUI.
// =============================================================================

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
