package com.mahesh.keerthan.tanvasfarmerapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class VillageSelect extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_select);
        Button goButton = findViewById(R.id.goButton);
        View.OnClickListener goButtonPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent villageSelected = new Intent(VillageSelect.this,HomeActivity.class);
                startActivity(villageSelected);
            }
        };
        goButton.setOnClickListener(goButtonPressed);
    }
}
