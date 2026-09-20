package xyz.bluspring.systems.hms.role.patient;

import xyz.bluspring.systems.hms.role.PersonalizableUser;

public class Patient extends PersonalizableUser {

    // Patient attributes
    private String patientId;
    private int age;
    private String gender;
    private String phoneNumber;

    // Constructor
    public Patient(String patientId, int age, String gender, String phoneNumber) {
        this.patientId = patientId;
        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }

    // Patient ID
    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    // Age
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    // Gender
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    // Phone Number
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public void displayPatientInfo() {
        System.out.println("Patient ID: " + patientId);
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone Number: " + phoneNumber);
    }
    public void updateProfile(String displayName, String address,
                              int age, String gender, String phoneNumber) {

        getProfile().setDisplayName(displayName);
        getProfile().setAddress(address);

        this.age = age;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
    }
    public void displayProfile() {
        System.out.println("===== PATIENT PROFILE =====");
        System.out.println("Patient ID: " + patientId);
        System.out.println("Name: " + getProfile().getDisplayName());
        System.out.println("Address: " + getProfile().getAddress());
        System.out.println("Age: " + age);
        System.out.println("Gender: " + gender);
        System.out.println("Phone Number: " + phoneNumber);
    }
}

