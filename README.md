# OOP-Group-14
Composed of: Eussef Maniebo, Zenith Lemuel Dullas, Christian Gabriel Narte, Franchesca Oceja

## How to run

Requirements: Java JDK. The data files `MotorPH_Employee Data.csv` and `attendance.csv` are in the repo root and the app reads them from there.

From the repo root (Windows PowerShell):

    mkdir out
    javac -encoding UTF-8 -cp "lib/*" -d out src\motorphpayrollsystem\*.java
    java -cp "out;src;lib/*" motorphpayrollsystem.loginUI

Log in with an Employee Number and the password from the CSV. The `src` entry on the classpath is required so the screens can load their images.

## Architecture (OOP backend)

- `Employee` is the parent class and holds the payroll formulas.
- `HRStaff` and `PayrollStaff` extend it and override `canAccessHRAdmin()` / `canAccessPayrollAdmin()`.
- `PayrollProcessor` runs the computation order and returns a `PayrollResult`.
- `EmployeeDao` reads the CSV and builds the right `Employee` subclass from the Department column.
- The GUI screens call these classes and do not compute payroll themselves.

## Known issues

See the Known Issues List tab of the Milestone 2 worksheet. `EmployeeRecords` still uses the CP2 `Payroll` static methods.
