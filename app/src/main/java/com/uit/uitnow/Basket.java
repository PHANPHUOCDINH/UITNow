package com.uit.uitnow;

import java.util.HashMap;

public class Basket {
    HashMap<String, ItemBasket> items;
    int totalPrice;
    int totalItem;

    public Basket() {
        items = new HashMap<>();
        totalPrice = 0;
        totalItem = 0;
    }

    public void addItem(ItemBasket item) { // 1
        items.put(item.id, item);
    }

    public ItemBasket getItem(String id) { // 2
        return items.get(id);
    }

    public void calculateBasket() { // 3
        totalPrice = 0;
        totalItem = 0;
        for (ItemBasket itemBasket : items.values()) {
            totalPrice += itemBasket.price * itemBasket.quantity;
            totalItem += itemBasket.quantity;
        }
    }

    public String getTotalPrice() { // 4
        return totalPrice + " VND";
    }

    public int getTotalItem() { // 5
        return totalItem;
    }

    public HashMap<String, ItemBasket> getItems() {
        return items;
    }
}
