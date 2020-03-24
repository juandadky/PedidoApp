package com.dadky.pedidoapp.Service;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {

    private static Retrofit retrofit = null;
    private final static String BASE_URL = "http://192.168.0.26:5000/";

    private static Retrofit getClient(){

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static IServiceApi getServiceApi(){
        return getClient().create(IServiceApi.class);
    }
}
