package dao;

import db.DBConnection;
import model.User;
import java.sql.*;

public class UserDao {

public void addUser(String name, String email, String password, String role) {
    String sql = "INSERT INTO USERS (user_id, name, email, password, role) " +
                 "VALUES (users_seq.NEXTVAL, ?, ?, ?, ?)";

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, name);
        ps.setString(2, email);
        ps.setString(3, password);
        ps.setString(4, role);

        ps.executeUpdate();
        System.out.println("User added: " + email);

    } catch (SQLException e) {
        e.printStackTrace();
    }
    }
    
    public boolean login(String email, String password, String role) {
    String sql = "SELECT COUNT(*) AS count FROM USERS WHERE email=? AND password=? AND role=?";
    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, email);
        ps.setString(2, password);
        ps.setString(3, role.toUpperCase());

        ResultSet rs = ps.executeQuery();
        if (rs.next()) return rs.getInt("count") > 0;

    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    
    public int getUserIdByEmail(String email) {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // return -1 if not found
    }

}
