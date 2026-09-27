package xyz.bluspring.systems.hms.role.manager;

import java.util.List;
import javax.swing.JPanel;

import xyz.bluspring.systems.hms.data.ManagerDataStorage;
import xyz.bluspring.systems.hms.data.RoleManager;
import xyz.bluspring.systems.hms.role.PersonalizableUser;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

public class MedicalManager extends PersonalizableUser<MedicalManager> {
    public static final DataSerializer<MedicalManager> SERIALIZER = RecordDataSerializer.of(
        Profile.SERIALIZER, MedicalManager::getProfile,
        MedicalManager::new
    );

    public static final DataSerializer<MedicalManager> REFERENCE_SERIALIZER = DataSerializer.STRING.map(RoleManager.INSTANCE::findManagerById, MedicalManager::getManagerId);

    public MedicalManager(Profile profile) {
        super(profile);
    }

    @Override
    public DataSerializer<MedicalManager> getSerializer() {
        return SERIALIZER;
    }

    public String getManagerId() {
        return this.getProfile().getId().toString();
    }

    // Updates manager profile details
    public void updateProfile(String displayName, String address) {
        getProfile().setDisplayName(displayName);
        getProfile().setAddress(address);
    }

    // Add and save a new clinical department
    public void addDepartment(Department department) {
        ManagerDataStorage.INSTANCE.saveDepartment(department);
    }

    // Find department by its ID
    public Department getDepartmentById(String id) {
        for (Department d : ManagerDataStorage.INSTANCE.readDepartments()) {
            if (d.getId().equalsIgnoreCase(id.trim())) {
                return d;
            }
        }
        return null;
    }

    // Update an existing department by its ID
    public boolean updateDepartment(String id, String newName, String newDescription) {
        for (Department d : ManagerDataStorage.INSTANCE.readDepartments()) {
            if (d.getId().equalsIgnoreCase(id.trim())) {
                d.setDepartmentName(newName);
                d.setDescription(newDescription);
                ManagerDataStorage.INSTANCE.save();
                return true;
            }
        }
        return false;
    }

    public List<Department> getDepartments() {
        return ManagerDataStorage.INSTANCE.readDepartments();
    }

    // Add and save a new doctor shift roster
    public void addShift(DoctorShift shift) {
        ManagerDataStorage.INSTANCE.saveShift(shift);
    }

    // Find doctor shift by its ID
    public DoctorShift getShiftById(String id) {
        for (DoctorShift s : ManagerDataStorage.INSTANCE.readShifts()) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                return s;
            }
        }
        return null;
    }

    // Modify a doctor shift by its ID
    public boolean updateShift(String id, Doctor doctor, String newDepartment, String newDate, String newShiftType) {
        for (DoctorShift s : ManagerDataStorage.INSTANCE.readShifts()) {
            if (s.getId().equalsIgnoreCase(id.trim())) {
                s.setDoctor(doctor);
                s.setDepartmentName(newDepartment);
                s.setShiftDate(newDate);
                s.setShiftType(newShiftType);
                ManagerDataStorage.INSTANCE.save();
                return true;
            }
        }
        return false;
    }

    public List<DoctorShift> getDoctorShifts() {
        return ManagerDataStorage.INSTANCE.readShifts();
    }

    public ManagerDataStorage getStorage() {
        return ManagerDataStorage.INSTANCE;
    }

    public List<Doctor> getAssignedDoctors() {
        return RoleManager.INSTANCE.getDoctors().stream().filter(e -> e.getAssignedManager() == this).toList();
    }

    public void assignDoctor(Doctor doctor) {
        doctor.setAssignedManager(this);
        RoleManager.INSTANCE.save();
    }

    // Calculates estimated revenue based on total consultations and consultation rate
    public double calculateEstimatedRevenue(double baseRate) {
        int consultations = ManagerDataStorage.INSTANCE.getConsultationCount();
        return consultations * baseRate;
    }

    public JPanel createDashboardUI() {
        return new ManagerDashboard(this);
    }
}
