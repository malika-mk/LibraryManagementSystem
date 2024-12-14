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

    public Book getBookById(int bookId) {
        String query = "SELECT id, name, author, description, image, isbn, likes, sales FROM public.book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                byte[] imageBytes = resultSet.getBytes("image");
                String isbn = resultSet.getString("isbn");
                int likes = resultSet.getInt("likes");
                int sales = resultSet.getInt("sales");

                Image bookImage = (imageBytes != null) ? new Image(new ByteArrayInputStream(imageBytes)) : null;

                return new Book(id, name, author, description, bookImage, isbn, likes, sales);
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
        return 0; // Возвращаем 0, если ничего не найдено или произошла ошибка
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