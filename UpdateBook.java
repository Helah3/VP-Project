 import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.border.TitledBorder;
/**
 *
 * @author helah
 */
public class UpdateBook extends JFrame{
    private JLabel BookName , ISBN , Author , PublishingDate , Description ;
    private JTextField BookNamet , ISBNt , Authort , PublishingDatet , Descriptiont ;
    private JButton UpdateBook , Back ;
    
    public UpdateBook (String bookID){
        super("");
        
        //Window setting
        this.setSize(1024, 576);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocation(0,0);
        
        Font fontButton = new Font("Arial", Font.PLAIN, 16); 
        Font fontText = new Font("Segoe UI Variable Display Semib", Font.BOLD, 14); 
        Font fonttitle = new Font("Segoe UI Variable Display Semib", Font.BOLD, 16); 
        
        BookName = new JLabel ("Book Name: ");
            BookName.setFont(fontText);
            
        ISBN = new JLabel ("ISBN: ");
            ISBN.setFont(fontText);
        
        Author = new JLabel ("Author: ");
            Author.setFont(fontText);
        
        PublishingDate = new JLabel ("Publishing Date: ");
            PublishingDate.setFont(fontText);
        
        Description = new JLabel ("Description: ");
            Description.setFont(fontText);
            
        BookNamet = new JTextField ();
            BookNamet.setPreferredSize(new Dimension(150, 25));
            
        ISBNt = new JTextField();
            ISBNt.setPreferredSize(new Dimension(150, 25));
            
        Authort = new JTextField();
            Authort.setPreferredSize(new Dimension(150, 25));
            
        PublishingDatet = new JTextField();
            PublishingDatet.setPreferredSize(new Dimension(150, 25));
            
        Descriptiont = new JTextField();
            Descriptiont.setPreferredSize(new Dimension(150, 25));
            
        JPanel subpaanel = new JPanel() ;
            subpaanel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints() ;
            constraints.insets = new Insets(4, 5, 20, 5);
            constraints.anchor = GridBagConstraints.EAST;
        
            constraints.gridx = 0 ;
            constraints.gridy = 0 ;
            
            subpaanel.add(BookName , constraints);
            constraints.gridx = 1 ;
            subpaanel.add(BookNamet,constraints);
            
            constraints.gridx = 0 ;
            constraints.gridy = 1 ;
            subpaanel.add(ISBN,constraints);
            constraints.gridx = 1 ;
            subpaanel.add(ISBNt,constraints);
            
            constraints.gridx = 0 ;
            constraints.gridy = 2 ;
            subpaanel.add(Author,constraints);
            constraints.gridx = 1 ;
            subpaanel.add(Authort,constraints);
            
            constraints.gridx = 0 ;
            constraints.gridy = 3 ;
            subpaanel.add(PublishingDate,constraints);
            constraints.gridx = 1 ;
            subpaanel.add(PublishingDatet,constraints);
            
            constraints.gridx = 0 ;
            constraints.gridy = 4 ;
            subpaanel.add(Description,constraints);
            constraints.gridx = 1 ;
            subpaanel.add(Descriptiont,constraints);
            
        UpdateBook = new JButton("Update Book");
            UpdateBook.setFont(fontButton);
            
        Back = new JButton("Back");
            Back.setFont(fontButton);
            
        JPanel Buttons = new JPanel();
            Buttons.add(UpdateBook);
            Buttons.add(Back);
            
            
        JPanel mainpanel1 =new JPanel();
            mainpanel1.setLayout(new BoxLayout(mainpanel1 , BoxLayout.Y_AXIS));
            mainpanel1.setPreferredSize(new Dimension(700, 400));
            mainpanel1.add(Box.createVerticalStrut(10));
            mainpanel1.add(subpaanel);
           
            
        JPanel mainpanel = (JPanel) this.getContentPane();
            TitledBorder title;
            title = BorderFactory.createTitledBorder("Update Book");
            title.setTitleFont(fonttitle);
            mainpanel.setBorder(title);
            mainpanel.add(mainpanel1,BorderLayout.NORTH);
            mainpanel.add(Buttons,BorderLayout.CENTER);
            
        this.setVisible(true) ;

          Back.addActionListener(new BackToTable());
   
    }
    
          public class BackToTable implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            new BookInventory();
            dispose();
        }
    }
}
