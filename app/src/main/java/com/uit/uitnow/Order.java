package com.uit.uitnow;

import com.google.android.gms.maps.model.LatLng;

public class Order {
    String id;
    String idKhachHang;
    String trangThai;
    Basket basket;
    String deliveryAddress;
    String storeName;
    String tongGia;
    LatLng deliveryLoccation;
    LatLng storeLocation;
    public Order()
    {

    }
    public Order(String id,String idKhachHang,Basket basket, String deliveryAddress, String storeName, LatLng
            deliveryLoccation, LatLng storeLocation) {
        this.id=id;
        this.idKhachHang=idKhachHang;
        this.basket = basket;
        this.deliveryAddress = deliveryAddress;
        this.storeName = storeName;
        this.deliveryLoccation = deliveryLoccation;
        this.storeLocation = storeLocation;
        this.trangThai="Booking";
    }

    public String getTongGia() {
        return tongGia;
    }

    public void setTongGia(String tongGia) {
        this.tongGia = tongGia;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public Basket getBasket() {
        return basket;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public LatLng getDeliveryLoccation() {
        return deliveryLoccation;
    }

    public void setDeliveryLoccation(LatLng deliveryLoccation) {
        this.deliveryLoccation = deliveryLoccation;
    }

    public LatLng getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(LatLng storeLocation) {
        this.storeLocation = storeLocation;
    }
}
