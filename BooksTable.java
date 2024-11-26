 import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import static java.awt.image.ImageObserver.HEIGHT;
import java.sql.*;
import java.text.ParseException;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BooksTable extends JFrame {

    private JButton Borrow, Back;
    private String[] columns = {"No.", "Title", "Author", "Genre", "Description", "Publishing Date"};
    private JTable table;
    private DefaultTableModel model;

    public BooksTable() {
        super("Book Inventory");

        // Window settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Font settings
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        // Button initialization
        Borrow = new JButton("Borrow");
        Borrow.setFont(fontButton);
        Back = new JButton("Back");
        Back.setFont(fontButton);

        // Table initialization
        model = new NonEditableTableModel(columns, 0); // Custom table model to make cells non-editable
        table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);

        // Retrieve data from the database
        RetrieveBook();

        // Panels
        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel subPanel2 = new JPanel();
        subPanel2.add(Borrow);
        subPanel2.add(Back);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.NORTH);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        // Button actions
        Borrow.addActionListener(new BorrowOperation());

        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardUser();
                dispose();
            }
        });

        this.setVisible(true);
    }

    // Establish a connection to the database
    private static Connection DatabaseConnection() {
        try {
Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/Lamia/LibraryDB.accdb");
            System.out.println("Database connected successfully!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    // Retrieve book records from the database
    private void RetrieveBook() {
        Connection connection = DatabaseConnection();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (connection == null) return;

        String query = "SELECT Title, Author, Genre, Descripation ,PublicationDate " +
                       "FROM Books WHERE Avaliable = TRUE"; // Query to fetch book data
        

        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            model.setRowCount(0); // Clear table rows
            int rowNumber = 1;
            while (rs.next()) {

                // Retrieve data from the ResultSet
                String No = String.valueOf(rowNumber);
                String title = rs.getString("Title");
                String author = rs.getString("Author");
                String genre = rs.getString("Genre");
                String rawDate = rs.getString("PublicationDate");
                String description = rs.getString("Descripation");
                String formattedDate = "";

                if (rawDate != null && !rawDate.isEmpty()) {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate);
                    formattedDate = dateFormat.format(date); // Format the date
                }

                // Add row to the table
                model.addRow(new Object[]{No, title, author, genre , description, formattedDate});
                rowNumber++;
            }

            rs.close();
            stmt.close();
            connection.close();

            System.out.println("Successfully retrieved data.");
        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to retrieve data from the database", "Database Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            System.out.println("Error in parsing Date");
        }
    }

    // Inner class for the update operation
    public class BorrowOperation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedBook = table.getSelectedRow();

            if (selectedBook != -1) {
                String title = model.getValueAt(selectedBook, 1).toString();
                int bookID = getBookIDFromDatabase(title);

                if (bookID != 0) {
                    new BorrowBook(bookID); // Open the update window with the selected book's ID
                    System.out.println("Selected BookID: " + bookID);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to retrieve BookID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Please select a book to Borrow", "Borrow Book", HEIGHT);
            }
        }

        private int getBookIDFromDatabase(String title) {
            int bookID = 0;
            Connection connection = DatabaseConnection();

            if (connection != null) {
                String query = "SELECT BookID FROM Books WHERE Title = ?";

                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, title);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        bookID = rs.getInt("BookID");
                    }

                    rs.close();
                    pstmt.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "Error retrieving BookID from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            return bookID;
        }

}}
