package org.example.librarymanagementsystem.dao;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetImageFromBooks {
    public static javafx.scene.image.Image getImage(int bookId) {
        String query = "SELECT image FROM book WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                InputStream imageStream = resultSet.getBinaryStream("image");
                if (imageStream != null) {
                    return new javafx.scene.image.Image(imageStream);
                } else {
                    System.out.println("Изображение не найдено для книги с ID: " + bookId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}