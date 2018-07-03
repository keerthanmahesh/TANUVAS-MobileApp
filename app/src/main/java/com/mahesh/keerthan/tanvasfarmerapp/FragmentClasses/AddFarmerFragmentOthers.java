package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.Adapters.questionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.SlideInLeftAnimator;
import okhttp3.OkHttpClient;

public class AddFarmerFragmentOthers extends AppCompatActivity implements questionAdapter.ItemClickListener {

    private ArrayList<QuestionClass> mainQuestions = new ArrayList<>();
    private RecyclerView othersList;
    private questionAdapter othersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farmer_fragment_others);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.Back));
        othersList = findViewById(R.id.list123);
        othersList.setItemAnimator(new SlideInLeftAnimator());
        othersList.getItemAnimator().setAddDuration(1000);
        Intent incomingIntent = getIntent();
        mainQuestions = (ArrayList<QuestionClass>) incomingIntent.getSerializableExtra("othersQuestions");
        initialiseOthersCard();






    }



    private void initialiseOthersCard(){
        othersList.setLayoutManager(new LinearLayoutManager(this));
        othersAdapter = new questionAdapter(mainQuestions,this);
        othersList.setAdapter(othersAdapter);
    }


    @Override
    public void onItemClick(View view, int position) {

    }
}
