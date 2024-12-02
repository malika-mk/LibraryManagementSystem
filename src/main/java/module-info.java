module org.example.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.librarymanagementsystem to javafx.fxml;
    exports org.example.librarymanagementsystem;
}