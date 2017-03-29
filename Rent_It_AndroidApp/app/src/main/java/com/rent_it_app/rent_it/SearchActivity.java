package com.rent_it_app.rent_it;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.google.gson.Gson;
import com.rent_it_app.rent_it.firebase.Config;
import com.rent_it_app.rent_it.json_models.Category;
import com.rent_it_app.rent_it.json_models.Item;
import com.rent_it_app.rent_it.json_models.ItemEndpoint;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by malhan on 3/17/17.
 */

public class SearchActivity extends BaseActivity {
    private static final String TAG = "BrowseActivity";

    private String myTag;
    //private String category_id;
    //private String category_name;
    private ArrayList<Item> brwsList;
    private SearchAdapter srchAdapter;
    private ArrayList<File> photoList;
    private ListView blist;
    Retrofit retrofit;
    ItemEndpoint itemEndpoint;
    Gson gson;

    private CognitoCachingCredentialsProvider credentialsProvider;
    private CognitoSyncManager syncClient;
    private AmazonS3 s3;
    private Handler mHandler = new Handler();
    private TransferUtility transferUtility;
    private File imageFile;
    private HashMap<Integer, Integer> percentageHash;
    private HashMap<Integer, File> fileHash;
    private ArrayList<TransferObserver> observers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);

        photoList = new ArrayList<>();
        final ProgressDialog dia = ProgressDialog.show(this, null, "Loading...");

        percentageHash = new HashMap<>();
        fileHash = new HashMap<>();
        observers = new ArrayList<>();
        //final ProgressDialog dia = ProgressDialog.show(this, null, "Loading...");

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

        /*Intent intent = getIntent();
        String value = intent.getStringExtra("key");*/
        myTag = (String) getIntent().getSerializableExtra(Config.EXTRA_DATA);
        Log.d("extra",""+myTag);
        //category_id = myCategory.getId();
        //category_name = myCategory.getName();
        blist = (ListView) findViewById(R.id.browselist);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back_white);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                //startActivity(new Intent(SearchActivity.this, HomeActivity.class));
                //.putExtra("fragment_name", "ChatListFragment"));
                //startActivity(new Intent(this, ChatListFragment.class));
            }
        });
        this.getSupportActionBar().setTitle(myTag);

        gson = new Gson();
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.REST_API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        itemEndpoint = retrofit.create(ItemEndpoint.class);

        brwsList = new ArrayList<Item>();
        //Call<ArrayList<Item>> call = itemEndpoint.getItems();
        Call<ArrayList<Item>> call = itemEndpoint.getItemsByTag(myTag);
        call.enqueue(new Callback<ArrayList<Item>>() {
            @Override
            public void onResponse(Call<ArrayList<Item>> call, Response<ArrayList<Item>> response) {
                int statusCode = response.code();
                //List<Item> items = response.body();
                brwsList = response.body();

                for(int i = 0; i < brwsList.size(); i++) {
                    Item item = brwsList.get(i);

                    beginPhotoDownload(item.getImage(), i);
                }

                /*for(Item item : brwsList) {
                    // load the bitmapList with the images
                    File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
                    try {
                        File imageFile = File.createTempFile(item.getImage(), "", outputDir);
                        imageFile.deleteOnExit();
                        TransferObserver transferObserver =
                                transferUtility.download(Constants.BUCKET_NAME, item.getImage(), imageFile);
                        photoList.add(imageFile);
                        Log.d("pos:id ", pos + ":" + transferObserver.getId());
                        transferObserver.setTransferListener(new TransferListener() {
                            @Override
                            public void onStateChanged(int id, TransferState state) {
                                if(state == TransferState.COMPLETED) {
                                    //myPhoto.setImageResource(R.drawable.bg);
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imageHash.get(id).getAbsolutePath());
                                    imageViewHash.get(id).setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 200, 150, false));
                                    Log.d("onStateChanged.id:", ""+id);
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
                }*/


                //tv1.setText(sb.toString());
                srchAdapter = new SearchAdapter();
                blist.setAdapter(srchAdapter);

                blist.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0,
                                            View arg1, int pos, long arg3) {
                        //startActivity(new Intent(this, EditItemActivity.class).putExtra(Config.EXTRA_DATA, brwsList.get(pos)));
                        Intent myIntent = new Intent(SearchActivity.this, ListingActivity.class);
                        myIntent.putExtra(Config.MORE_DATA, brwsList.get(pos));
                        SearchActivity.this.startActivity(myIntent);
                    }
                });

                Log.d("retrofit.call.enqueue", ""+statusCode);
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        dia.dismiss();
                    }
                }, 1000);
            }

            @Override
            public void onFailure(Call<ArrayList<Item>> call, Throwable t) {
                Log.d("retrofit.call.enqueue", "failed");
                mHandler.postDelayed(new Runnable() {
                    public void run() {
                        dia.dismiss();
                    }
                }, 1000);
            }
        });
    }

    private void beginPhotoDownload(String key, int index){
        File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer
        try {

            File imageFile = File.createTempFile(key, "", outputDir);
            fileHash.put(index, imageFile);
            imageFile.deleteOnExit();
            TransferObserver transferObserver =
                    transferUtility.download(Constants.BUCKET_NAME, key, imageFile);
            transferObserver.setTransferListener(new SearchActivity.DownloadListener());
            observers.add(transferObserver);
            Log.d("beginPhotoDownload:", "added TransferObserver");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     * Updates the ListView according to observers, by making transferRecordMap
     * reflect the current data in observers.
     */
    private void updateList() {
        for (int i = 0; i < observers.size(); i++) {
            int progress = (int) ((double) observers.get(0).getBytesTransferred() * 100 / observers.get(0)
                    .getBytesTotal());
            percentageHash.put(i, progress);
        }
        srchAdapter.notifyDataSetChanged();
    }

    /*
     * A TransferListener class that can listen to a download task and be
     * notified when the status changes.
     */
    private class DownloadListener implements TransferListener {
        // Simply updates the list when notified.
        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "onError: " + id, e);
            //updateList();
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
            //updateList();
        }

        @Override
        public void onStateChanged(int id, TransferState state) {
            Log.d(TAG, "onStateChanged: " + id + ", " + state);
            if(state == TransferState.COMPLETED) {
                Log.d("DownloadListener:", "COMPLETED");
                updateList();
            }

        }
    }

    private class SearchAdapter extends BaseAdapter
    {

        /* (non-Javadoc)
         * @see android.widget.Adapter#getCount()
         */
        @Override
        public int getCount() { return brwsList.size(); }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem(int)
         */
        @Override
        public Item getItem(int arg0)
        {
            return brwsList.get(arg0);
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getItem_id(int)
         */
        @Override
        public long getItemId(int arg0)
        {
            return arg0;
        }

        /* (non-Javadoc)
         * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
         */
        @Override
        public View getView(int pos, View v, ViewGroup arg2)
        {
            if (v == null)
                v = getLayoutInflater().inflate(R.layout.listing_item, arg2, false);



            Item c = getItem(pos);

            /*TextView lbl = (TextView) v;
            lbl.setText(c.getTitle());*/
            LinearLayout ll = (LinearLayout) v; // get the parent layout view
            TextView title = (TextView) ll.findViewById(R.id.txtTitle); // get the child text view
            TextView city = (TextView) ll.findViewById(R.id.txtCity);
            TextView rate = (TextView) ll.findViewById(R.id.txtRate);
            ImageView photo = (ImageView) ll.findViewById(R.id.photo);
            title.setText(c.getTitle());
            city.setText(c.getCity());
            rate.setText("$" + c.getRate() + " /day");
            File outputDir = getApplicationContext().getCacheDir(); // context being the Activity pointer

            if(percentageHash.size() > pos && percentageHash.get(pos) >= 100) {
                Log.d("adp.getView", "percentage: " + percentageHash.get(pos));
                Bitmap myBitmap = BitmapFactory.decodeFile(fileHash.get(pos).getAbsolutePath());
                photo.setImageBitmap(myBitmap/*Bitmap.createScaledBitmap(myBitmap, 200, 150, false)*/);
            }

            /*try {
                File imageFile = File.createTempFile(c.getImage(), "", outputDir);
                imageFile.deleteOnExit();
                TransferObserver transferObserver =
                        transferUtility.download(Constants.BUCKET_NAME, c.getImage(), imageFile);
                imageHash.put(transferObserver.getId(), imageFile);
                imageViewHash.put(transferObserver.getId(), photo);
                Log.d("pos:id ", pos + ":" + transferObserver.getId());
                transferObserver.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        if(state == TransferState.COMPLETED) {
                            //myPhoto.setImageResource(R.drawable.bg);
                            Bitmap myBitmap = BitmapFactory.decodeFile(imageHash.get(id).getAbsolutePath());
                            imageViewHash.get(id).setImageBitmap(Bitmap.createScaledBitmap(myBitmap, 200, 150, false));
                            Log.d("onStateChanged.id:", ""+id);
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
            }*/

            return v;
        }

    }

}
