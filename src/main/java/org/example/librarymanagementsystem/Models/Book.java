package org.example.librarymanagementsystem.Models;

import javafx.scene.image.Image;

import java.io.ByteArrayInputStream;

public class Book {
    private int id;
    private String name;
    private String author;
    private String description;
    private byte[] imageBytes; // Бинарные данные изображения
    private Image image;       // JavaFX Image
    private String isbn;

    // Конструктор с byte[]
    public Book(int id, String name, String author, String description, byte[] imageBytes, String isbn) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imageBytes = imageBytes;
        this.isbn = isbn;
        if (imageBytes != null) {
            this.image = new Image(new ByteArrayInputStream(imageBytes));
        }
    }

    // Конструктор с Image
    public Book(int id, String name, String author, String description, Image image, String isbn) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.image = image;
        this.isbn = isbn;
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
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(byte[] imageBytes) {
        this.imageBytes = imageBytes;
        if (imageBytes != null) {
            this.image = new Image(new ByteArrayInputStream(imageBytes));
        }
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}