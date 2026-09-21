package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.doctor.Doctor;

/**
 * A patient's rating and comment about a doctor or clinic visit.
 */
public class Rating {
    private int score; // 1-5
    private String comment;
    private Doctor doctor;
    private String dateSubmitted;

    public Rating(int score, String comment, Doctor doctor, String dateSubmitted) {
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

    public String getDateSubmitted() {
        return dateSubmitted;
    }

    public void setDateSubmitted(String dateSubmitted) {
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
