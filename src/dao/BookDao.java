package dao;

import db.DBConnection;
import model.Book;
import java.sql.*;
import java.util.ArrayList;

public class BookDao {

    public void addBook(Book book) {
        String sql = "INSERT INTO BOOKS (title, author, quantity) VALUES (?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setInt(3, book.getQuantity());
            ps.executeUpdate();

            System.out.println("Book added successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Book> getAllBooks() {
        ArrayList<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM BOOKS";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(new Book(
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getInt("quantity")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean borrowBook(int bookId) {
        String check = "SELECT quantity FROM BOOKS WHERE book_id = ?";
        String update = "UPDATE BOOKS SET quantity = quantity - 1 WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps1 = con.prepareStatement(check)) {

            ps1.setInt(1, bookId);
            ResultSet rs = ps1.executeQuery();

            if (rs.next() && rs.getInt("quantity") > 0) {
                PreparedStatement ps2 = con.prepareStatement(update);
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public void returnBook(int bookId) {
        String sql = "UPDATE BOOKS SET quantity = quantity + 1 WHERE book_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, bookId);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
