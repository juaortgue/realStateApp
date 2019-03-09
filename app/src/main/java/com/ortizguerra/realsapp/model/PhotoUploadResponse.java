package com.ortizguerra.realsapp.model;

public class PhotoUploadResponse {
    private String id;
    private String propertyId;
    private String imgurLink;
    private String deletehash;

    public PhotoUploadResponse(String id, String propertyId, String imgurlink, String deletehash) {
        this.id = id;
        this.propertyId = propertyId;
        this.imgurLink = imgurlink;
        this.deletehash = deletehash;
    }

    public PhotoUploadResponse() {

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
