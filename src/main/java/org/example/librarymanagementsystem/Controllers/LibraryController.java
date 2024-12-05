package org.example.librarymanagementsystem.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.HelloApplication;
import org.example.librarymanagementsystem.dao.BookDAO;
import org.example.librarymanagementsystem.dao.DatabaseConnection;
import org.example.librarymanagementsystem.dao.GetImageFromBooks;

import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibraryController {
    private BookDAO bookDAO = new BookDAO();

    @FXML
    private ImageView bannerImageView;

    @FXML
    private GridPane booksGrid;

    @FXML
    private Label popularNowLabel;

    @FXML
    private VBox searchResultsBox;

    @FXML
    private Button bestsellerButton;

    @FXML
    private ChoiceBox<String> categoryChoiceBox;

    @FXML
    private TextField searchField;

    @FXML
    private VBox contentBox; // Контейнер для отображения результатов

    private ObservableList<Book> filteredList = FXCollections.observableArrayList();

    @FXML
    private Button searchButton; // Кнопка-кружок

    @FXML
    private void onSearchButtonClick() {
        System.out.println("Кнопка нажата");
        String searchText = searchField.getText().trim();
        System.out.println("Текст поиска: " + searchText);

        if (!searchText.isEmpty()) {
            Book foundBook = searchBookByName(searchText);
            if (foundBook != null) {
                System.out.println("Книга найдена: " + foundBook.getName());

                // Скрываем "Popular Now"
                booksGrid.setVisible(false); // Скрыть GridPane
                booksGrid.setManaged(false); // Убрать из компоновки

                // Показываем результаты поиска
                displaySearchResult(foundBook);
            } else {
                System.out.println("Книга не найдена");

                // Показываем "Popular Now", если результатов нет
                booksGrid.setVisible(true);
                booksGrid.setManaged(true);

                displayNoResult();
            }
        } else {
            System.out.println("Поле поиска пустое");

            // Восстанавливаем отображение "Popular Now"
            booksGrid.setVisible(true);
            booksGrid.setManaged(true);

            displayDefaultContent();
        }
    }

    @FXML
    public void initialize() {
        categoryChoiceBox.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        // Добавляем категории
        categoryChoiceBox.getItems().addAll("Romance novel", "Fantasy", "Adventure fiction", "Horror");

        // Устанавливаем начальное значение
        categoryChoiceBox.setValue("Category");

        // Добавляем обработчик выбора категории
        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                switchToCategoryPage(newValue);
            }
        });

        // Обработка нажатия Enter в поле поиска
        searchField.setOnAction(event -> {
            // Выполняем поиск только по нажатию Enter
            onSearch();
        });

        // Слушатель изменений текста (только для валидации или дополнительных действий)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                // Если поле поиска пустое, показываем секцию "Popular Now"
                booksGrid.setVisible(true);
                booksGrid.setManaged(true);
                searchResultsBox.setVisible(false);
                searchResultsBox.setManaged(false);
                popularNowLabel.setVisible(true);
            }
        });
    }

    private void switchToCategoryPage(String category) {
        try {
            Stage stage = (Stage) categoryChoiceBox.getScene().getWindow();

            // Загружаем FXML-файл для выбранной категории
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/Fantasy.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void switchToBestseller() throws IOException {
        Stage stage = (Stage) bestsellerButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/librarymanagementsystem/view/bestseller-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
        stage.setTitle("Hello!");
        stage.setScene(scene);
    }

    @FXML
    private Button blogbutton;

    @FXML
    void switchToBlog() throws IOException {
        Stage stage = (Stage) blogbutton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/librarymanagementsystem/view/blog.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
            stage.setTitle("Hello!");
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    @FXML
    private Button HomeButton;

    @FXML
    void switchToHome() throws IOException {
        Stage stage = (Stage) HomeButton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/librarymanagementsystem/view/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
        stage.setTitle("Hello!");
        stage.setScene(scene);
    }

    @FXML
    private ImageView bookImageView;

    // Метод поиска
    private void onSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            System.out.println("Поле поиска пустое. Показать дефолтное содержимое.");
            displayDefaultContent();
            return;
        }

        System.out.println("Выполняется поиск книги с запросом: " + query);

        try {
            // Получаем информацию о книге
            Book result = bookDAO.searchBookByName(query);

            if (result != null) {
                System.out.println("Книга найдена: " + result.getName());

                // Загружаем изображение книги
                javafx.scene.image.Image bookImage = GetImageFromBooks.getImage(result.getId());
                result.setImage(bookImage); // Устанавливаем изображение

                // Скрываем популярное только если нашли результат
                booksGrid.setVisible(false);
                booksGrid.setManaged(false);
                popularNowLabel.setVisible(false);
                popularNowLabel.setManaged(false);

                // Отображаем результаты поиска с изображением
                displaySearchResult(result, bookImage);
            } else {
                System.out.println("Книга не найдена.");
                booksGrid.setVisible(true);
                booksGrid.setManaged(true);
                popularNowLabel.setVisible(true);
                popularNowLabel.setManaged(true);
                displayNoResult();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Ошибка при выполнении поиска: " + e.getMessage());
        }
    }

    private void displayNoResult() {
        searchResultsBox.getChildren().clear();

        Label noResultLabel = new Label("Книга не найдена.");
        noResultLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");

        searchResultsBox.getChildren().add(noResultLabel);
        searchResultsBox.setVisible(true);
        searchResultsBox.setManaged(true);
    }

    private void displaySearchResult(Book book, javafx.scene.image.Image bookImage) {
        searchResultsBox.getChildren().clear();

        Label titleLabel = new Label("Название: " + book.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label authorLabel = new Label("Автор: " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 16px;");

        ImageView bookImageView = new ImageView(bookImage);
        bookImageView.setFitWidth(200); // Установите ширину
        bookImageView.setFitHeight(300); // Установите высоту
        bookImageView.setPreserveRatio(true);

        VBox bookDetailsBox = new VBox(bookImageView, titleLabel, authorLabel);
        bookDetailsBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f4f4f4;");
        bookDetailsBox.setSpacing(10);

        searchResultsBox.getChildren().add(bookDetailsBox);
        searchResultsBox.setVisible(true);
        searchResultsBox.setManaged(true);
    }

    // Запрос к базе данных для поиска книги
    public Book searchBookByName(String name) {
        String query = "SELECT id, name, author FROM book WHERE name ILIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String bookName = resultSet.getString("name");
                String author = resultSet.getString("author");

                return new Book(id, bookName, author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

//    private void displayDefaultContent() {
//        // Показать GridPane с популярными книгами
//        booksGrid.setVisible(true);
//        booksGrid.setManaged(true);
//
//        // Скрыть VBox с результатами поиска
//        searchResultsBox.setVisible(false);
//        searchResultsBox.setManaged(false);
//    }

    // Метод для отображения результатов поиска
    private void displaySearchResult(Book book) {
        searchResultsBox.getChildren().clear();

        Label titleLabel = new Label("Название: " + book.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label authorLabel = new Label("Автор: " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 16px;");

        VBox bookDetailsBox = new VBox(titleLabel, authorLabel);
        bookDetailsBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #f4f4f4;");

        // Добавляем обработчик клика
        bookDetailsBox.setOnMouseClicked(event -> openBookDetailsPage(book));

        searchResultsBox.getChildren().add(bookDetailsBox);
        searchResultsBox.setVisible(true);
        searchResultsBox.setManaged(true);
    }

    private void openBookDetailsPage(Book book) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/BookDetails.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            // Получаем контроллер нового окна
            BookDetailsController1 controller = loader.getController();
            controller.setBookDetails(book.getName(), book.getAuthor(), "Описание книги будет здесь.");

            stage.setTitle("Детали книги");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayDefaultContent() {
        booksGrid.setVisible(true);
        booksGrid.setManaged(true);
        popularNowLabel.setVisible(true);
        popularNowLabel.setManaged(true);
        searchResultsBox.setVisible(false);
        searchResultsBox.setManaged(false);
    }



}