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

public interface RentalEndpoint {

    // Request method and URL specified in the annotation
    // Callback for the parsed response is the last parameter

    @GET("api/rentals/renter/{renter}")
    Call<ArrayList<Rental>> getContactedRentalsItems(@Path("renter") String renter);

    @PUT("api/rental/{rental_id}")
    Call<Rental> updateRental(@Path("rental_id") String rental_id, @Body Rental rental);

    @POST("api/rentals")
    Call<Rental> addRental(@Body Rental rental);

}
