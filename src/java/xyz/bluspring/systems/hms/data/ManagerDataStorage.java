package xyz.bluspring.systems.hms.data;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.role.manager.Department;
import xyz.bluspring.systems.hms.role.manager.DoctorShift;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class ManagerDataStorage {
    public static final ManagerDataStorage INSTANCE = new ManagerDataStorage();

    private static final File DEPARTMENT_FILE = DataSerializers.getPath("departments.txt");
    private static final File ROSTER_FILE = DataSerializers.getPath("doctor_rosters.txt");
    private static final File CONSULTATION_NOTES_FILE = DataSerializers.getPath("consultation_notes.txt");

    private final List<Department> departments = new ArrayList<>();
    private final List<DoctorShift> doctorShifts = new ArrayList<>();

    private ManagerDataStorage() {
    }

    public void load() {
        departments.clear();
        doctorShifts.clear();

        DataSerializers.deserializeLines(Department.SERIALIZER, DEPARTMENT_FILE, departments);
        DataSerializers.deserializeLines(DoctorShift.SERIALIZER, ROSTER_FILE, doctorShifts);
    }

    public void save() {
        DataSerializers.serializeValues(Department.SERIALIZER, DEPARTMENT_FILE, departments);
        DataSerializers.serializeValues(DoctorShift.SERIALIZER, ROSTER_FILE, doctorShifts);
    }

    // Saves the department to departments.txt file.
    public void saveDepartment(Department department) {
        if (!departments.contains(department)) {
            departments.add(department);
        }
        this.save();
    }

    // Read all departments from memory (loaded from file)
    public List<Department> readDepartments() {
        return departments;
    }

    // Save a new doctor shift roster to file
    public void saveShift(DoctorShift shift) {
        if (!doctorShifts.contains(shift)) {
            doctorShifts.add(shift);
        }
        this.save();
    }

    // Read all shifts from memory (loaded from file)
    public List<DoctorShift> readShifts() {
        return doctorShifts;
    }

    // Count total consultations from doctor file for metrics
    public int getConsultationCount() {
        try {
            if (!CONSULTATION_NOTES_FILE.exists()) {
                return 0;
            }

            return Files.readAllLines(CONSULTATION_NOTES_FILE.toPath()).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
