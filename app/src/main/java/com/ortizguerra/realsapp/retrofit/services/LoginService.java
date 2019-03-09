package com.ortizguerra.realsapp.retrofit.services;


import com.ortizguerra.realsapp.model.LoginRegisterResponse;
import com.ortizguerra.realsapp.model.UserResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/auth")
    Call<LoginRegisterResponse> doLogin(@Header("Authorization") String authorization);
    @POST("/users")
    Call<LoginRegisterResponse> doRegister(@Body UserResponse signedUpUser);



}

