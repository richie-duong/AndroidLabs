package com.cst2335.duon0065;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class MainActivity extends AppCompatActivity {

    private EditText email;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);
        email = findViewById(R.id.editEmail);

        SharedPreferences sp = getSharedPreferences("SP_EMAIL", Context.MODE_PRIVATE);
        String a = sp.getString("SP_EMAIL","");
        email.setText(a);

        Button button = findViewById(R.id.login_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToProfile = new Intent(MainActivity.this, ProfileActivity.class);
                goToProfile.putExtra("emailKey", email.getText().toString());
                startActivity(goToProfile);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sp = getSharedPreferences("SP_EMAIL", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sp.edit();

        myEdit.putString("SP_EMAIL", email.getText().toString());
        myEdit.apply();
    }
}