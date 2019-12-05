package com.uit.uitnow;

public class ItemBasket extends Item {
    int quantity = 0;
    int sum = 0;

    public ItemBasket(Item item, int quantity, int sum) {
        this.id = item.id;
        this.name = item.name;
        this.image = item.image;
        this.price = item.price;
        this.quantity = quantity;
        this.sum = sum;
    }

    public void increase() {
        quantity++;
        sum = price * quantity;
    }

    public void decrease() {
        if (quantity > 0) {
            quantity--;
            sum = price * quantity;
        }
    }

    public String getSum() {
        return sum + " VND";
    }
    public String getQuantityStr() {
        return quantity + "";
    }
}
