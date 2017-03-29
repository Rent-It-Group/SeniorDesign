package com.rent_it_app.rent_it;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by malhan on 3/26/17.
 */

public class ConfirmRentalActivity extends BaseActivity{

    private Button btnCancel;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_rental);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.getSupportActionBar().setTitle("CONFIRM RENTAL REQUEST");

        btnCancel = (Button)findViewById(R.id.cancel_button);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(ConfirmRentalActivity.this, HomeActivity.class);
                ConfirmRentalActivity.this.startActivity(myIntent);
            }
        });

        if (getIntent().getExtras() != null) {
            for (String key : getIntent().getExtras().keySet()) {
                Object value = getIntent().getExtras().get(key);
                Log.d("FCM DATA", "Key: " + key + " Value: " + value);
            }
        }
    }
}
