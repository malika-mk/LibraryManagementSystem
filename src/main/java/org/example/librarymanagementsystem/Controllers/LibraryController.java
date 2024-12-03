package org.example.librarymanagementsystem.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import javafx.scene.control.Button;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.Application.HelloApplication;

import java.io.IOException;

public class LibraryController {


    @FXML
    private ImageView bannerImageView;

    @FXML
    private Button bestsellerButton;

    @FXML
    void switchToBestseller() throws IOException {
        Stage stage = (Stage) bestsellerButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/librarymanagementsystem/View/bestseller-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
        stage.setTitle("Hello!");
        stage.setScene(scene);

    }

    @FXML
    private Button blogbutton;

    @FXML
    void switchToBlog() throws IOException {
        Stage stage = (Stage) blogbutton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("blog.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
        stage.setTitle("Hello!");
        stage.setScene(scene);

    }

    // Поле для ввода поиска
    @FXML
    private TextField searchField;

    // Таблица для отображения данных
    @FXML
    private TableView<Book> tableView;

    // Столбцы для таблицы
    @FXML
    private TableColumn<Book, String> titleColumn;

    @FXML
    private TableColumn<Book, String> authorColumn;


    private ObservableList<Book> filteredList = FXCollections.observableArrayList();

//    @FXML
//    public void initialize() {
//        if (bannerImageView != null) {
//            // Убедитесь, что размеры ImageView установлены
//            bannerImageView.setFitWidth(600);
//            bannerImageView.setFitHeight(300);
//
//            // Создаём прямоугольник для клипа
//            Rectangle clip = new Rectangle();
//            clip.setWidth(bannerImageView.getFitWidth());  // Устанавливаем ширину
//            clip.setHeight(bannerImageView.getFitHeight()); // Устанавливаем высоту
//            clip.setArcWidth(40); // Радиус скругления углов по ширине
//            clip.setArcHeight(40); // Радиус скругления углов по высоте
//
//            // Применяем clip к ImageView
//            bannerImageView.setClip(clip);
//        }
//   }
//    private void filterList(String query) {
//        if (query == null || query.isEmpty()) {
//            tableView.setItems(bookList); // Показываем все книги, если строка пустая
//        } else {
//            filteredList.clear();
//            for (Book book : bookList) {
//                if (book.getTitle().toLowerCase().contains(query.toLowerCase())
//                        || book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
//                    filteredList.add(book); // Добавляем книги, которые соответствуют запросу
//                }
//            }
//            tableView.setItems(filteredList); // Устанавливаем отфильтрованный список
//        }
//    }

//    public void switchScene(String fxmlFile) {
//
//        try {
//            System.out.println("Switching to: " + fxmlFile); // Отладка
//            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));
//            Scene newScene = new Scene(fxmlLoader.load());
//            Stage currentStage = (Stage) searchField.getScene().getWindow();
//            currentStage.setScene(newScene);
//            System.out.println("Scene switched successfully to " + fxmlFile); // Отладка
//        } catch (IOException e) {
//            System.out.println("Failed to switch to scene: " + fxmlFile); // Отладка
//            e.printStackTrace();
//        }
//    }

//    @FXML
//    public void switchToBestseller() {
//        switchScene("bestseller-view.fxml");
//    }
}