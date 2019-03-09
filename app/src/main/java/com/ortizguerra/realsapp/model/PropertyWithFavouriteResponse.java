package com.ortizguerra.realsapp.model;

import java.util.ArrayList;
import java.util.List;

public class PropertyWithFavouriteResponse {
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
}
