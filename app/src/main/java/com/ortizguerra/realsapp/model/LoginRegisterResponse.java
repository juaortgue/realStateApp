package com.ortizguerra.realsapp.model;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginRegisterResponse {

    @SerializedName("token")
    @Expose
    private String token;
    @SerializedName("user")
    @Expose
    private UserResponse user;


    public LoginRegisterResponse() {
    }

    /**
     * @param token
     * @param user
     */
    public LoginRegisterResponse(String token, UserResponse user) {
        this.token = token;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginRegisterResponse that = (LoginRegisterResponse) o;

        if (!token.equals(that.token)) return false;
        return user.equals(that.user);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        int result = token.hashCode();
        result = 31 * result + user.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "LoginRegisterResponse{" +
                "token='" + token + '\'' +
                ", user=" + user +
                '}';
    }
}
