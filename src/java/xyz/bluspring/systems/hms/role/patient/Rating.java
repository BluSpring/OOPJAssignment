package xyz.bluspring.systems.hms.role.patient;

import java.util.Date;

import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.data.DataSerializer;
import xyz.bluspring.systems.hms.utils.data.RecordDataSerializer;

/**
 * A patient's rating and comment about a doctor or clinic visit.
 */
public class Rating {
    public static final DataSerializer<Rating> SERIALIZER = RecordDataSerializer.of(
        DataSerializer.INT, Rating::getScore,
        DataSerializer.STRING, Rating::getComment,
        Doctor.REFERENCE_SERIALIZER, Rating::getDoctor,
        DataSerializer.DATE, Rating::getDateSubmitted,
        Rating::new
    );

    private int score; // 1-5
    private String comment;
    private Doctor doctor;
    private Date dateSubmitted;

    public Rating(int score, String comment, Doctor doctor, Date dateSubmitted) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }

        this.score = score;
        this.comment = comment;
        this.doctor = doctor;
        this.dateSubmitted = dateSubmitted;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        if (score < 1 || score > 5) {
            throw new IllegalArgumentException("Score must be between 1 and 5");
        }
        this.score = score;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(Date dateSubmitted) {
        this.dateSubmitted = dateSubmitted;
    }

    @Override
    public String toString() {
        String doctorName = (doctor != null && doctor.getProfile() != null)
            ? doctor.getProfile().getDisplayName()
            : "Unknown doctor";

        return dateSubmitted + " - " + score + "/5 for Dr. " + doctorName
            + (comment != null && !comment.isBlank() ? ": \"" + comment + "\"" : "");
    }
}
