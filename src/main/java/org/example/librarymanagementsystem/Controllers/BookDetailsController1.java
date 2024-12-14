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

    private Book currentBook;

    private Book book;

    private int likes = 0;
    private int sales = 0;

    public void initialize() {
        likeButton.setOnAction(event -> {
            likes++;
            updateLikesLabel();
            updateBookLikesInDatabase();
        });

        sellButton.setOnAction(event -> {
            sales++;
            updateSalesLabel();
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
        BookDAO bookDAO = new BookDAO();
        bookDAO.updateLikes(book.getId(), likes);
    }

    private void updateBookSalesInDatabase() {
        BookDAO bookDAO = new BookDAO();
        bookDAO.updateSales(book.getId(), sales);
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setBookDetails(String name, String author, String description, Image bookImage, Book book) {
        if (book == null) {
            System.err.println("Ошибка: передан null вместо объекта Book.");
            return;
        }

        this.currentBook = book;
        titleLabel.setText(name);
        authorLabel.setText(author);
        descriptionLabel.setText(description);
        bookImageView.setImage(bookImage);

        BookDAO bookDAO = new BookDAO();
        int likes = bookDAO.getLikesForBook(book.getId());
        int sales = bookDAO.getSalesForBook(book.getId());

        likesLabel.setText("Likes: " + likes);
        salesLabel.setText("Sales: " + sales);

        currentBook.setLikes(likes);
        currentBook.setSales(sales);
    }

    @FXML
    private void onLikeButtonClick() {
        if (book != null) {
            book.setLikes(book.getLikes() + 1);
            likesLabel.setText("Likes: " + book.getLikes());

            BookDAO bookDAO = new BookDAO();
            bookDAO.updateLikes(book.getId(), book.getLikes());
        }
    }

    @FXML
    private void onSellButtonClick() {
        if (book != null) {
            book.setSales(book.getSales() + 1);
            salesLabel.setText("Sales: " + book.getSales());

            BookDAO bookDAO = new BookDAO();
            bookDAO.updateSales(book.getId(), book.getSales());
        }
    }

}