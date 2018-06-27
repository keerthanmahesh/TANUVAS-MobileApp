package com.mahesh.keerthan.tanvasfarmerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private UserClass user;
    private EditText username;
    private EditText password;
    private String passwordText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button SignInButton = findViewById(R.id.signInButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        View.OnClickListener SignInPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordText = password.getText().toString();
                new login().execute(username.getText().toString());
            }
        };
        SignInButton.setOnClickListener(SignInPressed);
    }

   /* private void login(final String username, final String password,final View v){

        AsyncTask<String,Void,JSONObject> asyncTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.45/~vandit/username.php?username=" + username).build();

                try{
                    Response response = client.newCall(request).execute();

                    JSONArray array = new JSONArray(response.body().string());
                    JSONObject object = array.getJSONObject(0);
                    user = new UserClass(object.getInt("u_id"), object.getString("username"), object.getString("password"), object.getString("fullname"), object.getInt("district_id"), object.getString("phone_number"));

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
                if(user.getPassword().equals(password)){
                    Intent signInSuccess = new Intent(MainActivity.this, VillageSelect.class);
                    signInSuccess.putExtra("user",user);
                    startActivity(signInSuccess);
                }
                else{
                    Snackbar.make(v, "Incorrect Password", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        };
        asyncTask.execute(username);
    }*/

    public class login extends AsyncTask<String,Void,JSONObject>{

        private ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this,"Loading....","We appreciate your patience",true,false);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String username = strings[0];
            OkHttpClient client = new OkHttpClient();
            try {
                JSONObject array = new JSONObject(APICall.GET(client,RequestBuilder.buildURL("username.php", new String[]{"username"},new String[]{username})));
                return array;
            }catch (JSONException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            loading.dismiss();
            try {
                user = new UserClass(object.getInt("u_id"), object.getString("username"), object.getString("password"), object.getString("fullname"), object.getInt("district_id"), object.getString("phone_number"));
            }catch (JSONException e){
                username.setError("Invalid Username");
            }
            if (user.getPassword().equals(passwordText)){
                Intent signInSuccess = new Intent(MainActivity.this, VillageSelect.class);
                signInSuccess.putExtra("user",user);
                startActivity(signInSuccess);
            }else{
                password.setError("Incorrect Password");
            }

        }
    }
}



