
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import javax.swing.table.*;
import java.util.Date;
//import visualprogramming.projectfinal.DashboardAdmin;

// BorrowedBook Class - Displays the borrowed books in a table format.

public class BorrowedBook extends JFrame {
    
    private JButton Back,filterButton;
    private String[] columns = {"No.", "Title", "Author", "Genre", "Borrowing Date", "Returned Date", "By user"};
    private JTable table;
    private JLabel genreLabel,authorFilterLabel,yearLabel ;
    private Object[][] data = {};
    private DefaultTableModel model;
    private JComboBox<String> genreComboBox, authorComboBox,yearComboBox ;

    public BorrowedBook() {
        super("Borrowed Books");
        
        // Frame settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Fonts
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTable = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        
        genreLabel = new JLabel("Genre:      ");  
        genreLabel.setFont(fontText);
        genreComboBox = new JComboBox<>();
        genreComboBox.setPreferredSize(new Dimension(150, 25));
        
        authorFilterLabel = new JLabel("      Author:      ");
        authorFilterLabel.setFont(fontText);
        authorComboBox = new JComboBox<>();
        authorComboBox.setPreferredSize(new Dimension(150, 25));
       
        yearLabel = new JLabel("      Publication Year:      ");
        yearLabel.setFont(fontText);
        yearComboBox = new JComboBox<>();
        yearComboBox.setPreferredSize(new Dimension(150, 25));
         // Button initialization
        filterButton = new JButton("Filter");
        filterButton.setFont(fontButton); 
       
        Back = new JButton("Back");
        Back.setFont(fontButton);

        // Table and model
        model = new DefaultTableModel(data, columns);
        table = new JTable(model);
        JTableHeader header = table.getTableHeader();
        header.setReorderingAllowed(false);
        header.setFont(fontTable);
        JScrollPane scrollPane = new JScrollPane(table);

       
        // Layout components
        
         JPanel subPanel3 = new JPanel(); 
         subPanel3.setLayout(new FlowLayout(FlowLayout.LEFT));
         subPanel3.add(genreLabel);
         subPanel3.add(genreComboBox);
         subPanel3.add(authorFilterLabel);
         subPanel3.add(authorComboBox);
         subPanel3.add(yearLabel);
         subPanel3.add(yearComboBox);
         subPanel3.add(filterButton);
         
        JPanel subPanel2 = new JPanel();
        subPanel2.add(Back);
        
        JPanel subPanel1 = new JPanel(new BorderLayout());
        subPanel1.add(scrollPane);

        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(subPanel3, BorderLayout.NORTH);
        mainPanel.add(subPanel1, BorderLayout.CENTER);
        mainPanel.add(subPanel2, BorderLayout.PAGE_END);
       
           // Retrieve data
           
        RetrieveBook();
        loadComboBoxData();

        // Back button action
        filterButton.addActionListener(new filterOperation() );
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
Connection connection = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/baato/Downloads/LibraryDB.accdb");
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
    
  
    private void loadComboBoxData() {
        // Load Genre
        try (Connection conn = DatabaseConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Genre FROM Books")) {

            genreComboBox.addItem("All");
            while (rs.next()) {
                genreComboBox.addItem(rs.getString("Genre"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading genres: " + e.getMessage());
        }

        // Load Authors
        try (Connection conn = DatabaseConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT DISTINCT Author FROM Books")) {

            authorComboBox.addItem("All");
            while (rs.next()) {
                authorComboBox.addItem(rs.getString("Author"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading authors: " + e.getMessage());
        }
        
        // Load Publication Years

try (Connection conn = DatabaseConnection();
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery("SELECT DISTINCT YEAR(PublicationDate) AS year FROM Books")) {

    yearComboBox.addItem("All");
    while (rs.next()) {
        yearComboBox.addItem(rs.getInt("year") + ""); // إضافة السنة كعدد صحيح
    }
} catch (SQLException e) {
    JOptionPane.showMessageDialog(this, "Error loading publication years: " + e.getMessage());
}}

   
  
  private class filterOperation implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
        String genre = genreComboBox.getSelectedItem().toString();
        String author = authorComboBox.getSelectedItem().toString();
        String year = yearComboBox.getSelectedItem().toString();
        
        
        String query = "SELECT Title, Author, Genre, Descripation, PublicationDate FROM Books WHERE 1=1";
        
        
        if (!genre.equals("All")) {
            query += " AND Genre = '" + genre + "'";
        }
        if (!author.equals("All")) {
            query += " AND Author = '" + author + "'";
        }
        if (!year.equals("All")) {
            query += " AND YEAR(PublicationDate) = '" + year + "'";  // تصفية حسب السنة
        }
        
        try (Connection connection = DatabaseConnection();
             PreparedStatement pstmt = connection.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
             
            
            model.setRowCount(0); 
            int rowNumber = 1;

            
            if (!rs.isBeforeFirst()) {
                   throw new NoresultException("No results found for the selected filters ");
            } else {
                while (rs.next()) {
                    String No = "" + rowNumber;
                    String title = rs.getString("Title");
                    author = rs.getString("Author");
                    genre = rs.getString("Genre");
                   String description = rs.getString("Descripation");
                    String rawDate = rs.getString("PublicationDate");
                    String formattedDate = "";

                    if (rawDate != null && !rawDate.isEmpty()) {
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate);
                        formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
                    }

                    model.addRow(new Object[]{No, title, author, genre, description ,formattedDate});
                    rowNumber++;
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace(); 
            JOptionPane.showMessageDialog(null, "SQL error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } catch (ParseException ex) {
            System.out.println("Error in parsing Date\n");
        }catch(NoresultException e2){
               JOptionPane.showMessageDialog(null , e2.getMessage(),"No Results",JOptionPane.WARNING_MESSAGE);
            }
    }
}
    public class NoresultException extends Exception{
        public NoresultException(String msg){
            super(msg);
        }
    } 
    
}
