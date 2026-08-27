package xyz.bluspring.systems.hms;

import java.awt.*;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import xyz.bluspring.systems.hms.auth.Account;
import xyz.bluspring.systems.hms.auth.AuthLog;

public class Main {
    public static final boolean IS_TESTING = false;

    private static final JFrame window = new JFrame("Hospital Management System"); // TODO: we can come up with a better name right?
    public static Color topGradient = Color.WHITE;
    public static Color bottomGradient = Color.WHITE;

    public static JFrame getFrame() {
        return window;
    }

    public static void reset() {
        window.getContentPane().removeAll();
    }

    public static void refresh() {
        window.invalidate();
        window.validate();
        window.repaint();
    }

    public static void resetSizesToSmallWindow() {
        window.setPreferredSize(new Dimension(843, 600));
        window.setLocationRelativeTo(null);
        window.pack();
    }

    public static void main(String[] args) {
        window.setMinimumSize(new Dimension(640, 480));
        window.setVisible(false);
        window.setResizable(false);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Sets default colours for each component
        // Key reference: https://alvinalexander.com/java/java-uimanager-color-keys-list/
        UIManager.getDefaults().put("Label.foreground", Color.WHITE);

        window.setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                var g2d = (Graphics2D) g;
                // Makes the rendering use "quality" rendering
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                var width = this.getWidth();
                var height = this.getHeight();
                var gradient = new GradientPaint(0f, 0f, topGradient, 0f, height, bottomGradient);

                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);

                //super.paintComponent(g);
            }
        });

        // Initialize data classes, just to make sure that their serializers are registered first.
        Account.init();
        AuthLog.init();
        // TODO

        if (IS_TESTING) { // Add testing data
            System.out.println("Testing data enabled!");
            // TODO
        }
        // I will comment this out in the next one..just testing if everything is working from my side or not.
       /* var manager = new xyz.bluspring.systems.hms.role.manager.MedicalManager();

        window.getContentPane().setLayout(new java.awt.BorderLayout());

        window.getContentPane().add(manager.createDashboardUI(), java.awt.BorderLayout.CENTER);

        resetSizesToSmallWindow(); */

        window.setVisible(true);
    }
}
