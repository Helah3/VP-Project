 import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class DashboardAdmin extends JFrame {
    
    private JLabel titleLabel, descriptionLabel;
    public DashboardAdmin(){

        this.setTitle("");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0,0);
       
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
        JMenu member = new JMenu("Member");
        JMenu notification  = new JMenu("Notification ");
        JMenu content = new JMenu("=");
        JMenuItem Profile = new JMenuItem("Profile");
        JMenuItem addBook = new JMenuItem("Add Book");
        JMenuItem Borrowed = new JMenuItem("Borrowed Book");
        JMenuItem Search = new JMenuItem("Search Book");
        JMenuItem Update = new JMenuItem("Update Book");
        JMenuItem AddMember = new JMenuItem("Add Member");
        JMenuItem UpdateMember = new JMenuItem("Update Member");
        JMenuItem  Statistic = new JMenuItem("Statistics Report");
        JMenuItem ExitOption = new JMenuItem("Log Out");

        
        setJMenuBar(mb);
        mb.add(content);
        mb.add(book);
        mb.add(member);
        mb.add(notification ); 
        
        book.add(addBook);
        book.add(Borrowed);
        book.add(Search);
        book.add(Update);
        book.add(Statistic); 

        member.add(AddMember);
        member.add(UpdateMember);
        
        content.add(Profile);

        mb.add(Box.createHorizontalGlue());
        JPanel logoutPane = new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        logoutPane.setOpaque(false);
        logoutPane.add(ExitOption);
        mb.add(logoutPane);
   

        this.setVisible(true);

        Profile.addActionListener(new profileUser());
        addBook.addActionListener(new addBookButton());
        Borrowed.addActionListener(new Borrowed() );
        Search.addActionListener(new searchBook());
        Update.addActionListener(new updateBook());
        AddMember.addActionListener(new addMember());
        UpdateMember.addActionListener(new updateMember());
        Statistic.addActionListener(new statisticReport());
        ExitOption.addActionListener(new Logout());

    }
    
     public class profileUser implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           new UserProfile();
            dispose();
        }
    }
    
    public class addBookButton implements ActionListener{
    public void actionPerformed(ActionEvent e){
        new addBook();
        dispose();
    
    }}
    
    public class Borrowed implements ActionListener{
    public void actionPerformed(ActionEvent e){
    new BorrowedBook();
            dispose();
    }}
    
      public class searchBook implements ActionListener{
    public void actionPerformed(ActionEvent e){
    new BookSearch();
            dispose();
    }}
      
        public class updateBook implements ActionListener{
    public void actionPerformed(ActionEvent e){
     new BookInventory();
            dispose();
    }}
        
    public class addMember implements ActionListener{
    public void actionPerformed(ActionEvent e){
            new NewMember();
            dispose();


    }}
            
    public class updateMember implements ActionListener{
    public void actionPerformed(ActionEvent e){
                new UpdateMember();
                 dispose();


    }}
    
    public class statisticReport implements ActionListener{
    public void actionPerformed(ActionEvent e){
            new ReportModule();
            dispose();

    }}
    public class Logout implements ActionListener{
    public void actionPerformed(ActionEvent e){
         int n = JOptionPane.showConfirmDialog(null , "Do you Want Log out ?","Log Out",JOptionPane.YES_NO_OPTION);
            if(n==0)
            System.exit(0);
    
    }}
   
    
}
