package com.cst2335.duon0065;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.util.Log;

public class ProfileActivity extends AppCompatActivity {
    ImageButton img;
    EditText emailEditText;
    public static final String TAG = "PROFILE_ACTIVITY";
    public static String ACTIVITY_NAME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ACTIVITY_NAME = "onCreate()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);

        setContentView(R.layout.activity_profile);

        img = findViewById(R.id.cameraButton);
        emailEditText = findViewById(R.id.emailEditText);

        Intent fromMain = getIntent();
        emailEditText.setText(fromMain.getStringExtra("emailKey"));

        ActivityResultLauncher<Intent> myPictureTakerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult()
                ,new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    Bitmap imgbitmap = (Bitmap) data.getExtras().get("data");
                    img.setImageBitmap(imgbitmap); //the imageButton
                }
                else if (result.getResultCode() == Activity.RESULT_CANCELED)
                    Log.i(TAG, "User refused to capture a picture.");
            }
        });


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    myPictureTakerLauncher.launch(takePictureIntent);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ACTIVITY_NAME = "onStart()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);
    }

    @Override
    protected void onResume() {
        super.onResume();
        ACTIVITY_NAME = "onResume()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);
    }

    @Override
    protected void onPause(){
        super.onPause();
        ACTIVITY_NAME = "onPause()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);
    }

    @Override
    protected void onStop(){
        super.onStop();
        ACTIVITY_NAME = "onStop()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        ACTIVITY_NAME = "onDestroy()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode,resultCode, data);
        ACTIVITY_NAME = "onActivityResult()";
        Log.e(TAG, "In function: " + ACTIVITY_NAME);
    }





}