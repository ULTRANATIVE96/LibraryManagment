package controller;

import dao.BookDao;
import dao.UserDao;
import dao.TransactionDao;
import model.Book;
import model.User;
import java.sql.*;
import java.util.List;

public class LibraryController {

    private BookDao bookDAO;
    private UserDao userDAO;
    private TransactionDao transactionDAO;

    public LibraryController() {
        this.bookDAO = new BookDao();
        this.userDAO = new UserDao();
        this.transactionDAO = new TransactionDao();
    }

    // ===== USER METHODS =====
public void addUser(String name, String email, String password, String role) {  
    userDAO.addUser(name, email, password, role);
}
  
     //===== USER ID VERIFY =====
public int getUserIdByEmail(String email) {
        return userDAO.getUserIdByEmail(email);
    }

     //===== BOOK ID METHOD=====
  public int getBookIdByTrans(int transId) {
        return transactionDAO.getBookIdByTrans(transId);
    }
  
    // ===== BOOK METHODS =====
    public void addBook(String title, String author, int quantity) {
        Book book = new Book(title, author, quantity);
        bookDAO.addBook(book);
    }

    public List<Book> listAllBooks() {
        return bookDAO.getAllBooks();
    }

    public boolean borrowBook(int userId, int bookId) {
        boolean success = bookDAO.borrowBook(bookId);
        if (success) {
            transactionDAO.borrowBook(userId, bookId);
            return true;
        }
        return false;
    }

    public void returnBook(int userId, int bookId, int transId) {
        bookDAO.returnBook(bookId);
        transactionDAO.returnBook(transId);
    }

    // ===== UTILITY =====
    public String getBooksString() {
    List<Book> books = listAllBooks();

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-5s %-20s %-20s %-10s%n",
            "ID", "Title", "Author", "Qty"));
    sb.append("--------------------------------------------------------------\n");

    for (Book book : books) {
        sb.append(String.format("%-5d %-20s %-20s %-10d%n",
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getQuantity()
        ));
    }

    return sb.toString();
}
    
    // Add this public method to LibraryController
public String getUnreturnedBooks() {
    return transactionDAO.getUnreturnedBooks();
}

public String getCustomerUnreturnedBooks(int userId) {
    String sql = """
        SELECT t.trans_id,
               b.title AS book_title,
               TO_CHAR(t.date_borrowed, 'YYYY-MM-DD') AS borrowed
        FROM transactions t
        JOIN books b ON t.book_id = b.book_id
        WHERE t.user_id = ? AND t.date_returned IS NULL
        ORDER BY t.date_borrowed DESC
        """;

    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%-8s %-30s %-12s%n", "T_ID", "Book", "Borrowed"));
    sb.append("---------------------------------------------------\n");

    try (Connection con = db.DBConnection.getConnection();
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setInt(1, userId);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            sb.append(String.format("%-8d %-30s %-12s%n",
                    rs.getInt("trans_id"),
                    rs.getString("book_title"),
                    rs.getString("borrowed")));
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return sb.toString();
}


}