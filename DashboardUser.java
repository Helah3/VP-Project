import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.sql.*;
import javax.swing.border.EmptyBorder;
import java.util.LinkedList;
import java.util.Queue;

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

     // Load Notifications from Database
    private void loadNotifications() {
        NotifyUser.clearNotifications(); // Clear existing notifications

        try (Connection conn = getConnection()) {
            // Notifications for reminders (books due within the next 3 days)
            String reminderQuery = "SELECT b.Title, br.ReturnDate FROM Borrowings br "
                    + "JOIN Books b ON br.BookID = b.BookID "
                    + "WHERE br.ReturnDate BETWEEN DATE() AND DATE() + 3 AND br.IsReturned = FALSE";
            try (PreparedStatement stmt = conn.prepareStatement(reminderQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String returnDate = rs.getDate("ReturnDate").toString();
                    NotifyUser.addNotification("Return Reminder: " + title + " is due on " + returnDate, "Reminder");
                }
            }

            // Notifications for overdue books
            String overdueQuery = "SELECT b.Title, br.ReturnDate FROM Borrowings br "
                    + "JOIN Books b ON br.BookID = b.BookID "
                    + "WHERE br.ReturnDate < DATE() AND br.IsReturned = FALSE";
            try (PreparedStatement stmt = conn.prepareStatement(overdueQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String overdueDate = rs.getDate("ReturnDate").toString();
                    NotifyUser.addNotification("Overdue: " + title + " was due on " + overdueDate, "Overdue");
                }
            }

            // Notifications for new borrowings
            String newBorrowQuery = "SELECT b.Title, br.BorrowDate, br.ReturnDate FROM Borrowings br "
                    + "JOIN Books b ON br.BookID = b.BookID "
                    + "WHERE br.BorrowDate = DATE()";
            try (PreparedStatement stmt = conn.prepareStatement(newBorrowQuery);
                 ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String title = rs.getString("Title");
                    String borrowDate = rs.getDate("BorrowDate").toString();
                    String returnDate = rs.getDate("ReturnDate").toString();
                    NotifyUser.addNotification("New Borrow: " + title + " (from " + borrowDate + " to " + returnDate + ")", "New Borrow");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading notifications: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

     // Establish a database connection
    private Connection getConnection() throws SQLException {
        String url = "jdbc:ucanaccess://C:/Users/helah/Downloads/LibraryDB (1).accdb";
        return DriverManager.getConnection(url);
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

    // Notification System
    public static class NotifyUser {

        private static final JPanel notificationPanel = new JPanel();
        private static final Queue<String> notificationQueue = new LinkedList<>();

        public static void initializeNotificationPanel(JFrame frame) {
            notificationPanel.setLayout(new BoxLayout(notificationPanel, BoxLayout.Y_AXIS));
            notificationPanel.setBackground(Color.LIGHT_GRAY);
            notificationPanel.setPreferredSize(new Dimension(300, frame.getHeight()));
            notificationPanel.setBorder(BorderFactory.createTitledBorder("Notifications"));

            JScrollPane scrollPane = new JScrollPane(notificationPanel);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            frame.add(scrollPane, BorderLayout.EAST);
        }

        public static void addNotification(String message, String title) {
            notificationQueue.add("<html><b>" + title + ":</b> " + message + "</html>");
            updateNotificationPanel();
        }

        public static void updateNotificationPanel() {
            notificationPanel.removeAll();
            for (String notification : notificationQueue) {
                JLabel label = new JLabel(notification);
                label.setFont(new Font("Arial", Font.PLAIN, 14));
                label.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
                notificationPanel.add(label);
            }
            notificationPanel.revalidate();
            notificationPanel.repaint();
        }

        public static void clearNotifications() {
            notificationQueue.clear();
            updateNotificationPanel();
        }
    }


}
