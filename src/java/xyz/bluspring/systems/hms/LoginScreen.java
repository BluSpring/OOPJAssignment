package xyz.bluspring.systems.hms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.auth.AccountType;
import xyz.bluspring.systems.hms.auth.AuthManager;
import xyz.bluspring.systems.hms.data.RoleManager;
import xyz.bluspring.systems.hms.role.Profile;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.role.doctor.DoctorGUI;
import xyz.bluspring.systems.hms.role.manager.MedicalManager;
import xyz.bluspring.systems.hms.role.patient.Patient;
import xyz.bluspring.systems.hms.role.patient.PatientDashboard;
import xyz.bluspring.systems.hms.role.patient.PatientEditForm;
import xyz.bluspring.systems.hms.role.patient.PatientRecordsPanel;
import xyz.bluspring.systems.hms.role.patient.RatingForm;

/**
 * The screen shown on startup, letting a user log in or register,
 * and then routing them to the correct dashboard based on their account type.
 */
public class LoginScreen extends JPanel {
    // One AuthManager per account type, since accounts are stored separately per type.
    private static final Map<AccountType, AuthManager> AUTH_MANAGERS = new EnumMap<>(AccountType.class);

    public static AuthManager getAuthManager(AccountType type) {
        return AUTH_MANAGERS.computeIfAbsent(type, t -> {
            AuthManager manager = new AuthManager(t);
            manager.load();
            return manager;
        });
    }

    /**
     * The app sets a global white label color for the dark/gradient background,
     * but forms like this one sit on a plain light panel, so labels need to
     * be forced back to black here (same pattern PatientDashboard etc. use).
     */
    private static JLabel label(String text) {
        var label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private final JComboBox<AccountType> roleSelector;
    private final JTextField emailField;
    private final JPasswordField passwordField;

    public LoginScreen() {
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var title = label("Hospital Management System - Login");
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        List<AccountType> types = new ArrayList<>(List.of(AccountType.values()));
        Collections.reverse(types);
        roleSelector = new JComboBox<>(types.toArray(new AccountType[0]));
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
        registerButton.addActionListener(e -> openRegisterScreen());

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
            Account account = getAuthManager(type).login(email, password);
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

    private void showOnWindow(JComponent component) {
        Main.reset();
        JFrame frame = Main.getFrame();
        frame.getContentPane().setLayout(new BorderLayout());
        frame.getContentPane().add(component, BorderLayout.CENTER);
        Main.resetSizesToSmallWindow();
        Main.refresh();
    }

    private void openRegisterScreen() {
        Main.reset();
        var dialog = new RegisterScreen();
        Main.getFrame().getContentPane().add(dialog);
        Main.refresh();
    }

    /**
     * A separate dialog for creating a new account, with fields that change
     * depending on which account type is selected.
     */
    private static class RegisterScreen extends JPanel {
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

        RegisterScreen() {
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.setAlignmentX(JPanel.CENTER_ALIGNMENT);

            var mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
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

            var patientFields = new JPanel();
            patientFields.setLayout(new BoxLayout(patientFields, BoxLayout.Y_AXIS));
            patientFields.add(label("Age:"));
            patientFields.add(ageField);
            patientFields.add(Box.createVerticalStrut(10));
            patientFields.add(label("Gender:"));
            patientFields.add(genderSelector);
            patientFields.add(Box.createVerticalStrut(10));
            patientFields.add(label("Phone Number:"));
            patientFields.add(phoneField);

            var doctorFields = new JPanel();
            doctorFields.setLayout(new BoxLayout(doctorFields, BoxLayout.Y_AXIS));
            doctorFields.add(label("Specialization:"));
            doctorFields.add(specializationField);
            doctorFields.add(Box.createVerticalStrut(10));
            doctorFields.add(label("Assigned Manager:"));
            doctorFields.add(managerSelector);

            var emptyFields = new JPanel();

            var rolePanel = new JPanel();
            rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.Y_AXIS));

            for (JComponent field : new JComponent[] {roleSelector, emailField, nameField, addressField, passwordField, ageField, genderSelector, phoneField, specializationField, managerSelector}) {
                field.setMaximumSize(new Dimension(250, 30));
                field.setAlignmentX(Component.CENTER_ALIGNMENT);
            }

            roleSelector.addActionListener(e -> {
                rolePanel.removeAll();

                switch (((AccountType) roleSelector.getSelectedItem())) {
                case DOCTOR -> {
                    rolePanel.add(doctorFields);
                }
                case PATIENT -> {
                    rolePanel.add(patientFields);
                }

                default -> {
                }
                }

                rolePanel.invalidate();
                rolePanel.validate();
                rolePanel.repaint();

                this.invalidate();
                this.validate();
                this.repaint();
            });

            var submitPanel = new JPanel();
            submitPanel.setLayout(new BoxLayout(submitPanel, BoxLayout.Y_AXIS));

            var submitButton = new JButton("Create Account");
            submitButton.addActionListener(e -> submit());
            submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(label("Account Type:"));
            mainPanel.add(roleSelector);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(label("Display Name:"));
            mainPanel.add(nameField);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(label("Address:"));
            mainPanel.add(addressField);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(label("Email:"));
            mainPanel.add(emailField);
            mainPanel.add(Box.createVerticalStrut(10));
            mainPanel.add(label("Password:"));
            mainPanel.add(passwordField);
            mainPanel.add(Box.createVerticalStrut(10));

            submitPanel.add(Box.createVerticalStrut(10));
            submitPanel.add(submitButton);

            this.add(mainPanel);
            this.add(rolePanel);
            this.add(submitPanel);
        }

        private void submit() {
            var type = (AccountType) roleSelector.getSelectedItem();

            try {
                Account account = getAuthManager(type).create(
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
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Please enter a valid age.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

