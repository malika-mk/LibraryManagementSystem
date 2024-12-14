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
    private int likes; // Количество лайков
    private double price;
    private String imagePath;
    private long totalSales;// Путь к изображению// Цена
    private int sales;
    private String value;
    private int categoryId;


    public Book(String name, String author, String description, int categoryId, double price, String imagePath, String isbn) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.imagePath = imagePath;
        this.isbn = isbn; // Установите значение isbn
    }

    // Добавьте конструктор
    public Book(int id, String name, String author, String description, Image image, int likes, int sales) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.image = image;
        this.likes = likes;
        this.sales = sales;
    }

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

    // Конструктор для добавления книги
    public Book(String name, String author, String description, int categoryId, double price, String imagePath) {
        this.name = name;
        this.author = author;
        this.description = description;
        this.categoryId = categoryId;
        this.price = price;
        this.imagePath = imagePath;
    }

    public Book(int id, String name, String author, String description, String imagePath, double price, int likes, long totalSales) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
        this.likes = likes;
        this.totalSales = totalSales;
    }

    public Book(String name, String author, String description, String imagePath, double price, String category) {
        this.category = category;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imagePath = imagePath;
        this.price = price;
    }

    public Book(int id, String name, String author,double price) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.price = price;
    }

    public Book(int id, String name, String author, String description, Image image, String isbn, double price, int sales) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.image = image;
        this.isbn = isbn;
        this.price = price;
        this.sales = sales;
    }

    public Book(int id, String name, String author, String description, String imagePath, String isbn, double price, int likes, String category) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imagePath = imagePath; // Используем imagePath
        this.isbn = isbn;
        this.price = price;
        this.likes = likes;
        this.category = category;  // Инициализируем категорию
    }

    public Book(int id, String name, String author, String description, String imagePath, String isbn, double price, int likes) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.imagePath = imagePath; // Устанавливаем путь к изображению
        this.isbn = isbn;
        this.price = price;
        this.likes = likes;
    }

    public Book(int id, String name, String author, String description, Image image, String isbn, int likes, int sales) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.description = description;
        this.image = image;
        this.isbn = isbn;
        this.likes = likes;
        this.sales = sales;
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


    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }

    private String category; // Для категории

    // Конструктор, геттеры и сеттеры для category
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getSales() {
        return sales;
    }

    public void setSales(int sales) {
        this.sales = sales;
    }

    public long getTotalSales() {
        return totalSales;
    }

    public void setTotalSales(long totalSales) {
        this.totalSales = totalSales;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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