package org.example.librarymanagementsystem.dao;

import org.example.librarymanagementsystem.Models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {
    // Метод для поиска книги по имени
    public Book searchBookByName(String name) {
        String query = "SELECT name, author FROM public.book WHERE name ILIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, "%" + name + "%");
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String bookName = resultSet.getString("name");
                String author = resultSet.getString("author");
                return new Book(bookName, author); // Убедитесь, что у вас есть класс Book с полями name и author
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Если книга не найдена, возвращаем null
    }
}