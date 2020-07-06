package com.uit.uitnow;

import com.google.firebase.firestore.GeoPoint;

public class Booking {
    String id;
    String idKhachHang;
    String idBooking;
    String trangThai;
    String resName;
    String time;
    String date;
    String resAddress;
    int numTreEm;
    int numNguoiLon;
    String ghiChu;
    String cusPhoneNum;
    String cusName;
    String url;
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCusPhoneNum() {
        return cusPhoneNum;
    }

    public void setCusPhoneNum(String cusPhoneNum) {
        this.cusPhoneNum = cusPhoneNum;
    }

    public String getCusName() {
        return cusName;
    }

    public void setCusName(String cusName) {
        this.cusName = cusName;
    }

    public String getPhoneNum() {
        return cusPhoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.cusPhoneNum = phoneNum;
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public Booking()
    {

    }

    public Booking(String id, String idKhachHang, String idBooking, String trangThai, String resName, String thoiGian, String resAddress) {
        this.id = id;
        this.idKhachHang = idKhachHang;
        this.idBooking = idBooking;
        this.trangThai = trangThai;
        this.resName = resName;
        this.time = thoiGian;
        this.resAddress = resAddress;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdKhachHang() {
        return idKhachHang;
    }

    public void setIdKhachHang(String idKhachHang) {
        this.idKhachHang = idKhachHang;
    }

    public String getIdBooking() {
        return idBooking;
    }

    public void setIdBooking(String idBooking) {
        this.idBooking = idBooking;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    public String getResName() {
        return resName;
    }

    public void setResName(String resName) {
        this.resName = resName;
    }

    public String getResAddress() {
        return resAddress;
    }

    public void setResAddress(String resAddress) {
        this.resAddress = resAddress;
    }

    public int getNumTreEm() {
        return numTreEm;
    }

    public void setNumTreEm(int numTreEm) {
        this.numTreEm = numTreEm;
    }

    public int getNumNguoiLon() {
        return numNguoiLon;
    }

    public void setNumNguoiLon(int numNguoiLon) {
        this.numNguoiLon = numNguoiLon;
    }
}
