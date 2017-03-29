package com.rent_it_app.rent_it.json_models;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by Miz on 2/1/17.
 */

public interface UserEndpoint {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter


    @GET("api//user/{uid}")
    Call<User> getUserByUid(@Path("uid") String uid);

    @PUT("api/user/{uid}")
    Call<User> updateUser(@Path("uid") String uid, @Body User user);

    @POST("api/users")
    Call<User> addUser(@Body User user);

}
