package org.example.librarymanagementsystem.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.Application.HelloApplication;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.dao.SalesDAO;

import java.io.IOException;

public class AdminController {

    @FXML
    private TableView<Book> statisticsTable;
    @FXML
    private TableColumn<Book, String> nameColumn;
    @FXML
    private TableColumn<Book, String> authorColumn;
    @FXML
    private TableColumn<Book, String> valueColumn;
    @FXML
    private TableColumn<Book, Double> priceColumn;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    private final SalesDAO salesDAO = new SalesDAO();

    @FXML
    private TableColumn<Book, String> categoryColumn;

    @FXML
    public void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
    }

    @FXML
    private Button HomeButton;

    @FXML
    void switchToHome() throws IOException {
        Stage stage = (Stage) HomeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/librarymanagementsystem/view/hello-view.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
            stage.setTitle("Hello!");
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private void onShowPopularBook() {
        Book mostLikedBook = salesDAO.getMostLikedBook();
        if (mostLikedBook != null) {
            ObservableList<Book> books = FXCollections.observableArrayList(mostLikedBook);
            statisticsTable.setItems(books);
        } else {
            System.out.println("Нет данных о популярной книге.");
        }
    }

    @FXML
    private void onShowMostSoldBook() {
        Book mostSoldBook = salesDAO.getMostSoldBook();
        if (mostSoldBook != null) {
            ObservableList<Book> books = FXCollections.observableArrayList(mostSoldBook);
            statisticsTable.setItems(books);
        } else {
            System.out.println("Нет данных о продаваемых книгах.");
        }
    }

    @FXML
    private void onShowMostExpensiveBook() {
        Book mostExpensiveBook = salesDAO.getMostExpensiveBook();
        if (mostExpensiveBook != null) {
            ObservableList<Book> books = FXCollections.observableArrayList(mostExpensiveBook);
            statisticsTable.setItems(books);
        } else {
            System.out.println("Нет данных о дорогих книгах.");
        }
    }

    @FXML
    private void onShowCheapestBook() {
        Book cheapestBook = salesDAO.getCheapestBook();
        if (cheapestBook != null) {
            ObservableList<Book> books = FXCollections.observableArrayList(cheapestBook);
            statisticsTable.setItems(books);
        } else {
            System.out.println("Нет данных о самой дешевой книге.");
        }
    }

    @FXML
    private void onShowMostPopularCategory() {
        String mostPopularCategory = salesDAO.getMostPopularCategory();
        if (mostPopularCategory != null) {
            ObservableList<Book> booksInCategory = salesDAO.getBooksByCategory(mostPopularCategory);

            for (Book book : booksInCategory) {
                book.setCategory(mostPopularCategory);
            }

            statisticsTable.setItems(booksInCategory);
            System.out.println("Самая популярная категория: " + mostPopularCategory);
        } else {
            System.out.println("Нет данных о категориях.");
        }
    }

    @FXML
    private void onShowLeastPopularCategory() {
        String leastPopularCategory = salesDAO.getLeastPopularCategory();

        if (leastPopularCategory != null) {
            ObservableList<Book> booksInCategory = salesDAO.getBooksByCategory(leastPopularCategory);

            for (Book book : booksInCategory) {
                book.setCategory(leastPopularCategory);
            }
            statisticsTable.setItems(booksInCategory);

            System.out.println("Самая непопулярная категория: " + leastPopularCategory);
        } else {
            System.out.println("Нет данных о категориях.");
        }
    }
}
