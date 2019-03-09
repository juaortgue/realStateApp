package com.ortizguerra.realsapp.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Objects;

public class PhotoResponse {
    private String id;
    private String propertyId;
    private String imgurLink;
    private String deletehash;

    public PhotoResponse() {

    }

    public PhotoResponse(String propertyId, String imgurlink, String deletehash) {
        this.propertyId = propertyId;
        this.imgurLink = imgurlink;
        this.deletehash = deletehash;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(String propertyId) {
        this.propertyId = propertyId;
    }

    public String getImgurlink() {
        return imgurLink;
    }

    public void setImgurlink(String imgurlink) {
        this.imgurLink = imgurlink;
    }

    public String getDeletehash() {
        return deletehash;
    }

    public void setDeletehash(String deletehash) {
        this.deletehash = deletehash;
    }
}
