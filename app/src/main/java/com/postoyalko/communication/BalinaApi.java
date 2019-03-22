package com.postoyalko.communication;

/**
 * Created by aleksei on 20.03.2019.
 */

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BalinaApi {
    @Headers("Accept: application/json")
    @POST("/api/account/signup")
    Call<AccountResponse> registerAccount(@Body AccountBody accountBody);
}
