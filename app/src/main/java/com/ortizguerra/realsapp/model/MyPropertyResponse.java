package com.ortizguerra.realsapp.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyPropertyResponse implements Serializable {
    private String id;
    private String ownerId;
    private String title;
    private String description;
    private Long price;
    private int rooms;
    private Long size;
    private CategoryResponse categoryId;
    private String address;
    private String zipcode;
    private String city;
    private String province;
    private String loc;
    private List<String> photos = new ArrayList<>();
    public MyPropertyResponse() {

    }

    public MyPropertyResponse(String id, String ownerId, String title, String description, Long price, int rooms, Long size, CategoryResponse categoryId, String address, String zipcode, String city, String province, String loc, List<String> photos) {
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
        this.photos = photos;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
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

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
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

    public int getRooms() {
        return rooms;
    }

    public void setRooms(int rooms) {
        this.rooms = rooms;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }
}
