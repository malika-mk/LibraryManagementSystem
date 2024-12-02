package org.example.librarymanagementsystem;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Failed to connect!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
