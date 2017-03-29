package com.rent_it_app.rent_it.views;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.Constants;
import com.rent_it_app.rent_it.EditItemActivity;
import com.rent_it_app.rent_it.HomeActivity;
import com.rent_it_app.rent_it.R;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Claim;
import com.rent_it_app.rent_it.json_models.ClaimEndpoint;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;
import com.rent_it_app.rent_it.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A simple {@link Fragment} subclass.
 */
public class FileClaimFragment extends Fragment {

    private Spinner spinner1,spinner2;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private String userChoosenTask;
    private ImageView ivImage;
    private static final String TAG = HomeActivity.class.getName();
    private ArrayList<Item> iList;
    Retrofit retrofit;
    ClaimEndpoint claimEndpoint;
    ItemEndpoint itemEndpoint;
    private EditText txtIssue;
    private EditText txtDate;
    private String myIssue, myItem, myReason, myRental, mDate;
    private String mRole = "Owner";
    private TextView myStatusText;
    private FirebaseAuth mAuth;
    private RadioGroup rg;
    private RadioButton owner, renter;
    private String userId;
    private int mYear, mMonth, mDay, mHour, mMinute, myRole;
    Button btnDatePicker;
    Gson gson;
    CognitoCachingCredentialsProvider credentialsProvider;
    CognitoSyncManager syncClient;
    Calendar myCalendar = Calendar.getInstance();
    public String[] nameArray;

    File photo_destination;
    String imgS3Name;

    public FileClaimFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file_claim, container, false);
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("FILE CLAIM");


        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        claimEndpoint = retrofit.create(ClaimEndpoint.class);
        itemEndpoint = retrofit.create(ItemEndpoint.class);

        gson = new Gson();

        // Initialize the Amazon Cognito credentials provider
        credentialsProvider = new CognitoCachingCredentialsProvider(
                getContext(),  // getApplicationContext(),
                Constants.COGNITO_POOL_ID, // Identity Pool ID
                Regions.US_WEST_2 // Region
        );

        // Initialize the Cognito Sync client
        syncClient = new CognitoSyncManager(
                getContext(),
                Regions.US_WEST_2, // Region
                credentialsProvider);

        //Define
        spinner1 = (Spinner) view.findViewById(R.id.spinner1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner2);
        txtIssue = (EditText)view.findViewById(R.id.issue);

        txtDate = (EditText)view.findViewById(R.id.in_date);
        btnDatePicker=(Button)view.findViewById(R.id.btn_date);
        rg = (RadioGroup)view.findViewById(R.id.radio_item);

        //Spinner - Item

        final ArrayAdapter<CharSequence> owner_adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.owner_reason_array, android.R.layout.simple_spinner_item);
        owner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        final ArrayAdapter<CharSequence> renter_adapter = ArrayAdapter.createFromResource(
                getActivity(), R.array.renter_reason_array, android.R.layout.simple_spinner_item);
        renter_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner2.setAdapter(owner_adapter);

        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected

                switch(checkedId) {
                    case R.id.owner:
                        // switch to fragment 1
                        Log.d("radio","owner selected");
                        mRole="Owner";
                        spinner2.setAdapter(owner_adapter);
                        break;
                    case R.id.renter:
                        // Fragment 2
                        Log.d("radio","renter selected");
                        mRole="Renter";
                        spinner2.setAdapter(renter_adapter);
                        break;
                }
            }
        });

        owner = (RadioButton)view.findViewById(R.id.owner);
        renter = (RadioButton)view.findViewById(R.id.renter);

        btnDatePicker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                txtDate.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

        final Button submitButton = (Button) view.findViewById(R.id.submit_button);


        //getuid
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        userId = user.getUid().toString();


        Call<ArrayList<Item>> call = itemEndpoint.getItemsByUid(userId);
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                int statusCode = response.code();
                //List<Item> items = response.body();
                ArrayList<String> nameArray = new ArrayList<String>();
                iList = response.body();

                //Log.d("claim",""+nameArray);
                ArrayAdapter<Item> itemAdapter = new ArrayAdapter<Item>(getActivity(),
                        android.R.layout.simple_spinner_item, iList);
                itemAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner1.setAdapter(itemAdapter);

                Log.d("retrofit.call.enqueue", ""+statusCode);
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                Log.d("retrofit.call.enqueue", "failed");
            }
        });



        //Button - List
        submitButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.d("radio",""+rg.getCheckedRadioButtonId());
                //Define
                myIssue = txtIssue.getText().toString();
                mDate = txtDate.getText().toString();
                myRental = "temp_rental_id";
                myItem = ((Item)spinner1.getSelectedItem()).getId();
                Log.d("myRental:getID():", myItem);
                myReason = spinner2.getSelectedItem().toString();

                /*switch (rg.getCheckedRadioButtonId()){
                    case 2131755235:
                        mRole = "Owner";
                        break;
                    case 2131755236:
                        mRole = "Renter";
                        break;
                }*/

                /*int selectedId = rg .getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) v.findViewById(selectedId);*/



                //radioValue
                //String mRole = ((RadioButton)view.findViewById(rg.getCheckedRadioButtonId())).getText().toString();

                //Log.d("Category","category is: "+myCategory);
                if (myIssue.trim().equals("")) {
                    txtIssue.requestFocus();
                    txtIssue.setError("Title is required!");
                }else {
                    //Toast.makeText(getActivity(), spinner1.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
                    //post Item
                    imgS3Name = UUID.randomUUID().toString() + ".jpg";

                    if(mRole.equals("Owner")){
                        myRole = 1;
                    }else if(mRole.equals("Renter")){
                        myRole = 2;
                    }else{
                        myRole = 0;
                    }

                    Claim new_claim = new Claim();
                    new_claim.setSubmittedBy(userId);
                    new_claim.setIssue(myIssue);
                    new_claim.setRole(myRole);
                    new_claim.setItem(myItem);
                    new_claim.setReason(myReason);
                    new_claim.setMeetingDate(mDate);
                    new_claim.setStatus(1);
                    new_claim.setRentalId(myRental);

                    if (photo_destination != null) {
                        new_claim.setImage(imgS3Name);
                    }


                    Call<Claim> call = claimEndpoint.addClaim(new_claim);
                    call.enqueue(new Callback<Claim>() {
                        @Override
                        public void onResponse(Call<Claim> call, Response<Claim> response) {
                            int statusCode = response.code();

                            Log.d("retrofit.call.enqueue", "" + statusCode);

                            //Log.d("photo_dest!=null?", photo_destination.toString());
                            if (photo_destination != null) {
                                Log.d("photo_destination!=null", "" + (photo_destination != null));
                                AmazonS3 s3 = new AmazonS3Client(credentialsProvider);
                                TransferUtility transferUtility = new TransferUtility(s3, getContext());

                                TransferObserver observer = transferUtility.upload(
                                        Constants.BUCKET_NAME,
                                        imgS3Name,
                                        photo_destination
                                );
                            }
                            Toast.makeText(getActivity(), "Sucessfully Submitted Claim", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<Claim> call, Throwable t) {
                            Log.d("retrofit.call.enqueue", t.toString());
                        }

                    });
                    //end of post item
                }//end of if statement
            }
        });
        //Button - Image
        Button imageButton = (Button) view.findViewById(R.id.image_button);
        imageButton.setOnClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                selectImage();
            }
        });
        ivImage = (ImageView) view.findViewById(R.id.preview);
        return view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result= Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask ="Take Photo";
                    if(result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask ="Choose from Library";
                    if(result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        photo_destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            photo_destination.createNewFile();
            fo = new FileOutputStream(photo_destination);
            fo.write(bytes.toByteArray());
            fo.close();
            Log.d("photo_des.createNewFile", "success");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Log.d("photo_dest!=null?", photo_destination.toString());

        //Rsizing the image
        Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 900, 600, true);

        /*ivImage.setImageBitmap(thumbnail);*/
        ivImage.setImageBitmap(resized);

        Log.d(TAG, "Height: " + resized.getHeight() + " & Width: " + resized.getWidth());
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        photo_destination = new File(getRealPathFromURI(getContext(), getImageUri(getContext(), bm)));

        ivImage.setImageBitmap(bm);
        Log.d("PATH",""+ photo_destination);
    }

    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(Context inContext, Uri uri) {
        Cursor cursor = inContext.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

}