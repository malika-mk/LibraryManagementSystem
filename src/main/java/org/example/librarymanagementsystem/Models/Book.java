package org.example.librarymanagementsystem.Models;

import javafx.scene.image.Image;

public class Book {
    private int id;
    private String name;
    private String author;
    private String description; // Новое поле для описания
    private Image image;  // Поле для изображения

    // Конструктор без изображения
    public Book(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image = null;  // Изображение по умолчанию не задано
    }

    // Конструктор с изображением
    public Book(int id, String name, String author, Image image) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image = image;
    }

    // Конструктор с описанием и изображением
    public Book(int id, String name, String author, String description, Image image) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.image = image;
    }

    // Геттеры и сеттеры
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getDescription() {
        return description; // Геттер для описания
    }

    public void setDescription(String description) {
        this.description = description; // Сеттер для описания
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}