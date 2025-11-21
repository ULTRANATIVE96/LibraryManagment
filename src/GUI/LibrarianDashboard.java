package gui;

import controller.LibraryController;
import utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LibrarianDashboard extends JFrame {

    private LibraryController controller;
    private JTextArea displayArea;
    private List<JButton> buttons;

    private JButton toggleThemeBtn;
    private JButton viewBooksBtn;
    private JButton addBookBtn;
    private JButton addCustomerBtn;
    private JButton viewUnreturnedBtn;
    private JButton logoutBtn;

    public LibrarianDashboard() {
        super("Librarian Dashboard");
        controller = new LibraryController();
        buttons = new ArrayList<>();

        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        // ===== Text Area =====
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(displayArea);
        add(scroll, BorderLayout.CENTER);

        // ===== Sidebar Buttons =====
        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new BoxLayout(btnPanel, BoxLayout.Y_AXIS));
        btnPanel.setPreferredSize(new Dimension(180, 0));
        btnPanel.setBorder(BorderFactory.createEmptyBorder(20,10,20,10));

        toggleThemeBtn = createButton("🌓 Toggle Theme");
        viewBooksBtn = createButton("📚 View All Books");
        addBookBtn = createButton("➕ Add Book");
        addCustomerBtn = createButton("🧑‍💼 Add Customer");
        viewUnreturnedBtn = createButton("📖 Unreturned Books");
        logoutBtn = createButton("🚪 Logout");

        btnPanel.add(toggleThemeBtn);
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(viewBooksBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(addBookBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(addCustomerBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(viewUnreturnedBtn);
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(logoutBtn);

        add(btnPanel, BorderLayout.WEST);

        // ===== Button Actions =====
        viewBooksBtn.addActionListener(e ->
                displayArea.setText(controller.getBooksString())
        );

        addBookBtn.addActionListener(e -> {
            String title = JOptionPane.showInputDialog(this, "Enter Book Title:");
            if (title == null || title.trim().isEmpty()) return;

            String author = JOptionPane.showInputDialog(this, "Enter Author:");
            if (author == null || author.trim().isEmpty()) return;

            String qtyStr = JOptionPane.showInputDialog(this, "Enter Quantity:");
            if (qtyStr == null || qtyStr.trim().isEmpty()) return;

            try {
                int qty = Integer.parseInt(qtyStr);
                controller.addBook(title, author, qty);
                displayArea.setText("Book added successfully!\n\n" + controller.getBooksString());
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Quantity must be a number.");
            }
        });

        addCustomerBtn.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(this, "Enter Customer Name:");
            if (name == null || name.trim().isEmpty()) return;

            String email = JOptionPane.showInputDialog(this, "Enter Customer Email:");
            if (email == null || email.trim().isEmpty()) return;

            String password = JOptionPane.showInputDialog(this, "Enter Password:");
            if (password == null || password.trim().isEmpty()) return;

            controller.addUser(name, email, password, "CUSTOMER");

            displayArea.setText("Customer added successfully!\nName: " + name +
                    "\nEmail: " + email +
                    "\nRole: CUSTOMER");
        });

        viewUnreturnedBtn.addActionListener(e ->
                displayArea.setText(controller.getUnreturnedBooks())
        );

        logoutBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        toggleThemeBtn.addActionListener(e ->
                ThemeManager.toggleTheme(btnPanel, displayArea, buttons, this)
        );

        setVisible(true);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        buttons.add(btn);
        return btn;
    }
}
