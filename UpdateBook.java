import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.*;
import java.util.Date;
import javax.swing.border.*;

public class UpdateBook extends JFrame {

    private JLabel BookName, Author, Genre, PublishingDate, ISBN, Description;
    private JTextField BookNamet, Authort, Genret, ISBNt, Descriptiont;
    private JDateChooser datePublishedText;
    private JButton UpdateBook, Back;
    private String index;

    public UpdateBook(String index) {
        super("");
        this.index = index;//BookID that will be updated 
        
        //Window settings
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        //fonts setting
        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);

        //components 
        BookName = new JLabel("Book Name: ");
        BookName.setFont(fontText);

        Author = new JLabel("Author: ");
        Author.setFont(fontText);

        Genre = new JLabel("Genre");
        Genre.setFont(fontText);

        PublishingDate = new JLabel("Publication Date: ");
        PublishingDate.setFont(fontText);

        ISBN = new JLabel("ISBN: ");
        ISBN.setFont(fontText);

        Description = new JLabel("Description: ");
        Description.setFont(fontText);

        BookNamet = new JTextField();
        BookNamet.setPreferredSize(new Dimension(150, 25));

        Authort = new JTextField();
        Authort.setPreferredSize(new Dimension(150, 25));

        Genret = new JTextField();
        Genret.setPreferredSize(new Dimension(150, 25));

        datePublishedText = new JDateChooser();
        datePublishedText.setPreferredSize(new Dimension(150, 25));

        ISBNt = new JTextField();
        ISBNt.setPreferredSize(new Dimension(150, 25));

        Descriptiont = new JTextField();
        Descriptiont.setPreferredSize(new Dimension(150, 25));
        
        //___________________________________________
        
        //Panels
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(4, 5, 20, 5);
        constraints.anchor = GridBagConstraints.EAST;

        constraints.gridx = 0;
        constraints.gridy = 0;
        subPanel.add(BookName, constraints);
        constraints.gridx = 1;
        subPanel.add(BookNamet, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        subPanel.add(Author, constraints);
        constraints.gridx = 1;
        subPanel.add(Authort, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        subPanel.add(Genre, constraints);
        constraints.gridx = 1;
        subPanel.add(Genret, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        subPanel.add(PublishingDate, constraints);
        constraints.gridx = 1;
        subPanel.add(datePublishedText, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        subPanel.add(ISBN, constraints);
        constraints.gridx = 1;
        subPanel.add(ISBNt, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        subPanel.add(Description, constraints);
        constraints.gridx = 1;
        subPanel.add(Descriptiont, constraints);

        UpdateBook = new JButton("Update Book");
        UpdateBook.setFont(fontButton);

        Back = new JButton("Back");
        Back.setFont(fontButton);

        JPanel buttons = new JPanel();
        buttons.add(UpdateBook);
        buttons.add(Back);

        retrieveBookData(); // To get the book information for Admin

        JPanel mainPanel1 = new JPanel();
        mainPanel1.setLayout(new BoxLayout(mainPanel1, BoxLayout.Y_AXIS));
        mainPanel1.setPreferredSize(new Dimension(700, 400));
        mainPanel1.add(Box.createVerticalStrut(10));
        mainPanel1.add(subPanel);

        JPanel mainPanel = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("Update Book");
        title.setTitleFont(fontTitle);
        mainPanel.setBorder(title);
        mainPanel.add(mainPanel1, BorderLayout.NORTH);
        mainPanel.add(buttons, BorderLayout.CENTER);

        UpdateBook.addActionListener(new UpdateOperation()); //Update button Action
        
        Back.addActionListener(new ActionListener(){ //Back button Action
        public void actionPerformed (ActionEvent e){
            new DashboardAdmin();
            dispose(); 
        }
        });

        this.setVisible(true);
    }

    private static Connection databaseConnection() { //Connection Process
        try {
            return DriverManager.getConnection("jdbc:ucanaccess://C:/Users/helah/Downloads/LibraryDB.accdb");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database", "Database Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private void retrieveBookData() { 
        Connection connection = databaseConnection(); //start Connection
        if (connection != null) {
            try {
                String query = "SELECT Title, Author, Genre, PublicationDate, ISBN, Descripation FROM Books WHERE BookID = ?"; // sql statement (query) to get info from database
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, index);
                ResultSet rs = pstmt.executeQuery();
                
                
                if (rs.next()) { // put the retrived data into the appropraite fields 
                    BookNamet.setText(rs.getString("Title"));
                    Authort.setText(rs.getString("Author"));
                    Genret.setText(rs.getString("Genre"));
                    String rawDate = rs.getString("PublicationDate");
                    
                    if (rawDate != null && !rawDate.isEmpty()) { // Format the Date 
                        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(rawDate);
                        datePublishedText.setDate(date);
                    }
                    
                    ISBNt.setText(rs.getString("ISBN"));
                    Descriptiont.setText(rs.getString("Descripation"));
                }
                
                rs.close();
                pstmt.close();
                connection.close();
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Failed to load book data", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Error parsing publication date", "Date Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    private class UpdateOperation implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Connection connection = databaseConnection(); //start connection 
            
            if (connection == null) return;

            try {
                String isbn = ISBNt.getText();
                if (!isbn.matches("\\d+")) { // check if it it valid ISBN
                    JOptionPane.showMessageDialog(null, "ISBN must be numeric.", "Input Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog( // Confirmation message for admin
                        null,
                        "Are you sure you want to update this book's details?",
                        "Confirm Update",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE
                );
                if (confirm != JOptionPane.YES_OPTION) return;
                
                Date utilDate = datePublishedText.getDate();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                //start the update process
                String query = "UPDATE Books SET Title = ?, Author = ?, Genre = ?, PublicationDate = ?, ISBN = ?, Descripation = ? WHERE BookID = ?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, BookNamet.getText());
                pstmt.setString(2, Authort.getText());
                pstmt.setString(3, Genret.getText());
                pstmt.setDate(4, sqlDate);
                pstmt.setString(5, isbn);
                pstmt.setString(6, Descriptiont.getText());
                pstmt.setString(7, index);

                if (pstmt.executeUpdate() > 0) { // message
                    JOptionPane.showMessageDialog(null, "Book updated successfully!", "Update Done", JOptionPane.INFORMATION_MESSAGE);
                }
                pstmt.close();
                connection.close();
                
            } catch (SQLException e1) {
                JOptionPane.showMessageDialog(null, "Failed to update book in the database", "Database Error", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (IllegalArgumentException e1) {
                JOptionPane.showMessageDialog(null, "Invalid data provided: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            } catch (Exception e1) {
                JOptionPane.showMessageDialog(null, "An unexpected error occurred: " + e1.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e1.printStackTrace();
            }
        }
    }
}
