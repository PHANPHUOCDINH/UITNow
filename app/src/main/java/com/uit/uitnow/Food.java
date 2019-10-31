package com.uit.uitnow;

public class Food {
    public int id;
    public String name;
    public String imageUrl;
    public int price;

    public Food() {
    }

    public Food(int id,String name, String imageUrl, int price) {
        this.id=id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return imageUrl;
    }

    public void setImage(String image) {
        this.imageUrl = image;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
