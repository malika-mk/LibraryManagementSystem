package org.example.librarymanagementsystem.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeLabel;

    @FXML
    private void onButtonClick() {
        welcomeLabel.setText("Welcome to the Library Management System!");
    }
}