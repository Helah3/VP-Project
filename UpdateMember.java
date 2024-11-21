import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.util.Date;

public class UpdateMember extends JFrame {

    private JLabel labelUserName, labelPassword, labelDOB, labelGender, labelEmail;
    private JTextField textUserName, textEmail;
    private JPasswordField passwordTextField;
    private JDateChooser datePublishedText;
    private JRadioButton radioMale, radioFemale;
    private JButton buttonUpd, buttonBack;
    private ButtonGroup genderGroup;

    public UpdateMember() {
        this.setTitle("Update Member");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);

        JPanel mainPanel = new JPanel(new GridLayout(5, 1));

        // User Name
        labelUserName = new JLabel("User Name:");
        textUserName = new JTextField(20);
        labelUserName.setBorder(new EmptyBorder(0, 20, 0, 25));
        labelUserName.setFont(fontText);
        JPanel panelUserName = new JPanel(new FlowLayout());
        panelUserName.add(labelUserName);
        panelUserName.add(textUserName);

        // Password
        labelPassword = new JLabel("Password:");
        passwordTextField = new JPasswordField(20);
        labelPassword.setBorder(new EmptyBorder(0, 30, 0, 25));
        labelPassword.setFont(fontText);
        JPanel panelPassword = new JPanel(new FlowLayout());
        panelPassword.add(labelPassword);
        panelPassword.add(passwordTextField);

        // Date of Birth
        labelDOB = new JLabel("Date of Birth:");
        labelDOB.setBorder(new EmptyBorder(0, 20, 0, 30));
        labelDOB.setFont(fontText);
        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 20));
        JPanel panelDOB = new JPanel(new FlowLayout());
        panelDOB.add(labelDOB);
        panelDOB.add(datePublishedText);

        // Gender
        labelGender = new JLabel("Gender:");
        labelGender.setBorder(new EmptyBorder(0, 20, 0, 50));
        labelGender.setFont(fontText);
        radioMale = new JRadioButton("Male");
        radioFemale = new JRadioButton("Female");
        genderGroup = new ButtonGroup();
        genderGroup.add(radioMale);
        genderGroup.add(radioFemale);
        JPanel panelGender = new JPanel(new FlowLayout());
        panelGender.add(labelGender);
        panelGender.add(radioMale);
        panelGender.add(radioFemale);

        // Email
        labelEmail = new JLabel("Email:");
        textEmail = new JTextField(15);
        labelEmail.setBorder(new EmptyBorder(0, 50, 0, 25));
        labelEmail.setFont(fontText);
        JPanel panelEmail = new JPanel(new FlowLayout());
        panelEmail.add(labelEmail);
        panelEmail.add(textEmail);

        // Buttons
        buttonUpd = new JButton("Update");
        buttonUpd.setFont(fontButton);
        buttonBack = new JButton("Back");
        buttonBack.setFont(fontButton);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buttonUpd);
        buttonPanel.add(buttonBack);

        // Adding panels to the form panel
        mainPanel.add(panelUserName);
        mainPanel.add(panelPassword);
        mainPanel.add(panelDOB);
        mainPanel.add(panelGender);
        mainPanel.add(panelEmail);

        JPanel mainContainer = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("Update Member Details");
        title.setTitleFont(fontTitle);
        mainContainer.setBorder(title);

        mainContainer.add(mainPanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners
        buttonUpd.addActionListener(new UpdateButton());
        buttonBack.addActionListener(new BackButton());

        this.setVisible(true);
    }

    // Event handling for the Update button
    public class UpdateButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Input validation
                if (textUserName.getText().isEmpty() || passwordTextField.getPassword().length == 0 ||
                        datePublishedText.getDate() == null || textEmail.getText().isEmpty() ||
                        (!radioMale.isSelected() && !radioFemale.isSelected())) {
                    throw new EmptyFieldsException("All fields are required!");
                }

                // Get input values
                String username = textUserName.getText();
                String password = new String(passwordTextField.getPassword());
                String email = textEmail.getText();
                String gender = radioMale.isSelected() ? "Male" : "Female";

                Date utilDate = datePublishedText.getDate();
                java.sql.Date dob = new java.sql.Date(utilDate.getTime());

                // Update database
                if (updateMemberDetails(username, password, dob, gender, email)) {
                    JOptionPane.showMessageDialog(null, "Member updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new DatabaseException("Failed to update member. Please try again.");
                }
            } catch (EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Event handling for the Back button
    public class BackButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Navigate back to the previous screen (assuming DashboardAdmin exists)
            new DashboardUser();
            dispose();
        }
    }

    // Method to update member details in the database
    public boolean updateMemberDetails(String username, String password, java.sql.Date dob, String gender, String email) {
        String query = "UPDATE Members SET Password = ?, DOB = ?, Gender = ?, Email = ? WHERE UserName = ?";
        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C://Users//baato//OneDrive//المستندات//NetBeansProjects"); // Update the path to your database
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, password);
            stmt.setDate(2, dob);
            stmt.setString(3, gender);
            stmt.setString(4, email);
            stmt.setString(5, username);

            int rowsUpdated = stmt.executeUpdate();
            return rowsUpdated > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // Custom exception for empty fields
    public class EmptyFieldsException extends Exception {
        public EmptyFieldsException(String message) {
            super(message);
        }
    }

    // Custom exception for database errors
    public class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }

    // Main method
    public static void main(String[] args) {
        new UpdateMember();
    }
}
