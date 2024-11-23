package com.mycompany.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ReturnBook extends JFrame {
    private JLabel dateReturnedLabel, bookNameLabel, yourNameLabel, emailLabel;
    private JTextField dateReturnedField, bookNameField, yourNameField, emailField;
    private JButton okButton, backButton;

    public ReturnBook() {
        setTitle("Return Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create a panel for the form with GridLayout
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // Add padding

        // Create components
        dateReturnedLabel = new JLabel("Date Returned:");
        dateReturnedField = new JTextField(20);
        bookNameLabel = new JLabel("Book name:");
        bookNameField = new JTextField(20);
        yourNameLabel = new JLabel("Your name:");
        yourNameField = new JTextField(20);
        emailLabel = new JLabel("Email:");
        emailField = new JTextField(20);

        // Add components to the form panel
        formPanel.add(dateReturnedLabel);
        formPanel.add(dateReturnedField);
        formPanel.add(bookNameLabel);
        formPanel.add(bookNameField);
        formPanel.add(yourNameLabel);
        formPanel.add(yourNameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);

        // Create a panel for buttons with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        okButton = new JButton("OK");
        backButton = new JButton("Back");
        buttonPanel.add(okButton);
        buttonPanel.add(backButton);

        // Add form panel and button panel to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener to clear text fields on Back button click
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear all text fields
                dateReturnedField.setText("");
                bookNameField.setText("");
                yourNameField.setText("");
                emailField.setText("");
            }
        });

        // Add ActionListener to OK button to check for empty fields
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Check if any field is empty
                if (dateReturnedField.getText().trim().isEmpty() ||
                        bookNameField.getText().trim().isEmpty() ||
                        yourNameField.getText().trim().isEmpty() ||
                        emailField.getText().trim().isEmpty()) {
                    // Show warning message
                    JOptionPane.showMessageDialog(null,"Please fill in the text field!","Warning",JOptionPane.WARNING_MESSAGE);
                } else {
                    // Process the form data (this is a placeholder for actual logic)
JOptionPane.showMessageDialog(null,"Book return processed successfully!","Success",JOptionPane.INFORMATION_MESSAGE);
                }
            }
        });

        pack();
        setVisible(true);
    }

    public static void main(String[] args) {
        new ReturnBook();
    }
}
