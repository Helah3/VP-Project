import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.TitledBorder;
import java.sql.*;

public class DashboardUser extends JFrame {

    private JButton buttonSearch, buttonBorrowed, buttonReturn , LogoutB;

    public DashboardUser() {
        this.setTitle("User Dashboard");
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0, 0);

        Font fontButton = new Font("Arial", Font.PLAIN, 16);
        Font fontTitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16);
        JPanel buttonPanel = new JPanel(new GridLayout(4, 1));

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

        buttonReturn = new JButton("Borrow Book");
        buttonReturn.setPreferredSize(new Dimension(200, 30));
        buttonReturn.setFont(fontButton);
        JPanel m3 = new JPanel(new FlowLayout());
        m3.add(buttonReturn);

        LogoutB = new JButton("Log Out");
        LogoutB.setPreferredSize(new Dimension(200, 30));

        LogoutB.setFont(fontButton);
        JPanel m4 = new JPanel(new FlowLayout());
        m4.add(LogoutB);
        
        buttonPanel.add(m1);
        buttonPanel.add(m2);
        buttonPanel.add(m3);
        buttonPanel.add(m4);


        JPanel mainPanel = (JPanel) this.getContentPane();
        TitledBorder title = BorderFactory.createTitledBorder("Dashboard User");
        title.setTitleFont(fontTitle);
        mainPanel.setBorder(title);
        mainPanel.add(buttonPanel , BorderLayout.CENTER);

        buttonSearch.addActionListener(new SearchBookListener());
        buttonBorrowed.addActionListener(new ViewBorrowedBooksListener());
        buttonReturn.addActionListener(new ReturnBookListener());
        LogoutB.addActionListener(new Logout());

        this.setVisible(true);
    }

    public class SearchBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           new BookSearch();
            dispose();
        }
    }

    public class ViewBorrowedBooksListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
           new BorrowedTableBook();
            dispose();
        }}

        
    public class ReturnBookListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BooksTable();
            dispose(); 
        }
    }
   public class Logout implements ActionListener{
    public void actionPerformed(ActionEvent e){
         int n = JOptionPane.showConfirmDialog(null , "Do you Want Log out ?","Log Out",JOptionPane.YES_NO_OPTION);
            if(n==0)
            System.exit(0);
    
    }}

}
