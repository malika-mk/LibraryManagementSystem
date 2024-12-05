package org.example.librarymanagementsystem.dao;

import javafx.scene.image.Image;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GetImageFromBooks {
    public static javafx.scene.image.Image getImage(int bookId) {
        String query = "SELECT image FROM books WHERE id = ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, bookId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                InputStream inputStream = resultSet.getBinaryStream("image");
                return new javafx.scene.image.Image(inputStream); // Используем JavaFX Image
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}