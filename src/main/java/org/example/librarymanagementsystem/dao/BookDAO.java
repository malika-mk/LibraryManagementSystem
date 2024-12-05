package org.example.librarymanagementsystem.dao;

import org.example.librarymanagementsystem.Models.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BookDAO {
    // Метод для поиска книги по имени
    public Book searchBookByName(String name) {
        String query = "SELECT id, name, author FROM public.book WHERE name ILIKE ?";
        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "%" + name + "%");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id"); // Получаем id
                String bookName = resultSet.getString("name");
                String author = resultSet.getString("author");

                // Возвращаем объект Book с id, name и author
                return new Book(id, bookName, author);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Если книга не найдена, возвращаем null
    }
}