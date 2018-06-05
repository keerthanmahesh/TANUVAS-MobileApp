package com.mahesh.keerthan.tanvasfarmerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button SignInButton = findViewById(R.id.signInButton);
        View.OnClickListener SignInPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInSuccess = new Intent(MainActivity.this, VillageSelect.class);
                startActivity(signInSuccess);
            }
        };
        SignInButton.setOnClickListener(SignInPressed);
    }
}



