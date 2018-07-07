package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mahesh.keerthan.tanvasfarmerapp.R;

public class QuestionFragment2 extends AppCompatActivity {


    public static int LIVESTOCKHOLDING = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_fragment2);
        /*Intent intent = new Intent(QuestionFragment2.this,QuestionFragment3.class);
        intent.putExtra("whichQuestion",LIVESTOCKHOLDING);*/
    }
}
