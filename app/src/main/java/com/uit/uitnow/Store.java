package com.uit.uitnow;

import java.util.ArrayList;

public class Store  {
    String id;
    String name;
    String logoUrl;
    String address;
    String openHour;
    ArrayList<Food> menu;
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

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOpenHour() {
        return "Open Hours: " + openHour;
    }

    public void setOpenHour(String openHour) {
        this.openHour = openHour;
    }

    public ArrayList<Food> getMenu() {
        return menu;
    }

    public void setMenu(ArrayList<Food> menu) {
        this.menu = menu;
    }
}
