package xyz.bluspring.systems.hms.role.manager;

import javax.swing.JPanel;
import xyz.bluspring.systems.hms.role.PersonalizableUser;

public class MedicalManager extends  PersonalizableUser {
    public JPanel createDashboardUI() {
        return new ManagerDashboard();
    }
}
