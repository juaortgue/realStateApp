package com.ortizguerra.realsapp.retrofit.services;

import com.ortizguerra.realsapp.model.CategoryResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CategoryService {

    final String BASE_URL = "/categories";

    @GET(BASE_URL)
    Call<ResponseContainer<CategoryResponse>> listCategories();

    @GET(BASE_URL+"/{id}")
    Call<CategoryResponse> getOne(@Path("id") String id);

}
