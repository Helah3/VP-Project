import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.sql.*;

public class DashboardUser extends JFrame {

    private JButton buttonSearch, buttonBorrowed, buttonReturn;

    public DashboardUser() {
        this.setTitle("User Dashboard");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1));

        // Creating buttons
        buttonSearch = new JButton("Search Book");
        buttonSearch.setPreferredSize(new Dimension(200, 30));
        buttonSearch.setFont(fontButton);
        JPanel m1 = new JPanel(new FlowLayout());
        m1.add(buttonSearch);

        buttonBorrowed = new JButton("Borrowed Books");
        buttonBorrowed.setPreferredSize(new Dimension(200, 30));
        buttonBorrowed.setFont(fontButton);
        JPanel m2 = new JPanel(new FlowLayout());
        m2.add(buttonBorrowed);

        buttonReturn = new JButton("Return Book");
        buttonReturn.setPreferredSize(new Dimension(200, 30));
        buttonReturn.setFont(fontButton);
        JPanel m3 = new JPanel(new FlowLayout());
        m3.add(buttonReturn);

        buttonPanel.add(m1);
        buttonPanel.add(m2);
        buttonPanel.add(m3);

        JPanel mainPanel = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("Dashboard User");
        title.setTitleFont(fontTitle);
        mainPanel.setBorder(title);
        mainPanel.add(buttonPanel);

        // Adding event listeners
        buttonSearch.addActionListener(new SearchBookListener());
        buttonBorrowed.addActionListener(new ViewBorrowedBooksListener());
        buttonReturn.addActionListener(new ReturnBookListener());

        this.setVisible(true);
    }

    // Event listener for "Search Book" button
    public class SearchBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String searchTerm = JOptionPane.showInputDialog(null, "Enter book title or author:", "Search Book", JOptionPane.QUESTION_MESSAGE);
                if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                    searchBookInDatabase(searchTerm);
                } else {
                    throw new EmptyFieldsException("Search term cannot be empty!");
                }
            } catch (EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void searchBookInDatabase(String searchTerm) {
            String query = "SELECT * FROM Books WHERE Title LIKE ? OR Author LIKE ?";
            try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb"); // Update database path
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setString(1, "%" + searchTerm + "%");
                stmt.setString(2, "%" + searchTerm + "%");
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    StringBuilder result = new StringBuilder("Search Results:\n");
                    do {
                        result.append("ID: ").append(rs.getInt("ID"))
                                .append(", Title: ").append(rs.getString("Title"))
                                .append(", Author: ").append(rs.getString("Author"))
                                .append("\n");
                    } while (rs.next());
                    JOptionPane.showMessageDialog(null, result.toString(), "Search Results", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No books found!", "Search Results", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Database error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Event listener for "Borrowed Books" button
    public class ViewBorrowedBooksListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                viewBorrowedBooks();
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void viewBorrowedBooks() throws DatabaseException {
            String query = "SELECT * FROM BorrowedBooks"; // Update table name as per your schema
            try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb"); // Update database path
                 Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                if (rs.next()) {
                    StringBuilder borrowedBooks = new StringBuilder("Borrowed Books:\n");
                    do {
                        borrowedBooks.append("Book ID: ").append(rs.getInt("BookID"))
                                .append(", Title: ").append(rs.getString("Title"))
                                .append(", Borrowed Date: ").append(rs.getDate("BorrowedDate"))
                                .append("\n");
                    } while (rs.next());
                    JOptionPane.showMessageDialog(null, borrowedBooks.toString(), "Borrowed Books", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "No borrowed books found!", "Borrowed Books", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException ex) {
                throw new DatabaseException("Error fetching borrowed books: " + ex.getMessage());
            }
        }
    }

    // Event listener for "Return Book" button
    public class ReturnBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            try {
                String bookID = JOptionPane.showInputDialog(null, "Enter Book ID to return:", "Return Book", JOptionPane.QUESTION_MESSAGE);
                if (bookID != null && !bookID.trim().isEmpty()) {
                    returnBook(Integer.parseInt(bookID));
                } else {
                    throw new EmptyFieldsException("Book ID cannot be empty!");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Invalid Book ID format!", "Input Error", JOptionPane.ERROR_MESSAGE);
            } catch (EmptyFieldsException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Input Error", JOptionPane.WARNING_MESSAGE);
            } catch (DatabaseException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void returnBook(int bookID) throws DatabaseException {
            String query = "DELETE FROM BorrowedBooks WHERE BookID = ?";
            try (Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb"); // Update database path
                 PreparedStatement stmt = connection.prepareStatement(query)) {

                stmt.setInt(1, bookID);
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(null, "Book returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    throw new DatabaseException("Book ID not found!");
                }

            } catch (SQLException ex) {
                throw new DatabaseException("Error returning book: " + ex.getMessage());
            }
        }
    }

    // Custom exception for empty fields
    public class EmptyFieldsException extends Exception {
        public EmptyFieldsException(String message) {
            super(message);
        }
    }

    // Custom exception for database errors
    public class DatabaseException extends Exception {
        public DatabaseException(String message) {
            super(message);
        }
    }

    // Main method
    public static void main(String[] args) {
        new DashboardUser();
    }
}
