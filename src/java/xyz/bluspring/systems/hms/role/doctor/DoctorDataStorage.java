package xyz.bluspring.systems.hms.role.doctor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class DoctorDataStorage {

    private static final String DATA_FOLDER = "data";

    private static final String VITAL_SIGNS_FILE =
        DATA_FOLDER + File.separator + "vital_signs.txt";

    private static final String CONSULTATION_NOTES_FILE =
        DATA_FOLDER + File.separator + "consultation_notes.txt";

    private static final String PRESCRIPTIONS_FILE =
        DATA_FOLDER + File.separator + "prescriptions.txt";

    private static final String MEDICAL_TESTS_FILE =
        DATA_FOLDER + File.separator + "medical_test_requests.txt";


    public DoctorDataStorage() {
        createDataFolder();
    }


    private void createDataFolder() {

        File folder = new File(DATA_FOLDER);

        if (!folder.exists()) {
            folder.mkdirs();
        }
    }


    public void saveVitalSign(VitalSign vitalSign) {

        try (PrintWriter writer =
                 new PrintWriter(
                     new FileWriter(VITAL_SIGNS_FILE, true))) {

            writer.println(vitalSign.toString());

        } catch (IOException e) {

            System.out.println(
                "Error saving vital sign: "
                    + e.getMessage()
            );
        }
    }


    public void saveConsultationNote(ConsultationNote note) {

        try (PrintWriter writer =
                 new PrintWriter(
                     new FileWriter(CONSULTATION_NOTES_FILE, true))) {

            writer.println(note.toString());

        } catch (IOException e) {

            System.out.println(
                "Error saving consultation note: "
                    + e.getMessage()
            );
        }
    }


    public void savePrescription(Prescription prescription) {

        try (PrintWriter writer =
                 new PrintWriter(
                     new FileWriter(PRESCRIPTIONS_FILE, true))) {

            writer.println(prescription.toString());

        } catch (IOException e) {

            System.out.println(
                "Error saving prescription: "
                    + e.getMessage()
            );
        }
    }


    public void saveMedicalTestRequest(
        MedicalTestRequest request) {

        try (PrintWriter writer =
                 new PrintWriter(
                     new FileWriter(MEDICAL_TESTS_FILE, true))) {

            writer.println(request.toString());

        } catch (IOException e) {

            System.out.println(
                "Error saving medical test request: "
                    + e.getMessage()
            );
        }
    }
}
