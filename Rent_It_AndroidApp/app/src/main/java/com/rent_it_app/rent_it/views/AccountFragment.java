package com.rent_it_app.rent_it.views;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.braintreepayments.api.dropin.utils.PaymentMethodType;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
//import com.loopj.android.http.AsyncHttpClient;
//import com.loopj.android.http.TextHttpResponseHandler;
import com.rent_it_app.rent_it.Constants;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.json_models.FunctionEndpoint;
import com.rent_it_app.rent_it.testing.NotificationActivity;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

//import cz.msebera.android.httpclient.Header;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {

    private static final int REQUEST_CODE = Menu.FIRST;
    private static final String CLIENT_TOKEN_TESTING = "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJiMjBmZTk5YjY0MTFmNTU4YmEwYzRjYzU3ZWE2OTBjNDYyOGI5MDIxNzNiODdkYzFhOTdkMDM5YWFhMDUxY2Q3fGNyZWF0ZWRfYXQ9MjAxNy0wMy0xOVQyMTowMTowMi41NjYzMjQ3MjArMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tLzM0OHBrOWNnZjNiZ3l3MmIifSwidGhyZWVEU2VjdXJlRW5hYmxlZCI6dHJ1ZSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=";

    //private AsyncHttpClient client = new AsyncHttpClient();
    private String clientToken;
    public static FirebaseUser myUser;
    Retrofit retrofit;
    FunctionEndpoint braintreeEndpoint;
    private PaymentMethodNonce recentPaymentMethod;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("PROFILE");

        myUser = FirebaseAuth.getInstance().getCurrentUser();

        //getToken();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .build();

        braintreeEndpoint = retrofit.create(FunctionEndpoint.class);

        Call<ResponseBody> call = braintreeEndpoint.getToken(myUser.getUid());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call,Response<ResponseBody> response) {
                int statusCode = response.code();
                //List<Item> items = response.body();
                try{
                    clientToken = response.body().string();
                    Log.d("myToken ",clientToken);

                    //Log.d("Client Token:", this.clientToken);
                    Log.d("Client Token:", ""+clientToken);



                    DropInResult.fetchDropInResult(getActivity(), clientToken, new DropInResult.DropInResultListener() {
                        @Override
                        public void onError(Exception exception) {
                            // an error occurred
                        }

                        @Override
                        public void onResult(DropInResult result) {
                            if (result.getPaymentMethodType() != null) {
                                // use the icon and name to show in your UI
                                int icon = result.getPaymentMethodType().getDrawable();
                                int name = result.getPaymentMethodType().getLocalizedName();

                                if (result.getPaymentMethodType() == PaymentMethodType.ANDROID_PAY) {

                                    // The last payment method the user used was Android Pay.
                                    // The Android Pay flow will need to be performed by the
                                    // user again at the time of checkout.
                                } else {
                                    // Use the payment method show in your UI and charge the user
                                    // at the time of checkout.
                                    recentPaymentMethod = result.getPaymentMethodNonce();

                                    Log.d("paymentMethodNonce: ", recentPaymentMethod.getDescription());
                                }
                            } else {
                                // there was no existing payment method
                                Log.d("braintree.paymentMethod", ".noExistingMethod");
                            }
                        }
                    });

                    Log.d("retrofit.call.enqueue", "success");
                }catch(IOException ioe){

                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call,Throwable t) {
                Log.d("retrofit.call.enqueue", "failed");
            }
        });




        Button brainTreeButton = (Button) view.findViewById(R.id.braintree_button);
        brainTreeButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                onBraintreeSubmit(view);
            }

        });

        Button fcmButton = (Button) view.findViewById(R.id.FCM_button);
        fcmButton.setOnClickListener(new View.OnClickListener(){

            public void onClick(View view)
            {
                startActivity(new Intent(getActivity(), NotificationActivity.class));
            }

        });

        // Inflate the layout for this fragment
        return view;
    }

    public void onBraintreeSubmit(View v) {
        DropInRequest dropInRequest = new DropInRequest()
                .clientToken(clientToken);
        startActivityForResult(dropInRequest.getIntent(getContext()), REQUEST_CODE);
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                Log.d("paymentMethodNonce: ", result.getPaymentMethodNonce().toString());

                // use the result to update your UI and send the payment method nonce to your server
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("resultCode: ", "RESULT_CANCELLED");
                // the user canceled
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                Log.d("Braintree.DropInUI", "onActivityResult.exception");
                Log.d("Braintree.DropInUI", "onActivityResult.exception:"+error.getMessage());
                error.printStackTrace();
            }
        }
    }

    /*private void getToken() {
        client.get(Constants.REST_API_BASE_URL + "/api/bt/client_token/" + myUser.getUid(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                clientToken = responseString;
            }
        });
    }*/



}
