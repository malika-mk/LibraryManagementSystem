package org.example.librarymanagementsystem.dao;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class AddImageToBooks {
    public static void saveImage(int bookId, File imageFile) {
        String query = "UPDATE book SET image = ? WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             FileInputStream fis = new FileInputStream(imageFile)) {

            statement.setBinaryStream(1, fis, (int) imageFile.length());
            statement.setInt(2, bookId);

            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Image updated successfully for book ID: " + bookId);
            } else {
                System.out.println("No book found with ID: " + bookId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Укажите путь к изображению
        File image = new File("resources/images/1984.jpeg");
        saveImage(1, image);
    }
}