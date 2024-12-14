package org.example.librarymanagementsystem.dao;

import org.example.librarymanagementsystem.Models.Category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // Метод для получения всех категорий
    public List<Category> getAllCategories() throws Exception {
        List<Category> categories = new ArrayList<>();
        Connection connection = DatabaseConnection.getConnection();
        String sql = "SELECT id, categoryname FROM category";

        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            categories.add(new Category(rs.getInt("id"), rs.getString("categoryname")));
        }

        return categories;
    }
}