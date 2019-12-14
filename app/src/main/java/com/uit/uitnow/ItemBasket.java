package com.uit.uitnow;

public class ItemBasket extends Item {
    int quantity = 0;
    int sum = 0;
    String ghichu;
    public ItemBasket()
    {

    }
    public ItemBasket(Item item, int quantity, int sum,String ghichu) {
        this.id = item.id;
        this.name = item.name;
        this.image = item.image;
        this.price = item.price;
        this.quantity = quantity;
        this.sum = sum;
        this.ghichu=ghichu;
    }
    public ItemBasket(int quantity, int sum,String ghichu) {
        super();
        this.quantity = quantity;
        this.sum = sum;
        this.ghichu=ghichu;
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


    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

}
