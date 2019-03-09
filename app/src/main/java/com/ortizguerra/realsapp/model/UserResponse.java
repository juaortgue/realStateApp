package com.ortizguerra.realsapp.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.Serializable;
import java.util.Objects;
@SuppressWarnings("serial")
public class UserResponse implements Serializable {
    private String id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String picture;
    public UserResponse() {

    }

    public UserResponse(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserResponse(String _id, String email, String password, String name, String role, String picture) {
        this.id = _id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
        this.picture = picture;
    }

    public String get_id() {
        return id;
    }

    public void set_id(String _id) {
        this.id = _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "UserResponse{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
