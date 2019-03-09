package com.ortizguerra.realsapp.retrofit.services;

import com.ortizguerra.realsapp.dto.CreatePropertyDto;
import com.ortizguerra.realsapp.dto.EditPropertyDto;
import com.ortizguerra.realsapp.model.AddFavResponse;
import com.ortizguerra.realsapp.model.CreatePropertyResponse;
import com.ortizguerra.realsapp.model.EditPropertyResponse;
import com.ortizguerra.realsapp.model.MyPropertyResponse;
import com.ortizguerra.realsapp.model.PropertyOneResponse;
import com.ortizguerra.realsapp.model.PropertyResponse;
import com.ortizguerra.realsapp.model.ResponseContainer;
import com.ortizguerra.realsapp.model.ResponseContainerOneRow;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PropertyService {
    final String BASE_URL = "/properties";

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties();
    @GET(BASE_URL + "/auth")
    Call<ResponseContainer<PropertyResponse>> listFavsProperties();

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listGeo(@QueryMap Map<String, String> options);

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listProperties(@QueryMap Map<String, String> options);

    @GET(BASE_URL)
    Call<ResponseContainer<PropertyResponse>> listGeo(@Query("near") String near);

    @GET(BASE_URL + "/mine")
    Call<ResponseContainer<MyPropertyResponse>> getMine();

    @GET(BASE_URL + "/fav")
    Call<ResponseContainer<PropertyResponse>> getFavs();

    @GET(BASE_URL + "/{id}")
    Call<ResponseContainerOneRow<PropertyOneResponse>> getOne(@Path("id") String id);

    @POST(BASE_URL)
    Call<CreatePropertyResponse> create (@Body CreatePropertyDto property);

    @POST(BASE_URL+"/fav/{id}")
    Call<AddFavResponse> addFav (@Path("id") String id);

    /*@PUT(BASE_URL + "/{id}")
    Call<PropertyResponse> edit(@Path("id") String id, @Body PropertyResponse edited);*/
    @PUT(BASE_URL + "/{id}")
    Call<EditPropertyResponse> edit(@Path("id") String id, @Body EditPropertyDto edited);

    @DELETE(BASE_URL + "/{id}")
    Call<MyPropertyResponse> delete(@Path("id") String id);

    @DELETE(BASE_URL + "/fav/{id}")
    Call<AddFavResponse> deleteFav(@Path("id") String id);

}
