package org.example.librarymanagementsystem.Controllers;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.dao.BookDAO;

public class NewBookController {

    @FXML
    private TextField nameField; // Поле ввода имени книги

    @FXML
    private TextField authorField; // Поле ввода автора

    @FXML
    private TextField imageField; // Поле ввода пути к изображению

    @FXML
    private TextArea descriptionField; // Поле ввода описания

    private BookDAO bookDAO = new BookDAO();

    @FXML
    private void saveBook() {
        // Получаем значения из полей
        String name = nameField.getText();
        String author = authorField.getText();
        String description = descriptionField.getText();
        String imagePath = imageField.getText();

        // Проверка на пустые значения
        if (name.isEmpty() || author.isEmpty() || description.isEmpty() || imagePath.isEmpty()) {
            System.out.println("Заполните все поля.");
            return;
        }

        // Загружаем изображение из пути
        Image bookImage;
        try {
            bookImage = new Image(imagePath);
        } catch (IllegalArgumentException e) {
            System.out.println("Ошибка загрузки изображения. Убедитесь, что путь корректный.");
            return;
        }

        // Создаем объект Book и сохраняем его в базу данных
        Book newBook = new Book(0, name, author, description, bookImage); // Обновленный конструктор
        bookDAO.addBook(newBook);

        // Закрытие окна после добавления
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}