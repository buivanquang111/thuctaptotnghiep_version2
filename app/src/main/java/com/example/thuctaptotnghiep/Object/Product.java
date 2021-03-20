package com.example.thuctaptotnghiep.Object;

import java.io.Serializable;

public class Product implements Serializable {

    public int Id;
    public String title;
    public String description;
    public String image;
    public String price;
    public  int soluong;

    public Product(int id,String title, String description, String image, String price, int soluong) {
        this.Id=id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.price = price;
        this.soluong = soluong;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
