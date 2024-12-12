package org.example.librarymanagementsystem.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.dao.BookDAO;

public class BookDetailsController1 {

    @FXML
    private Label titleLabel;

    @FXML
    private Label authorLabel;

    @FXML
    private Label descriptionLabel;

    @FXML
    private ImageView bookImageView;

    @FXML
    private Button likeButton;

    @FXML
    private Button sellButton;

    @FXML
    private Label likesLabel;

    @FXML
    private Label salesLabel;

    private Book book;


    private int likes = 0; // Лайки текущей книги
    private int sales = 0; // Продажи текущей книги

    public void initialize() {
        likeButton.setOnAction(event -> {
            likes++;
            updateLikesLabel();
            // Здесь обновляем данные книги в базе данных
            updateBookLikesInDatabase();
        });

        sellButton.setOnAction(event -> {
            sales++;
            updateSalesLabel();
            // Здесь обновляем данные книги в базе данных
            updateBookSalesInDatabase();
        });
    }

    private void updateLikesLabel() {
        likesLabel.setText("Likes: " + likes);
    }

    private void updateSalesLabel() {
        salesLabel.setText("Sales: " + sales);
    }

    private void updateBookLikesInDatabase() {
        // Логика обновления лайков в базе данных
        BookDAO bookDAO = new BookDAO();
        bookDAO.updateLikes(book.getId(), likes); // Метод для обновления в DAO
    }

    private void updateBookSalesInDatabase() {
        // Логика обновления продаж в базе данных
        BookDAO bookDAO = new BookDAO();
        bookDAO.updateSales(book.getId(), sales); // Метод для обновления в DAO
    }

    /**
     * Метод для установки данных книги в интерфейс.
     *
     * @param title       Название книги
     * @param author      Автор книги
     * @param description Описание книги
     * @param bookImage   Изображение книги
     */
    public void setBookDetails(String name, String author, String description, Image bookImage) {
        titleLabel.setText(name);
        authorLabel.setText(author);
        descriptionLabel.setText(description);

        if (bookImage != null) {
            bookImageView.setImage(bookImage);
        } else {
            // Замените на изображение по умолчанию
            bookImageView.setImage(new Image("/path/to/default/image.png"));
        }
    }

    public void setBook(Book book) {
        this.book = book;
    }

    private BookDAO bookDAO = new BookDAO();
    private Book currentBook;

    @FXML
    private void onLikeButtonClick() {
        if (currentBook != null) {
            bookDAO.addLikeToBook(currentBook.getId());
            System.out.println("Лайк добавлен для книги: " + currentBook.getName());
        }
    }

    @FXML
    private void onSellButtonClick() {
        if (currentBook != null) {
            bookDAO.addSaleToBook(currentBook.getId());
            System.out.println("Продажа добавлена для книги: " + currentBook.getName());
        }
    }
}