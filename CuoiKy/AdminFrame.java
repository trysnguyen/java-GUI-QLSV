package CuoiKy;

import CuoiKy.DatabaseConnection;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class AdminFrame extends JFrame {
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField idField, usernameField, nameField, studentIdField, classField, hometownField;
    private JButton searchButton, deleteButton, updateButton, loadAllButton;

    public AdminFrame() {
        setTitle("Admin - Student Management");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(7, 2, 10, 10));

        inputPanel.add(new JLabel("Search (ID/Name):"));
        searchField = new JTextField();
        inputPanel.add(searchField);

        inputPanel.add(new JLabel("Student ID:"));
        idField = new JTextField();
        idField.setEditable(false);
        inputPanel.add(idField);

        inputPanel.add(new JLabel("Username:"));
        usernameField = new JTextField();
        inputPanel.add(usernameField);

        inputPanel.add(new JLabel("Name:"));
        nameField = new JTextField();
        inputPanel.add(nameField);

        inputPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        inputPanel.add(studentIdField);

        inputPanel.add(new JLabel("Class:"));
        classField = new JTextField();
        inputPanel.add(classField);

        inputPanel.add(new JLabel("Hometown:"));
        hometownField = new JTextField();
        inputPanel.add(hometownField);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        searchButton = new JButton("Search");
        deleteButton = new JButton("Delete");
        updateButton = new JButton("Update");
        loadAllButton = new JButton("Load All");

        buttonPanel.add(searchButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(loadAllButton);

        tableModel = new DefaultTableModel(new String[]{"ID", "Username", "Name", "Student ID", "Class", "Hometown"}, 0);
        studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);

        add(inputPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        loadAllStudents();

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchStudent();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteStudent();
            }
        });

        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateStudent();
            }
        });

        loadAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadAllStudents();
            }
        });

        studentsTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = studentsTable.getSelectedRow();
                if (row != -1) {
                    idField.setText(studentsTable.getValueAt(row, 0).toString());
                    usernameField.setText(studentsTable.getValueAt(row, 1).toString());
                    nameField.setText(studentsTable.getValueAt(row, 2).toString());
                    studentIdField.setText(studentsTable.getValueAt(row, 3).toString());
                    classField.setText(studentsTable.getValueAt(row, 4).toString());
                    hometownField.setText(studentsTable.getValueAt(row, 5).toString());
                }
            }
        });
    }

    private void loadAllStudents() {
        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students";
            PreparedStatement ps = con.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String name = rs.getString("name");
                String studentId = rs.getString("student_id");
                String studentClass = rs.getString("class");
                String hometown = rs.getString("hometown");
                tableModel.addRow(new Object[]{id, username, name, studentId, studentClass, hometown});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading student data.");
        }
    }

    private void searchStudent() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter an ID or name to search.");
            return;
        }

        try (Connection con = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM students WHERE student_id LIKE ? OR name LIKE ?";
            PreparedStatement ps = con.prepareStatement(query);
            ps.setString(1, "%" + searchTerm + "%");
            ps.setString(2, "%" + searchTerm + "%");
            ResultSet rs = ps.executeQuery();

            tableModel.setRowCount(0);

            while (rs.next()) {
                int id = rs.getInt("id");
                String username = rs.getString("username");
                String name = rs.getString("name");
                String studentId = rs.getString("student_id");
                String studentClass = rs.getString("class");
                String hometown = rs.getString("hometown");
                tableModel.addRow(new Object[]{id, username, name, studentId, studentClass, hometown});
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching student data.");
        }
    }

    private void deleteStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentId = (int) studentsTable.getValueAt(selectedRow, 0);

            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this student?");
            if (confirmation == JOptionPane.YES_OPTION) {
                try (Connection con = DatabaseConnection.getConnection()) {
                    String query = "DELETE FROM students WHERE id = ?";
                    PreparedStatement ps = con.prepareStatement(query);
                    ps.setInt(1, studentId);
                    int rowsAffected = ps.executeUpdate();
                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(this, "Student deleted successfully.");
                        loadAllStudents();
                    } else {
                        JOptionPane.showMessageDialog(this, "Error deleting student.");
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error deleting student.");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to delete.");
        }
    }

    private void updateStudent() {
        int selectedRow = studentsTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentId = (int) studentsTable.getValueAt(selectedRow, 0);
            String username = usernameField.getText();
            String name = nameField.getText();
            String studentIdValue = studentIdField.getText();
            String studentClass = classField.getText();
            String hometown = hometownField.getText();

            try (Connection con = DatabaseConnection.getConnection()) {
                String query = "UPDATE students SET username = ?, name = ?, student_id = ?, class = ?, hometown = ? WHERE id = ?";
                PreparedStatement ps = con.prepareStatement(query);
                ps.setString(1, username);
                ps.setString(2, name);
                ps.setString(3, studentIdValue);
                ps.setString(4, studentClass);
                ps.setString(5, hometown);
                ps.setInt(6, studentId);

                int rowsAffected = ps.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Student updated successfully.");
                    loadAllStudents();
                } else {
                    JOptionPane.showMessageDialog(this, "Error updating student.");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error updating student.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a student to update.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AdminFrame().setVisible(true));
    }
}
