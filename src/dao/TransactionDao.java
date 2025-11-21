package dao;

import db.DBConnection;
import java.sql.*;

public class TransactionDao {

    public void borrowBook(int userId, int bookId) {
        String sql = "INSERT INTO TRANSACTIONS (user_id, book_id, date_borrowed) VALUES (?, ?, SYSDATE)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setInt(2, bookId);

            ps.executeUpdate();
            System.out.println("Transaction recorded!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnBook(int transId) {
        String sql = "UPDATE TRANSACTIONS SET date_returned = SYSDATE WHERE trans_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, transId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    // inside public class TransactionDao { ... }

public String getUnreturnedBooks() {
    String sql = """
        SELECT t.trans_id,
               u.user_id,
               u.name AS user_name,
               b.title AS book_title,
               TO_CHAR(t.date_borrowed, 'YYYY-MM-DD') AS borrowed
        FROM transactions t
        JOIN users u ON t.user_id = u.user_id
        JOIN books b ON t.book_id = b.book_id
        WHERE t.date_returned IS NULL
        ORDER BY t.date_borrowed DESC
        """;

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-8s %-8s %-20s %-30s %-12s%n",
            "T_ID", "U_ID", "User", "Book", "Borrowed"));
    sb.append("----------------------------------------------------------------------------\n");

    try (Connection con = DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            sb.append(String.format("%-8d %-8d %-20s %-30s %-12s%n",
                    rs.getInt("trans_id"),
                    rs.getInt("user_id"),
                    rs.getString("user_name"),
                    rs.getString("book_title"),
                    rs.getString("borrowed")
            ));
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return "Error loading unreturned books: " + e.getMessage();
    }

    return sb.toString();
}
public int getBookIdByTrans(int transId) {
        String sql = "SELECT book_id FROM transactions WHERE trans_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, transId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("book_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // return -1 if not found
    }
}