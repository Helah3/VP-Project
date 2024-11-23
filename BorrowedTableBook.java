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

        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        Back = new JButton("Back");
        Back.setFont(fontButton);

        DefaultTableModel model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);

        int id = User.getUserID();
        loadBorrowedBooks(model , id);
       

        JPanel subPanel1 = new JPanel();
        subPanel1.setLayout(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.CENTER);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
               new DashboardUser();
                dispose(); 
            }
        });

        this.setVisible(true);
    }

 private void loadBorrowedBooks(DefaultTableModel model, int loggedInUserID) {
    String query = "SELECT Borrowings.*, Books.Title, Books.Author, Books.Genre " +
                   "FROM Borrowings " +
                   "INNER JOIN Books ON Borrowings.BookID = Books.BookID " +
                   "WHERE Borrowings.UserID = ?";

    try {
        // Establishing the database connection
        Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
        PreparedStatement stmt = c.prepareStatement(query);
        stmt.setInt(1, loggedInUserID); 

        ResultSet rs = stmt.executeQuery();

        int rowNumber = 1; 
        java.sql.Date currentDate = new java.sql.Date(System.currentTimeMillis()); 

        model.setRowCount(0);

        while (rs.next()) {
            String title = rs.getString("Title");
            String author = rs.getString("Author");
            String genre = rs.getString("Genre");
            java.sql.Date borrowDate = rs.getDate("BorrowDate");
            java.sql.Date returnDate = rs.getDate("ReturnDate");
            boolean isReturned = rs.getBoolean("IsReturned");

            long delayDays = 0;
            if (!isReturned && returnDate != null) {
                long diff = currentDate.getTime() - returnDate.getTime();
                delayDays = diff > 0 ? diff / (1000 * 60 * 60 * 24) : 0; 
            }

            model.addRow(new Object[]{
                rowNumber,
                title,
                author,
                genre,
                borrowDate != null ? borrowDate.toString() : "N/A",
                returnDate != null ? returnDate.toString() : "N/A",
                isReturned ? "No Delay" : (delayDays > 0 ? delayDays + " days" : "No Delay")
            });

            rowNumber++;
        }
    } catch (SQLException ex) {
       ex.printStackTrace();
    }
}

   
}
