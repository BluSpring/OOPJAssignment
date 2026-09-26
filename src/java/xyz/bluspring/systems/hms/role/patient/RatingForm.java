package xyz.bluspring.systems.hms.role.patient;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import xyz.bluspring.systems.hms.ui.ComponentHelper;
import xyz.bluspring.systems.hms.ui.ListenableTextArea;

/**
 * Lets a patient submit a rating and comment about their assigned doctor.
 */
public class RatingForm extends JPanel {
    private final Patient patient;
    private final JComboBox<Integer> scoreSelector;
    private final ListenableTextArea commentArea;

    public RatingForm(Patient patient) {
        this.patient = patient;

        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        var scoreLabel = new JLabel("Score (1-5):");
        scoreSelector = new JComboBox<>(new Integer[]{1, 2, 3, 4, 5});
        scoreSelector.setMaximumSize(new Dimension(80, 30));
        scoreSelector.setAlignmentX(Component.LEFT_ALIGNMENT);

        var commentLabel = new JLabel("Comment:");
        commentArea = new ListenableTextArea();
        commentArea.setLineWrap(true);
        commentArea.setRows(4);
        ComponentHelper.makePaddedAndMarginedTextField(commentArea);
        commentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton submitButton = new JButton("Submit Rating");
        submitButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        submitButton.addActionListener(e -> submit());

        this.add(scoreLabel);
        this.add(scoreSelector);
        this.add(Box.createVerticalStrut(8));
        this.add(commentLabel);
        this.add(commentArea);
        this.add(Box.createVerticalStrut(8));
        this.add(submitButton);
    }

    private void submit() {
        if (patient.getAssignedDoctor() == null) {
            JOptionPane.showMessageDialog(this, "You don't have an assigned doctor to rate yet.");
            return;
        }

        int score = (int) scoreSelector.getSelectedItem();
        String comment = commentArea.getText();
        Date today = new Date();

        Rating rating = new Rating(score, comment, patient.getAssignedDoctor(), today);
        patient.addRating(rating);

        commentArea.setText("");
        JOptionPane.showMessageDialog(this, "Rating submitted, thank you!");
    }
}
