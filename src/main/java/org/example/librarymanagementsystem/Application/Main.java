package org.example.librarymanagementsystem.Application;

import org.example.librarymanagementsystem.dao.DatabaseConnection;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connection established!");

                // Укажите путь к изображению, которое вы хотите добавить
                File imageFile = new File("src/main/resources/images/1984.jpeg");

                // Сохранение изображения в базу данных для книги с ID 1
                saveImage(connection, 1, imageFile);

            } else {
                System.out.println("Failed to connect to the database.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Метод для сохранения изображения в базу данных.
     *
     * @param connection Активное соединение с базой данных.
     * @param bookId     ID книги, для которой добавляется изображение.
     * @param imageFile  Файл изображения для добавления.
     */
    public static void saveImage(Connection connection, int bookId, File imageFile) {
        String query = "UPDATE book SET image = ? WHERE id = ?";
        try (FileInputStream inputStream = new FileInputStream(imageFile);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            // Устанавливаем параметры
            preparedStatement.setBinaryStream(1, inputStream, (int) imageFile.length());
            preparedStatement.setInt(2, bookId);

            // Выполняем запрос
            int rowsUpdated = preparedStatement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Image updated successfully for book ID: " + bookId);
            } else {
                System.out.println("No book found with ID: " + bookId);
            }
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }
}