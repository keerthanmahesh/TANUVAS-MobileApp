package com.mahesh.keerthan.tanvasfarmerapp;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserVillagesClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class VillageSelect extends AppCompatActivity {

    private UserClass user;
    private List<UserVillagesClass> villagesClass = new ArrayList<>();
    private List<Villages> villages = new ArrayList<>();

    private  JSONArray array;
    private  JSONArray array1;
    private static VillageSelect instance = null;
    private Villages selectedVillage = new Villages(0,0,null,0,0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_select);
        instance = this;
        Button goButton = findViewById(R.id.goButton);
        View.OnClickListener goButtonPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent villageSelected = new Intent(VillageSelect.this,HomeActivity.class);
               Bundle bundle = new Bundle();
               bundle.putSerializable("user",user);
               bundle.putSerializable("village",selectedVillage);
               villageSelected.putExtras(bundle);
                startActivity(villageSelected);
            }
        };
        goButton.setOnClickListener(goButtonPressed);
        Intent intent = getIntent();
        user = (UserClass) intent.getSerializableExtra("user");
        getUserVillages(user.getU_id());

    }
    private void getUserVillages(final int u_id){
        AsyncTask<Integer,Void,JSONArray> asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.45/~vandit/justtesting.php?u_id=" + u_id).build();

                try{
                    Response response = client.newCall(request).execute();

                    array = new JSONArray(response.body().string());
                    for(int i = 0; i<array.length();i++){
                        JSONObject object = array.getJSONObject(i);
                        UserVillagesClass uservillagesClass = new UserVillagesClass(object.getInt("s_no"), object.getInt("u_id"), object.getInt("district_id"), object.getInt("village_id"));
                        VillageSelect.this.villagesClass.add(uservillagesClass);
                    }


                }catch( IOException e){
                    e.printStackTrace();
                }catch( JSONException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                    getVillages(user.getU_id());
            }
        };
        asyncTask.execute(u_id);
    }

    private void getVillages(final int village_u_id){
        AsyncTask<Integer,Void,JSONObject> asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.45/~vandit/justtesting.php?village_u_id=" + village_u_id).build();

                try{
                    Response response = client.newCall(request).execute();

                    array1 = new JSONArray(response.body().string());
                    for(int i = 0; i<array1.length();i++){
                        JSONObject object = array1.getJSONObject(i);
                        Villages village = new Villages(object.getInt("village_id"), object.getInt("district_id"), object.getString("en_village_name"), object.getInt("allocated"),object.getInt("u_id"));
                        VillageSelect.this.villages.add(village);
                    }


                }catch( IOException e){
                    e.printStackTrace();
                }catch( JSONException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                super.onPostExecute(o);
                Spinner dropdown = findViewById(R.id.spinner1);
                final List<String> items = new ArrayList<>();
                for (int i =0;i<array1.length();i++){
                    String village = villages.get(i).getEn_village_name();
                    items.add(village);
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(instance,android.R.layout.simple_spinner_dropdown_item,items);
                dropdown.setAdapter(adapter);
                dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedVillage = villages.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        selectedVillage = villages.get(0);
                    }
                });


            }
        };
        asyncTask.execute(village_u_id);
    }
}
