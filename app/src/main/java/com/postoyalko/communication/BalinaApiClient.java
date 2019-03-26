package com.postoyalko.communication;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by aleksei on 20.03.2019.
 */

public class BalinaApiClient {
    public static final String BASE_URL = "http://junior.balinasoft.com";
    private static Retrofit retrofit = null;
    private static BalinaApi balinaApi = null;
    public static final int BAD_REQUEST = 400;
    public static final int SUCCESS = 200;
    public static final String LOGIN_ALREADY_USE_MESSAGE = "Login already use";
    public static final String SUCCESS_MESSAGE = "Registration success";
    public static final String ERROR_MESSAGE = "Registration error";


    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static BalinaApi getApi(){
        if (balinaApi == null) {
            balinaApi = getClient().create(BalinaApi.class);
        }
        return balinaApi;
    }
}
