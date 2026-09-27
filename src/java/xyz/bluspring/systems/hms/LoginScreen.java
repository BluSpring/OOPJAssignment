package xyz.bluspring.systems.hms;

import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.auth.AccountType;
import xyz.bluspring.systems.hms.auth.AuthManager;
import xyz.bluspring.systems.hms.data.RoleManager;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.doctor.DoctorGUI;
import xyz.bluspring.systems.hms.role.manager.MedicalManager;
import xyz.bluspring.systems.hms.role.patient.*;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.Map;

/**
 * The screen shown on startup, letting a user log in or register,
 * and then routing them to the correct dashboard based on their account type.
 */
public class LoginScreen extends JPanel {
    // One AuthManager per account type, since accounts are stored separately per type.
    private static final Map<AccountType, AuthManager> AUTH_MANAGERS = new EnumMap<>(AccountType.class);

    static {
        for (AccountType type : AccountType.values()) {
            AUTH_MANAGERS.put(type, new AuthManager(type));
        }
    }

    /**
     * The app sets a global white label color for the dark/gradient background,
     * but forms like this one sit on a plain light panel, so labels need to
     * be forced back to black here (same pattern PatientDashboard etc. use).
     */
    private static JLabel label(String text) {
        var label = new JLabel(text);
        label.setForeground(Color.BLACK);
        return label;
    }

    private final JComboBox<AccountType> roleSelector;
    private final JTextField emailField;
    private final JPasswordField passwordField;

    public LoginScreen() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var title = label("Hospital Management System - Login");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        roleSelector = new JComboBox<>(AccountType.values());
        emailField = new JTextField();
        passwordField = new JPasswordField();

        for (JTextField field : new JTextField[]{emailField, passwordField}) {
            field.setMaximumSize(new Dimension(250, 30));
            field.setAlignmentX(Component.CENTER_ALIGNMENT);
        }
        roleSelector.setMaximumSize(new Dimension(250, 30));
        roleSelector.setAlignmentX(Component.CENTER_ALIGNMENT);

        var loginButton = new JButton("Log In");
        var registerButton = new JButton("Register");
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        registerButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> login());
        registerButton.addActionListener(e -> openRegisterDialog());

        var typeLabel = label("Account Type:");
        var emailLabel = label("Email:");
        var passwordLabel = label("Password:");

        this.add(Box.createVerticalStrut(20));
        this.add(title);
        this.add(Box.createVerticalStrut(20));
        this.add(typeLabel);
        this.add(roleSelector);
        this.add(Box.createVerticalStrut(10));
        this.add(emailLabel);
        this.add(emailField);
        this.add(Box.createVerticalStrut(10));
        this.add(passwordLabel);
        this.add(passwordField);
        this.add(Box.createVerticalStrut(20));
        this.add(loginButton);
        this.add(Box.createVerticalStrut(5));
        this.add(registerButton);
    }

    private void login() {
        var type = (AccountType) roleSelector.getSelectedItem();
        var email = emailField.getText();
        var password = new String(passwordField.getPassword());

        try {
            Account account = AUTH_MANAGERS.get(type).login(email, password);
            routeToDashboard(type, account);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void routeToDashboard(AccountType type, Account account) {
        String id = account.getUUID().toString();

        switch (type) {
            case PATIENT -> {
                Patient patient = RoleManager.INSTANCE.findPatientById(id);
                if (patient == null) {
                    JOptionPane.showMessageDialog(this, "No patient record linked to this account.");
                    return;
                }

                JTabbedPane tabs = new JTabbedPane();
                tabs.addTab("Dashboard", new PatientDashboard(patient));
                tabs.addTab("Edit Profile", new PatientEditForm(patient));
                tabs.addTab("Medical Records", new PatientRecordsPanel(patient));
                tabs.addTab("Rate Doctor", new RatingForm(patient));

                showOnWindow(tabs);
            }
            case DOCTOR -> {
                Doctor doctor = RoleManager.INSTANCE.findDoctorById(id);
                if (doctor == null) {
                    JOptionPane.showMessageDialog(this, "No doctor record linked to this account.");
                    return;
                }

                Patient chosenPatient = choosePatient();
                if (chosenPatient == null) {
                    return;
                }

                new DoctorGUI(doctor, chosenPatient).setVisible(true);
            }
            case MEDICAL_MANAGER -> {
                MedicalManager manager = RoleManager.INSTANCE.findManagerById(id);
                if (manager == null) {
                    JOptionPane.showMessageDialog(this, "No manager record linked to this account.");
                    return;
                }

                showOnWindow(manager.createDashboardUI());
            }
            case ADMIN -> JOptionPane.showMessageDialog(this, "The Admin dashboard hasn't been built yet.");
        }
    }

    /**
     * Doctor's dashboard currently needs a specific patient chosen up-front,
     * so this shows a simple picker listing every registered patient.
     */
    private Patient choosePatient() {
        var patients = RoleManager.INSTANCE.getPatients();

        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(this, "There are no registered patients yet.");
            return null;
        }

        Patient[] options = patients.toArray(new Patient[0]);

        return (Patient) JOptionPane.showInputDialog(
            this,
            "Choose a patient to view:",
            "Select Patient",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );
    }

    private void showOnWindow(javax.swing.JComponent component) {
        JFrame frame = Main.getFrame();
        frame.getContentPane().removeAll();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(component, BorderLayout.CENTER);
        Main.resetSizesToSmallWindow();
        Main.refresh();
    }

    private void openRegisterDialog() {
        new RegisterDialog(this).setVisible(true);
    }

    /**
     * A separate dialog for creating a new account, with fields that change
     * depending on which account type is selected.
     */
    private static class RegisterDialog extends javax.swing.JDialog {
        private final JComboBox<AccountType> roleSelector;
        private final JTextField nameField;
        private final JTextField addressField;
        private final JTextField emailField;
        private final JPasswordField passwordField;

        // Patient-specific fields
        private final JTextField ageField;
        private final JComboBox<String> genderSelector;
        private final JTextField phoneField;

        // Doctor-specific fields
        private final JTextField specializationField;
        private final JComboBox<MedicalManager> managerSelector;

        private final CardLayout roleFieldsLayout = new CardLayout();
        private final JPanel roleFieldsPanel = new JPanel(roleFieldsLayout);

        RegisterDialog(Component parent) {
            super((java.awt.Frame) null, "Register New Account", true);
            this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

            roleSelector = new JComboBox<>(AccountType.values());
            nameField = new JTextField();
            addressField = new JTextField();
            emailField = new JTextField();
            passwordField = new JPasswordField();

            ageField = new JTextField();
            genderSelector = new JComboBox<>(new String[]{"Male", "Female", "Other"});
            phoneField = new JTextField();

            specializationField = new JTextField();
            managerSelector = new JComboBox<>(RoleManager.INSTANCE.getManagers().toArray(new MedicalManager[0]));
            managerSelector.setRenderer(new javax.swing.DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                    String text = (value instanceof MedicalManager manager) ? manager.getProfile().getDisplayName() : "";
                    return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                }
            });

            var patientFields = new JPanel(new GridLayout(3, 2, 5, 5));
            patientFields.add(label("Age:"));
            patientFields.add(ageField);
            patientFields.add(label("Gender:"));
            patientFields.add(genderSelector);
            patientFields.add(label("Phone Number:"));
            patientFields.add(phoneField);

            var doctorFields = new JPanel(new GridLayout(2, 2, 5, 5));
            doctorFields.add(label("Specialization:"));
            doctorFields.add(specializationField);
            doctorFields.add(label("Assigned Manager:"));
            doctorFields.add(managerSelector);

            var emptyFields = new JPanel();

            roleFieldsPanel.add(patientFields, AccountType.PATIENT.name());
            roleFieldsPanel.add(doctorFields, AccountType.DOCTOR.name());
            roleFieldsPanel.add(emptyFields, AccountType.MEDICAL_MANAGER.name());
            roleFieldsPanel.add(emptyFields, AccountType.ADMIN.name());

            roleSelector.addActionListener(e ->
                roleFieldsLayout.show(roleFieldsPanel, ((AccountType) roleSelector.getSelectedItem()).name())
            );

            var submitButton = new JButton("Create Account");
            submitButton.addActionListener(e -> submit());

            this.add(label("Account Type:"));
            this.add(roleSelector);
            this.add(label("Display Name:"));
            this.add(nameField);
            this.add(label("Address:"));
            this.add(addressField);
            this.add(label("Email:"));
            this.add(emailField);
            this.add(label("Password:"));
            this.add(passwordField);
            this.add(roleFieldsPanel);
            this.add(submitButton);

            this.setSize(320, 480);
            this.setLocationRelativeTo(parent);
        }

        private void submit() {
            var type = (AccountType) roleSelector.getSelectedItem();

            try {
                Account account = AUTH_MANAGERS.get(type).create(
                    emailField.getText(),
                    nameField.getText(),
                    new String(passwordField.getPassword())
                );

                Profile profile = new Profile(account.getUUID(), addressField.getText(), nameField.getText());

                switch (type) {
                    case PATIENT -> {
                        int age = Integer.parseInt(ageField.getText());
                        String gender = (String) genderSelector.getSelectedItem();
                        String phone = phoneField.getText();

                        Patient patient = new Patient(profile, age, gender, phone);
                        RoleManager.INSTANCE.getPatients().add(patient);
                        RoleManager.INSTANCE.save();
                    }
                    case DOCTOR -> {
                        String specialization = specializationField.getText();
                        MedicalManager manager = (MedicalManager) managerSelector.getSelectedItem();

                        Doctor doctor = new Doctor(profile, specialization, manager);
                        RoleManager.INSTANCE.getDoctors().add(doctor);
                        RoleManager.INSTANCE.save();
                    }
                    case MEDICAL_MANAGER -> {
                        MedicalManager manager = new MedicalManager(profile);
                        RoleManager.INSTANCE.getManagers().add(manager);
                        RoleManager.INSTANCE.save();
                    }
                    case ADMIN -> {
                        // Admin has no linked role object yet; only the login Account is created.
                    }
                }

                JOptionPane.showMessageDialog(this, "Account created! You can now log in.");
                this.dispose();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

