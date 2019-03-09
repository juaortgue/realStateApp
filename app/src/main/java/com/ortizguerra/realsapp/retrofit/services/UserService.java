package com.ortizguerra.realsapp.retrofit.services;

import com.ortizguerra.realsapp.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface UserService {

    @GET("/users/me")
    Call<UserResponse> getMe();
}
