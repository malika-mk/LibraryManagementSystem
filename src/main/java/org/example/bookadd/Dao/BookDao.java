package org.example.bookadd.Dao;

import org.example.bookadd.Model.Book;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class BookDao {

    public void addBook(Book book) {
        String sql = "INSERT INTO books (title, author, description, genre, price) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getTitle());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.setString(4, book.getGenre());
            preparedStatement.setDouble(5, book.getPrice());

            preparedStatement.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            System.out.println("Error while adding book: " + e.getMessage());
        }
    }

}