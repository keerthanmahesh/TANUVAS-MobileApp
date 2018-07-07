package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;

import com.mahesh.keerthan.tanvasfarmerapp.R;

public class QuestionFragment2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_fragment2);
        CardView cardView = findViewById(R.id.livestockholdingcardview);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntentLivestockHolding(v);
            }
        });
    }

    private void animateIntentLivestockHolding(View v) {
        Intent intent = new Intent(QuestionFragment2.this,QuestionFragment3.class);
        String transitionName = "supguys";
        View view_start = findViewById(R.id.livestockholdingcardview);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(QuestionFragment2.this,view_start,transitionName);
        ActivityCompat.startActivity(QuestionFragment2.this,intent,optionsCompat.toBundle());
    }
}
