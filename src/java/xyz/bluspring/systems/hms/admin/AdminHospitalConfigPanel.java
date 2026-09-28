package xyz.bluspring.systems.hms.admin;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.TitledBorder;

import xyz.bluspring.systems.hms.Main;
import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.data.AdminDataStorage;
import xyz.bluspring.systems.hms.data.DoctorDataStorage;
import xyz.bluspring.systems.hms.data.InsuranceNetwork;

public class AdminHospitalConfigPanel extends JPanel {
    public AdminHospitalConfigPanel(Account account) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var insuranceNetworks = new ArrayList<>(AdminDataStorage.INSTANCE.getInsuranceNetworks());

        {
            var networksMainPanel = new JPanel();
            networksMainPanel.setLayout(new BoxLayout(networksMainPanel, BoxLayout.Y_AXIS));
            networksMainPanel.setBorder(new TitledBorder("Insurance Networks"));

            var topPanel = new JPanel();
            topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

            var nameLabel = new JLabel("Name: ");
            var nameBox = new JTextField();
            var addButton = new JButton("+");

            topPanel.add(nameLabel);
            topPanel.add(nameBox);
            topPanel.add(addButton);

            addButton.addActionListener(_ -> {
                if (nameBox.getText().isBlank()) {
                    JOptionPane.showMessageDialog(this, "Insurance network name should not be blank!");
                    return;
                }

                AdminDataStorage.INSTANCE.getInsuranceNetworks().add(new InsuranceNetwork(nameBox.getText()));
                AdminDataStorage.INSTANCE.save();
                refreshPage(account);
            });

            networksMainPanel.add(topPanel);

            var networksPanel = new JPanel();
            networksPanel.setLayout(new BoxLayout(networksPanel, BoxLayout.Y_AXIS));

            for (InsuranceNetwork network : insuranceNetworks) {
                var networkPanel = new JPanel();
                networkPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
                networkPanel.setBorder(new TitledBorder(network.name()));
                networkPanel.setPreferredSize(new Dimension(820, 100));

                var deleteRoomButton = new JButton("Delete Network");
                deleteRoomButton.addActionListener(_ -> {
                    var option = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this network?");

                    if (option == JOptionPane.YES_OPTION) {
                        AdminDataStorage.INSTANCE.getInsuranceNetworks().remove(network);

                        AdminDataStorage.INSTANCE.save();
                        DoctorDataStorage.INSTANCE.save();
                        refreshPage(account);
                    }
                });

                deleteRoomButton.setPreferredSize(new Dimension(140, 30));
                networkPanel.add(deleteRoomButton);

                networksPanel.add(networkPanel);
            }

            var scrollPane = new JScrollPane(networksPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setPreferredSize(new Dimension(820, 290));

            networksMainPanel.add(scrollPane);
            this.add(networksMainPanel);
        }

        {
            var configPanel = new JPanel();
            configPanel.setLayout(new BoxLayout(configPanel, BoxLayout.Y_AXIS));
            configPanel.setBorder(new TitledBorder("Hospital Configuration"));

            configPanel.add(new JLabel("Base Consultation Rate (RM):"));
            var baseConsultationRate = (new JSpinner(new SpinnerNumberModel(AdminDataStorage.INSTANCE.getBaseConsultationRate(), 0f, 100_000_000f, 1f)));
            configPanel.add(baseConsultationRate);

            baseConsultationRate.addChangeListener(_ -> {
                AdminDataStorage.INSTANCE.setBaseConsultationRate((float) baseConsultationRate.getValue());
            });

            this.add(configPanel);
        }
    }

    private static void refreshPage(Account account) {
        Main.reset();
        var page = AdminUI.openAdminUI(account);
        Main.getFrame().getContentPane().add(page);
        page.setSelectedIndex(2);

        Main.refresh();
    }
}
