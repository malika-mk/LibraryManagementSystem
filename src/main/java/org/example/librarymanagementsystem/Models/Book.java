package org.example.librarymanagementsystem.Models;

import javafx.scene.image.Image;

public class Book {
    private int id;
    private String name;
    private String author;
    private Image image;

    public Book(int id, String name, String author) {
        this.id = id;
        this.name = name;
        this.author = author;
    }

    public Book(int id, String name, String author, Image image) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}