package xyz.bluspring.systems.hms.admin;

import javax.swing.JTabbedPane;

import xyz.bluspring.systems.hms.auth.Account;

public class AdminUI {
    public static JTabbedPane openAdminUI(Account account) {
        JTabbedPane tabs = new JTabbedPane();

        // todo: user management (doctor assign to managers)
        //       allocate rooms
        //       config insurance networks
        //       config base consultation rates
        tabs.addTab("User Management", new AdminUserManagementPanel(account));
        tabs.addTab("Room Allocation", new AdminRoomAllocationPanel());
        tabs.addTab("Hospital Configuration", new AdminHospitalConfigPanel());
        return tabs;
    }
}
