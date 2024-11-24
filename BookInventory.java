
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import javax.swing.table.*;
import java.text.SimpleDateFormat;
import java.util.Date;

// BookInventory Class - Handles displaying, updating, and deleting books from the inventory.

public class BookInventory extends JFrame {

    private JButton Delete, Update, Back;
    private String[] columns = {"No.", "Title", "Author", "Genre", "Description", "Publishing Date"};
    private JTable table;
    private DefaultTableModel model;

    public BookInventory() {
        super("Book Inventory");

        // Window settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Font settings
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);

        // Button initialization
        Delete = new JButton("Delete");
        Delete.setFont(fontButton);
        Update = new JButton("Update");
        Update.setFont(fontButton);
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
        subPanel2.add(Delete);
        subPanel2.add(Update);
        subPanel2.add(Back);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel1, BorderLayout.NORTH);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);

        // Button actions
        Delete.addActionListener(new DeleteOperation());
        Update.addActionListener(new UpdateOperation());

        Back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardAdmin();
                dispose();
            }
        });

        this.setVisible(true);
    }

    // Establish a connection to the database
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

    // Retrieve book records from the database
    private void RetrieveBook() {
        Connection connection = DatabaseConnection();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if (connection == null) return;

        String query = "SELECT Title, Author, Genre, PublicationDate, Descripation " +
                       "FROM Books"; // Query to fetch book data

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
                String description = rs.getString("Descripation");
                String rawDate = rs.getString("PublicationDate");
                String formattedDate = "";

                if (rawDate != null && !rawDate.isEmpty()) {
                    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate);
                    formattedDate = dateFormat.format(date); // Format the date
                }

                // Add row to the table
                model.addRow(new Object[]{No, title, author, genre, description, formattedDate});
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

    // Inner class for the delete operation
    public class DeleteOperation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedBook = table.getSelectedRow();

            if (selectedBook != -1) {
                // Get the selected book's title
                String selectedBookName = model.getValueAt(selectedBook, 1).toString();

                // Confirm deletion
                int confirm = JOptionPane.showConfirmDialog(BookInventory.this,
                        "Are you sure you want to delete (" + selectedBookName + ")?",
                        "Delete Book", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean success = deleteFromDatabase(selectedBookName);

                    if (success) {
                        model.removeRow(selectedBook); // Remove row from the table
                        JOptionPane.showMessageDialog(BookInventory.this,
                                "Book: " + selectedBookName + "\nDeleted successfully!", "Delete Book", JOptionPane.INFORMATION_MESSAGE);
                        System.out.println("Book Deleted.");
                    }
                } else if (confirm == JOptionPane.NO_OPTION) {
                    System.out.println("Book not Deleted.");
                } else {
                    System.out.println("Action cancelled.");
                }
            } else {
                JOptionPane.showMessageDialog(BookInventory.this, "Please select a book to delete", "Delete Book", HEIGHT);
            }
        }

        // Delete the selected book from the database
        private boolean deleteFromDatabase(String bookTitle) {
            Connection connection = DatabaseConnection();
            if (connection == null) return false;

            String deleteQuery = "DELETE FROM Books WHERE Title = ?";

            try (PreparedStatement pstmt = connection.prepareStatement(deleteQuery)) {
                pstmt.setString(1, bookTitle);

                int rowsAffected = pstmt.executeUpdate();
                pstmt.close();
                connection.close();

                if (rowsAffected > 0) {
                    System.out.println("Book \"" + bookTitle + "\" deleted from database.");
                    return true;
                } else {
                    JOptionPane.showMessageDialog(null, "Book not found in the database", "Delete Error", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Failed to delete book from database", "Database Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
    }

    // Inner class for the update operation
    public class UpdateOperation implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int selectedBook = table.getSelectedRow();

            if (selectedBook != -1) {
                String title = model.getValueAt(selectedBook, 1).toString();
                String bookID = getBookIDFromDatabase(title);

                if (bookID != null) {
                    new UpdateBook(bookID); // Open the update window with the selected book's ID
                    System.out.println("Selected BookID: " + bookID);
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(BookInventory.this, "Failed to retrieve BookID.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(BookInventory.this, "Please select a book to update", "Update Book", HEIGHT);
            }
        }

        // Retrieve the BookID from the database based on the book title
        private String getBookIDFromDatabase(String title) {
            String bookID = null;
            Connection connection = DatabaseConnection();

            if (connection != null) {
                String query = "SELECT BookID FROM Books WHERE Title = ?";

                try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                    pstmt.setString(1, title);
                    ResultSet rs = pstmt.executeQuery();

                    if (rs.next()) {
                        bookID = rs.getString("BookID");
                    }

                    rs.close();
                    pstmt.close();
                    connection.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(BookInventory.this, "Error retrieving BookID from database.", "Database Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            return bookID;
        }
    }
}
