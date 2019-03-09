package com.ortizguerra.realsapp.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@SuppressWarnings("serial")
public class PropertyResponse implements Serializable {

    /*private String id;
    private UserResponse ownerId;
    private String title;
    private String description;
    private double price;
    private int rooms;
    private CategoryResponse categoryId;
    private String address;
    private String zipcode;
    private String city;
    private String province;
    private String loc;
    private String createdAt;
    private String updatedAt;
    private int size;*/
    private String id;
    private UserResponse ownerId;
    private String title;
    private String description;
    private double price;
    private int rooms;
    private float size;
    private CategoryResponse categoryId;
    private String address;
    private String zipcode;
    private String city;
    private String province;
    private String loc;
    private List<String> favs = new ArrayList<>();
    private List<String> photos = new ArrayList<>();
    private boolean isFav;

    public PropertyResponse() {

    }

    public PropertyResponse(String id, UserResponse ownerId, String title, String description, double price, int rooms, float size, CategoryResponse categoryId, String address, String zipcode, String city, String province, String loc, List<String> favs, List<String> photos, boolean isFav) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.rooms = rooms;
        this.size = size;
        this.categoryId = categoryId;
        this.address = address;
        this.zipcode = zipcode;
        this.city = city;
        this.province = province;
        this.loc = loc;
        this.favs = favs;
        this.photos = photos;
        this.isFav = isFav;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public UserResponse getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(UserResponse ownerId) {
        this.ownerId = ownerId;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public float getSize() {
        return size;
    }

    public void setSize(float size) {
        this.size = size;
    }

    public CategoryResponse getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(CategoryResponse categoryId) {
        this.categoryId = categoryId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public List<String> getFavs() {
        return favs;
    }

    public void setFavs(List<String> favs) {
        this.favs = favs;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public boolean isFav() {
        return isFav;
    }

    public void setFav(boolean fav) {
        isFav = fav;
    }
}
