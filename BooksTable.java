package com.mycompany.project;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.DefaultTableModel;

public class BooksTable extends JFrame {

    public BooksTable() {
        // Set frame properties
        setTitle("Book List");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create the table model and table
        String[] columns = {"NO.", "Title", "Author", "Genre", "Description", "Publish Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
        JTable table = new JTable(tableModel);

        // Add sample data (optional)
        tableModel.addRow(new Object[]{"1", "Book Title 1", "Author 1", "Genre 1", "Description 1", "2024-01-01"});
        tableModel.addRow(new Object[]{"2", "Book Title 2", "Author 2", "Genre 2", "Description 2", "2023-06-15"});

        // Add the table to a scroll pane
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel();
        JButton borrowButton = new JButton("Borrow");
        JButton backButton = new JButton("BACK");
        buttonPanel.add(borrowButton);
        buttonPanel.add(backButton);

        // Add the button panel to the frame
        add(buttonPanel, BorderLayout.SOUTH);

        // Add ActionListener for the BACK button
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Clear any selection in the table
                table.clearSelection();
                JOptionPane.showMessageDialog(null,"Selections cleared!","Back Action",JOptionPane.INFORMATION_MESSAGE);
            }
        });

        // Add ActionListener for the Borrow button
        borrowButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    // Remove the selected row
                    tableModel.removeRow(selectedRow);
                    JOptionPane.showMessageDialog(null,"Book borrowed successfully!","Borrow Action",JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Show warning if no row is selected
                    JOptionPane.showMessageDialog(null,"Please select a book to borrow!", "Warning",JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        // Set frame visibility
        setVisible(true);
    }

    public static void main(String[] args) {
        new BooksTable();
    }
}
