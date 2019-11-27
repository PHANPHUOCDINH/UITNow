package com.uit.uitnow;

public class Item {

    public String id;
    public String name;
    public String image;
    public int price;

    public Item() {
    }

    public Item(String id,String name, String image, int price) {
        this.id=id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public int getPrice() {
        return price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
