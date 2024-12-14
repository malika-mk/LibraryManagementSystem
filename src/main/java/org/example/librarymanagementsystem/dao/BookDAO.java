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
                    bookImage = new Image("/path/to/default/image.png");
                }

                return new Book(id, bookName, author, description, bookImage, isbn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

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
                        resultSet.getInt("category_id"),
                        resultSet.getDouble("price"),
                        resultSet.getString("imagepath"),
                        resultSet.getString("isbn")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error while retrieving book: " + e.getMessage());
        }
        return null;
    }

    public void addBook(Book book) {
        String sql = "INSERT INTO book (name, author, description, category_id, price, imagepath, isbn) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, book.getName());
            preparedStatement.setString(2, book.getAuthor());
            preparedStatement.setString(3, book.getDescription());
            preparedStatement.setInt(4, book.getCategoryId());
            preparedStatement.setDouble(5, book.getPrice());
            preparedStatement.setString(6, book.getImagePath());
            preparedStatement.setString(7, book.getIsbn());

            preparedStatement.executeUpdate();
            System.out.println("Book added successfully.");
        } catch (SQLException e) {
            System.out.println("Error while adding book: " + e.getMessage());
        }
    }

    public void addLike(int bookId) {
        String query = "INSERT INTO likes (book_id) VALUES (?)";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            statement.executeUpdate();

            // Обновляем счетчик лайков в таблице `book`
            String updateQuery = "UPDATE book SET likes = likes + 1 WHERE id = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setInt(1, bookId);
                updateStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLikes(int bookId) {
        String query = "SELECT COUNT(*) AS like_count FROM likes WHERE book_id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("like_count");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
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

                Image bookImage = null;
                if (imageBytes != null) {
                    bookImage = new Image(new ByteArrayInputStream(imageBytes));
                } else if (imagePath != null && !imagePath.isEmpty()) {
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

        String query = "SELECT id, name, author, description, image, isbn FROM book ORDER BY id LIMIT 10";
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

    public void addLikeToBook(int bookId) {
        String query = "UPDATE book SET likes = likes + 1 WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Лайк успешно добавлен.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении лайка: " + e.getMessage());
        }
    }

    public void addSaleToBook(int bookId) {
        String query = "UPDATE book SET sales = sales + 1 WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            statement.executeUpdate();
            System.out.println("Продажа успешно добавлена.");
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении продажи: " + e.getMessage());
        }
    }

    public void updateLikes(int bookId, int newLikes) {
        String query = "UPDATE public.book SET likes = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newLikes);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateSales(int bookId, int newSales) {
        String query = "UPDATE public.book SET sales = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, newSales);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLikesForBook(int bookId) {
        String query = "SELECT likes FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("likes");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int getSalesForBook(int bookId) {
        String query = "SELECT sales FROM public.book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("sales");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void updateBookLikes(int bookId, int likes) {
        String query = "UPDATE book SET likes = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, likes);
            statement.setInt(2, bookId);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateBookSales(int bookId, int sales) {
        String query = "UPDATE book SET sales = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, sales);
            statement.setInt(2, bookId);
            statement.executeUpdate();
            System.out.println("Продажи успешно обновлены для книги ID: " + bookId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}