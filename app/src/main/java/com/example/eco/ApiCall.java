package com.example.eco;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiCall {
/*
    @GET("api/?key=13269434-7c292aebf90e99a9e1a573256&q={name}&image_type=photo&pretty=true")


    Call<ImageInfo> getImageData(@Path("name") String imageName);


    *//* Call<>getWeatherData(
            @Query("lat") float lat,
            @Query("lon") float lon,
            @Query("appid") String appid

    );*/

    @GET("api")
    Call<ImageInfo> getImageData(
            @Query("key") String key,
            @Query("q") String q,
            @Query("image_type") String image_type,
            @Query("pretty") boolean pretty


    );


}

