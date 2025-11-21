package gui;

import controller.LibraryController;
import utils.ThemeManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class VisitorDashboard extends JFrame {

    private LibraryController controller;
    private JTextArea displayArea;

    private JButton viewBooksBtn, backBtn, toggleThemeBtn;
    private JPanel leftPanel;

    // Store all buttons so ThemeManager can apply colors
    private java.util.List<JButton> buttons = new ArrayList<>();

    public VisitorDashboard() {
        super("Visitor Dashboard");
        controller = new LibraryController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // ===== CENTER DISPLAY AREA =====
        displayArea = new JTextArea();
        displayArea.setEditable(false);
        add(new JScrollPane(displayArea), BorderLayout.CENTER);

        // ===== LEFT BUTTON PANEL =====
        leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.setPreferredSize(new Dimension(170, 0));
        leftPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        // Create buttons
        toggleThemeBtn = createButton("🌓 Toggle Theme");
        viewBooksBtn   = createButton("📘 View Books");
        backBtn        = createButton("🔙 Back to Login");

        // Add buttons to panel
        leftPanel.add(toggleThemeBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(viewBooksBtn);
        leftPanel.add(Box.createVerticalStrut(10));
        leftPanel.add(backBtn);

        add(leftPanel, BorderLayout.WEST);

        // ===== BUTTON ACTIONS =====

        viewBooksBtn.addActionListener(e ->
                displayArea.setText(controller.getBooksString())
        );

        backBtn.addActionListener(e -> {
            new LoginFrame();
            dispose();
        });

        toggleThemeBtn.addActionListener(e -> {
            ThemeManager.toggleTheme(leftPanel, displayArea, buttons, this);
        });

        setVisible(true);
    }

    // Helper to create consistent buttons & add to list for theming
    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(160, 40));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        buttons.add(btn);
        return btn;
    }
}
