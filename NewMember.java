import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import com.toedter.calendar.JDateChooser;
import java.sql.*;
import java.text.SimpleDateFormat;

public class NewMember extends JFrame {

    private JLabel labelUserName, labelPassword, labelDOB, labelGender, labelEmail, labelRole;
    private JTextField textUserName, textEmail;
    private JPasswordField passwordTextField;
    private JRadioButton radioMale, radioFemale, radioAdmin, radioMember;
    private JButton buttonAdd, buttonBack;
    private ButtonGroup genderGroup, roleGroup;
    private JDateChooser datePublishedText;

    public NewMember() {

        this.setTitle("New Member");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);

        JPanel mainPanel = new JPanel(new GridLayout(6, 1));

        // User Name
        labelUserName = new JLabel("User Name:");
        textUserName = new JTextField(15);
        labelUserName.setFont(fontText);
        JPanel panelUserName = new JPanel(new FlowLayout());
        panelUserName.add(labelUserName);
        panelUserName.add(textUserName);

        // Password
        labelPassword = new JLabel("Password:");
        passwordTextField = new JPasswordField(15);
        labelPassword.setFont(fontText);
        JPanel panelPassword = new JPanel(new FlowLayout());
        panelPassword.add(labelPassword);
        panelPassword.add(passwordTextField);

        // Date of Birth
        labelDOB = new JLabel("Date of Birth:");
        labelDOB.setFont(fontText);
        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 25));
        JPanel panelDOB = new JPanel(new FlowLayout());
        panelDOB.add(labelDOB);
        panelDOB.add(datePublishedText);

        // Gender
        labelGender = new JLabel("Gender:");
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

        // Role
        labelRole = new JLabel("Role:");
        labelRole.setFont(fontText);
        radioAdmin = new JRadioButton("Admin");
        radioMember = new JRadioButton("Member");
        roleGroup = new ButtonGroup();
        roleGroup.add(radioAdmin);
        roleGroup.add(radioMember);
        JPanel panelRole = new JPanel(new FlowLayout());
        panelRole.add(labelRole);
        panelRole.add(radioAdmin);
        panelRole.add(radioMember);

        // Email
        labelEmail = new JLabel("Email:");
        textEmail = new JTextField(15);
        labelEmail.setFont(fontText);
        JPanel panelEmail = new JPanel(new FlowLayout());
        panelEmail.add(labelEmail);
        panelEmail.add(textEmail);

        // Buttons
        buttonAdd = new JButton("Add");
        buttonAdd.setFont(fontButton);
        buttonBack = new JButton("Back");
        buttonBack.setFont(fontButton);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonBack);

        // Add panels to the main panel
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.add(panelUserName);
        mainPanel.add(panelPassword);
        mainPanel.add(panelDOB);
        mainPanel.add(panelGender);
        mainPanel.add(panelRole);
        mainPanel.add(panelEmail);

        JPanel mainContainer = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("New Member Details");
        title.setTitleFont(fontTitle);
        mainContainer.setBorder(title);

        mainContainer.add(mainPanel, BorderLayout.CENTER);
        mainContainer.add(buttonPanel, BorderLayout.SOUTH);

        // Add action listeners to buttons
        buttonAdd.addActionListener(new AddButton());
        buttonBack.addActionListener(new BackToDashboard());

        this.setVisible(true);
    }

    // ** Event Handling for Add Button **
    public class AddButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                // Validate input
                if (textUserName.getText().equals("") || passwordTextField.getPassword().length == 0 ||
                        datePublishedText.getDate() == null || textEmail.getText().equals("") ||
                        (!radioMale.isSelected() && !radioFemale.isSelected()) ||
                        (!radioAdmin.isSelected() && !radioMember.isSelected())) {
                    throw new EmptyFieldsException("All fields are required!");
                }

                // Get input values
                String username = textUserName.getText();
                String password = new String(passwordTextField.getPassword());
                String email = textEmail.getText();
                String gender = radioMale.isSelected() ? "Male" : "Female";
                String role = radioAdmin.isSelected() ? "Admin" : "Member";

                java.util.Date utilDate = datePublishedText.getDate();
                java.sql.Date dob = new java.sql.Date(utilDate.getTime());

                // Insert data into database
                if (addNewMember(username, password, dob, gender, role, email)) {
                    JOptionPane.showMessageDialog(null, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new DatabaseException("Failed to add member. Try again.");
                }
            } catch (EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ** Event Handling for Back Button **
    public class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            // Navigate back to the dashboard
            new DashboardUser(); // Assuming a DashboardAdmin class exists
            dispose(); // Close the current window
        }
    }

    // ** Database Method for Adding Member **
    public boolean addNewMember(String username, String password, java.sql.Date dob, String gender, String role, String email) {
        String query = "INSERT INTO Members (UserName, Password, DOB, Gender, Role, Email) VALUES (?, ?, ?, ?, ?, ?)";
        try {
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C://Users//baato//OneDrive//المستندات//NetBeansProjects"); // Update with your database path
            PreparedStatement stmt = connection.prepareStatement(query);

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setDate(3, dob);
            stmt.setString(4, gender);
            stmt.setString(5, role);
            stmt.setString(6, email);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    // ** Custom Exception for Empty Fields **
    public class EmptyFieldsException extends Exception {
        public EmptyFieldsException(String message) {
            super(message);
        }
    }

    // ** Custom Exception for Database Errors **
    public class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }

    // ** Main Method **
    public static void main(String[] args) {
        new NewMember();
    }
}
