import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import javax.swing.table.*;
import java.util.Date;

// BorrowedBook Class - Displays the borrowed books in a table format.

public class BorrowedBook extends JFrame {
    
    private JButton Back;
    private String[] columns = {"No.", "Title", "Author", "Genre", "Borrowing Date", "Returned Date", "By user"};
    private JTable table;
    private Object[][] data = {};
    private DefaultTableModel model;

    public BorrowedBook() {
        super("Borrowed Books");
        
        // Frame settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Fonts
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        // Back button
        Back = new JButton("Back");
        Back.setFont(fontButton);

        // Table and model
        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);
        JScrollPane scrollPane = new JScrollPane(table);

        // Retrieve data
        RetrieveBook();

        // Layout components
        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.NORTH);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        // Back button action
        Back.addActionListener(new ActionListener(){ //Back button Action
        public void actionPerformed (ActionEvent e){
            new DashboardAdmin();
            dispose(); 
        }
        });

        this.setVisible(true);
    }

    // Database connection method
    private static Connection DatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/helah/Downloads/LibraryDB.accdb");
            System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Retrieve data from database
    private void RetrieveBook() {
        Connection connection = DatabaseConnection(); //Start Connection
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (connection == null) return;

        String query = "SELECT " + // sql statement (query) to get Borrowings detials from database
                "Books.Title AS Title, " +
                "Books.Author AS Author, " +
                "Books.Genre AS Genre, " +
                "Borrowings.BorrowDate AS BorrowDate, " +
                "Borrowings.ReturnDate AS ReturnDate, " +
                "Users.Username AS Username " +
                "FROM Borrowings " +
                "JOIN Books ON Borrowings.BookID = Books.BookID " +
                "JOIN Users ON Borrowings.UserID = Users.UserID";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            model.setRowCount(0); // Clear table rows
            int rowNumber = 1;

            while (rs.next()) {
                // Retrieve data
                String No = "" + rowNumber;
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                String borrowDate = rs.getString("BorrowDate");
                String returnDate = rs.getString("ReturnDate");
                String username = rs.getString("Username");

                // Format dates
                String formattedDate1 = "";
                String formattedDate2 = "";
                if ((borrowDate != null && !borrowDate.isEmpty()) || (returnDate != null && !returnDate.isEmpty())) { // format date
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(borrowDate);
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(returnDate);
                    formattedDate1 = dateFormat.format(date1);
                    formattedDate2 = dateFormat.format(date2);
                }

                // Add data to table
                model.addRow(new Object[]{No, title, author, genre, formattedDate1, formattedDate2, username});
                rowNumber++;
            }

            rs.close();
            stmt.close();
            connection.close();
            System.out.println("Successfully retrieved data.");

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            System.out.println("Error in parsing Date\n");
        }
    }
}
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import javax.swing.table.*;
import java.util.Date;

// BorrowedBook Class - Displays the borrowed books in a table format.

public class BorrowedBook extends JFrame {
    
    private JButton Back;
    private String[] columns = {"No.", "Title", "Author", "Genre", "Borrowing Date", "Returned Date", "By user"};
    private JTable table;
    private Object[][] data = {};
    private DefaultTableModel model;

    public BorrowedBook() {
        super("Borrowed Books");
        
        // Frame settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Fonts
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        // Back button
        Back = new JButton("Back");
        Back.setFont(fontButton);

        // Table and model
        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);
        JScrollPane scrollPane = new JScrollPane(table);

        // Retrieve data
        RetrieveBook();

        // Layout components
        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.NORTH);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        // Back button action
        Back.addActionListener(new ActionListener(){ //Back button Action
        public void actionPerformed (ActionEvent e){
            new DashboardAdmin();
            dispose(); 
        }
        });

        this.setVisible(true);
    }

    // Database connection method
    private static Connection DatabaseConnection() {
        try {
            Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/helah/Downloads/LibraryDB.accdb");
            System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Retrieve data from database
    private void RetrieveBook() {
        Connection connection = DatabaseConnection(); //Start Connection
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (connection == null) return;

        String query = "SELECT " + // sql statement (query) to get Borrowings detials from database
                "Books.Title AS Title, " +
                "Books.Author AS Author, " +
                "Books.Genre AS Genre, " +
                "Borrowings.BorrowDate AS BorrowDate, " +
                "Borrowings.ReturnDate AS ReturnDate, " +
                "Users.Username AS Username " +
                "FROM Borrowings " +
                "JOIN Books ON Borrowings.BookID = Books.BookID " +
                "JOIN Users ON Borrowings.UserID = Users.UserID";

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            model.setRowCount(0); // Clear table rows
            int rowNumber = 1;

            while (rs.next()) {
                // Retrieve data
                String No = "" + rowNumber;
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                String borrowDate = rs.getString("BorrowDate");
                String returnDate = rs.getString("ReturnDate");
                String username = rs.getString("Username");

                // Format dates
                String formattedDate1 = "";
                String formattedDate2 = "";
                if ((borrowDate != null && !borrowDate.isEmpty()) || (returnDate != null && !returnDate.isEmpty())) { // format date
                    Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(borrowDate);
                    Date date2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(returnDate);
                    formattedDate1 = dateFormat.format(date1);
                    formattedDate2 = dateFormat.format(date2);
                }

                // Add data to table
                model.addRow(new Object[]{No, title, author, genre, formattedDate1, formattedDate2, username});
                rowNumber++;
            }

            rs.close();
            stmt.close();
            connection.close();
            System.out.println("Successfully retrieved data.");

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve data from database", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            System.out.println("Error in parsing Date\n");
        }
    }
}
