package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.gson.JsonArray;
import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AddOfficialFragment extends Fragment {

    private View view;
    private ArrayList<District> districts = new ArrayList<>();
    private ArrayList<Villages> villages = new ArrayList<>();
    private List<String> districtNames = new ArrayList<>();
    private List<String> villageNames = new ArrayList<>();
    private Spinner districtSpinner;
    private Spinner villageSpinner;
    private Boolean isSuperAdmin = false;
    private UserClass user;
    private ProgressDialog progressDialog ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_official_fragment,container,false);
        districtSpinner = view.findViewById(R.id.spinnerDistrictSelect);
        villageSpinner = view.findViewById(R.id.spinnerVillageSelect);
        final RelativeLayout containerView = view.findViewById(R.id.containerView);
        new getDistricts().execute();
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                new getVillages().execute(districts.get(position).getDistrict_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                new getVillages().execute(districts.get(0).getDistrict_id());
            }
        });

        final EditText usernameET = view.findViewById(R.id.addOfficialUsernameEditText), passwordET = view.findViewById(R.id.addOfficialPasswordEditText),
                mobileET = view.findViewById(R.id.addOfficialPhonenumberEditText), fullnameET = view.findViewById(R.id.addOfficialFullnameEditText);
        CheckBox checkBox = view.findViewById(R.id.superAdminCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isSuperAdmin = true;
                else
                    isSuperAdmin = false;
            }
        });

        Button goButton = view.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new UserClass(UUID.randomUUID().toString(),usernameET.getText().toString(),passwordET.getText().toString(),
                        fullnameET.getText().toString(),districts.get(districtSpinner.getSelectedItemPosition()).getDistrict_id(),
                        mobileET.getText().toString(),isSuperAdmin? 1:0);

                new uploadUser().execute();

            }
        });

        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                containerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                containerView.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }

    private class getVillages extends AsyncTask<Integer, Void, JSONArray>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading...","We appreciate your patience");

        }

        @Override
        protected JSONArray doInBackground(Integer... integers) {

            String district_id = integers[0].toString();
            OkHttpClient client = new OkHttpClient();
            //RequestBody requestBody = new MultipartBody.Builder()
                    //.addFormDataPart("district_id",district_id).build();
            try {
                JSONArray array = new JSONArray(APICall.GET(client,RequestBuilder.buildURL("getVillages.php",new String[]{"district_id"},new String[]{district_id})));
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
            progressDialog.dismiss();
            villages.clear();
            villageNames.clear();
            if(jsonArray != null){
                JSONObject object;
                for(int i=0; i<jsonArray.length();i++){
                    try {
                        object = jsonArray.getJSONObject(i);
                        villages.add(new Villages(object.getInt("village_id"),object.getInt("district_id"),object.getString("en_village_name"),0,null));
                        villageNames.add(villages.get(i).getEn_village_name());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,villageNames);
            villageSpinner.setAdapter(adapter);
        }
    }

    private class getDistricts extends AsyncTask<Void,Void,JSONArray>{

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading...","We appreciate your patience");
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            try {
                JSONArray array = new JSONArray(APICall.GET(client, RequestBuilder.buildURL("getDistricts.php",null,null)));
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
            progressDialog.dismiss();

            if(jsonArray != null){
                try{
                    JSONObject object;
                    for(int i = 0; i < jsonArray.length();i++){
                        object = jsonArray.getJSONObject(i);
                        districts.add(new District(object.getInt("district_id"),object.getString("en_district_name")));
                        districtNames.add(districts.get(i).getEn_district_name());
                    }

                    ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(AddOfficialFragment.this.getActivity(),android.R.layout.simple_spinner_dropdown_item,districtNames);
                    districtSpinner.setAdapter(districtAdapter);
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private final class uploadUser extends AsyncTask<Void,Void,String>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading...","We appreciate your patience");
        }

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("u_id",user.getU_id())
                    .addFormDataPart("fullname",user.getFullname())
                    .addFormDataPart("username",user.getUsername())
                    .addFormDataPart("password",user.getPassword())
                    .addFormDataPart("district_id",Integer.toString(user.getDistrict_id()))
                    .addFormDataPart("phone_number",user.getPhone_number())
                    .addFormDataPart("isSuperAdmin",Integer.toString(user.getIsSuperAdmin())).build();

            try {
                String resp = APICall.POST(client,RequestBuilder.buildURL("insertUser.php",null,null),requestBody);
                Log.d("resp",resp);
                return resp;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s.equals("New Record Created Successfully")){
                new updateVillages().execute();
            }
        }
    }

    private class updateVillages extends AsyncTask<Void,Void,String>{

        @Override
        protected String doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("u_id",user.getU_id())
                    .addFormDataPart("village_id",Integer.toString(villages.get(villageSpinner.getSelectedItemPosition()).getVillage_id()))
                    .addFormDataPart("district_id",Integer.toString(user.getDistrict_id()))
                    .build();
            try {
                String response = APICall.POST(client,RequestBuilder.buildURL("updateVillage.php",null,null),requestBody);
                return response;
            }catch (IOException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            if(s.equals("Record Updated Successfully")){
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Success").setMessage(s).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setCancelable(true).show();
            }
        }
    }
}
