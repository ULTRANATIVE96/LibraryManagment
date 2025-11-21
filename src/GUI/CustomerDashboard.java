package gui;

import controller.LibraryController;
import utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDashboard extends JFrame {

    private LibraryController controller;
    private JTextArea displayArea;
    private List<JButton> buttons;

    private JButton toggleThemeBtn;
    private JButton viewBooksBtn;
    private JButton borrowBtn;
    private JButton returnBtn;
    private JButton myLoansBtn;
    private JButton backBtn;

    private String email;

    public CustomerDashboard(String email) {
        super("Customer Dashboard");
        this.email = email;
        controller = new LibraryController();
        buttons = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

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
        viewBooksBtn = createButton("📚 View Books");
        borrowBtn = createButton("🔖 Borrow Book");
        returnBtn = createButton("↩️ Return Book");
        myLoansBtn = createButton("📖 My Loans");
        backBtn = createButton("🚪 Back to Login");

        btnPanel.add(toggleThemeBtn);
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(viewBooksBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(borrowBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(returnBtn);
        btnPanel.add(Box.createVerticalStrut(10));
        btnPanel.add(myLoansBtn);
        btnPanel.add(Box.createVerticalStrut(20));
        btnPanel.add(backBtn);

        add(btnPanel, BorderLayout.WEST);

        // ===== Button Actions =====
        viewBooksBtn.addActionListener(e -> displayArea.setText(controller.getBooksString()));

        borrowBtn.addActionListener(e -> {
            String bookIdStr = JOptionPane.showInputDialog(this, "Enter Book ID to borrow:");
            try {
                int bookId = Integer.parseInt(bookIdStr);
                int userId = controller.getUserIdByEmail(email);
                boolean success = controller.borrowBook(userId, bookId);
                displayArea.setText(success ? "Book borrowed successfully!" : "Book unavailable.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        });

        returnBtn.addActionListener(e -> {
            String transIdStr = JOptionPane.showInputDialog(this, "Enter Transaction ID to return:");
            try {
                int transId = Integer.parseInt(transIdStr);
                int userId = controller.getUserIdByEmail(email);
                int bookId = controller.getBookIdByTrans(transId);
                controller.returnBook(userId, bookId, transId);
                displayArea.setText("Book returned successfully!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Invalid input.");
            }
        });

        myLoansBtn.addActionListener(e -> {
            int userId = controller.getUserIdByEmail(email);
            displayArea.setText(controller.getCustomerUnreturnedBooks(userId));
        });

        backBtn.addActionListener(e -> {
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
