package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FarmerClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditFarmerDetailsActivity extends AppCompatActivity {

    private FarmerClass farmer;
    private Villages village;
    private District district;
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_farmer_details);
        invalidateOptionsMenu();
        Intent incomingIntent = getIntent();
        Bundle bundle = new Bundle();
        bundle = incomingIntent.getExtras();
        farmer = (FarmerClass) bundle.getSerializable("farmer");
        village = (Villages) bundle.getSerializable("village");
        district = (District) bundle.getSerializable("district");
        view = this.view;
        inflateLayouts();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_farmer_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save ) {
            EditText phoneTV = (EditText) findViewById(R.id.mobileNumber);
            String phone_number = phoneTV.getText().toString();
            EditText address1TV = (EditText) findViewById(R.id.address1);
            String address_1 = address1TV.getText().toString();
            EditText address2TV = (EditText) findViewById(R.id.address2);
            String address_2 = address2TV.getText().toString();
            int village_id = farmer.getVillage_id(),district_id = farmer.getDistrict_id();
            updateFarmer(farmer.getU_id(),phone_number,address_1,address_2);
        }
        return super.onOptionsItemSelected(item);
    }

    private void inflateLayouts(){
        EditText first_name = findViewById(R.id.firstName);
        first_name.setFocusable(false);
        first_name.setEnabled(false);
        first_name.setCursorVisible(false);
        first_name.setKeyListener(null);
        first_name.setText(farmer.getFirst_name());

        EditText last_name = findViewById(R.id.lastName);
        last_name.setFocusable(false);
        last_name.setEnabled(false);
        last_name.setCursorVisible(false);
        last_name.setKeyListener(null);
        last_name.setText(farmer.getLast_name());

        EditText phone_number = findViewById(R.id.mobileNumber);
        phone_number.setText(farmer.getPhone_number());

        EditText aadhar_number = findViewById(R.id.adhaarNumber);
        aadhar_number.setFocusable(false);
        aadhar_number.setEnabled(false);
        aadhar_number.setCursorVisible(false);
        aadhar_number.setKeyListener(null);
        aadhar_number.setText(farmer.getAadhar_number());

        EditText address_1 = findViewById(R.id.address1);
        address_1.setText(farmer.getAddress_1());

        EditText address_2 = findViewById(R.id.address2);
        address_2.setText(farmer.getAddress_2());

        TextView village_name = findViewById(R.id.villageName);
        village_name.setText(village.getEn_village_name());

        TextView district_name = findViewById(R.id.districtName);
        district_name.setText(district.getEn_district_name());

       RadioGroup radioGroup = findViewById(R.id.radioGroup);
       radioGroup.getChildAt(0).setEnabled(false);
       radioGroup.getChildAt(1).setEnabled(false);
       radioGroup.getChildAt(2).setEnabled(false);

        switch(farmer.getGender()){
            case "Male":
                radioGroup.check(R.id.radiobuttonMale);
                break;
            case "Female":
                radioGroup.check(R.id.radiobuttonFemale);
                break;
            case "Others":
                radioGroup.check(R.id.radiobuttonOthers);
                break;
        }

    }

    public void updateFarmer(final String u_id, final String phone, final String address_1, final String address_2){
        AsyncTask<String,Void,String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.45/~vandit/editfarmerdetails.php?u_id=" + u_id+"&phone_number="+phone+"&address_1="+address_1+"&address_2="+address_2).build();

                try{
                    Response response = client.newCall(request).execute();
                    String message = response.body().string();
                    return message;


                }catch( IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("Record edited successfully")){

                    finish();
                    Snackbar.make(HomeActivity.instance.findViewById(android.R.id.content),"Details Edited Successfully",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            }
        };
        asyncTask.execute(u_id);
    }
}

