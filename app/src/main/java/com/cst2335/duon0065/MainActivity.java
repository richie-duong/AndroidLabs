package com.cst2335.duon0065;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_linear);

        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, getResources().getString(R.string.toast_message), Toast.LENGTH_LONG).show();
            }
        });

        CompoundButton s = findViewById(R.id.swtch);

        s.setOnCheckedChangeListener((cb, b) -> {
            if (b){
                Snackbar sp = Snackbar.make(s, getString(R.string.sbon), Snackbar.LENGTH_LONG);
                sp.show();
                sp.setAction( "Undo", click -> cb.setChecked(!b));
            } else {
                Snackbar sp = Snackbar.make(s, getString(R.string.sboff), Snackbar.LENGTH_LONG);
                sp.show();
                sp.setAction( "Undo", click -> cb.setChecked(!b));
            }

        });
    }
}