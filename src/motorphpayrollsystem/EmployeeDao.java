package motorphpayrollsystem;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {

    // Column positions in MotorPH_Employee Data.csv (same as CP2 Payroll.java).
    private static final int COL_EMP_NO     = 0;
    private static final int COL_LAST_NAME  = 1;
    private static final int COL_FIRST_NAME = 2;
    private static final int COL_POSITION   = 11;
    private static final int COL_BASIC      = 13;
    private static final int COL_RICE       = 14;
    private static final int COL_PHONE      = 15;
    private static final int COL_CLOTHING   = 16;
    private static final int COL_SEMI       = 17;
    private static final int COL_HOURLY     = 18;
    private static final int COL_DEPARTMENT = 20;
    private static final int MIN_COLUMNS    = 21;

    private final Path csvPath;

    public EmployeeDao(String csvFilePath) {
        if (csvFilePath == null || csvFilePath.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV file path must not be empty.");
        }
        this.csvPath = Paths.get(csvFilePath.trim());
    }

    /**
     * Loads every valid employee row. Returns an empty list (never null) if
     * the file cannot be read; the reason is printed to stderr.
     */
    public List<Employee> loadEmployees() {
        List<Employee> employees = new ArrayList<>();

        if (!Files.isReadable(csvPath)) {
            System.err.println("EmployeeDao: cannot read file: " + csvPath.toAbsolutePath());
            return employees;
        }

        try (BufferedReader reader = Files.newBufferedReader(csvPath, StandardCharsets.UTF_8)) {
            String line = reader.readLine(); // header row
            int lineNo = 1;
            if (line == null) {
                System.err.println("EmployeeDao: file is empty: " + csvPath);
                return employees;
            }

            while ((line = reader.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) continue;

                String[] f = parseCsvLine(line);
                Employee emp = toEmployee(f, lineNo);
                if (emp != null) employees.add(emp);
            }
        } catch (IOException e) {
            System.err.println("EmployeeDao: error reading " + csvPath + ": " + e.getMessage());
        }
        return employees;
    }

    /** Returns the employee with the given number, or null if not found. */
    public Employee findByEmpNo(String empNo) {
        if (empNo == null) return null;
        for (Employee e : loadEmployees()) {
            if (e.getEmpNo().equals(empNo.trim())) return e;
        }
        return null;
    }

    // ── Row -> object ──────────────────────────────────────────────────────
    private Employee toEmployee(String[] f, int lineNo) {
        if (f.length < MIN_COLUMNS) {
            System.err.println("EmployeeDao: line " + lineNo + " skipped, expected "
                + MIN_COLUMNS + " columns but found " + f.length);
            return null;
        }

        String empNo = f[COL_EMP_NO].trim();
        if (empNo.isEmpty()) {
            System.err.println("EmployeeDao: line " + lineNo + " skipped, missing employee number");
            return null;
        }

        double basic = parseAmount(f[COL_BASIC]);
        if (basic <= 0) {
            System.err.println("EmployeeDao: line " + lineNo + " (" + empNo
                + ") skipped, basic salary missing or not positive");
            return null;
        }

        String firstName  = f[COL_FIRST_NAME].trim();
        String lastName   = f[COL_LAST_NAME].trim();
        String position   = f[COL_POSITION].trim();
        String department = f[COL_DEPARTMENT].trim();
        double hourly     = parseAmount(f[COL_HOURLY]);
        double semi       = parseAmount(f[COL_SEMI]);
        double rice       = parseAmount(f[COL_RICE]);
        double phone      = parseAmount(f[COL_PHONE]);
        double clothing   = parseAmount(f[COL_CLOTHING]);

        // Polymorphism starts here: one place decides the runtime type.
        if (department.equalsIgnoreCase("HR")) {
            return new HRStaff(empNo, firstName, lastName, position, department,
                               basic, hourly, semi, rice, phone, clothing);
        }
        if (department.equalsIgnoreCase("Payroll")) {
            return new PayrollStaff(empNo, firstName, lastName, position, department,
                                    basic, hourly, semi, rice, phone, clothing);
        }
        return new Employee(empNo, firstName, lastName, position, department,
                            basic, hourly, semi, rice, phone, clothing);
    }

    // ── Parsing helpers ────────────────────────────────────────────────────

    /** Null-safe, comma-tolerant number parse (same behavior as CP2). */
    static double parseAmount(String raw) {
        if (raw == null || raw.trim().isEmpty()) return 0.0;
        try {
            return Double.parseDouble(raw.trim().replace(",", "").trim());
        } catch (NumberFormatException e) {
            return 0.0;
        }
    }

    /** Splits one CSV line, honoring double quotes and "" escapes. */
    static String[] parseCsvLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (inQuotes) {
                if (c == '"') {
                    if (i + 1 < line.length() && line.charAt(i + 1) == '"') {
                        cur.append('"');
                        i++;
                    } else {
                        inQuotes = false;
                    }
                } else {
                    cur.append(c);
                }
            } else if (c == '"') {
                inQuotes = true;
            } else if (c == ',') {
                fields.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        fields.add(cur.toString());
        return fields.toArray(new String[0]);
    }
}
