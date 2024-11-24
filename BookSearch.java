package ProjectPackage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;
import java.text.*;
import javax.swing.table.*;
import java.util.Date;


/**
 * This class creates a "Book Search" GUI that allows users to search for books 
 * in a database based on selected criteria (e.g., Title, Author, Genre).
 */
public class BookSearch extends JFrame {
    private JLabel searchBy, searchLabel;
    private JComboBox list; 
    private JTextField field; 
    private String columns[] = {"No.", "Title", "Author", "Genre", "Description", "Publishing Date"}; 
    private JTable table; 
    private DefaultTableModel model; 
    private JButton searchButton, back; 
    private String columnsr[] = {"Title", "Author", "Genre"};

    // Constructor for initializing the GUI components.
    public BookSearch() {
        super(""); // Create a window without a title.

        // Set up the window properties.
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        // Define fonts for different components.
        Font fontButton = new Font("Arial", Font.PLAIN, 16); 
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14); 
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16); 
        
        // Initialize labels.
        searchBy = new JLabel("Search by: ");
        searchBy.setFont(fontText);
        
        searchLabel = new JLabel("Search:      "); 
        searchLabel.setFont(fontText);
        
 
        list = new JComboBox(columnsr); 
        list.setPreferredSize(new Dimension(150, 25)); 
        
      
        field = new JTextField(); 
        field.setPreferredSize(new Dimension(150, 25));  
        
       
        searchButton = new JButton("Search"); 
        searchButton.setFont(fontButton);
        back = new JButton("Back"); 
        back.setFont(fontButton);
        
        
        model = new NonEditableTableModel(columns, 0); 
        table = new JTable(model); 
        JScrollPane scrollPane = new JScrollPane(table); 
        JTableHeader header = table.getTableHeader(); 
        header.setReorderingAllowed(false); 
        header.setFont(fontText);
        
        
        JPanel subPanel1 = new JPanel();
        subPanel1.add(searchBy, FlowLayout.LEFT); 
        subPanel1.add(list); 
            
        
        JPanel subPanel2 = new JPanel();
        subPanel2.add(searchLabel, FlowLayout.LEFT); 
        subPanel2.add(field); 
            
        
        JPanel subPanel3 = new JPanel();
        subPanel3.add(searchButton); 
        subPanel3.add(back); 
            
        
        JPanel mainPanel1 = new JPanel();
        TitledBorder title = BorderFactory.createTitledBorder("Search Book"); 
        title.setTitleFont(fontTitle);
        mainPanel1.setBorder(title); 
        mainPanel1.setLayout(new BoxLayout(mainPanel1, BoxLayout.Y_AXIS)); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel1); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel2); 
        mainPanel1.add(Box.createVerticalStrut(10)); 
        mainPanel1.add(subPanel3); 
        mainPanel1.add(Box.createVerticalStrut(30)); 
        mainPanel1.add(scrollPane); 
        
        
        JPanel mainPanel = (JPanel) this.getContentPane();
        mainPanel.add(mainPanel1, BorderLayout.NORTH);

        // Action for the "Back" button
        back.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new DashboardAdmin(); 
                dispose(); // Close the current window.
            }
        });
        
        // Action for the "Search" button
        searchButton.addActionListener(new searchOperation());
        this.setVisible(true); 
    }

    // Establish connection to the database.
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
    
    // Action listener class to handle the search operation.
    public class searchOperation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = DatabaseConnection(); // Connect to the database.
            String selectedColumn = list.getSelectedItem().toString(); 
            String searchValue = field.getText(); 
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format for the publication date.
            
            // Check if the search value is empty.
            if (searchValue.equals("")) {
                JOptionPane.showMessageDialog(null, "Please enter a value to search.", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // SQL query to search for books based on the selected column and value.
            String query = "SELECT Title , Author , Genre , Descripation, PublicationDate from Books Where " + selectedColumn + " LIKE ?";
            try {
                if (connection == null) {
                    return; 
                }
                  
                PreparedStatement pstmt = connection.prepareStatement(query); 
                pstmt.setString(1, "%" + searchValue + "%"); 

                ResultSet rs = pstmt.executeQuery();
                model.setRowCount(0); // Clear existing rows in the table.
                int rowNumber = 1; 

                
                while (rs.next()) {
                    String No = "" + rowNumber; // Generate row number.
                    String title = rs.getString("Title");
                    String author = rs.getString("Author");
                    String genre = rs.getString("Genre");
                    String description = rs.getString("Descripation");
                    String rawDate = rs.getString("PublicationDate"); 
                    String formattedDate = "";
                    
                    if (rawDate != null && !rawDate.isEmpty()) { // Format the date.
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate); 
                        formattedDate = dateFormat.format(date); 
                    }
                    
                    
                    model.addRow(new Object[]{No, title, author, genre, description, formattedDate});
                    rowNumber++;
                }
                rs.close(); 
                pstmt.close(); 
                connection.close(); 
                
                System.out.println("Successful Search Operation.");
                
            } catch (SQLException ex) {
                ex.printStackTrace(); 
                JOptionPane.showMessageDialog(null, "SQL error occurred: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (ParseException ex) {
                System.out.println("Error in parsing Date\n");
            }
        }
    }
}
