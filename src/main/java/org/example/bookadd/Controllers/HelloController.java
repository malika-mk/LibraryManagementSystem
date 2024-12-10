package org.example.bookadd.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.bookadd.Dao.BookDao;
import org.example.bookadd.Model.Book;

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
    private void onAddBookButtonClick(ActionEvent event) {
        try {
            // Получение данных из текстовых полей
            String title = titleField.getText();
            String author = authorField.getText();
            String description = descriptionField.getText();
            String genre = genreField.getText();
            double price = Double.parseDouble(priceField.getText());

            // Создание объекта книги
            Book newBook = new Book(title, author, description, genre, price);

            // Добавление книги в базу данных через DAO
            BookDao bookDao = new BookDao();
            bookDao.addBook(newBook);

            // Сообщение об успешном добавлении
            System.out.println("Книга добавлена: " + title);
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: Проверьте правильность ввода стоимости.");
        } catch (Exception e) {
            System.out.println("Ошибка при добавлении книги: " + e.getMessage());
        }
    }
}
