package xyz.bluspring.systems.hms.role.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import xyz.bluspring.systems.hms.LoginScreen;
import xyz.bluspring.systems.hms.Main;
import xyz.bluspring.systems.hms.data.RoleManager;
import xyz.bluspring.systems.hms.role.doctor.Doctor;
import xyz.bluspring.systems.hms.utils.Utils;

// Medical manager dashboard panel
public class ManagerDashboard extends JPanel {
    private final MedicalManager manager;
    private JLabel headerInfoLabel;

    // overview fields
    private JLabel overviewNameLabel;
    private JLabel overviewAddressLabel;
    private JLabel overviewIdLabel;
    private JLabel totalDeptsLabel;
    private JLabel totalShiftsLabel;
    private JLabel totalConsultationsLabel;
    private JLabel standardFeeLabel;
    private JLabel estimatedRevenueLabel;

    // profile fields
    private JTextField profileNameField;
    private JTextField profileAddressField;

    // department fields
    private JTextField deptIdField;
    private JTextField deptNameField;
    private JTextField deptDescField;
    private DefaultTableModel deptTableModel;
    private JTable deptTable;

    // shift fields
    private JTextField shiftIdField;
    private JComboBox<Doctor> doctorSelector;
    private JComboBox<String> shiftDeptSelector;
    private JTextField shiftDateField;
    private JComboBox<String> shiftTypeSelector;
    private DefaultTableModel shiftTableModel;
    private JTable shiftTable;

    private static final String[] SHIFT_OPTIONS = {
        "Morning (08:00 - 16:00)",
        "Afternoon (16:00 - 00:00)",
        "Night (00:00 - 08:00)"
    };

    // Global UIManager sets label foreground to white, so need black labels here
    private static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        return label;
    }

    private static JLabel boldLabel(String text, int size) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.BLACK);
        label.setFont(new Font("SansSerif", Font.BOLD, size));
        return label;
    }

    public ManagerDashboard(MedicalManager manager) {
        this.manager = manager;

        this.setLayout(new BorderLayout(10, 10));
        this.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

        this.add(createHeaderPanel(), BorderLayout.NORTH);

        // tabbed navigation for the manager features
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Overview & Metrics", createOverviewTab());
        tabbedPane.addTab("Edit Profile", createProfileTab());
        tabbedPane.addTab("Clinical Departments", createDepartmentsTab());
        tabbedPane.addTab("Doctor Shifts", createShiftsTab());

        this.add(tabbedPane, BorderLayout.CENTER);

        refreshAllData();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel leftPanel = new JPanel(new GridLayout(2, 1, 2, 2));
        leftPanel.setOpaque(false);

        JLabel title = boldLabel("Medical Manager Dashboard", 20);
        headerInfoLabel = new JLabel("Logged in as: " + getManagerDisplayName());
        headerInfoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        headerInfoLabel.setForeground(Color.DARK_GRAY);

        leftPanel.add(title);
        leftPanel.add(headerInfoLabel);

        JButton logoutButton = new JButton("Log Out");
        logoutButton.addActionListener(e -> logout());

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        rightPanel.setOpaque(false);
        rightPanel.add(logoutButton);

        header.add(leftPanel, BorderLayout.CENTER);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to log out?",
            "Confirm Logout",
            JOptionPane.YES_NO_OPTION
        );
        if (confirm == JOptionPane.YES_OPTION) {
            JFrame frame = Main.getFrame();
            frame.getContentPane().removeAll();
            frame.getContentPane().setLayout(new BorderLayout());
            frame.getContentPane().add(new LoginScreen(), BorderLayout.CENTER);
            Main.resetSizesToSmallWindow();
            Main.refresh();
        }
    }

    private String getManagerDisplayName() {
        String name = manager.getProfile().getDisplayName();
        return (name != null && !name.trim().isEmpty()) ? name : "Mustakim";
    }

    // Overview tab showing profile and hospital stats
    private JPanel createOverviewTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Profile details
        JPanel profileCard = new JPanel(new GridLayout(3, 1, 6, 6));
        profileCard.setBorder(BorderFactory.createTitledBorder("Manager Profile"));
        overviewNameLabel = label("Name: ");
        overviewAddressLabel = label("Address: ");
        overviewIdLabel = label("Manager ID: ");
        profileCard.add(overviewNameLabel);
        profileCard.add(overviewAddressLabel);
        profileCard.add(overviewIdLabel);

        // Hospital metrics
        JPanel metricsCard = new JPanel(new GridLayout(5, 2, 10, 8));
        metricsCard.setBorder(BorderFactory.createTitledBorder("Hospital Operational & Financial Metrics"));

        totalDeptsLabel = label("0");
        totalShiftsLabel = label("0");
        totalConsultationsLabel = label("0");
        standardFeeLabel = label(Utils.formatCurrency(50.00));
        estimatedRevenueLabel = label(Utils.formatCurrency(0.00));

        metricsCard.add(label("Total Clinical Departments:"));
        metricsCard.add(totalDeptsLabel);
        metricsCard.add(label("Total Scheduled Doctor Shifts:"));
        metricsCard.add(totalShiftsLabel);
        metricsCard.add(label("Total Logged Consultations:"));
        metricsCard.add(totalConsultationsLabel);
        metricsCard.add(label("Standard Consultation Fee:"));
        metricsCard.add(standardFeeLabel);
        metricsCard.add(label("Estimated Consultation Revenue:"));
        metricsCard.add(estimatedRevenueLabel);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        JLabel statusLabel = label("System Status: All hospital records synchronized and operational.");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));

        JButton refreshButton = new JButton("Refresh Overview");
        refreshButton.addActionListener(e -> refreshAllData());

        bottomPanel.add(statusLabel, BorderLayout.WEST);
        bottomPanel.add(refreshButton, BorderLayout.EAST);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(profileCard);
        content.add(Box.createVerticalStrut(15));
        content.add(metricsCard);

        panel.add(content, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        return panel;
    }

    // Form to edit and save manager profile
    private JPanel createProfileTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel title = boldLabel("Edit Medical Manager Profile", 16);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);

        profileNameField = new JTextField();
        profileAddressField = new JTextField();

        for (JTextField field : new JTextField[]{profileNameField, profileAddressField}) {
            field.setMaximumSize(new Dimension(400, 30));
            field.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        JButton saveButton = new JButton("Save Profile Changes");
        saveButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        saveButton.addActionListener(e -> saveProfile());

        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        panel.add(label("Full Name:"));
        panel.add(profileNameField);
        panel.add(Box.createVerticalStrut(10));
        panel.add(label("Address:"));
        panel.add(profileAddressField);
        panel.add(Box.createVerticalStrut(20));
        panel.add(saveButton);

        return panel;
    }

    private void saveProfile() {
        String name = profileNameField.getText().trim();
        String address = profileAddressField.getText().trim();

        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name cannot be empty.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        manager.updateProfile(name, address);
        RoleManager.INSTANCE.save();

        refreshAllData();
        JOptionPane.showMessageDialog(this, "Profile updated and saved successfully!");
    }

    // Departments tab to add and view departments
    private JPanel createDepartmentsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form to add new department
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBorder(BorderFactory.createTitledBorder("Create New Department"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        deptIdField = new JTextField();
        deptNameField = new JTextField();
        deptDescField = new JTextField();

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.2;
        formCard.add(label("Department ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        formCard.add(deptIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.2;
        formCard.add(label("Department Name:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        formCard.add(deptNameField, gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.2;
        formCard.add(label("Description:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.8;
        formCard.add(deptDescField, gbc);

        JButton addDeptButton = new JButton("Add Department");
        addDeptButton.addActionListener(e -> addDepartment());
        gbc.gridx = 1; gbc.gridy = 3; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        formCard.add(addDeptButton, gbc);

        // Table of existing departments
        JPanel listCard = new JPanel(new BorderLayout(5, 5));
        listCard.setBorder(BorderFactory.createTitledBorder("Existing Clinical Departments"));

        String[] columns = {"ID", "Department Name", "Description"};
        deptTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        deptTable = new JTable(deptTableModel);
        deptTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        deptTable.setForeground(Color.BLACK);
        deptTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(deptTable);
        scrollPane.setPreferredSize(new Dimension(400, 160));

        JPanel tableButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton editDeptButton = new JButton("Update Selected Department");
        editDeptButton.addActionListener(e -> editSelectedDepartment());
        tableButtons.add(editDeptButton);

        listCard.add(scrollPane, BorderLayout.CENTER);
        listCard.add(tableButtons, BorderLayout.SOUTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(formCard);
        content.add(Box.createVerticalStrut(10));
        content.add(listCard);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void addDepartment() {
        String id = deptIdField.getText().trim();
        String name = deptNameField.getText().trim();
        String desc = deptDescField.getText().trim();

        if (id.isEmpty() || name.isEmpty() || desc.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all department fields.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (manager.getDepartmentById(id) != null) {
            JOptionPane.showMessageDialog(this, "A department with ID '" + id + "' already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Department department = new Department(id, name, desc);
        manager.addDepartment(department);

        deptNameField.setText("");
        deptDescField.setText("");

        refreshDepartments();
        refreshOverview();
        JOptionPane.showMessageDialog(this, "Department [" + id + "] " + name + " created successfully!");
    }

    private void editSelectedDepartment() {
        int selectedRow = deptTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a department from the table to update.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String deptId = (String) deptTableModel.getValueAt(selectedRow, 0);
        Department existing = manager.getDepartmentById(deptId);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Department not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JTextField nameField = new JTextField(existing.getDepartmentName());
        JTextField descField = new JTextField(existing.getDescription());

        JPanel form = new JPanel(new GridLayout(2, 2, 8, 8));
        form.add(label("Department Name:"));
        form.add(nameField);
        form.add(label("Description:"));
        form.add(descField);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Update Department [" + existing.getId() + "]",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String newName = nameField.getText().trim();
            String newDesc = descField.getText().trim();

            if (newName.isEmpty()) newName = existing.getDepartmentName();
            if (newDesc.isEmpty()) newDesc = existing.getDescription();

            manager.updateDepartment(deptId, newName, newDesc);
            refreshDepartments();
            refreshOverview();
            JOptionPane.showMessageDialog(this, "Department updated successfully!");
        }
    }

    // Doctor shifts tab to assign and edit shifts
    private JPanel createShiftsTab() {
        JPanel panel = new JPanel(new BorderLayout(15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Form to assign shift
        JPanel formCard = new JPanel(new GridBagLayout());
        formCard.setBorder(BorderFactory.createTitledBorder("Assign Doctor Shift"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        shiftIdField = new JTextField();
        doctorSelector = new JComboBox<>();
        configureDoctorRenderer(doctorSelector);

        shiftDeptSelector = new JComboBox<>();
        shiftDateField = new JTextField("2026-10-05");
        shiftTypeSelector = new JComboBox<>(SHIFT_OPTIONS);

        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.25;
        formCard.add(label("Shift ID:"), gbc);
        gbc.gridx = 1; gbc.weightx = 0.75;
        formCard.add(shiftIdField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formCard.add(label("Select Doctor:"), gbc);
        gbc.gridx = 1;
        formCard.add(doctorSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formCard.add(label("Select Department:"), gbc);
        gbc.gridx = 1;
        formCard.add(shiftDeptSelector, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        formCard.add(label("Shift Date (YYYY-MM-DD):"), gbc);
        gbc.gridx = 1;
        formCard.add(shiftDateField, gbc);

        gbc.gridx = 0; gbc.gridy = 4;
        formCard.add(label("Shift Type:"), gbc);
        gbc.gridx = 1;
        formCard.add(shiftTypeSelector, gbc);

        JButton assignButton = new JButton("Assign Shift");
        assignButton.addActionListener(e -> assignShift());
        gbc.gridx = 1; gbc.gridy = 5; gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.EAST;
        formCard.add(assignButton, gbc);

        // Table of scheduled shifts
        JPanel listCard = new JPanel(new BorderLayout(5, 5));
        listCard.setBorder(BorderFactory.createTitledBorder("Scheduled Doctor Shifts"));

        String[] columns = {"Shift ID", "Doctor", "Department", "Shift Date", "Shift Type"};
        shiftTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        shiftTable = new JTable(shiftTableModel);
        shiftTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        shiftTable.setForeground(Color.BLACK);
        shiftTable.getTableHeader().setForeground(Color.BLACK);

        JScrollPane scrollPane = new JScrollPane(shiftTable);
        scrollPane.setPreferredSize(new Dimension(500, 160));

        JPanel tableButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton modifyShiftButton = new JButton("Modify Selected Shift");
        modifyShiftButton.addActionListener(e -> modifySelectedShift());
        tableButtons.add(modifyShiftButton);

        listCard.add(scrollPane, BorderLayout.CENTER);
        listCard.add(tableButtons, BorderLayout.SOUTH);

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.add(formCard);
        content.add(Box.createVerticalStrut(10));
        content.add(listCard);

        panel.add(content, BorderLayout.CENTER);
        return panel;
    }

    private void configureDoctorRenderer(JComboBox<Doctor> comboBox) {
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                String text = (value instanceof Doctor doctor)
                    ? "Dr. " + doctor.getProfile().getDisplayName() + " (" + doctor.getSpecialization() + ")"
                    : "No Doctor Selected";
                return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
            }
        });
    }

    private void assignShift() {
        String id = shiftIdField.getText().trim();
        Doctor doctor = (Doctor) doctorSelector.getSelectedItem();
        String department = (String) shiftDeptSelector.getSelectedItem();
        String shiftDate = shiftDateField.getText().trim();
        String shiftType = (String) shiftTypeSelector.getSelectedItem();

        if (doctor == null) {
            JOptionPane.showMessageDialog(this, "Please register or select a Doctor first.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (department == null || department.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please create or select a Department first.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (id.isEmpty() || shiftDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all shift details.", "Validation Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (manager.getShiftById(id) != null) {
            JOptionPane.showMessageDialog(this, "A shift with ID '" + id + "' already exists.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        DoctorShift shift = new DoctorShift(id, doctor, department, shiftDate, shiftType);
        manager.addShift(shift);

        refreshShifts();
        refreshOverview();
        JOptionPane.showMessageDialog(this, "Doctor shift [" + id + "] assigned successfully!");
    }

    private void modifySelectedShift() {
        int selectedRow = shiftTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a shift from the table to modify.", "Notice", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String shiftId = (String) shiftTableModel.getValueAt(selectedRow, 0);
        DoctorShift existing = manager.getShiftById(shiftId);
        if (existing == null) {
            JOptionPane.showMessageDialog(this, "Shift not found.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Doctor> doctors = RoleManager.INSTANCE.getDoctors();
        JComboBox<Doctor> docBox = new JComboBox<>(doctors.toArray(new Doctor[0]));
        configureDoctorRenderer(docBox);
        if (existing.getDoctor() != null) {
            docBox.setSelectedItem(existing.getDoctor());
        }

        List<Department> departments = manager.getDepartments();
        String[] deptNames = departments.stream().map(Department::getDepartmentName).toArray(String[]::new);
        JComboBox<String> deptBox = new JComboBox<>(deptNames);
        deptBox.setSelectedItem(existing.getDepartmentName());

        JTextField dateField = new JTextField(existing.getShiftDate());
        JComboBox<String> typeBox = new JComboBox<>(SHIFT_OPTIONS);
        typeBox.setSelectedItem(existing.getShiftType());

        JPanel form = new JPanel(new GridLayout(4, 2, 8, 8));
        form.add(label("Doctor:"));
        form.add(docBox);
        form.add(label("Department:"));
        form.add(deptBox);
        form.add(label("Shift Date (YYYY-MM-DD):"));
        form.add(dateField);
        form.add(label("Shift Type:"));
        form.add(typeBox);

        int result = JOptionPane.showConfirmDialog(
            this,
            form,
            "Modify Shift [" + existing.getId() + "]",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            Doctor newDoctor = (Doctor) docBox.getSelectedItem();
            String newDept = (String) deptBox.getSelectedItem();
            String newDate = dateField.getText().trim();
            String newType = (String) typeBox.getSelectedItem();

            if (newDoctor == null) newDoctor = existing.getDoctor();
            if (newDept == null) newDept = existing.getDepartmentName();
            if (newDate.isEmpty()) newDate = existing.getShiftDate();

            manager.updateShift(shiftId, newDoctor, newDept, newDate, newType);
            refreshShifts();
            refreshOverview();
            JOptionPane.showMessageDialog(this, "Doctor shift modified successfully!");
        }
    }

    // reload tables and form data
    public void refreshAllData() {
        refreshOverview();
        refreshProfileTab();
        refreshDepartments();
        refreshShifts();
    }

    private void refreshOverview() {
        String displayName = getManagerDisplayName();
        headerInfoLabel.setText("Logged in as: " + displayName);

        overviewNameLabel.setText("Name: " + displayName);
        String address = manager.getProfile().getAddress();
        overviewAddressLabel.setText("Address: " + (address != null && !address.trim().isEmpty() ? address : "Not specified"));
        overviewIdLabel.setText("Manager ID: " + manager.getManagerId());

        totalDeptsLabel.setText(String.valueOf(manager.getDepartments().size()));
        totalShiftsLabel.setText(String.valueOf(manager.getDoctorShifts().size()));

        int consultations = manager.getStorage().getConsultationCount();
        totalConsultationsLabel.setText(String.valueOf(consultations));

        double baseRate = 50.00;
        standardFeeLabel.setText(Utils.formatCurrency(baseRate));
        estimatedRevenueLabel.setText(Utils.formatCurrency(manager.calculateEstimatedRevenue(baseRate)));
    }

    private void refreshProfileTab() {
        profileNameField.setText(getManagerDisplayName());
        String address = manager.getProfile().getAddress();
        profileAddressField.setText(address != null ? address : "");
    }

    private void refreshDepartments() {
        List<Department> list = manager.getDepartments();

        // Update auto-id suggestion
        deptIdField.setText(String.format("%02d", list.size() + 1));

        // Update departments table
        deptTableModel.setRowCount(0);
        for (Department d : list) {
            deptTableModel.addRow(new Object[]{d.getId(), d.getDepartmentName(), d.getDescription()});
        }

        // Synchronize department dropdown in Shifts tab
        if (shiftDeptSelector != null) {
            shiftDeptSelector.removeAllItems();
            for (Department d : list) {
                shiftDeptSelector.addItem(d.getDepartmentName());
            }
        }
    }

    private void refreshShifts() {
        List<DoctorShift> list = manager.getDoctorShifts();

        // Update auto-id suggestion
        shiftIdField.setText(String.format("%02d", list.size() + 1));

        // Update doctor dropdown from RoleManager
        if (doctorSelector != null) {
            Doctor selected = (Doctor) doctorSelector.getSelectedItem();
            doctorSelector.removeAllItems();
            for (Doctor d : RoleManager.INSTANCE.getDoctors()) {
                doctorSelector.addItem(d);
            }
            if (selected != null) {
                doctorSelector.setSelectedItem(selected);
            }
        }

        // Update shifts table
        shiftTableModel.setRowCount(0);
        for (DoctorShift s : list) {
            String docName = (s.getDoctor() != null && s.getDoctor().getProfile() != null)
                ? "Dr. " + s.getDoctor().getProfile().getDisplayName() + " (" + s.getDoctor().getSpecialization() + ")"
                : "Unknown";
            shiftTableModel.addRow(new Object[]{
                s.getId(),
                docName,
                s.getDepartmentName(),
                s.getShiftDate(),
                s.getShiftType()
            });
        }
    }
}

