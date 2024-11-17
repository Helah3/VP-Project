import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ReportModule extends JFrame {
    private JComboBox reportType;
    private JButton generateReportButton , backButton;
    private JTable reportTable;
    private DefaultTableModel tableModel;

    public ReportModule() {
        setTitle("Library Reports");
         this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0,0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16); 
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14);
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel reportLabel = new JLabel("Select Report Type:");
        reportLabel.setFont(fontText);
        
        String [] list = {"Library Activity", "Popular Books", "Member Statistics"};
        reportType = new JComboBox(list);
        reportType.setFont(fontText);
        
        generateReportButton = new JButton("Generate Report");
        generateReportButton.setFont(fontButton);
        
        backButton = new JButton("Back");
        backButton.setFont(fontButton);

        panel.add(reportLabel);
        panel.add(reportType);
        panel.add(generateReportButton);
        panel.add(backButton);


        tableModel = new DefaultTableModel();
        reportTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(reportTable);

        add(panel, BorderLayout.NORTH);
        add(tableScrollPane, BorderLayout.CENTER);

        generateReportButton.addActionListener(new GenerateReportAction());
        backButton.addActionListener(new BackToDashboard());

        setVisible(true);
    }

    private class GenerateReportAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String selectedReport = (String) reportType.getSelectedItem();
            switch (selectedReport) {
                case "Library Activity":
                    generateLibraryActivityReport();
                    break;
                case "Popular Books":
                    generatePopularBooksReport();
                    break;
                case "Member Statistics":
                    generateMemberStatisticsReport();
                    break;
            }
        }
    }

    private void generateLibraryActivityReport() {
        
        tableModel.setColumnIdentifiers(new String[]{"User ID", "Book ID", "Borrow Date", "Return Date"});
        tableModel.setRowCount(0);
        String query = "SELECT UserID, BookID, BorrowDate, ReturnDate FROM Borrowings";

        try { 
          Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("UserID"),
                        rs.getInt("BookID"),
                        rs.getDate("BorrowDate"),
                        rs.getDate("ReturnDate")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void generatePopularBooksReport() {
        tableModel.setColumnIdentifiers(new String[]{"Book Title", "Borrow Count"});
        tableModel.setRowCount(0);

        String query = "SELECT b.Title, COUNT(br.BookID) AS BorrowCount " +
                       "FROM Books b " +
                       "JOIN Borrowings br ON b.BookID = br.BookID " +
                       "GROUP BY b.Title " +
                       "ORDER BY BorrowCount DESC";

        try {
          Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("Title"),
                        rs.getInt("BorrowCount")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private void generateMemberStatisticsReport() {
        tableModel.setColumnIdentifiers(new String[]{"User Name", "Borrow Count"});
        tableModel.setRowCount(0);

        String query = "SELECT u.UserName, COUNT(br.UserID) AS BorrowCount " +
                       "FROM Users u " +
                       "JOIN Borrowings br ON u.UserID = br.UserID " +
                       "GROUP BY u.UserName " +
                       "ORDER BY BorrowCount DESC";

        try {
          Connection c = DriverManager.getConnection("jdbc:ucanaccess://C:/Users/LibraryDB.accdb");
             PreparedStatement stmt = c.prepareStatement(query);
             ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getString("UserName"),
                        rs.getInt("BorrowCount")
                });
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
     public class BackToDashboard implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new DashboardAdmin();
            dispose();
        }
    }

}
