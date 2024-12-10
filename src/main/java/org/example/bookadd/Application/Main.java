package org.example.bookadd.Application;

import java.sql.Connection;
import java.sql.SQLException;
import org.example.bookadd.Dao.DbConnection;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DbConnection.getConnection()) {
            if (connection != null) {
                System.out.println("Connection established!");
            } else {
                System.out.println("Failed to connect!");
            }
        } catch (SQLException e) {
            System.out.println("Error while connecting to the database: " + e.getMessage());
            e.printStackTrace(); // Для отладки, вывод полного стека ошибок
        }
    }
}
