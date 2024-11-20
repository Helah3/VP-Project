package com.mycompany.project;

import javax.swing.*;
import java.awt.*;

public class SearchBook extends JFrame {
    private JLabel searchByLabel, searchLabel;
    private JTextField searchField;
    private JComboBox<String> searchByComboBox;
    private JButton searchButton, backButton;
    private String[] options = {"by title", "author", "genre"};

    public SearchBook() {
        setTitle("Search Book");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // استخدام التخطيط المطلق لتحديد مواقع المكونات يدويًا
        setSize(400, 250);

        // إعداد المكونات الأخرى
        searchByLabel = new JLabel("Search by:");
        searchByLabel.setBounds(20, 20, 100, 25);
        add(searchByLabel);

        searchByComboBox = new JComboBox<>(options);
        searchByComboBox.setBounds(140, 20, 200, 25);
        add(searchByComboBox);

        searchLabel = new JLabel("Search:");
        searchLabel.setBounds(20, 60, 100, 25);
        add(searchLabel);

        searchField = new JTextField();
        searchField.setBounds(140, 60, 200, 25);
        add(searchField);

        // إعداد الأزرار
        searchButton = new JButton("Search");
        searchButton.setBounds(140, 110, 80, 30);
        add(searchButton);

        backButton = new JButton("Back");
        backButton.setBounds(230, 110, 80, 30);
        add(backButton);

        setVisible(true);
    }

    public static void main(String[] args) {
        new SearchBook();
    }
}
