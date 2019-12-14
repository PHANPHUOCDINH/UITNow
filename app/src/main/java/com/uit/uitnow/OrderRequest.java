package com.uit.uitnow;

import com.google.firebase.firestore.GeoPoint;

public class OrderRequest {
    public String id;
    public String userId;
    public String userName;
    public GeoPoint userLocation;
    public String userAddress;
    public String storeName;
    public GeoPoint storeLocation;
    public String storeAddress;
    public String driverId;
    public String driverName;
    public GeoPoint driverLocation;
    public String total;
    public int status = OrderRequestStatus.REQUESTING;

    public OrderRequest() {
        super();
    }

    public OrderRequest(String id, String userId, String userName, GeoPoint userLocation, String userAddress, String storeName, GeoPoint storeLocation, String storeAddress, String driverId,String driverName, GeoPoint driverLocation, String total, int status) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.userLocation = userLocation;
        this.userAddress = userAddress;
        this.storeName = storeName;
        this.storeLocation = storeLocation;
        this.storeAddress = storeAddress;
        this.driverId = driverId;
        this.driverName=driverName;
        this.driverLocation = driverLocation;
        this.total = total;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public GeoPoint getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(GeoPoint userLocation) {
        this.userLocation = userLocation;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public GeoPoint getStoreLocation() {
        return storeLocation;
    }

    public void setStoreLocation(GeoPoint storeLocation) {
        this.storeLocation = storeLocation;
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public GeoPoint getDriverLocation() {
        return driverLocation;
    }

    public void setDriverLocation(GeoPoint driverLocation) {
        this.driverLocation = driverLocation;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
}
