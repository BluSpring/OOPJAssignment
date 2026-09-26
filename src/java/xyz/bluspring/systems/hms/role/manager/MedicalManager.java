package xyz.bluspring.systems.hms.role.manager;

import java.util.List;
import javax.swing.JPanel;
import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class MedicalManager extends PersonalizableUser<MedicalManager> {
    public static final DataSerializer<MedicalManager> SERIALIZER = RecordDataSerializer.of(
        Profile.SERIALIZER, MedicalManager::getProfile,
        MedicalManager::new
    );

    private List<Department> departments;
    private List<DoctorShift> doctorShifts;
    private ManagerDataStorage storage;

    public MedicalManager(Profile profile) {
        super(profile);

        this.storage = new ManagerDataStorage();
        this.departments = storage.readDepartments();
        this.doctorShifts = storage.readShifts();
    }

    @Override
    public DataSerializer<MedicalManager> getSerializer() {
        return SERIALIZER;
    }

    // Updates manager profile details
    public void updateProfile(String displayName, String address) {
        getProfile().setDisplayName(displayName);
        getProfile().setAddress(address);
    }

    // Add and save a new clinical department
    public void addDepartment(Department department) {
        departments.add(department);
        storage.saveDepartment(department);
    }

    // Find department by its ID
    public Department getDepartmentById(String id) {
        for (Department d : departments) {
            if (d.getId().equalsIgnoreCase(id.trim())) {
                return d;
            }
        }
        return null;
    }

    // Update an existing department by its ID
    public boolean updateDepartment(String id, String newName, String newDescription) {
        for (Department d : departments) {
            if (d.getId().equalsIgnoreCase(id.trim())) {
                d.setDepartmentName(newName);
                d.setDescription(newDescription);
                storage.rewriteDepartments(departments);
                return true;
            }
        }
        return false;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    // Add and save a new doctor shift roster
    public void addShift(DoctorShift shift) {
        doctorShifts.add(shift);
        storage.saveShift(shift);
    }

    // Find doctor shift by its ID
    public DoctorShift getShiftById(String id) {
        for (DoctorShift s : doctorShifts) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    // Modify a doctor shift by its ID
    public boolean updateShift(String id, String newDoctorName, String newDepartment, String newDate, String newShiftType) {
        for (DoctorShift s : doctorShifts) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                s.setDoctorName(newDoctorName);
                s.setDepartmentName(newDepartment);
                s.setShiftDate(newDate);
                s.setShiftType(newShiftType);
                storage.rewriteShifts(doctorShifts);
                return true;
            }
        }
        return false;
    }

    public List<DoctorShift> getDoctorShifts() {
        return doctorShifts;
    }

    public ManagerDataStorage getStorage() {
        return storage;
    }

    // Calculates estimated revenue based on total consultations and consultation rate
    public double calculateEstimatedRevenue(double baseRate) {
        int consultations = storage.getConsultationCount();
        return consultations * baseRate;
    }

    public JPanel createDashboardUI() {
        return new ManagerDashboard(this);
    }
}
