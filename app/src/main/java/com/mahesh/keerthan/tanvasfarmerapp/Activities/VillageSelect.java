package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserVillagesClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;
import com.wonderkiln.blurkit.BlurLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class VillageSelect extends AppCompatActivity {

    private UserClass user;
    private List<UserVillagesClass> villagesClass = new ArrayList<>();
    private List<Villages> villages = new ArrayList<>();
    private BlurLayout layout;

    //private  JSONArray array;
    //private  JSONArray array1;
    private static VillageSelect instance = null;
    private Villages selectedVillage = new Villages(0,0,null,0,0);
    private ProgressDialog dialog;
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
        new getUserVillages().execute(user.getU_id());
        layout = (BlurLayout) findViewById(R.id.cardView);

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    private class getUserVillages extends AsyncTask<Integer,Void,JSONArray>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(VillageSelect.this,"Loading....","We appreciate your patience",true,false);
        }

        @Override
        protected JSONArray doInBackground(Integer... integers) {
            String u_id = integers[0].toString();
            OkHttpClient client = new OkHttpClient();
            try{
                JSONArray array = new JSONArray(APICall.GET(client, RequestBuilder.buildURL("justtesting.php",new String[]{"u_id"},new String[]{u_id})));
                for(int i=0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    UserVillagesClass uservillagesClass = new UserVillagesClass(object.getInt("s_no"), object.getInt("u_id"), object.getInt("district_id"), object.getInt("village_id"));
                    VillageSelect.this.villagesClass.add(uservillagesClass);
                }
                return array;
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            if(jsonArray!=null){
                new getVillages().execute(user.getU_id());
            }
            else {
                dialog.dismiss();
            }
        }
    }

    private class getVillages extends AsyncTask<Integer,Void,JSONArray>{
        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            dialog.dismiss();
            Spinner dropdown = findViewById(R.id.spinner1);
            final List<String> items = new ArrayList<>();
            for (int i =0;i<jsonArray.length();i++){
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

        @Override
        protected JSONArray doInBackground(Integer... integers) {
            String village_u_id = integers[0].toString();
            OkHttpClient client = new OkHttpClient();
            try{
                JSONArray array = new JSONArray(APICall.GET(client,RequestBuilder.buildURL("justtesting.php",new String[]{"village_u_id"}, new String[]{village_u_id})));
                for(int i = 0; i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    Villages village = new Villages(object.getInt("village_id"), object.getInt("district_id"), object.getString("en_village_name"), object.getInt("allocated"),object.getInt("u_id"));
                    VillageSelect.this.villages.add(village);
                }
                return array;
            }catch (IOException e){
                e.printStackTrace();
            }catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
