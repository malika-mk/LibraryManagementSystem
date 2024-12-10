package org.example.bookadd.Model;


public class Book {
    private String title;
    private String author;
    private String description;
    private String genre;
    private double price;

    // Конструктор
    public Book(String title, String author, String description, String genre, double price) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.genre = genre;
        this.price = price;
    }

    // Геттеры и сеттеры
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}