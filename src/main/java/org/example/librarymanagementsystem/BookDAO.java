package org.example.librarymanagementsystem;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    public List<String> getAllBooks() {
        List<String> books = new ArrayList<>();
        String query = "SELECT name FROM Book";

        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                books.add(resultSet.getString("name"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }
}
