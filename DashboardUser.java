import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.sql.*;
import javax.swing.border.EmptyBorder;

public class DashboardUser extends JFrame {

    private JLabel titleLabel, descriptionLabel;

    public DashboardUser() {
        this.setTitle("User Dashboard");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

          Font titleFont = new Font("Arial", Font.BOLD, 24);
        Font textFont = new Font("Arial", Font.PLAIN, 16);
        
         String welcome = "  Welcome to Online Library Management System  ";
        titleLabel = new JLabel(welcome, SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        titleLabel.setForeground(new Color(34,139,230));
        titleLabel.setBorder(new EmptyBorder(150, 150, 0, 20));
        
         String description = "<html><div style='text-align: center;'>"
                + "Welcome to <b>Online Library Management System</b>, a comprehensive platform designed to enhance users' experience<br>"
                + "in accessing educational resources and academic references with ease. The system provides a wide range<br>"
                + "of services, including member registration, book search, availability checks, and borrowing management.<br><br>"
                + "This system is built with a user-friendly interface that combines simplicity and efficiency to cater<br>"
                + "to the needs of students, academics, and researchers. Our library supports e-learning by integrating<br>"
                + "the latest technologies, enabling access to knowledge anytime and anywhere."
                + "</div></html>";

        descriptionLabel = new JLabel(description, SwingConstants.CENTER);
        descriptionLabel.setFont(textFont);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // Add spacing
        mainPanel.add(descriptionLabel);

        
       JPanel Main = (JPanel) this.getContentPane();
       Main.setLayout(new FlowLayout(FlowLayout.CENTER));
       Main.add(mainPanel);
       
       JMenuBar mb = new JMenuBar();
       JMenu book = new JMenu("Book");
       JMenu notification  = new JMenu("Notification ");
       JMenuItem ExitOption = new JMenuItem("Log Out");
       JMenu content = new JMenu("=");
       JMenuItem Profile = new JMenuItem("Profile");
       JMenuItem Search = new JMenuItem("Search Book");
       JMenuItem Borrowed = new JMenuItem("Borrowed Book");
       JMenuItem Borrow = new JMenuItem("Borrow Book");

    
        setJMenuBar(mb);

  
        mb.add(content);
        mb.add(book);
        mb.add(notification ); 
        
        book.add(Search);
        book.add(Borrowed);
        book.add(Borrow);
        
        mb.add(Box.createHorizontalGlue());
        JPanel logoutPane = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        logoutPane.setOpaque(false);
        logoutPane.add(ExitOption);
        mb.add(logoutPane);
        
        content.add(Profile);

        Profile.addActionListener(new profileUser());
        Search.addActionListener(new SearchBookListener());
        Borrowed.addActionListener(new ViewBorrowedBooksListener());
        Borrow.addActionListener(new ReturnBookListener());
        ExitOption.addActionListener(new Logout());
        this.setVisible(true);
    }

     public class profileUser implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           new UserProfile();
            dispose();
        }
    }
    public class SearchBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           new BookSearch();
            dispose();
        }
    }

    public class ViewBorrowedBooksListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           new BorrowedTableBook();
            dispose();
        }}

        
    public class ReturnBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BooksTable();
            dispose(); 
        }
    }
   public class Logout implements ActionListener{
    public void actionPerformed(ActionEvent e){
         int n = JOptionPane.showConfirmDialog(null , "Do you Want Log out ?","Log Out",JOptionPane.YES_NO_OPTION);
            if(n==0)
            System.exit(0);
    
    }}

}
