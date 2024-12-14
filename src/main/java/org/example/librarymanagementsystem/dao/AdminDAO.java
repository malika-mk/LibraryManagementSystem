package org.example.librarymanagementsystem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    // Метод для проверки логина и пароля
    public static boolean checkAdminCredentials(String username, String password) {
        String query = "SELECT * FROM admin WHERE username = ? AND password_hash = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, username);  // Устанавливаем логин
            preparedStatement.setString(2, password);  // Сравниваем пароль напрямую (без хэширования)

            ResultSet resultSet = preparedStatement.executeQuery();

            // Если результат существует, значит логин и пароль совпали
            if (resultSet.next()) {
                return true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false; // Логин или пароль неверные
    }
}
