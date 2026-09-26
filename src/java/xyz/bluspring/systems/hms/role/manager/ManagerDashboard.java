package xyz.bluspring.systems.hms.role.manager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import xyz.bluspring.systems.hms.utils.Utils;

public class ManagerDashboard extends JPanel {
    private MedicalManager manager;
    private JLabel infoLabel;

    public ManagerDashboard(MedicalManager manager) {
        this.manager = manager;

        this.setLayout(new BorderLayout(15, 15));
        this.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // Header panel
        JPanel headerPanel = new JPanel(new GridLayout(2, 1, 4, 4));
        headerPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("Medical Manager Dashboard");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        titleLabel.setForeground(Color.BLACK);

        String currentName = manager.getProfile().getDisplayName();
        if (currentName == null || currentName.trim().isEmpty()) {
            currentName = "Mustakim";
        }
        infoLabel = new JLabel("Logged in as: " + currentName);
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));
        infoLabel.setForeground(Color.DARK_GRAY);

        headerPanel.add(titleLabel);
        headerPanel.add(infoLabel);
        this.add(headerPanel, BorderLayout.NORTH);

        // Buttons grid
        JPanel buttonPanel = new JPanel(new GridLayout(3, 2, 12, 12));
        buttonPanel.setOpaque(false);

        JButton editProfileButton = new JButton("Edit Profile");
        JButton addDeptButton = new JButton("Create New Department");
        JButton viewDeptButton = new JButton("View & Update Departments");
        JButton addShiftButton = new JButton("Assign Doctor Shift");
        JButton viewShiftButton = new JButton("View & Modify Doctor Shifts");
        JButton reportButton = new JButton("Hospital Metrics & Revenue");

        buttonPanel.add(editProfileButton);
        buttonPanel.add(addDeptButton);
        buttonPanel.add(viewDeptButton);
        buttonPanel.add(addShiftButton);
        buttonPanel.add(viewShiftButton);
        buttonPanel.add(reportButton);

        this.add(buttonPanel, BorderLayout.CENTER);

        // Action listeners
        editProfileButton.addActionListener(e -> editProfile());
        addDeptButton.addActionListener(e -> createDepartment());
        viewDeptButton.addActionListener(e -> viewAndUpdateDepartments());
        addShiftButton.addActionListener(e -> assignDoctorShift());
        viewShiftButton.addActionListener(e -> viewAndModifyShifts());
        reportButton.addActionListener(e -> viewMetricsAndRevenue());
    }

    // Edit personal profile
    private void editProfile() {
        String currentName = manager.getProfile().getDisplayName();
        if (currentName == null) {
            currentName = "Mustakim";
        }
        String name = JOptionPane.showInputDialog(this, "Enter full name:", currentName);
        if (name == null || name.trim().isEmpty()) {
            return;
        }

        String currentAddress = manager.getProfile().getAddress();
        if (currentAddress == null) {
            currentAddress = "";
        }
        String address = JOptionPane.showInputDialog(this, "Enter address:", currentAddress);
        if (address == null) {
            return;
        }

        manager.updateProfile(name.trim(), address.trim());
        infoLabel.setText("Logged in as: " + name.trim());
        JOptionPane.showMessageDialog(this, "Profile updated successfully!");
    }

    // Create a new clinical department with simple ID like 01, 02
    private void createDepartment() {
        String defaultId = String.format("%02d", manager.getDepartments().size() + 1);
        String id = JOptionPane.showInputDialog(this, "Enter Department ID (e.g., 01, 02):", defaultId);
        if (id == null || id.trim().isEmpty()) {
            return;
        }

        String name = JOptionPane.showInputDialog(this, "Enter Department Name (e.g., Cardiology):");
        if (name == null || name.trim().isEmpty()) {
            return;
        }

        String desc = JOptionPane.showInputDialog(this, "Enter Department Description:");
        if (desc == null || desc.trim().isEmpty()) {
            return;
        }

        Department dept = new Department(id.trim(), name.trim(), desc.trim());
        manager.addDepartment(dept);
        JOptionPane.showMessageDialog(this, "Department [" + id.trim() + "] " + name.trim() + " created successfully!");
    }

    // View departments and update by ID, preserving fields if unchanged
    private void viewAndUpdateDepartments() {
        List<Department> list = manager.getDepartments();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No departments recorded yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID | Department Name | Description\n");
        sb.append("--------------------------------------------------\n");
        for (Department d : list) {
            sb.append(d.getId()).append(" | ")
              .append(d.getDepartmentName()).append(" | ")
              .append(d.getDescription()).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(420, 160));

        int option = JOptionPane.showConfirmDialog(
            this,
            scrollPane,
            "Clinical Departments (Click Yes to Update a Department)",
            JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            String targetId = JOptionPane.showInputDialog(this, "Enter Department ID to update (e.g., 01):");
            if (targetId == null || targetId.trim().isEmpty()) {
                return;
            }

            Department existing = manager.getDepartmentById(targetId.trim());
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "Department ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String newName = JOptionPane.showInputDialog(this, "Enter Department Name (leave as is to keep):", existing.getDepartmentName());
            if (newName == null) {
                return;
            }
            if (newName.trim().isEmpty()) {
                newName = existing.getDepartmentName();
            }

            String newDesc = JOptionPane.showInputDialog(this, "Enter Description (leave as is to keep):", existing.getDescription());
            if (newDesc == null) {
                return;
            }
            if (newDesc.trim().isEmpty()) {
                newDesc = existing.getDescription();
            }

            manager.updateDepartment(targetId.trim(), newName.trim(), newDesc.trim());
            JOptionPane.showMessageDialog(this, "Department updated successfully!");
        }
    }

    // Assign shift to a doctor
    private void assignDoctorShift() {
        String defaultId = String.format("%02d", manager.getDoctorShifts().size() + 1);
        String id = JOptionPane.showInputDialog(this, "Enter Shift ID (e.g., 01, 02):", defaultId);
        if (id == null || id.trim().isEmpty()) {
            return;
        }

        String doctorName = JOptionPane.showInputDialog(this, "Enter Doctor Name (e.g., Dr. Rajesh):");
        if (doctorName == null || doctorName.trim().isEmpty()) {
            return;
        }

        String department = JOptionPane.showInputDialog(this, "Enter Department (e.g., Neuroscience):");
        if (department == null || department.trim().isEmpty()) {
            return;
        }

        String shiftDate = JOptionPane.showInputDialog(this, "Enter Shift Date (e.g., 2026-10-05):");
        if (shiftDate == null || shiftDate.trim().isEmpty()) {
            return;
        }

        String[] shiftOptions = {"Morning (08:00 - 16:00)", "Afternoon (16:00 - 00:00)", "Night (00:00 - 08:00)"};
        int choice = JOptionPane.showOptionDialog(
            this,
            "Select Shift Type:",
            "Shift Selection",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            shiftOptions,
            shiftOptions[0]
        );

        if (choice < 0) {
            return;
        }
        String shiftType = shiftOptions[choice];

//        DoctorShift shift = new DoctorShift(id.trim(), doctor, department.trim(), shiftDate.trim(), shiftType); // FIXME
//        manager.addShift(shift);
        JOptionPane.showMessageDialog(this, "Doctor shift [" + id.trim() + "] assigned successfully!");
    }

    // View existing shifts and modify by ID, with option to edit doctor, department, date, and shift
    private void viewAndModifyShifts() {
        List<DoctorShift> list = manager.getDoctorShifts();
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No doctor shifts recorded yet.");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("ID | Doctor | Department | Date | Shift\n");
        sb.append("----------------------------------------------------------------------\n");
        for (DoctorShift s : list) {
            sb.append(s.getId()).append(" | ")
                .append(s.getDoctor()).append(" | ")
              .append(s.getDepartmentName()).append(" | ")
              .append(s.getShiftDate()).append(" | ")
              .append(s.getShiftType()).append("\n");
        }

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(480, 160));

        int option = JOptionPane.showConfirmDialog(
            this,
            scrollPane,
            "Doctor Shifts (Click Yes to Modify a Shift)",
            JOptionPane.YES_NO_OPTION
        );

        if (option == JOptionPane.YES_OPTION) {
            String targetId = JOptionPane.showInputDialog(this, "Enter Shift ID to modify (e.g., 01):");
            if (targetId == null || targetId.trim().isEmpty()) {
                return;
            }

            DoctorShift existing = manager.getShiftById(targetId.trim());
            if (existing == null) {
                JOptionPane.showMessageDialog(this, "Shift ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Edit Doctor Name
            String newDoctor = JOptionPane.showInputDialog(this, "Enter Doctor Name (leave as is to keep):", existing.getDoctor());
            if (newDoctor == null) {
                return;
            }
            if (newDoctor.trim().isEmpty()) {
//                newDoctor = existing.getDoctor(); // FIXME
            }

            // Edit Department
            String newDept = JOptionPane.showInputDialog(this, "Enter Department (leave as is to keep):", existing.getDepartmentName());
            if (newDept == null) {
                return;
            }
            if (newDept.trim().isEmpty()) {
                newDept = existing.getDepartmentName();
            }

            // Edit Date
            String newDate = JOptionPane.showInputDialog(this, "Enter Shift Date (leave as is to keep):", existing.getShiftDate());
            if (newDate == null) {
                return;
            }
            if (newDate.trim().isEmpty()) {
                newDate = existing.getShiftDate();
            }

            // Edit Shift Type
            String[] shiftOptions = {
                "Keep current: " + existing.getShiftType(),
                "Morning (08:00 - 16:00)",
                "Afternoon (16:00 - 00:00)",
                "Night (00:00 - 08:00)"
            };
            int choice = JOptionPane.showOptionDialog(
                this,
                "Select Shift Type:",
                "Shift Selection",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                shiftOptions,
                shiftOptions[0]
            );

            if (choice < 0) {
                return;
            }

            String newShiftType = (choice == 0) ? existing.getShiftType() : shiftOptions[choice];

//            manager.updateShift(targetId.trim(), newDoctor.trim(), newDept.trim(), newDate.trim(), newShiftType); // FIXME
            JOptionPane.showMessageDialog(this, "Shift modified successfully!");
        }
    }

    // View metrics and revenue report
    private void viewMetricsAndRevenue() {
        int totalDepartments = manager.getDepartments().size();
        int totalShifts = manager.getDoctorShifts().size();
        int totalConsultations = manager.getStorage().getConsultationCount();

        double baseConsultationRate = 50.00;
        double estimatedRevenue = manager.calculateEstimatedRevenue(baseConsultationRate);

        String message = "===== HOSPITAL METRICS & REVENUE REPORT =====\n\n"
            + "Total Clinical Departments: " + totalDepartments + "\n"
            + "Total Scheduled Doctor Shifts: " + totalShifts + "\n"
            + "Total Logged Consultations: " + totalConsultations + "\n\n"
            + "Standard Consultation Rate: " + Utils.formatCurrency(baseConsultationRate) + "\n"
            + "Estimated Consultation Revenue: " + Utils.formatCurrency(estimatedRevenue) + "\n\n"
            + "Status: All hospital operational records up to date.";

        JOptionPane.showMessageDialog(
            this,
            message,
            "Hospital Analytical Report",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
}
