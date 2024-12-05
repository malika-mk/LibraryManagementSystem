package org.example.librarymanagementsystem.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class BookDetailsController1 {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView bookImageView;

    /**
     * Метод для установки данных книги в интерфейс.
     *
     * @param title       Название книги
     * @param author      Автор книги
     * @param description Описание книги
     * @param bookImage   Изображение книги
     */
    public void setBookDetails(String title, String author, String description, Image bookImage) {
        titleLabel.setText(title);
        authorLabel.setText(author);
        descriptionLabel.setText(description); // Убедитесь, что описание правильно передается

        if (bookImage != null) {
            bookImageView.setImage(bookImage);
        } else {
            bookImageView.setImage(new Image("/src/main/java/org/example/resources/images/_.jpeg"));
        }
    }
}