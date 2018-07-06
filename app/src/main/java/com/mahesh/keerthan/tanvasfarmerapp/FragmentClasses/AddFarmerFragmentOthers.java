package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.Adapters.questionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Responses;
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
    private ArrayList<Responses> responses = null;
    private Toolbar toolbar;
    private Intent incomingIntent;
    private ArrayList<Responses> previousResponses = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_farmer_fragment_others);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.Back));
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setTitleTextAppearance(this,R.style.AmericanTypewriterSemibold);
        toolbar.setTitle("Questionnaire");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                supportFinishAfterTransition();
            }
        });

        toolbar.inflateMenu(R.menu.home);
        Menu menu =  toolbar.getMenu();
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent();
                //Bundle bundle = new Bundle();
               // bundle.putSerializable("responses",responses);
                Gson gson = new Gson();
                String resp_json = gson.toJson(responses);
                intent.putExtra("responses",resp_json);
                setResult(Activity.RESULT_OK,intent);
                supportFinishAfterTransition();
                return false;
            }
        });
        othersList = findViewById(R.id.list123);
        othersList.setItemAnimator(new SlideInLeftAnimator());
        othersList.getItemAnimator().setAddDuration(1000);
        incomingIntent = getIntent();
        mainQuestions = (ArrayList<QuestionClass>) incomingIntent.getSerializableExtra("othersQuestions");
        initialiseOthersCard();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        supportFinishAfterTransition();
    }

    private void initialiseOthersCard(){

        othersList.setLayoutManager(new LinearLayoutManager(this));
        othersAdapter = new questionAdapter(mainQuestions,this);
        othersAdapter.setResult(new questionAdapter.OnResult() {
            @Override
            public void finish(ArrayList<Responses> responses) {
                AddFarmerFragmentOthers.this.responses = responses;
            }
        });
        if(incomingIntent.hasExtra("previousResponses")){

            String previousResponses_json = incomingIntent.getStringExtra("previousResponses");
            Gson gson = new Gson();
            previousResponses = gson.fromJson(previousResponses_json,new TypeToken<List<Responses>>(){}.getType());
            othersAdapter.setResponses(previousResponses);
            responses = previousResponses;

        }
        othersList.setAdapter(othersAdapter);
    }



    @Override
    public void onItemClick(View view, int position) {

    }
}

