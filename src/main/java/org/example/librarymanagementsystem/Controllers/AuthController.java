package org.example.librarymanagementsystem.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.dao.DatabaseConnection;

public class AuthController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginSignin;

    @FXML
    private PasswordField passwordSignin;

    @FXML
    private Button signinButton;

    @FXML
    void initialize() {
        // Логика для обработки нажатия кнопки "Sign In"
        signinButton.setOnAction(event -> {
            String loginText = loginSignin.getText().trim();
            String passwordText = passwordSignin.getText().trim();

            if (!loginText.equals("") && !passwordText.equals("")) {
                System.out.println("Attempting to login with username: " + loginText + " and password: " + passwordText);

                if (checkAdminCredentials(loginText, passwordText)) {
                    // Если логин и пароль верны, загрузить страницу hello-view.fxml
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/hello-view.fxml"));
                        Parent root = loader.load();
                        Stage stage = (Stage) signinButton.getScene().getWindow();
                        stage.setScene(new Scene(root));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Invalid login or password");
                }
            } else {
                System.out.println("Login and password are empty");
            }
        });
    }

    private boolean checkAdminCredentials(String login, String password) {
        // Измененный SQL запрос для проверки логина и пароля администратора в таблице admin_users
        String sql = "SELECT * FROM admin_users WHERE username = ? AND password_hash = ?";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, login);
            preparedStatement.setString(2, password);  // Передаем пароль как есть (без хэширования)

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                System.out.println("Admin found: " + resultSet.getString("username"));
                return true;  // Если запись найдена, значит логин и пароль верны
            } else {
                System.out.println("No admin found with given credentials");
                return false;  // Если администратор не найден, возвращаем false
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
