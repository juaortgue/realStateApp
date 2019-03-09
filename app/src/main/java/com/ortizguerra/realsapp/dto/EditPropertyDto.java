package com.ortizguerra.realsapp.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EditPropertyDto implements Serializable {
    //private String id;
    //private String ownerId;
    private String title;
    private String description;
    private Long price;
    private Long rooms;
    private Long size;
    private String categoryId;
    private String address;
    private String zipcode;
    private String city;
    private String province;
    private String loc;
    //private List<String> photos = new ArrayList<>();


    public EditPropertyDto() {
    }

    public EditPropertyDto(String title, String description, Long price, Long rooms, Long size, String categoryId, String address, String zipcode, String city, String province, String loc) {
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

    public Long getRooms() {
        return rooms;
    }

    public void setRooms(Long rooms) {
        this.rooms = rooms;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
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
}
