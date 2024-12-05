package org.example.librarymanagementsystem.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class BookDetailsController1 {
    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label descriptionLabel;

    public void setBookDetails(String title, String author, String description) {
        titleLabel.setText(title);
        authorLabel.setText(author);
        descriptionLabel.setText(description);
    }
}
