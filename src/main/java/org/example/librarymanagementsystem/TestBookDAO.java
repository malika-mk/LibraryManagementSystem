package org.example.librarymanagementsystem;

import java.util.List;

public class TestBookDAO {
    public static void main(String[] args) {
        BookDAO bookDAO = new BookDAO();
        List<String> books = bookDAO.getAllBooks();
        books.forEach(System.out::println);
    }
}
