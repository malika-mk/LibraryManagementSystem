package org.example.librarymanagementsystem.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.librarymanagementsystem.Models.Book;
import org.example.librarymanagementsystem.Application.HelloApplication;
import org.example.librarymanagementsystem.dao.BookDAO;
import org.example.librarymanagementsystem.dao.DatabaseConnection;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private ImageView bookImageView;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private VBox contentBox;

    private ObservableList<Book> filteredList = FXCollections.observableArrayList();

    @FXML
    private Button searchButton;

    @FXML
    private void onSearchButtonClick() {
        System.out.println("Кнопка нажата");
        String searchText = searchField.getText().trim();
        System.out.println("Текст поиска: " + searchText);

        if (!searchText.isEmpty()) {
            Book foundBook = searchBookByName(searchText);
            if (foundBook != null) {
                System.out.println("Книга найдена: " + foundBook.getName());

                booksGrid.setVisible(false);
                booksGrid.setManaged(false);

                displaySearchResult(foundBook);
            } else {
                System.out.println("Книга не найдена");

                booksGrid.setVisible(true);
                booksGrid.setManaged(true);

                displayNoResult();
            }
        } else {
            System.out.println("Поле поиска пустое");
            booksGrid.setVisible(true);
            booksGrid.setManaged(true);

            displayDefaultContent();
        }
    }

    @FXML
    public void initialize() {
        searchField.setOnAction(event -> {
            onSearch();
        });
        categoryChoiceBox.setStyle("-fx-font-size: 16px; -fx-text-fill: white;");

        categoryChoiceBox.setMinWidth(150);
        categoryChoiceBox.setMaxWidth(Double.MAX_VALUE);

        loadPopularBooks();
        categoryChoiceBox.getItems().addAll("Romance novel", "Fantasy", "Adventure fiction", "Horror");

        categoryChoiceBox.setValue("Category");

        categoryChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                displayBooksByCategory(newValue);
            }
        });

        searchField.setOnAction(event -> {
            onSearch();
        });

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) {
                booksGrid.setVisible(true);
                booksGrid.setManaged(true);
                searchResultsBox.setVisible(false);
                searchResultsBox.setManaged(false);
                popularNowLabel.setVisible(true);
            }
        });
        booksGrid.prefWidthProperty().bind(scrollPane.widthProperty());
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
    private Button Hbutton;

    @FXML
    void switchToH() throws IOException {
        Stage stage = (Stage) Hbutton.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("/org/example/librarymanagementsystem/view/AdminDashboard.fxml"));
        try {
            Scene scene = new Scene(fxmlLoader.load(), 800, 1000);
            stage.setTitle("Hello!");
            stage.setScene(scene);
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    private void onSearch() {
        String query = searchField.getText().trim();

        if (query.isEmpty()) {
            System.out.println("Поле поиска пустое");
            displayDefaultContent();
            return;
        }

        System.out.println("Выполняется поиск по запросу: " + query);

        try {
            Book result = bookDAO.searchBookByName(query);

            if (result != null) {
                System.out.println("Книга найдена: " + result.getName());

                Object imageObject = result.getImage();
                Image bookImage;
                if (imageObject instanceof byte[]) {
                    byte[] imageBytes = (byte[]) imageObject;
                    InputStream inputStream = new ByteArrayInputStream(imageBytes);
                    bookImage = new Image(inputStream);
                } else {
                    bookImage = (Image) imageObject;
                }
                result.setImage(bookImage);
                String description = bookDAO.getBookDescription(result.getId());
                result.setDescription(description);

                booksGrid.setVisible(false);
                booksGrid.setManaged(false);
                popularNowLabel.setVisible(false);
                popularNowLabel.setManaged(false);
                displaySearchResult(result, bookImage);
                loadBookDetails(result, bookImage, description);
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

    private void loadBookDetails(Book book, Image bookImage, String description) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/BookDetails.fxml"));
            Parent bookDetailsRoot = loader.load();

            BookDetailsController1 bookDetailsController = loader.getController();

            bookDetailsController.setBookDetails(
                    book.getName(),
                    book.getAuthor(),
                    description,
                    bookImage,
                    book
            );

            Stage stage = new Stage();
            stage.setTitle("Book Details");
            stage.setScene(new Scene(bookDetailsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при загрузке страницы с деталями книги: " + e.getMessage());
        }
    }

    private void displaySearchResult(Book book, javafx.scene.image.Image bookImage) {
        searchResultsBox.getChildren().clear();

        Label titleLabel = new Label("Name: " + book.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label authorLabel = new Label("Author: " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 16px;");

        ImageView bookImageView = new ImageView(bookImage);
        bookImageView.setFitWidth(200);
        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        VBox bookDetailsBox = new VBox(bookImageView, titleLabel, authorLabel);
        bookDetailsBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #F7F5D4;");
        bookDetailsBox.setSpacing(10);
        bookDetailsBox.setAlignment(Pos.CENTER);

        bookDetailsBox.setOnMouseClicked(event -> {
            System.out.println("Клик на книге: " + book.getName());
            openBookDetailsPage(book, bookImage);
        });

        searchResultsBox.getChildren().add(bookDetailsBox);
        searchResultsBox.setAlignment(Pos.CENTER);
        searchResultsBox.setVisible(true);
        searchResultsBox.setManaged(true);
    }

    private void displayNoResult() {
        searchResultsBox.getChildren().clear();
        Label noResultLabel = new Label("Книга не найдена.");
        noResultLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: red;");

        searchResultsBox.getChildren().add(noResultLabel);
        booksGrid.setVisible(false);
        booksGrid.setManaged(false);
        searchResultsBox.setVisible(true);
        searchResultsBox.setManaged(true);
    }

    public Book searchBookByName(String name) {
        String query = "SELECT id, name, author, description, image, imagepath, isbn FROM public.book WHERE name ILIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String bookName = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                byte[] imageBytes = resultSet.getBytes("image");
                String imagePath = resultSet.getString("imagepath");
                String isbn = resultSet.getString("isbn");

                Image bookImage = null;
                if (imageBytes != null) {
                    bookImage = new Image(new ByteArrayInputStream(imageBytes));
                } else if (imagePath != null && !imagePath.isEmpty()) {
                    try {
                        bookImage = new Image("file:" + imagePath);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Ошибка загрузки изображения: " + e.getMessage());
                        bookImage = new Image("file:/path/to/default/image.png");
                    }
                } else {
                    bookImage = new Image("file:/path/to/default/image.png");
                }

                return new Book(id, bookName, author, description, bookImage, isbn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void displaySearchResult(Book book) {
        searchResultsBox.getChildren().clear();

        Label titleLabel = new Label("Название: " + book.getName());
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label authorLabel = new Label("Автор: " + book.getAuthor());
        authorLabel.setStyle("-fx-font-size: 16px;");

        javafx.scene.image.Image bookImage = book.getImage();

        ImageView bookImageView = new ImageView(bookImage);
        bookImageView.setFitWidth(200);
        bookImageView.setFitHeight(300);
        bookImageView.setPreserveRatio(true);

        VBox bookDetailsBox = new VBox(bookImageView, titleLabel, authorLabel);
        bookDetailsBox.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #F7F5D4;");
        bookDetailsBox.setSpacing(10);
        bookDetailsBox.setAlignment(Pos.CENTER);

        bookDetailsBox.setOnMouseClicked(event -> {
            System.out.println("Клик на книге: " + book.getName());
            openBookDetailsPage(book, bookImage);
        });

        searchResultsBox.getChildren().add(bookDetailsBox);
        searchResultsBox.setAlignment(Pos.CENTER);
        searchResultsBox.setVisible(true);
        searchResultsBox.setManaged(true);
    }


    private void openBookDetailsPage(Book book, Image bookImage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/BookDetails.fxml"));
            Parent bookDetailsRoot = loader.load();
            BookDetailsController1 controller = loader.getController();
            controller.setBookDetails(
                    book.getName(),
                    book.getAuthor(),
                    book.getDescription(),
                    bookImage,
                    book
            );
            Stage stage = new Stage();
            stage.setTitle("Book Details");
            stage.setScene(new Scene(bookDetailsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при открытии страницы с деталями книги: " + e.getMessage());
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

    @FXML private Button addBookButton;

    @FXML
    private void openAddBookWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/NewBook.fxml"));
            Scene scene = new Scene(loader.load());

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("Добавить книгу");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openBookDetailsPage(Book book) {
        try {
            javafx.scene.image.Image bookImage;
            if (book.getImage() instanceof javafx.scene.image.Image) {
                bookImage = (javafx.scene.image.Image) book.getImage();
            } else {
                bookImage = new javafx.scene.image.Image("/path/to/default/image.png");
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/librarymanagementsystem/view/BookDetails.fxml"));
            Parent bookDetailsRoot = loader.load();
            BookDetailsController1 controller = loader.getController();
            controller.setBook(book);
            controller.setBookDetails(book.getName(), book.getAuthor(), book.getDescription(), book.getImage(), book);

            Stage stage = new Stage();
            stage.setTitle("Book Details");
            stage.setScene(new Scene(bookDetailsRoot));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Ошибка при открытии страницы с деталями книги: " + e.getMessage());
        }
    }

    private void displayBooksByCategory(String category) {
        booksGrid.getChildren().clear();

        ObservableList<Book> books = bookDAO.getBooksByCategory(category);

        int column = 0;
        int row = 0;

        for (Book book : books) {
            VBox bookItem = createBookItem(book);
            booksGrid.add(bookItem, column, row);

            column++;
            if (column == 4) {
                column = 0;
                row++;
            }
        }
    }
    private VBox createBookItem(Book book) {
        ImageView bookImageView = new ImageView();
        bookImageView.setImage(book.getImage());
        bookImageView.setFitWidth(100);
        bookImageView.setFitHeight(150);
        bookImageView.setPreserveRatio(true);

        Label bookTitleLabel = new Label(book.getName());
        bookTitleLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");

        Button viewButton = new Button("View");
        viewButton.setOnAction(event -> openBookDetailsPage(book));

        VBox bookItem = new VBox(bookImageView, bookTitleLabel, viewButton);
        bookItem.setSpacing(5);
        bookItem.setAlignment(Pos.CENTER);
        bookItem.setStyle("-fx-padding: 10px; -fx-border-color: black; -fx-border-width: 1; -fx-background-color: #F7F5D4;");

        return bookItem;

    }

    @FXML
    private void goToHome() {
        booksGrid.setVisible(true);
        booksGrid.setManaged(true);
        popularNowLabel.setVisible(true);
        popularNowLabel.setManaged(true);

        searchResultsBox.getChildren().clear();
        loadPopularBooks();

        System.out.println("Home button clicked and content set to Popular Now");
    }

    private void loadPopularBooks() {
        booksGrid.getChildren().clear();
        ObservableList<Book> popularBooks = bookDAO.getPopularBooks();

        int column = 0;
        int row = 0;

        for (Book book : popularBooks) {
            VBox bookItem = createBookItem(book);
            booksGrid.add(bookItem, column++, row);

            if (column == 4) {
                column = 0;
                row++;
            }
        }
    }

    public ObservableList<Book> getPopularBooks() {
        String query = "SELECT * FROM public.book ORDER BY likes DESC LIMIT 10";
        ObservableList<Book> popularBooks = FXCollections.observableArrayList();
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String author = resultSet.getString("author");
                String description = resultSet.getString("description");
                int likes = resultSet.getInt("likes");
                int sales = resultSet.getInt("sales");
                byte[] imageBytes = resultSet.getBytes("image");

                Image image = null;
                if (imageBytes != null) {
                    image = new Image(new ByteArrayInputStream(imageBytes));
                }
                popularBooks.add(new Book(id, name, author, description, image, likes, sales));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return popularBooks;
    }





}