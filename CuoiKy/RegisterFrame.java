package CuoiKy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterFrame extends JFrame {
    private JTextField usernameField, nameField, studentIdField, classField, hometownField;
    private JPasswordField passwordField;

    public RegisterFrame() {
        setTitle("Register");
        setSize(400, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel usernameLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameLabel, gbc);
        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(usernameField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordLabel, gbc);
        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(passwordField, gbc);

        JLabel nameLabel = new JLabel("Name:");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        add(nameLabel, gbc);
        nameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(nameField, gbc);

        JLabel studentIdLabel = new JLabel("Student ID:");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.WEST;
        add(studentIdLabel, gbc);
        studentIdField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(studentIdField, gbc);

        JLabel classLabel = new JLabel("Class:");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        add(classLabel, gbc);
        classField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(classField, gbc);

        JLabel hometownLabel = new JLabel("Hometown:");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.WEST;
        add(hometownLabel, gbc);
        hometownField = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        add(hometownField, gbc);

        JButton registerButton = new JButton("Register");
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerStudent();
            }
        });
    }

    private void registerStudent() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String name = nameField.getText();
        String studentId = studentIdField.getText();
        String studentClass = classField.getText();
        String hometown = hometownField.getText();

        if (username.isEmpty() || password.isEmpty() || name.isEmpty() || studentId.isEmpty() || studentClass.isEmpty() || hometown.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields!");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO students (username, password, name, student_id, class, hometown) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, name);
            ps.setString(4, studentId);
            ps.setString(5, studentClass);
            ps.setString(6, hometown);

            int rowsAffected = ps.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                new LoginFrame().setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed. Please try again.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred during registration.");
        }
    }
}

