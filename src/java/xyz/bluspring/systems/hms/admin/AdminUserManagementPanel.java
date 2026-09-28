package xyz.bluspring.systems.hms.admin;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Arrays;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

import xyz.bluspring.systems.hms.LoginScreen;
import xyz.bluspring.systems.hms.Main;
import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.auth.AccountType;
import xyz.bluspring.systems.hms.auth.AuthManager;
import xyz.bluspring.systems.hms.data.RoleManager;
import xyz.bluspring.systems.hms.role.manager.MedicalManager;
import xyz.bluspring.systems.hms.ui.ComponentHelper;
import xyz.bluspring.systems.hms.ui.PlaceholderPasswordTextField;
import xyz.bluspring.systems.hms.ui.PlaceholderTextField;
import xyz.bluspring.systems.hms.ui.ScrollablePanel;
import xyz.bluspring.systems.hms.utils.Utils;

public class AdminUserManagementPanel extends JPanel {
    private static final int PANEL_WIDTH = 775;

    public AdminUserManagementPanel(Account account) {
        var authManagers = Arrays.stream(AccountType.values()).map(LoginScreen::getAuthManager).toList();
        var window = Main.getFrame();

        if (authManagers.stream().allMatch(e -> e.getAccounts().isEmpty())) {
            this.add(Utils.make(new JLabel("Error: No accounts are available!"), label -> label.setForeground(Color.RED)));
            return;
        }

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        var sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.X_AXIS));

        var createAccountBtn = new JButton("Create Account");
        createAccountBtn.addActionListener(e -> {
            JFrame frame = new JFrame("Creating a new account");
            LoginScreen.openRegisterScreen(frame, true, _ -> {
                frame.setVisible(false);
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                frame.dispose();
            });

            frame.setPreferredSize(new Dimension(843, 600));
            frame.setVisible(true);
            frame.pack();
            frame.setLocationRelativeTo(null);

            frame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    super.windowClosing(e);
                    frame.dispose();

                    // we want to go back to the accounts page.
                    Main.reset();
                    var tabs = AdminUI.openAdminUI(account);
                    tabs.setSelectedIndex(0);
                    window.getContentPane().add(tabs);
                    Main.refresh();
                }
            });
        });

        sidePanel.add(Utils.make(new JButton("Log Out"), button -> {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);

            button.addActionListener(e -> {
                Main.reset();
                Main.getFrame().getContentPane().add(new LoginScreen());
                Main.refresh();
            });
        }));

        sidePanel.add(createAccountBtn);
        mainPanel.add(sidePanel);

        var secondPanel = new JPanel();
        secondPanel.setOpaque(false);

        {
            var contentPanel = new ScrollablePanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setOpaque(false);

            var mainScrollPane = new JScrollPane(contentPanel);
            mainScrollPane.setPreferredSize(new Dimension(PANEL_WIDTH, 500));
            mainScrollPane.setBorder(new LineBorder(new Color(0f, 0f, 0f, 0.2f), 1));
            mainScrollPane.setOpaque(false);

            mainScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            mainScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

            for (AuthManager authManager : authManagers) {
                for (Account acc : authManager.getAccounts()) {
                    var panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

                    panel.setPreferredSize(new Dimension(PANEL_WIDTH - 2, 120));
                    panel.setMaximumSize(new Dimension(PANEL_WIDTH - 2, 120));
                    panel.setBackground(new Color(0xeeeeee));
                    panel.setBorder(new LineBorder(Color.BLACK, 1, true));

                    var infoPanel = new JPanel();
                    infoPanel.setOpaque(false);
                    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
                    infoPanel.add(new JLabel("Email: " + acc.getEmail()));
                    infoPanel.add(new JLabel("Name: " + acc.getDisplayName()));
                    infoPanel.add(new JLabel("Type: " + acc.getAccountType().getProperName()));

                    panel.add(infoPanel);

                    panel.add(Utils.make(new JButton(new ImageIcon(Utils.resizeImage("pencil.png", 24, 24))), button -> {
                        button.setPreferredSize(new Dimension(24, 24));
                        button.setToolTipText("Edit");

                        button.addActionListener(e -> {
                            showAccountDetailsScreen(authManager, acc, account, () -> {
                                Main.reset();
                                AdminUI.openAdminUI(account);
                                Main.refresh();
                            });
                        });
                    }));

                    panel.add(Utils.make(new JButton(new ImageIcon(Utils.resizeImage("trash_bin.png", 24, 24))), button -> {
                        button.setPreferredSize(new Dimension(24, 24));
                        button.setToolTipText("Delete");

                        if (account == acc) {
                            button.setEnabled(false);
                        }

                        button.addActionListener(e -> {
                            var result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this account?");

                            if (result == JOptionPane.YES_OPTION) {
                                authManager.deleteAccount(acc);
                                Main.reset();
                                AdminUI.openAdminUI(account);
                                Main.refresh();
                            }
                        });
                    }));

                    contentPanel.add(panel);
                }
            }

            secondPanel.add(mainScrollPane);
        }

        mainPanel.add(secondPanel);
        this.add(mainPanel);
    }

    private static void showAccountDetailsScreen(AuthManager manager, Account account, Account accountToNavigate, Runnable onExit) {
        JFrame frame = new JFrame("Editing " + account.getDisplayName());

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            // not sure why Swing's not allowing us to resize anything, so we're just adding padding ourselves.
            panel.add(new JLabel(new ImageIcon(Utils.createEmptyImage(15, 18))));
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setOpaque(false);

            try {
                var image = Utils.getCircularImage(ImageIO.read(Main.class.getResourceAsStream("/images/profile.png")))
                    .getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                panel.add(new JLabel(new ImageIcon(image)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mainPanel.add(panel);
        }

        var email = new PlaceholderTextField("E-mail");
        var displayName = new PlaceholderTextField("Display Name");
        var managerSelector = new JComboBox<>(RoleManager.INSTANCE.getManagers().toArray(new MedicalManager[0]));

        email.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        displayName.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        managerSelector.setAlignmentX(JComponent.CENTER_ALIGNMENT);

        email.setMaximumSize(new Dimension(300, 30));
        displayName.setMaximumSize(new Dimension(300, 30));
        managerSelector.setMaximumSize(new Dimension(300, 30));

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(Utils.make(new JLabel("Email"), label -> label.setAlignmentX(JLabel.CENTER_ALIGNMENT)));
            panel.add(
                Utils.make(email, field -> {
                    field.setText(account.getEmail());
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            panel.add(Utils.make(new JLabel("Display Name"), label -> label.setAlignmentX(JLabel.CENTER_ALIGNMENT)));
            panel.add(
                Utils.make(displayName, field -> {
                    field.setText(account.getDisplayName());
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            mainPanel.add(panel);

            if (account.getAccountType() == AccountType.DOCTOR) {
                panel.add(Utils.make(new JLabel("Assigned Manager"), label -> label.setAlignmentX(JLabel.CENTER_ALIGNMENT)));

                managerSelector.setRenderer(new javax.swing.DefaultListCellRenderer() {
                    @Override
                    public Component getListCellRendererComponent(javax.swing.JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                        String text = (value instanceof MedicalManager manager) ? manager.getProfile().getDisplayName() : "";
                        return super.getListCellRendererComponent(list, text, index, isSelected, cellHasFocus);
                    }
                });

                panel.add(managerSelector);
            }
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            panel.add(Box.createVerticalStrut(10));
            if (account == accountToNavigate) {
                panel.add(Utils.make(new JPanel(new FlowLayout(FlowLayout.CENTER)), changePassword -> {
                    changePassword.setOpaque(false);
                    changePassword.add(Utils.make(new JButton("Change Password"), button -> {
                        ComponentHelper.makeHyperlink(button);
                        button.addActionListener(e -> showChangePasswordScreen(frame, manager, account, onExit));
                    }));
                }));
                panel.add(Box.createVerticalStrut(10));
            }

            panel.add(
                Utils.make(new JButton("Save"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    button.setEnabled(false);

                    button.addActionListener(e -> {
                        account.setEmail(email.getText());
                        account.setDisplayName(displayName.getText());

                        manager.save();
                    });

                    if (account.getAccountType() == AccountType.DOCTOR) {
                        var item = managerSelector.getSelectedItem();
                        var doctor = RoleManager.INSTANCE.findDoctorById(account.getUUID().toString());

                        if (doctor != null) {
                            if (item != null) {
                                doctor.setAssignedManager((MedicalManager) item);
                            } else {
                                doctor.setAssignedManager(null);
                            }
                        }
                    }

                    var changeEvent = (ActionListener) e -> {
                        // Disable button if the email or display name is blank - we do not allow empty emails or display names.
                        if (email.getText().isBlank() || displayName.getText().isBlank()) {
                            button.setEnabled(false);
                        } else if (email.getText().equals(account.getEmail()) && displayName.getText().equals(account.getDisplayName())) {
                            button.setEnabled(false); // If they're the same, don't enable the Save button
                        } else {
                            button.setEnabled(true); // Otherwise just allow the button to be enabled
                        }
                    };

                    // Add the shared action event to both email and display name
                    email.addActionListener(changeEvent);
                    displayName.addActionListener(changeEvent);
                })
            );

            panel.add(Box.createVerticalStrut(10));

            panel.add(
                Utils.make(new JButton("Exit"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);

                    button.addActionListener(e -> {
                        frame.setVisible(false);
                        frame.dispose();
                        onExit.run();
                    });
                })
            );

            panel.add(Box.createVerticalStrut(10));

            if (accountToNavigate == account) {
                panel.add(
                    Utils.make(new JButton("Log Out"), button -> {
                        button.setAlignmentX(Component.CENTER_ALIGNMENT);

                        button.addActionListener(e -> {
                            Main.reset();
                            Main.getFrame().getContentPane().add(new LoginScreen());
                            Main.refresh();

                            frame.setVisible(false);
                            frame.dispose();
                        });
                    })
                );
            }

            mainPanel.add(panel);
        }

        var scrollable = new JScrollPane(mainPanel);
        scrollable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        frame.getContentPane().add(scrollable);

        frame.setPreferredSize(new Dimension(450, 400));
        frame.pack();
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    public static void showChangePasswordScreen(JFrame window, AuthManager authManager, Account account, Runnable onExit) {
        window.getContentPane().removeAll();

        var mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setOpaque(false);

        var oldPassword = new PlaceholderPasswordTextField("Old Password");
        var newPassword = new PlaceholderPasswordTextField("New Password");
        var confirmNewPassword = new PlaceholderPasswordTextField("Confirm New Password");

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            // not sure why Swing's not allowing us to resize anything, so we're just adding padding ourselves.
            panel.add(new JLabel(new ImageIcon(Utils.createEmptyImage(15, 18))));
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setOpaque(false);

            try {
                var image = Utils.getCircularImage(ImageIO.read(Main.class.getResourceAsStream("/images/profile.png")))
                    .getScaledInstance(128, 128, Image.SCALE_SMOOTH);
                panel.add(new JLabel(new ImageIcon(image)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            mainPanel.add(panel);
        }

        {
            var panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

            panel.add(new JLabel("Changing password for " + account.getDisplayName()));

            panel.setOpaque(false);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

            panel.add(
                Utils.make(oldPassword, field -> {
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            panel.add(
                Utils.make(newPassword, field -> {
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            panel.add(
                Utils.make(confirmNewPassword, field -> {
                    ComponentHelper.disallowWhitespace(field);
                    ComponentHelper.makePaddedAndMarginedTextField(field);
                })
            );

            mainPanel.add(panel);
        }

        var errorText = new JLabel("Error: [unknown]");
        errorText.setForeground(Color.RED);
        errorText.setFont(errorText.getFont().deriveFont(14f));

        {
            var panel = new JPanel();
            panel.setOpaque(false);
            panel.setLayout(new FlowLayout(FlowLayout.CENTER));
            errorText.setVisible(false);
            panel.add(errorText);
            mainPanel.add(panel);
        }

        {
            var panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(false);

            panel.add(
                Utils.make(new JButton("Save"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);
                    button.setEnabled(false);

                    button.addActionListener(e -> {
                        try {
                            authManager.changePassword(account, oldPassword.getText(), newPassword.getText());
                        } catch (Exception exception) {
                            errorText.setText("Error: " + exception.getMessage());
                            errorText.setVisible(true);
                            exception.printStackTrace();
                        }

                        showAccountDetailsScreen(authManager, account, account, onExit);
                    });

                    var changeEvent = (ActionListener) e -> {
                        // Disable button if the email or display name is blank - we do not allow empty emails or display names.
                        if (oldPassword.getText().isBlank() || newPassword.getText().isBlank() || confirmNewPassword.getText().isBlank()) {
                            button.setEnabled(false);
                        } else {
                            button.setEnabled(newPassword.getText().equals(confirmNewPassword.getText())); // If they're the same, enable the Save button
                        }
                    };

                    // Add the shared action event to all password fields
                    oldPassword.addActionListener(changeEvent);
                    newPassword.addActionListener(changeEvent);
                    confirmNewPassword.addActionListener(changeEvent);
                })
            );

            panel.add(
                Utils.make(new JButton("Exit"), button -> {
                    button.setAlignmentX(Component.CENTER_ALIGNMENT);

                    button.addActionListener(e -> {
                        window.setVisible(false);
                        window.dispose();
                    });
                })
            );

            mainPanel.add(panel);
        }

        window.getContentPane().add(mainPanel);

        window.pack();
        window.setLocationRelativeTo(null);

        window.invalidate();
        window.validate();
        window.repaint();
    }
}
