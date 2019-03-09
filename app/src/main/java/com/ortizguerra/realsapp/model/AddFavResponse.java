package com.ortizguerra.realsapp.model;

import java.util.List;

/*"role":"admin","favs":["5c73b3ca8f58ae001749df7e"],"
keywords":["admin gmail com","admin"],"_id":"5c7038e58183aa3c5878d445",
"picture":"https://gravatar.com/avatar/75d23af433e0cea4c0e45a56dba18b30?d=identicon",
"name":"admin","email":"admin@gmail.com","password":"$2b$09$.w0go/AwJmOM34rkMRew1OPjlEeNOY7RgXjBq71u30zmEi9fEjFji","
createdAt":"2019-02-22T18:01:09.879Z","updatedAt":"2019-02-26T09:39:50.316Z","__v":0}
    <-- END HTTP (410-byte body)
*/
public class AddFavResponse {
    private String role;
    private List<String> favs;
    private List<String> keywords;
    private String _id;
    private String picture;
    private String name;
    private String password;
    private String createdAt;
    private String updatedAt;

    public AddFavResponse() {
    }

    public AddFavResponse(String role, List<String> favs, List<String> keywords, String _id, String picture, String name, String password, String createdAt, String updatedAt) {
        this.role = role;
        this.favs = favs;
        this.keywords = keywords;
        this._id = _id;
        this.picture = picture;
        this.name = name;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<String> getFavs() {
        return favs;
    }

    public void setFavs(List<String> favs) {
        this.favs = favs;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "AddFavResponse{" +
                "role='" + role + '\'' +
                ", favs=" + favs +
                ", keywords=" + keywords +
                ", _id='" + _id + '\'' +
                ", picture='" + picture + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
