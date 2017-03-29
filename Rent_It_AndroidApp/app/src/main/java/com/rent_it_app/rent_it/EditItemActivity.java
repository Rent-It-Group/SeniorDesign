package com.rent_it_app.rent_it;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.util.StringUtils;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;
import com.rent_it_app.rent_it.views.AvailabeItemFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malhan on 3/4/17.
 */

public class EditItemActivity extends BaseActivity{

    Item myItem;
    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    Gson gson;
    private EditText txtTitle, txtDescription, txtCondition, txtZipcode;
    private EditText txtTags, txtValue, txtRate, txtCity;
    private RadioGroup rg;
    private String myTitle, myDescription, myCondition, myCategory, myZipcode, myTags, myValue, myRate, myCity;
    private Spinner spnCategory;
    private ImageView preview;
    private List<String> arrayTags;

    private CognitoCachingCredentialsProvider credentialsProvider;
    private CognitoSyncManager syncClient;
    private AmazonS3 s3;
    private TransferUtility transferUtility;
    private File imageFile;
    //private String[] arrayTag;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("EDIT LISTING");

        myItem = (Item) getIntent().getSerializableExtra(Config.EXTRA_DATA);

        credentialsProvider = new CognitoCachingCredentialsProvider(
                getApplicationContext(),  // getApplicationContext(),
                Constants.COGNITO_POOL_ID, // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getApplicationContext(),
                Regions.US_WEST_2, // Region
                credentialsProvider);

        s3 = new AmazonS3Client(credentialsProvider);
        transferUtility = new TransferUtility(s3, getApplicationContext());

        //Define
        spnCategory = (Spinner) findViewById(R.id.spinner1);
        txtTitle = (EditText) findViewById(R.id.title);
        txtDescription = (EditText) findViewById(R.id.description);
        txtCondition = (EditText) findViewById(R.id.condition);
        txtZipcode = (EditText) findViewById(R.id.zipcode);
        txtCity = (EditText) findViewById(R.id.city);
        txtTags = (EditText) findViewById(R.id.tags);
        txtRate = (EditText) findViewById(R.id.rate);
        txtValue = (EditText) findViewById(R.id.value);
        preview = (ImageView) findViewById(R.id.preview);
        rg = (RadioGroup)findViewById(R.id.radio_group);



        //populate fields
        txtTitle.setText(myItem.getTitle());
        txtDescription.setText(myItem.getDescription());
        txtCity.setText(myItem.getCity());
        txtCondition.setText(myItem.getCondition());
        txtZipcode.setText(Integer.toString(myItem.getZipcode()));
        Log.d("rate:", " " + myItem.getRate());
        //String myRate = String.format("%.2f", myItem.getRate());
        //Log.d("string rate:"," "+myRate);
        txtRate.setText(String.format("%.2f", myItem.getRate()));
        //txtValue.setText(Double.toString(myItem.getValue()));
        Log.d("value:", " " + myItem.getValue());
        txtValue.setText(String.format("%.2f", myItem.getValue()));

        //Tag value
        arrayTags = myItem.getTags();
        String myTag = TextUtils.join(",", arrayTags);
        txtTags.setText(myTag);

        //set category spinner value
        String compareValue = myItem.getCategory();
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnCategory.setAdapter(adapter);

        switch (compareValue) {
            case "58bd9baca8f8e676ea599e78":
                compareValue = "Vehicles and Equipment";
                break;
            case "58bd9dbba8f8e676ea599f1c":
                compareValue = "Sports";
                break;
            case "58bd9e1fa8f8e676ea599f3c":
                compareValue = "Outdoor Gear";
                break;
            case "58bda523a8f8e676ea59a0b1":
                compareValue = "Party Supplies";
                break;
            case "58bda53aa8f8e676ea59a0bc":
                compareValue = "Garden";
                break;
            case "58bda560a8f8e676ea59a0d2":
                compareValue = "Tools";
                break;
            case "58bda5a6a8f8e676ea59a0ec":
                compareValue = "Clothes";
                break;
            case "58bda5c8a8f8e676ea59a0f9":
                compareValue = "Electronics";
                break;
            case "58bda5e5a8f8e676ea59a103":
                compareValue = "Books";
                break;
            case "58bda65ba8f8e676ea59a128":
                compareValue = "Miscellaneous";
                break;
            case "Choose a Category":
                //Miscellaneous
                compareValue = "58bda65ba8f8e676ea59a128";
                break;
        }

        if (!compareValue.equals(null)) {
            int spinnerPosition = adapter.getPosition(compareValue);
            spnCategory.setSelection(spinnerPosition);
        }

        File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
        try {
            imageFile = File.createTempFile(myItem.getImage(), "", outputDir);
            imageFile.deleteOnExit();
            TransferObserver transferObserver =
                    transferUtility.download(Constants.BUCKET_NAME, myItem.getImage(), imageFile);
            transferObserver.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    if (state == TransferState.COMPLETED) {
                        //myPhoto.setImageResource(R.drawable.bg);
                        Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                        preview.setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 900, 600, false));
                    }
                }

                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    // Do something in the callback.
                }

                public void onError(int id, Exception e) {
                    // Do something in the callback.
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        //for updating data

        gson = new Gson();

        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemEndpoint = retrofit.create(ItemEndpoint.class);
        final Button listButton = (Button) findViewById(R.id.list_button);

        listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Define
                myTitle = txtTitle.getText().toString();
                myDescription = txtDescription.getText().toString();
                //myCondition = txtCondition.getText().toString();
                myCategory = spnCategory.getSelectedItem().toString();
                myCity = txtCity.getText().toString();
                myZipcode = txtZipcode.getText().toString();
                myTags = txtTags.getText().toString();
                myValue = txtValue.getText().toString();
                myRate= txtRate.getText().toString();
                String radioValue = ((RadioButton)findViewById(rg.getCheckedRadioButtonId())).getText().toString();
                Log.d("radio: ",""+ radioValue);

                Item listing_item = new Item();
                listing_item.setId(myItem.getId());
                listing_item.setTitle(myTitle);
                listing_item.setDescription(myDescription);
                listing_item.setCondition(myCondition);
                //listing_item.setCategory(myCategory);
                listing_item.setCity(myCity);
                listing_item.setZipcode(Integer.parseInt(myZipcode));
                List<String> tags = Arrays.asList(myTags.split("\\s*,\\s*"));
                listing_item.setTags(tags);
                listing_item.setValue(Double.parseDouble(myValue));
                listing_item.setRate(Double.parseDouble(myRate));
                if(radioValue.equals("Hidden")){
                    listing_item.setVisible(false);
                }else{
                    listing_item.setVisible(true);
                }


                Call<Item> call = itemEndpoint.updateItem(listing_item.getId(), listing_item);
                call.enqueue(new Callback<Item>() {
                    @Override
                    public void onResponse(Call<Item> call, Response<Item> response) {
                        int statusCode = response.code();

                        Log.d("retrofit.call.enqueue", "" + statusCode);

                        //Log.d("photo_dest!=null?", photo_destination.toString());

                        Toast.makeText(EditItemActivity.this, "Sucessfully Updated Listing", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Call<Item> call, Throwable t) {
                        Log.d("retrofit.call.enqueue", t.toString());
                    }

                });

            }


        });
    }
}
