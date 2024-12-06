package org.example.librarymanagementsystem.Controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AuthController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField lastnameSignup;

    @FXML
    private TextField loginSignin;

    @FXML
    private TextField loginSignup;

    @FXML
    private TextField nameSignup;

    @FXML
    private PasswordField passwordSignin;

    @FXML
    private PasswordField passwordSignup;

    @FXML
    private Button signinButton;

    @FXML
    private Button signupButton;

    @FXML
    void initialize() {

        signinButton.setOnAction(event -> {
            String loginText = loginSignin.getText().trim();
            String passwordText = passwordSignin.getText().trim();

            if(!loginText.equals("") && !passwordText.equals(""))
                loginUser(loginText, passwordText);
            else
                System.out.println("Login and password are empty");
        });
    }
private void loginUser(String loginText, String loginPassword) {

}
}





//signinButton.setOnAction(event -> {
//            signinButton.getScene().getWindow().hide();
//
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(getClass().getResource("/fxml/login.fxml"));
//            try {
//                loader.load();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Parent root = loader.getRoot();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.showAndWait();
//        });//