package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FarmerClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.Activities.EditFarmerDetailsActivity;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditFarmerFragment extends Fragment {

    private View view;
    private FarmerClass farmer;
    private Villages village_selected;
    private District district_selected;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.edit_farmer_fragment,container,false);

        Button searchbutton = view.findViewById(R.id.searchButton);
        View.OnClickListener searchBtnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText aadhar = view.findViewById(R.id.aadharNumberSearch);
                EditText phone = view.findViewById(R.id.phoneNumberSearch);
                if(TextUtils.isEmpty(aadhar.getText().toString()) && TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(view, "Error: Please Enter Any One Option", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if(TextUtils.isEmpty(phone.getText().toString())&& aadhar.getText().toString().length() != 12){
                    aadhar.setError("Invalid Aadhar Number");
                }else if(TextUtils.isEmpty(aadhar.getText().toString())&& phone.getText().toString().length() != 10){
                    phone.setError("Invalid Phone Number");
                }else if(!TextUtils.isEmpty(aadhar.getText().toString()) && !TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(view, "Error: Please Enter Only One Option", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{
                    getFarmer(aadhar.getText().toString(),phone.getText().toString());
                }

            }
        };
        searchbutton.setOnClickListener(searchBtnListener);


        village_selected = (Villages) getArguments().getSerializable("village");
        district_selected = (District) getArguments().getSerializable("district");
        return view;
    }

    private void getFarmer(final String aadhar_number, final String phone_number){
        if (TextUtils.isEmpty(aadhar_number)){
            AsyncTask<String,Void,JSONArray> asyncTask = new AsyncTask<String, Void, JSONArray>() {
                @Override
                protected JSONArray doInBackground(String... strings) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://192.168.1.45/~vandit/justTesting.php?phone_number=" + phone_number).build();

                    try{
                        Response response = client.newCall(request).execute();

                        JSONArray array = new JSONArray(response.body().string());
                        JSONObject object = array.getJSONObject(0);
                        farmer = new FarmerClass(object.getString("u_id"), object.getString("first_name"), object.getString("last_name"), object.getString("phone_number"), object.getString("aadhar_number"), object.getString("address_1"),object.getString("address_2"),object.getString("gender"),object.getString("dob"),object.getInt("village_id"),object.getInt("district_id"));

                    }catch( IOException e){
                        e.printStackTrace();
                    }catch( JSONException e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONArray jsonArray) {
                    super.onPostExecute(jsonArray);

                    Intent intent = new Intent(getActivity(), EditFarmerDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("farmer",farmer);
                    bundle.putSerializable("village",village_selected);
                    bundle.putSerializable("district",district_selected);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };
            asyncTask.execute(phone_number);

        }else{
            AsyncTask<String,Void,JSONArray> asyncTask = new AsyncTask<String, Void, JSONArray>() {
                @Override
                protected JSONArray doInBackground(String... strings) {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder().url("http://192.168.1.45/~vandit/justTesting.php?aadhar_number=" + aadhar_number).build();

                    try{
                        Response response = client.newCall(request).execute();
                        JSONArray array = new JSONArray(response.body().string());
                        JSONObject object = array.getJSONObject(0);
                        farmer = new FarmerClass(object.getString("u_id"), object.getString("first_name"), object.getString("last_name"), object.getString("phone_number"), object.getString("aadhar_number"), object.getString("address_1"),object.getString("address_2"),object.getString("gender"),object.getString("dob"),object.getInt("village_id"),object.getInt("district_id"));

                    }catch( IOException e){
                        e.printStackTrace();
                    }catch( JSONException e){
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(JSONArray jsonArray) {
                    super.onPostExecute(jsonArray);

                    Intent intent = new Intent(getActivity(), EditFarmerDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("farmer",farmer);
                    bundle.putSerializable("village",village_selected);
                    bundle.putSerializable("district",district_selected);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            };
            asyncTask.execute(phone_number);
        }
    }

    public static EditFarmerFragment newInstance(Villages villageSelected, District districtSelected){
        EditFarmerFragment newFarmer = new EditFarmerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("village",villageSelected);
        bundle.putSerializable("district",districtSelected);
        newFarmer.setArguments(bundle);
        return newFarmer;
    }
}
