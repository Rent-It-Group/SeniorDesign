package com.rent_it_app.rent_it.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.Constants;
import com.rent_it_app.rent_it.R;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.rent_it_app.rent_it.json_models.User;
import com.rent_it_app.rent_it.json_models.UserEndpoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Mimi on 2/16/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FirebaseIDService";
    //User myUser;
    private static FirebaseUser myUid;
    Retrofit retrofit;
    UserEndpoint userEndpoint;
    Gson gson;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);


        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(Config.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Send the token from here, along with the user ID to the NodeJS server
     *
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(final String token) {

        // Guard and only allow sending token to the server if a user is signed in
        myUid = FirebaseAuth.getInstance().getCurrentUser();
        if(myUid == null){
            return;
        }

        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
        gson = new Gson();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        userEndpoint = retrofit.create(UserEndpoint.class);



        User updatedUser = new User();
        updatedUser.setFcmToken(token);

        Call<User> call = userEndpoint.updateUser(myUid.getUid(), updatedUser);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                int statusCode = response.code();

                Log.d("retrofit.call.enqueue", "" + statusCode);

                //Log.d("photo_dest!=null?", photo_destination.toString());

                //Toast.makeText(EditItemActivity.this, "Sucessfully Updated Listing", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.d("retrofit.call.enqueue", t.toString());
            }

        });

    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}
