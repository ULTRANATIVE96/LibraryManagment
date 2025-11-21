package gui;

import controller.LibraryController;
import dao.UserDao;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField emailField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JButton loginBtn;
    private LibraryController controller;

    public LoginFrame() {
        super("Library Management System - Login");

        controller = new LibraryController();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Email
        gbc.gridx = 0; gbc.gridy = 0;
        add(new JLabel("Email:"), gbc);

        emailField = new JTextField(20);
        gbc.gridx = 1;
        add(emailField, gbc);

        // Password
        gbc.gridx = 0; gbc.gridy = 1;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        add(passwordField, gbc);

        // Role
        gbc.gridx = 0; gbc.gridy = 2;
        add(new JLabel("Role:"), gbc);

        roleBox = new JComboBox<>(new String[]{"LIBRARIAN", "CUSTOMER", "VISITOR"});
        gbc.gridx = 1;
        add(roleBox, gbc);

        // Login button
        loginBtn = new JButton("Login");
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        add(loginBtn, gbc);

        loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    private void login() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = (String) roleBox.getSelectedItem().toString().trim();

        UserDao userDao = new UserDao();

        if (role.equals("VISITOR")) {
            // Visitors don't need accounts
            new VisitorDashboard();
            dispose();
        } else if (userDao.login(email, password, role)) {
            if (role.equals("LIBRARIAN")) {
                new LibrarianDashboard();
            } else if (role.equals("CUSTOMER")) {
                new CustomerDashboard(email); // pass email for user-specific actions
            }
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Invalid credentials!", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }

   
}
