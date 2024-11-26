 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
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

        this.setTitle("");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);

        JPanel mainPanel = new JPanel(new GridLayout(6, 1));

        labelUserName = new JLabel("User Name");
        labelUserName.setBorder(new EmptyBorder(0, 20, 0, 20));
        textUserName = new JTextField(15);
        labelUserName.setFont(fontText);
        JPanel panelUserName = new JPanel(new FlowLayout());
        panelUserName.add(labelUserName);
        panelUserName.add(textUserName);

        labelPassword = new JLabel("Password");
        labelPassword.setBorder(new EmptyBorder(0, 20, 0, 20));
        passwordTextField = new JPasswordField(15);
        labelPassword.setFont(fontText);
        JPanel panelPassword = new JPanel(new FlowLayout());
        panelPassword.add(labelPassword);
        panelPassword.add(passwordTextField);

        labelDOB = new JLabel("Date of Birth");
        labelDOB.setBorder(new EmptyBorder(0, 20, 0, 20));
        labelDOB.setFont(fontText);
        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 18));
        JPanel panelDOB = new JPanel(new FlowLayout());
        panelDOB.add(labelDOB);
        panelDOB.add(datePublishedText);

        labelGender = new JLabel("Gender");
        labelGender.setBorder(new EmptyBorder(0, 20, 0, 20));
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

        labelRole = new JLabel("Role");
        labelRole.setBorder(new EmptyBorder(0, 40, 0, 20));
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

        labelEmail = new JLabel("Email");
        labelEmail.setBorder(new EmptyBorder(0, 20, 0, 20));
        textEmail = new JTextField(15);
        labelEmail.setFont(fontText);
        JPanel panelEmail = new JPanel(new FlowLayout());
        panelEmail.add(labelEmail);
        panelEmail.add(textEmail);


        buttonAdd = new JButton("Add");
        buttonAdd.setFont(fontButton);
        buttonBack = new JButton("Back");
        buttonBack.setFont(fontButton);
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(buttonAdd);
        buttonPanel.add(buttonBack);

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

        buttonAdd.addActionListener(new AddButton());
        buttonBack.addActionListener(new BackToDashboard());

        this.setVisible(true);
    }

    public class AddButton implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                if (textUserName.getText().equals("") || passwordTextField.getText().equals("")||
                        datePublishedText.getDate() == null || textEmail.getText().equals("") ||
                        (!radioMale.isSelected() && !radioFemale.isSelected()) ||
                        (!radioAdmin.isSelected() && !radioMember.isSelected())) {
                    throw new EmptyFieldsException("All fields are required !");
                }

                String UserName= textUserName.getText();
                String Password = new String(passwordTextField.getPassword());
                String Email = textEmail.getText();
                String Gender = radioMale.isSelected() ? "Male" : "Female";
                String Role = radioAdmin.isSelected() ? "Admin" : "Member";

                java.util.Date utilDate = datePublishedText.getDate();
                java.sql.Date  DateOfBitrthd = new java.sql.Date(utilDate.getTime());

                if (addNewMember(UserName,Email,Password , Role ,  Gender, DateOfBitrthd)) {
                    JOptionPane.showMessageDialog(null, "Member added successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                    textUserName.setText("");
                    passwordTextField.setText("");
                    textEmail.setText("");
                   genderGroup.clearSelection();
                    datePublishedText.setDate(null);
        roleGroup.clearSelection();
                } else {
                    throw new DatabaseException("Failed to add member. Try again.");
                }
            } catch (EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Empty text", JOptionPane.WARNING_MESSAGE);
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new DashboardAdmin(); 
            dispose(); 
        }
    }

    public boolean addNewMember(String UserName, String Email, String Password , String Role , String Gender ,java.sql.Date DateOfBitrthd  ) {
        String query = "INSERT INTO Users (UserName, Email, Password, Role, Gender, DateOfBitrthd) VALUES (?, ?, ?, ?, ?, ?)";
        try {
          Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
            PreparedStatement stmt = c.prepareStatement(query);

            stmt.setString(1, UserName);
            stmt.setString(2, Email );
            stmt.setString(3,Password );
            stmt.setString(4, Role );
            stmt.setString(5, Gender);
            stmt.setDate(6,DateOfBitrthd);

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public class EmptyFieldsException extends Exception {
        public EmptyFieldsException(String message) {
            super(message);
        }
    }

    public class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }
   
}
