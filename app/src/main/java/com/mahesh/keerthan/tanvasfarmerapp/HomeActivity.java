package com.mahesh.keerthan.tanvasfarmerapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.AddFarmerFragment;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.AddMultipleFarmersFragment;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.EditFarmerFragment;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.InputFarmersDialog;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.QuestionFragment;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.ReportsFragment;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.UpdateQuestionsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private  NavigationView navigationView;
    private UserClass user;
    private Villages villageSelected;
    private District districtSelected;
    public static HomeActivity instance;
    private FragmentManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        manager = getSupportFragmentManager();
        Intent incomingIntent = getIntent();
        user = (UserClass) incomingIntent.getExtras().getSerializable("user");
        villageSelected = (Villages) incomingIntent.getExtras().getSerializable("village");
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState == null){
            manager.beginTransaction().replace(R.id.mainFragment,new QuestionFragment()).commit();
            navigationView.setCheckedItem(R.id.Questions);
        }
        getUserDistrict(villageSelected.getDistrict_id());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem item = menu.findItem(R.id.action_save);
        item.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.AddFarmer) {
            android.support.v4.app.FragmentTransaction ft1 = manager.beginTransaction();
            Fragment newFarmer = AddFarmerFragment.newInstance(villageSelected,districtSelected);
            ft1.replace(R.id.mainFragment,newFarmer).commit();
            //navigationView.setCheckedItem(R.id.AddFarmer);
        } else if (id == R.id.AddMulFarmers) {
            android.support.v4.app.FragmentTransaction ft = manager.beginTransaction();
            Fragment newFarmers = AddMultipleFarmersFragment.newInstance(villageSelected,districtSelected);
            ft.replace(R.id.mainFragment,newFarmers).commit();
            //navigationView.setCheckedItem(R.id.AddMulFarmers);
        } else if (id == R.id.EditFarmer) {
            android.support.v4.app.FragmentTransaction newTransaction = manager.beginTransaction();
            Fragment editFarmer = EditFarmerFragment.newInstance(villageSelected,districtSelected);
            newTransaction.replace(R.id.mainFragment,editFarmer).commit();
           // navigationView.setCheckedItem(R.id.EditFarmer);
        } else if (id == R.id.Questions) {
            manager.beginTransaction().replace(R.id.mainFragment,new QuestionFragment()).commit();
            //navigationView.setCheckedItem(R.id.Questions);
        } else if (id == R.id.Reports) {
            manager.beginTransaction().replace(R.id.mainFragment,new ReportsFragment()).commit();
            //navigationView.setCheckedItem(R.id.Reports);
        } else if (id == R.id.UpdateQuestions) {
            manager.beginTransaction().replace(R.id.mainFragment,new UpdateQuestionsFragment()).commit();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserDistrict(final int district_id){
        AsyncTask<Integer,Void,JSONObject> asyncTask = new AsyncTask<Integer, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Integer... integers) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.45/~vandit/justtesting.php?district_id=" + district_id).build();
                try{
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());
                    JSONObject object = array.getJSONObject(0);
                    districtSelected = new District(object.getInt("district_id"), object.getString("en_district_name"));
                }catch( IOException e){
                    e.printStackTrace();
                }catch( JSONException e){
                    e.printStackTrace();
                }
                return null;
            }
        };
        asyncTask.execute(district_id);
    }
}
