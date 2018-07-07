package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.google.gson.Gson;
import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FarmerClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class QuestionFragment extends Fragment {

    private View view;
    private FarmerClass farmer;
    private Villages village_selected;
    private District district_selected;
    private EditText aadhar,phone;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.question_fragment,container,false);
        Button questionnaireSearchButton = view.findViewById(R.id.questionnaireSearchButton);
        View.OnClickListener searchButtonListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                aadhar = view.findViewById(R.id.questionnaireAadharNumberSearch);
                phone = view.findViewById(R.id.questionnairePhoneNumberSearch);

                if(TextUtils.isEmpty(phone.getText().toString()) && TextUtils.isEmpty(aadhar.getText().toString())){
                    Snackbar.make(view,"Error: Please Enter Any One Option",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
                else if(TextUtils.isEmpty(phone.getText().toString()) && aadhar.getText().toString().length() != 12){
                    aadhar.setError("Invalid Aadhar Number");
                }
                else if(TextUtils.isEmpty(aadhar.getText().toString()) && phone.getText().toString().length() != 10){
                    phone.setError("Invalid Phone Number");
                }
                else if(!TextUtils.isEmpty(aadhar.getText().toString()) && !TextUtils.isEmpty(phone.getText().toString())){
                    Snackbar.make(view,"Error: Please Enter Only One Option",Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
                else {
                    getFarmer(aadhar.getText().toString(),phone.getText().toString());
                }
            }
        };
        questionnaireSearchButton.setOnClickListener(searchButtonListener);


        return view;
    }

    private void getFarmer(final String aadhar_number, final String phone_number){
        if(TextUtils.isEmpty(aadhar_number)) {
            new getFarmer().execute(phone_number,"phone");
        }else {
            new getFarmer().execute(aadhar_number,"aadhar");
        }
    }


    private class getFarmer extends AsyncTask<String,Void,JSONArray>{


        private ProgressDialog progressDialog;
        @Override
        protected JSONArray doInBackground(String... strings) {
            if(strings[1].equals("aadhar")){
                String aadhar = strings[0];
                OkHttpClient client = new OkHttpClient();
                try {
                    String response = APICall.GET(client, RequestBuilder.buildURL("justTesting.php",new String[]{"aadhar_number"},new String[]{aadhar}));
                    if(response.equals("null")){
                        Snackbar.make(view, "Aadhar Number not found", Snackbar.LENGTH_LONG).show();
                    }else{
                        JSONArray array = new JSONArray(response);
                        return array;
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }else{
                String phone = strings[0];
                OkHttpClient client = new OkHttpClient();
                try {
                    String response = APICall.GET(client, RequestBuilder.buildURL("justTesting.php",new String[]{"phone_number"},new String[]{phone}));
                    if(response.equals("null")){
                        Snackbar.make(view, "Phone Number not found", Snackbar.LENGTH_LONG).show();
                    }else{
                        JSONArray array = new JSONArray(response);
                        return array;
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(),"Loading...","We appreciate your patience");
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            progressDialog.dismiss();
            if(jsonArray != null){
                try {
                    JSONObject object = jsonArray.getJSONObject(0);
                    String farmerJson = object.toString();
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.keerthan.tanuvas.tempFarmer",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("farmer",farmerJson);
                    editor.commit();
                    Intent intent = new Intent(getActivity(),QuestionFragment2.class);
                    startActivity(intent);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }
    }
}
