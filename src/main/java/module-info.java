module org.example.librarymanagementsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;


    opens org.example.librarymanagementsystem to javafx.fxml;
    exports org.example.librarymanagementsystem;
    exports org.example.librarymanagementsystem.Controllers;
    opens org.example.librarymanagementsystem.Controllers to javafx.fxml;
    exports org.example.librarymanagementsystem.Models;
    opens org.example.librarymanagementsystem.Models to javafx.fxml;
    exports org.example.librarymanagementsystem.dao;
    opens org.example.librarymanagementsystem.dao to javafx.fxml;
    exports org.example.librarymanagementsystem.Application;
    opens org.example.librarymanagementsystem.Application to javafx.fxml;
}