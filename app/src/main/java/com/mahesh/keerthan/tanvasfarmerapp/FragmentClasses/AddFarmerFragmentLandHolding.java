package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mahesh.keerthan.tanvasfarmerapp.Adapters.questionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.util.ArrayList;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;

public class AddFarmerFragmentLandHolding extends AppCompatActivity implements questionAdapter.ItemClickListener {

    private ArrayList<QuestionClass> questions = new ArrayList<>();
    private RecyclerView landholdingList;
    private questionAdapter landholdingAdapter;

    QuestionClass question1 = new QuestionClass(1,"what is your name","multiple choice",1,0,1,0);
    QuestionClass question2 = new QuestionClass(2,"what are your names","multiple choice",1,0,2,0);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farmer_fragment_land_holding);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this,R.color.Back));

        questions.add(question1);
        questions.add(question2);
        initialiseLandHoldingCard();
     }

     private void initialiseLandHoldingCard(){
        landholdingList = findViewById(R.id.landholdinglist);
        landholdingList.setItemAnimator(new SlideInLeftAnimator());
        landholdingList.getItemAnimator().setAddDuration(1000);
        landholdingList.setLayoutManager(new LinearLayoutManager(this));
        landholdingAdapter = new questionAdapter(questions,this);
        landholdingList.setAdapter(landholdingAdapter);
     }

    @Override
    public void onItemClick(View view, int position) {

    }
}
