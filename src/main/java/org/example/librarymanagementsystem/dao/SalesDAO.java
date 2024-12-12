package org.example.librarymanagementsystem.dao;

import org.example.librarymanagementsystem.Models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SalesDAO {
    public void recordSale(int bookId, int quantity) {
        String query = "INSERT INTO sales (book_id, quantity) VALUES (?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            statement.setInt(2, quantity);
            statement.executeUpdate();

            System.out.println("Продажа успешно записана.");
        } catch (SQLException e) {
            System.err.println("Ошибка при записи продажи: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public Book getMostPopularBookLastMonth() {
        String query = """
        SELECT b.id, b.name, b.author, b.description, b.imagepath, b.isbn, SUM(s.quantity) AS total_sales
        FROM book b
        JOIN sales s ON b.id = s.book_id
        WHERE s.sale_date >= NOW() - INTERVAL '1 month'
        GROUP BY b.id
        ORDER BY total_sales DESC
        LIMIT 1
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("imagepath"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("likes"),
                        resultSet.getLong("total_sales")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book getMostSoldBook() {
        String query = """
        SELECT b.id, b.name, b.author, b.description, b.imagepath, b.price, SUM(s.quantity) AS total_sales
        FROM book b
        JOIN sales s ON b.id = s.book_id
        GROUP BY b.id
        ORDER BY total_sales DESC
        LIMIT 1;
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("imagepath"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("likes"),
                        resultSet.getLong("total_sales")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Ошибка при получении самой продаваемой книги: " + e.getMessage());
        }
        return null;
    }

    public Book getMostExpensiveBook() {
        String query = """
            SELECT b.id, b.name, b.author, b.price
            FROM book b
            ORDER BY b.price DESC
            LIMIT 1
            """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new Book(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getDouble("price")
                );
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении самой дорогой книги: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}