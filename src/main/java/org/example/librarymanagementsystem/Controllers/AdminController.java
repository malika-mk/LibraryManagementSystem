package org.example.librarymanagementsystem.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.HelloApplication;
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

    private final SalesDAO salesDAO = new SalesDAO();

    @FXML
    public void initialize() {
        // Настройка колонок таблицы
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
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
        Book popularBook = salesDAO.getMostPopularBookLastMonth();
        if (popularBook != null) {
            ObservableList<Book> books = FXCollections.observableArrayList(popularBook);
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
}
