import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import java.awt.event.*;
import java.sql.*;

public class BorrowedTableBook extends JFrame {

    private JButton Back;
    private String columns[] = {"No.", "Title", "Author", "Genre", "Borrowing Date", "Returned", "Overdue"};
    private JTable table;

    public BorrowedTableBook() {
        super("Borrowed Books");

        // Window settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Font settings
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        // Back button
        Back = new JButton("Back");
        Back.setFont(fontButton);

        // Setting up the table
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);

        // Load borrowed books from the database
        try {
            loadBorrowedBooks(model);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Error loading borrowed books: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
        }

        // Panels
        JPanel subPanel1 = new JPanel();
        subPanel1.setLayout(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.CENTER);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        // Back button event
        Back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close current window
                // Add navigation to the previous screen here
                JOptionPane.showMessageDialog(null, "Returning to Dashboard...");
                // Example: new DashboardUser(); Uncomment if DashboardUser class exists
            }
        });

        this.setVisible(true);
    }

    private void loadBorrowedBooks(DefaultTableModel model) throws SQLException {
        String query = "SELECT * FROM BorrowedBooks"; // Replace with your table name

        try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb"); // Update database path
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            int rowNumber = 1;
            while (rs.next()) {
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                Date borrowingDate = rs.getDate("BorrowingDate");
                boolean returned = rs.getBoolean("Returned");
                boolean overdue = rs.getBoolean("Overdue");

                model.addRow(new Object[]{rowNumber, title, author, genre, borrowingDate, returned ? "Yes" : "No", overdue ? "Yes" : "No"});
                rowNumber++;
            }
        } catch (SQLException ex) {
            throw new SQLException("Error retrieving borrowed books: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new BorrowedTableBook();
    }
}
