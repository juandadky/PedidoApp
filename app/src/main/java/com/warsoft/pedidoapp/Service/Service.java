package com.warsoft.pedidoapp.Service;

import com.warsoft.pedidoapp.Utils.Util;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Service {

    public static final String VPS = "http://159.65.168.57:5000/";
    private static Retrofit retrofit = null;
    private static final String BASE_URL = VPS;
//    private final static String BASE_URL = Util.LOCALHOST;

    private static synchronized Retrofit getClient(){

        if(retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(obtenerCliente())
                    .build();
        }

        return retrofit;
    }

    private static  OkHttpClient obtenerCliente(){
        return new OkHttpClient.Builder()
                .readTimeout(100, TimeUnit.SECONDS)
                .connectTimeout(100,TimeUnit.SECONDS)
                .build();
    }

    public static IServiceApi getServiceApi(){
        return getClient().create(IServiceApi.class);
    }
}
