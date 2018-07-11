package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.google.gson.Gson;
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
    private Boolean isLocationSet = false;

    //private  JSONArray array;
    //private  JSONArray array1;
    private static VillageSelect instance = null;
    private Villages selectedVillage = new Villages(0, 0, null, 0, 0);
    private ProgressDialog dialog;
    private double latitude, longitude;
    final int LOCATION_PERMISSION_REQUEST_CODE = 121;
    private  LocationManager locationManager;
    private LocationListener locationListener;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_village_select);
        instance = this;
        Button goButton = findViewById(R.id.goButton);
        View.OnClickListener goButtonPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(isLocationSet){
                    if(measure(latitude,longitude,selectedVillage.getLatitude(),selectedVillage.getLongitude()) > 5000){
                        showError();
                    }else
                        proceed();
                }else{
                    progressDialog = ProgressDialog.show(VillageSelect.this,"Getting Location...","We appreciate your patience");
                }

                //startActivity(villageSelected);
            }
        };

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                if(!isLocationSet){
                    isLocationSet = true;
                    if(progressDialog.isShowing()){
                        progressDialog.dismiss();
                        if(measure(latitude,longitude,selectedVillage.getLatitude(),selectedVillage.getLongitude()) > 5000){
                            showError();
                        }else{
                            proceed();
                        }
                    }

                }

            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        if (ActivityCompat.checkSelfPermission(VillageSelect.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(VillageSelect.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= 23) { // Marshmallow

                ActivityCompat.requestPermissions(VillageSelect.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,locationListener);
        }

        goButton.setOnClickListener(goButtonPressed);
        Intent intent = getIntent();
        user = (UserClass) intent.getSerializableExtra("user");
        new getUserVillages().execute(user.getU_id());
        layout = (BlurLayout) findViewById(R.id.cardView);

    }


    private void showError(){
        AlertDialog.Builder builder = new AlertDialog.Builder(VillageSelect.this);
        builder.setTitle("Error!").setMessage("You seem to be in the wrong place. Please try again later").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        }).setCancelable(false).show();
    }
    private void proceed(){
        Intent villageSelected = new Intent(VillageSelect.this, HomeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("user", user);
        bundle.putSerializable("village", selectedVillage);
        villageSelected.putExtras(bundle);
        SharedPreferences sharedPreferences = VillageSelect.this.getSharedPreferences("com.keerthan.tanuvas.selectedArea", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(selectedVillage);
        editor.putString("selectedVillage", json);
        editor.commit();
        startActivity(villageSelected);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,1000,10,locationListener);
        } else {

            AlertDialog.Builder builder = new AlertDialog.Builder(VillageSelect.this);
            builder.setTitle("Sorry!").setMessage("You can't access this app withput location. Please enable location from settings and try again later.").setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    finish();
                }
            }).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
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
                    village.setLatitude(object.getDouble("latitude"));
                    village.setLongitude(object.getDouble("longitude"));
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

    private double measure(double lat1, double lon1, double lat2, double lon2){  // generally used geo measurement function
        double R = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = R * c;
        return d * 1000; // meters
    }
}
