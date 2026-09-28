package xyz.bluspring.systems.hms.admin;

import javax.swing.JTabbedPane;

import xyz.bluspring.systems.hms.auth.Account;

public class AdminUI {
    public static JTabbedPane openAdminUI(Account account) {
        JTabbedPane tabs = new JTabbedPane();

        tabs.addTab("User Management", new AdminUserManagementPanel(account));
        tabs.addTab("Room Allocation", new AdminRoomAllocationPanel(account));
        tabs.addTab("Hospital Configuration", new AdminHospitalConfigPanel(account));
        return tabs;
    }
}
