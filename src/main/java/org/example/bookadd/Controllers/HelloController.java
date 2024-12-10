package org.example.bookadd.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.bookadd.Dao.BookDao;
import org.example.bookadd.Model.Book;

import java.io.File;

public class HelloController {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField descriptionField;
    @FXML
    private TextField genreField;
    @FXML
    private TextField priceField;
    @FXML
    private TextField imagePathField;

    @FXML
    private ImageView bookImageView;
    @FXML
    private Label bookDetails;

    private String imagePath = "";

    @FXML
    private void onSelectImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png", "*.jpeg"));
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            imagePath = selectedFile.getAbsolutePath();
            imagePathField.setText(imagePath);
        }
    }

    @FXML
    private void onAddBookButtonClick() {
        try {
            // Validate if the fields are empty
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty() || descriptionField.getText().isEmpty() || genreField.getText().isEmpty() || priceField.getText().isEmpty() || imagePath.isEmpty()) {
                System.out.println("Please fill in all fields.");
                return;
            }

            // Validate the price input
            double price;
            try {
                price = Double.parseDouble(priceField.getText());
            } catch (NumberFormatException e) {
                System.out.println("Invalid price format. Please enter a valid number.");
                return;
            }

            // Create the book object and add it to the database
            Book newBook = new Book(titleField.getText(), authorField.getText(), descriptionField.getText(), genreField.getText(), price, imagePath);
            BookDao bookDao = new BookDao();
            bookDao.addBook(newBook);

            System.out.println("Book added successfully: " + titleField.getText());
            displayBook(newBook);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }


    private void displayBook(Book book) {
        bookImageView.setImage(new Image("file:" + book.getImagePath()));
        bookDetails.setText(
                "Title: " + book.getTitle() + "\n" +
                        "Author: " + book.getAuthor() + "\n" +
                        "Description: " + book.getDescription() + "\n" +
                        "Genre: " + book.getGenre() + "\n" +
                        "Price: $" + book.getPrice()
        );
    }
}
