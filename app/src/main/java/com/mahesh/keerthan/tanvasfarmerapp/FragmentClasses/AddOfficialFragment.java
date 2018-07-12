package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class AddOfficialFragment extends Fragment {

    private View view;
    private ArrayList<District> districts = new ArrayList<>();
    private List<String> districtNames = new ArrayList<>();
    private Spinner districtSpinner;
    private Spinner villageSpinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_official_fragment,container,false);
        districtSpinner = view.findViewById(R.id.spinnerDistrictSelect);
        villageSpinner = view.findViewById(R.id.spinnerVillageSelect);
        return view;
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

            if(jsonArray != null){
                try{
                    JSONObject object;
                    for(int i = 0; i < jsonArray.length();i++){
                        object = jsonArray.getJSONObject(i);
                        districts.add(new District(object.getInt("district_id"),object.getString("en_district_name ")));
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
}
