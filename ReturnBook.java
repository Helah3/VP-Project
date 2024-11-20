
package com.mycompany.project;

import javax.swing.*;
import java.awt.*;


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
        okButton = new JButton("Ok");
        backButton = new JButton("Back");
        buttonPanel.add(okButton);
        buttonPanel.add(backButton);

        // Add form panel and button panel to the frame
        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
     pack();

        setVisible(true);
    }

    public static void main(String[] args) {
        new ReturnBook();
    }
}

