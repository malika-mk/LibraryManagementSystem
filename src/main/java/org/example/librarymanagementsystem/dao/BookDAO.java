package org.example.librarymanagementsystem.dao;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import org.example.librarymanagementsystem.ImageUtils;
import org.example.librarymanagementsystem.Models.Book;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {

    // Метод для поиска книги по имени
    public Book searchBookByName(String name) {
        String query = "SELECT id, name, author, description, image, isbn FROM public.book WHERE name ILIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String bookName = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                byte[] imageBytes = resultSet.getBytes("image");
                String isbn = resultSet.getString("isbn");

                Image bookImage = null;
                if (imageBytes != null) {
                    bookImage = new Image(new ByteArrayInputStream(imageBytes));
                } else {
                    bookImage = new Image("/path/to/default/image.png"); // Замените на путь к изображению по умолчанию
                }

                return new Book(id, bookName, author, description, bookImage, isbn); // Используйте конструктор с Image
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для получения описания книги по ID
    public String getBookDescription(int bookId) {
        String query = "SELECT description FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, bookId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("description");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Метод для добавления книги в базу данных
    public void addBook(Book book) {
        String query = "INSERT INTO book (name, author, description, image) VALUES (?, ?, ?, ?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, book.getName());
            statement.setString(2, book.getAuthor());
            statement.setString(3, book.getDescription());

            // Проверка и преобразование изображения в байты
            if (book.getImage() instanceof javafx.scene.image.Image) {
                statement.setBytes(4, ImageUtils.convertImageToBytes((javafx.scene.image.Image) book.getImage()));
            } else {
                statement.setBytes(4, null); // Если изображение отсутствует
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<Book> getBooksByCategory(String category) {
        ObservableList<Book> books = FXCollections.observableArrayList();

        String query = "SELECT b.id, b.name, b.author, b.description, b.image, b.imagepath, b.isbn " +
                "FROM public.book b " +
                "JOIN public.book_category bc ON b.id = bc.book_id " +
                "JOIN public.category c ON bc.category_id = c.id " +
                "WHERE c.categoryname = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, category);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                byte[] imageBytes = resultSet.getBytes("image");
                String imagePath = resultSet.getString("imagepath");
                String isbn = resultSet.getString("isbn");

                // Создаем объект Image
                Image bookImage = null;
                if (imageBytes != null) {
                    // Если данные изображения сохранены в бинарном формате
                    bookImage = new Image(new ByteArrayInputStream(imageBytes));
                } else if (imagePath != null && !imagePath.isEmpty()) {
                    // Если указан путь к изображению
                    try {
                        bookImage = new Image("file:" + imagePath);
                    } catch (Exception e) {
                        System.err.println("Ошибка загрузки изображения по пути: " + imagePath);
                    }
                }

                books.add(new Book(id, name, author, description, bookImage, isbn));
            }

            System.out.println("Книги, найденные для категории " + category + ": " + books.size());
        } catch (SQLException e) {
            System.err.println("Ошибка при выполнении запроса: " + e.getMessage());
            e.printStackTrace();
        }

        return books;
    }

    public ObservableList<Book> getPopularBooks() {
        ObservableList<Book> books = FXCollections.observableArrayList();

        String query = "SELECT id, name, author, description, image, isbn FROM book ORDER BY id LIMIT 10"; // Здесь можно изменить условие
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                byte[] imageBytes = resultSet.getBytes("image");

                Image bookImage = (imageBytes != null) ? new Image(new ByteArrayInputStream(imageBytes)) : null;

                books.add(new Book(id, name, author, description, bookImage, resultSet.getString("isbn")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }
}