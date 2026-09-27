package xyz.bluspring.systems.hms;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
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
import xyz.bluspring.systems.hms.ui.ComponentHelper;
import xyz.bluspring.systems.hms.ui.PlaceholderFormattedTextField;
import xyz.bluspring.systems.hms.utils.Utils;

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
        loginButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        loginButton.addActionListener(e -> login());

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

        this.add(Utils.make(new JPanel(new FlowLayout(FlowLayout.CENTER)), signUp -> {
            signUp.setOpaque(false);
            signUp.add(new JLabel("Not a user? ")).setForeground(Color.BLACK);

            signUp.add(Utils.make(new JButton("Create an account"), button -> {
                ComponentHelper.makeHyperlink(button);
                button.addActionListener(e -> openRegisterScreen());
            }));
        }));
    }

    private void login() {
        var type = (AccountType) roleSelector.getSelectedItem();
        var email = emailField.getText();
        var password = new String(passwordField.getPassword());

        try {
            Account account = getAuthManager(type).login(email, password);
            routeToDashboard(this, type, account);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

    private static void routeToDashboard(JComponent parent, AccountType type, Account account) {
        String id = account.getUUID().toString();

        switch (type) {
            case PATIENT -> {
                Patient patient = RoleManager.INSTANCE.findPatientById(id);
                if (patient == null) {
                    JOptionPane.showMessageDialog(parent, "No patient record linked to this account.");
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
                    JOptionPane.showMessageDialog(parent, "No doctor record linked to this account.");
                    return;
                }

                Patient chosenPatient = choosePatient(parent);
                if (chosenPatient == null) {
                    return;
                }

                new DoctorGUI(doctor, chosenPatient).setVisible(true);
            }
            case MEDICAL_MANAGER -> {
                MedicalManager manager = RoleManager.INSTANCE.findManagerById(id);
                if (manager == null) {
                    JOptionPane.showMessageDialog(parent, "No manager record linked to this account.");
                    return;
                }

                showOnWindow(manager.createDashboardUI());
            }
        case ADMIN -> JOptionPane.showMessageDialog(parent, "The Admin dashboard hasn't been built yet."); // TODO
        }
    }

    /**
     * Doctor's dashboard currently needs a specific patient chosen up-front,
     * so this shows a simple picker listing every registered patient.
     */
    private static Patient choosePatient(JComponent parent) {
        var patients = RoleManager.INSTANCE.getPatients();

        if (patients.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "There are no registered patients yet.");
            return null;
        }

        Patient[] options = patients.toArray(new Patient[0]);

        return (Patient) JOptionPane.showInputDialog(
            parent,
            "Choose a patient to view:",
            "Select Patient",
            JOptionPane.PLAIN_MESSAGE,
            null,
            options,
            options[0]
        );
    }

    private static void showOnWindow(JComponent component) {
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
        private final JFormattedTextField dateOfBirthField;
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

            List<AccountType> types = new ArrayList<>(List.of(AccountType.values()));
            Collections.reverse(types);
            roleSelector = new JComboBox<>(types.toArray(new AccountType[0]));
            nameField = new JTextField();
            addressField = new JTextField();
            emailField = new JTextField();
            passwordField = new JPasswordField();

            dateOfBirthField = new PlaceholderFormattedTextField(new SimpleDateFormat("dd-MM-yyyy"), "dd-MM-yyyy");
            dateOfBirthField.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
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
            patientFields.add(label("Date of Birth:"));
            patientFields.add(dateOfBirthField);
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
            rolePanel.add(patientFields); // we're starting on patient, might as well use patient!

            for (JComponent field : new JComponent[] {roleSelector, emailField, nameField, addressField, passwordField, dateOfBirthField, genderSelector, phoneField, specializationField, managerSelector}) {
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

            var title = label("Hospital Management System - Register");
            title.setAlignmentX(Component.CENTER_ALIGNMENT);

            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(title);
            mainPanel.add(Box.createVerticalStrut(20));
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

            submitPanel.add(Utils.make(new JPanel(new FlowLayout(FlowLayout.CENTER)), signUp -> {
                signUp.setOpaque(false);
                signUp.add(new JLabel("Already a user? ")).setForeground(Color.BLACK);

                signUp.add(Utils.make(new JButton("Log in"), button -> {
                    ComponentHelper.makeHyperlink(button);
                    button.addActionListener(e -> {
                        Main.reset();
                        var dialog = new LoginScreen();
                        Main.getFrame().getContentPane().add(dialog);
                        Main.refresh();
                    });
                }));
            }));

            this.add(mainPanel);
            this.add(rolePanel);
            this.add(submitPanel);
        }

        private void submit() {
            var type = (AccountType) roleSelector.getSelectedItem();
            AuthManager authManager = getAuthManager(type);
            Account account = null;

            try {
                account = authManager.create(
                    emailField.getText(),
                    nameField.getText(),
                    new String(passwordField.getPassword())
                );

                Profile profile = new Profile(account.getUUID(), addressField.getText(), nameField.getText());

                switch (type) {
                    case PATIENT -> {
                        if (dateOfBirthField.getValue() == null) {
                            authManager.deleteAccount(account);

                            JOptionPane.showMessageDialog(this, "Please enter a valid date of birth.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
                            return;
                        }

                        Date dateOfBirth = (Date) dateOfBirthField.getValue();
                        String gender = (String) genderSelector.getSelectedItem();
                        String phone = phoneField.getText();

                        Patient patient = new Patient(profile, dateOfBirth, gender, phone);
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

                routeToDashboard(this, type, account);
            } catch (Exception e) {
                if (account != null) {
                    authManager.deleteAccount(account);
                }

                JOptionPane.showMessageDialog(this, e.getMessage(), "Registration Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

