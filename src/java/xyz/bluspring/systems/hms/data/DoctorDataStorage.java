package xyz.bluspring.systems.hms.data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xyz.bluspring.systems.hms.role.doctor.ConsultationNote;
import xyz.bluspring.systems.hms.role.doctor.MedicalTestRequest;
import xyz.bluspring.systems.hms.role.doctor.VitalSign;
import xyz.bluspring.systems.hms.utils.data.DataSerializers;

public class DoctorDataStorage {
    public static final DoctorDataStorage INSTANCE = new DoctorDataStorage();

    private static final File VITAL_SIGNS_FILE = DataSerializers.getPath("vital_signs.txt");
    private static final File CONSULTATION_NOTES_FILE = DataSerializers.getPath("consultation_notes.txt");
    private static final File MEDICAL_TESTS_FILE = DataSerializers.getPath("medical_test_requests.txt");

    private final List<VitalSign> vitalSigns = new ArrayList<>();
    private final List<ConsultationNote> consultationNotes = new ArrayList<>();
    private final List<MedicalTestRequest> medicalTestRequests = new ArrayList<>();

    private DoctorDataStorage() {
    }

    public List<VitalSign> getVitalSigns() {
        return vitalSigns;
    }

    public List<ConsultationNote> getConsultationNotes() {
        return consultationNotes;
    }

    public List<MedicalTestRequest> getMedicalTestRequests() {
        return medicalTestRequests;
    }

    public void saveVitalSign(VitalSign vitalSign) {
        this.vitalSigns.add(vitalSign);
    }

    public void saveConsultationNote(ConsultationNote note) {
        this.consultationNotes.add(note);
    }

    public void saveMedicalTestRequest(MedicalTestRequest request) {
        this.medicalTestRequests.add(request);
    }

    public void load() {
        DataSerializers.deserializeLines(VitalSign.SERIALIZER, VITAL_SIGNS_FILE, vitalSigns);
        DataSerializers.deserializeLines(ConsultationNote.SERIALIZER, CONSULTATION_NOTES_FILE, consultationNotes);
        DataSerializers.deserializeLines(MedicalTestRequest.SERIALIZER, MEDICAL_TESTS_FILE, medicalTestRequests);
    }

    public void save() {
        DataSerializers.serializeValues(VITAL_SIGNS_FILE, vitalSigns);
        DataSerializers.serializeValues(CONSULTATION_NOTES_FILE, consultationNotes);
        DataSerializers.serializeValues(MEDICAL_TESTS_FILE, medicalTestRequests);
    }
}
