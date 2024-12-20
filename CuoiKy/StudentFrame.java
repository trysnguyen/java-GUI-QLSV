package CuoiKy;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import javax.swing.table.DefaultTableModel;

public class StudentFrame extends JFrame {
    private JTable studentsTable;
    private DefaultTableModel tableModel;
    private JLabel welcomeLabel;

    public StudentFrame(String studentName, String studentId) {
        setTitle("Student - View All Students");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        welcomeLabel = new JLabel("Welcome, " + studentName + "!", JLabel.CENTER);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(welcomeLabel, BorderLayout.NORTH);

        String[] columns = {"ID", "Username", "Name", "Student ID", "Class", "Hometown"};
        tableModel = new DefaultTableModel(columns, 0);

        studentsTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(studentsTable);
        add(scrollPane, BorderLayout.CENTER);

        loadStudentData(studentName, studentId);
    }

    private void loadStudentData(String currentStudentName, String currentStudentId) {
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

            tableModel.insertRow(0, new Object[]{
                    "Current", "N/A", currentStudentName, currentStudentId, "N/A", "N/A"
            });

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading student data.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new StudentFrame("Example Student", "S12345").setVisible(true));
    }
}
