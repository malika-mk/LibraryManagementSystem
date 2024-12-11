//package org.example.librarymanagementsystem.Controllers;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.TextArea;
//import javafx.scene.control.TextField;
//import javafx.scene.image.Image;
//import javafx.stage.FileChooser;
//import javafx.stage.Stage;
//import org.example.librarymanagementsystem.Models.Book;
//import org.example.librarymanagementsystem.dao.BookDAO;
//
//import java.io.File;
//
//public class NewBookController {
//
//    @FXML
//    private TextField nameField; // Поле ввода имени книги
//
//    @FXML
//    private TextField authorField; // Поле ввода автора
//
//    @FXML
//    private TextField imageField; // Поле ввода пути к изображению
//
//    @FXML
//    private TextArea descriptionField; // Поле ввода описания
//
//    private BookDAO bookDAO = new BookDAO();
//
//    @FXML
//    private void saveBook() {
//        String name = nameField.getText();
//        String author = authorField.getText();
//        String description = descriptionField.getText();
//        String imagePath = imageField.getText();
//
//        if (name.isEmpty() || author.isEmpty() || description.isEmpty() || imagePath.isEmpty()) {
//            System.out.println("Пожалуйста, заполните все поля.");
//            return;
//        }
//
//        // Создаём объект книги
//        Book newBook = new Book(0, name, author, description, imagePath);
//
//        // Сохраняем книгу в базу данных
//        BookDAO bookDAO = new BookDAO();
//        bookDAO.addBook(newBook);
//
//        // Закрытие окна после добавления
//        Stage stage = (Stage) nameField.getScene().getWindow();
//        stage.close();
//    }
//
//    @FXML
//    private void selectImage() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Изображения", "*.png", "*.jpg", "*.jpeg"));
//        File selectedFile = fileChooser.showOpenDialog(null);
//
//        if (selectedFile != null) {
//            imageField.setText(selectedFile.getAbsolutePath());
//        }
//    }
//}
