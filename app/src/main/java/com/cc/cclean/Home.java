package com.cc.cclean;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.ebanx.swipebtn.OnActiveListener;
import com.ebanx.swipebtn.SwipeButton;


public class Home extends AppCompatActivity {

    SwipeButton swipeButton,swipeButton1;
    ProgressDialog progressDialog;



    @Override
    protected void onCreate( Bundle savedInstanceState) {

        progressDialog = new ProgressDialog(Home.this);
        progressDialog.setCancelable(false); // set cancelable to false
        progressDialog.setMessage("Loading Apps"); // set message

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        requestWriteExternalPermission();
        swipeButton=findViewById(R.id.swipe_btn);
        swipeButton1=findViewById(R.id.swipe_btn1);

        swipeButton.setOnActiveListener(new OnActiveListener() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public void onActive() {
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
            }
        });

        swipeButton1.setOnActiveListener(new OnActiveListener() {
            public void onActive() {
                progressDialog.show();
                Intent intent = new Intent(Home.this, InstalledApps.class);
                startActivity(intent);

            }
        });

    }

   public void onResume(){
        super.onResume();
        progressDialog.hide();
       swipeButton.setHasActivationState(false);
       swipeButton1.setHasActivationState(false);


    }

    /**
     * Request write permission
     */
    public synchronized void requestWriteExternalPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                1);
    }

}
