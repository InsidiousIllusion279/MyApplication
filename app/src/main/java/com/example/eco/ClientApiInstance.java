package com.example.eco;


import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientApiInstance {

    private static final String BASE_URL ="https://pixabay.com/" ;
    private static Retrofit retrofit1 =null;

    public static Retrofit getInstance()
    {
        if(retrofit1 == null)
        {
            retrofit1 = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit1;
    }

}
