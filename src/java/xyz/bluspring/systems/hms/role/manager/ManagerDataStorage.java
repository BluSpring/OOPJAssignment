package xyz.bluspring.systems.hms.role.manager;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import xyz.bluspring.systems.hms.utils.data.DataSerializable;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class ManagerDataStorage implements DataSerializable {
    private static final String DATA_FOLDER = "data";
    private final File departmentFile = new File(DATA_FOLDER + File.separator + "departments.txt");
    private final File rosterFile = new File(DATA_FOLDER + File.separator + "doctor_rosters.txt");
    private static final String CONSULTATION_NOTES_FILE = DATA_FOLDER + File.separator + "consultation_notes.txt";

    private final List<Department> departments = new ArrayList<>();
    private final List<DoctorShift> doctorShifts = new ArrayList<>();

    public ManagerDataStorage() {
        createDataFolder();
        this.load();
    }

    // creates the data folder if it does not exist already.
    private void createDataFolder() {
        File folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }

    @Override
    public void load() {
        departments.clear();
        doctorShifts.clear();

        DataSerializers.deserializeLines(Department.class, departmentFile, departments);
        DataSerializers.deserializeLines(DoctorShift.class, rosterFile, doctorShifts);
    }

    @Override
    public void save() {
        DataSerializers.serializeValues(Department.class, departmentFile, departments);
        DataSerializers.serializeValues(DoctorShift.class, rosterFile, doctorShifts);
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

    // Overwrite the departments file when updated
    public void rewriteDepartments(List<Department> departments) {
        this.save();
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

    // Overwrite the shifts file when modified
    public void rewriteShifts(List<DoctorShift> shifts) {
        this.save();
    }

    // Count total consultations from doctor file for metrics
    public int getConsultationCount() {
        try {
            Path path = Path.of(CONSULTATION_NOTES_FILE);
            if (!Files.exists(path)) {
                return 0;
            }
            return Files.readAllLines(path).size();
        } catch (Exception e) {
            return 0;
        }
    }
}
