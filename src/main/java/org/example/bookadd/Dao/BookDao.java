package org.example.bookadd.Dao;

import org.example.bookadd.Model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDao {

    // Existing addBook method
    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, description, genre, price, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setDouble(5, book.getPrice());
            preparedStatement.setString(6, book.getImagePath());  // Save the image path

            preparedStatement.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            System.out.println("Error while adding book: " + e.getMessage());
        }
    }

    // Method to get a book by its title
    public Book getBook(String title) {
        String sql = "SELECT * FROM books WHERE title = ?";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Book(
                        resultSet.getString("title"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("genre"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image_path")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving book: " + e.getMessage());
        }
        return null;  // Return null if the book is not found
    }
}
