package org.example.librarymanagementsystem.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.dao.BookDAO;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.dao.CategoryDAO; // DAO для получения категорий

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class NewBookController {

    @FXML
    private TextField nameField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField descriptionField;
    @FXML
    private ComboBox<String> categoryComboBox; // Замена TextField на ComboBox
    @FXML
    private TextField priceField;
    @FXML
    private TextField imagePathField;
    @FXML
    private TextField isbnField;

    @FXML
    private ImageView bookImageView;
    @FXML
    private Label bookDetails;

    private String imagePath = "";

    // Map для хранения соответствий между именами категорий и их ID
    private Map<String, Integer> categoryMap = new HashMap<>();

    @FXML
    private void initialize() {
        loadCategories(); // Загрузка категорий в ComboBox
    }

    private void loadCategories() {
        try {
            CategoryDAO categoryDAO = new CategoryDAO();
            ObservableList<String> categoryNames = FXCollections.observableArrayList();

            categoryDAO.getAllCategories().forEach(category -> {
                categoryNames.add(category.getName());
                categoryMap.put(category.getName(), category.getId());
            });

            categoryComboBox.setItems(categoryNames);
        } catch (Exception e) {
            System.out.println("Error loading categories: " + e.getMessage());
        }
    }

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
            if (nameField.getText().isEmpty() || authorField.getText().isEmpty() || descriptionField.getText().isEmpty() ||
                    categoryComboBox.getValue() == null || priceField.getText().isEmpty() || imagePath.isEmpty() || isbnField.getText().isEmpty()) {
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

            // Получить ID категории из выбранного значения ComboBox
            String selectedCategoryName = categoryComboBox.getValue();
            int categoryId = categoryMap.get(selectedCategoryName);

            // Получить значение ISBN
            String isbn = isbnField.getText();

            // Создать объект книги и добавить его в базу данных
            Book newBook = new Book(
                    nameField.getText(),
                    authorField.getText(),
                    descriptionField.getText(),
                    categoryId,
                    price,
                    imagePath,
                    isbn
            );
            BookDAO bookDAO = new BookDAO();
            bookDAO.addBook(newBook);

            System.out.println("Book added successfully: " + nameField.getText());
            displayBook(newBook);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void displayBook(Book book) {
        bookImageView.setImage(new Image("file:" + book.getImagePath()));
        bookDetails.setText(
                "Name: " + book.getName() + "\n" +
                        "Author: " + book.getAuthor() + "\n" +
                        "Description: " + book.getDescription() + "\n" +
                        "Category ID: " + book.getCategoryId() + "\n" +
                        "Price: $" + book.getPrice()
        );
    }
}