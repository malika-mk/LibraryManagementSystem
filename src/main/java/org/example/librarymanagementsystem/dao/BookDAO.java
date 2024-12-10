package org.example.librarymanagementsystem.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.example.librarymanagementsystem.Models.Book;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {

    // Метод для поиска книги по имени
    public Book searchBookByName(String name) {
        String query = "SELECT id, name, author FROM public.book WHERE name ILIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id"); // Получаем id
                String bookName = resultSet.getString("name");
                String author = resultSet.getString("author");

                // Возвращаем объект Book с id, name и author
                return new Book(id, bookName, author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Если книга не найдена, возвращаем null
    }

    // Метод для получения описания книги по ID
    public String getBookDescription(int bookId) {
        String query = "SELECT description FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("description"); // Возвращаем описание
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при извлечении описания книги: " + e.getMessage());
        }
        return null; // Если описание не найдено
    }

    // Метод для получения популярных книг
    public ObservableList<Book> getPopularBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();

        String query = "SELECT * FROM book ORDER BY popularity DESC LIMIT 10";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");

                // Загружаем изображение книги, если оно есть
                Image bookImage = getImageFromDB(id);

                // Если изображение книги не найдено, используем изображение по умолчанию
                if (bookImage == null) {
                    bookImage = new Image(getClass().getResource("/images/Art.jpeg").toExternalForm());
                }

                books.add(new Book(id, name, author, description, bookImage));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    // Метод для извлечения изображения книги из базы данных
    public static Image getImageFromDB(int bookId) {
        String query = "SELECT image FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                byte[] imageBytes = resultSet.getBytes("image"); // Извлекаем бинарные данные изображения
                if (imageBytes != null) {
                    InputStream is = new ByteArrayInputStream(imageBytes);
                    return new Image(is);  // Создаем объект Image из потока
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  // Если изображения нет, возвращаем null
    }

    // Метод для добавления новой книги в базу данных
    public void addBook(Book book) {
        String query = "INSERT INTO book (name, author, description) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Устанавливаем параметры в запрос
            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getDescription());

            statement.executeUpdate(); // Выполняем запрос
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка при добавлении книги в базу данных: " + e.getMessage());
        }
    }
}