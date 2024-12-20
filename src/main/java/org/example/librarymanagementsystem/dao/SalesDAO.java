package org.example.librarymanagementsystem.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
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

    public void addBook(Book book) {
        String sql = "INSERT INTO book (name, author, description, category, price, image_path) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.setString(4, book.getCategory());
            preparedStatement.setDouble(5, book.getPrice());
            preparedStatement.setString(6, book.getImagePath());

            preparedStatement.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            System.out.println("Error while adding book: " + e.getMessage());
        }
    }

    public Book getBook(String title) {
        String sql = "SELECT * FROM book WHERE name = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, title);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Book(
                        resultSet.getString("name"),
                        resultSet.getString("author"),
                        resultSet.getString("description"),
                        resultSet.getString("category"),
                        resultSet.getDouble("price"),
                        resultSet.getString("image_path")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving book: " + e.getMessage());
        }
        return null;
    }

    public Book getMostLikedBook() {
        String query = """
    SELECT b.id, b.name, b.author, b.description, b.imagepath, b.isbn, b.price, b.likes, c.categoryname
    FROM book b
    JOIN book_category bc ON b.id = bc.book_id
    JOIN category c ON bc.category_id = c.id
    ORDER BY b.likes DESC
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
                        resultSet.getString("isbn"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("likes"),
                        resultSet.getString("categoryname")
                );
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении популярной книги: " + e.getMessage());
        }
        return null;
    }

    public Book getMostSoldBook() {
        String query = """
    SELECT b.id, b.name, b.author, b.description, b.imagepath, b.isbn, b.price, b.sales, c.categoryname
    FROM book b
    JOIN book_category bc ON b.id = bc.book_id
    JOIN category c ON bc.category_id = c.id
    ORDER BY b.sales DESC
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
                        resultSet.getString("isbn"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("sales"),
                        resultSet.getString("categoryname")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Book getMostExpensiveBook() {
        String query = """
    SELECT b.id, b.name, b.author, b.description, b.imagepath, b.isbn, b.price, b.likes, c.categoryname
    FROM book b
    JOIN book_category bc ON b.id = bc.book_id
    JOIN category c ON bc.category_id = c.id
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
                        resultSet.getString("description"),
                        resultSet.getString("imagepath"),
                        resultSet.getString("isbn"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("likes"),
                        resultSet.getString("categoryname")
                );
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении самой дорогой книги: " + e.getMessage());
        }
        return null;
    }

    public Book getCheapestBook() {
        String query = """
    SELECT b.id, b.name, b.author, b.description, b.imagepath, b.isbn, b.price, b.sales, c.categoryname
    FROM book b
    JOIN book_category bc ON b.id = bc.book_id
    JOIN category c ON bc.category_id = c.id
    ORDER BY b.price ASC
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
                        resultSet.getString("isbn"),
                        resultSet.getDouble("price"),
                        resultSet.getInt("sales"),
                        resultSet.getString("categoryname")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMostPopularCategory() {
        String query = """
        SELECT c.categoryname, COUNT(b.id) AS book_count
        FROM category c
        JOIN book_category bc ON c.id = bc.category_id
        JOIN book b ON bc.book_id = b.id
        GROUP BY c.categoryname
        ORDER BY book_count DESC
        LIMIT 1
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("categoryname");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ObservableList<Book> getBooksByCategory(String category) {
        ObservableList<Book> books = FXCollections.observableArrayList();
        String query = """
        SELECT b.id, b.name, b.author, b.description, b.image, b.isbn, b.price, b.sales
        FROM book b
        JOIN book_category bc ON b.id = bc.book_id
        JOIN category c ON bc.category_id = c.id
        WHERE c.categoryname = ?
    """;
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                double price = resultSet.getDouble("price");
                int sales = resultSet.getInt("sales");
                String isbn = resultSet.getString("isbn");

                Image bookImage = null;
                String imagePath = resultSet.getString("image");
                if (imagePath != null && !imagePath.isEmpty()) {
                    bookImage = new Image("file:" + imagePath);
                }

                books.add(new Book(id, name, author, description, bookImage, isbn, price, sales));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    public String getLeastPopularCategory() {
        String query = """
    SELECT c.categoryname, COUNT(b.id) AS book_count
    FROM category c
    JOIN book_category bc ON c.id = bc.category_id
    JOIN book b ON bc.book_id = b.id
    GROUP BY c.categoryname
    ORDER BY book_count ASC
    LIMIT 1
    """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("categoryname");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении самой непопулярной категории: " + e.getMessage());
        }
        return null;
    }
}