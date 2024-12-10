module org.example.bookadd {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.bookadd to javafx.fxml;
    opens org.example.bookadd.Controllers to javafx.fxml;

    exports org.example.bookadd;
    exports org.example.bookadd.Controllers;
}
