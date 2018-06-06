package com.mahesh.keerthan.tanvasfarmerapp;

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

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    private UserClass user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button SignInButton = findViewById(R.id.signInButton);
        final EditText username = findViewById(R.id.username);
        final EditText password = findViewById(R.id.password);
        View.OnClickListener SignInPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(username.getText().toString(),password.getText().toString(),v);
            }
        };
        SignInButton.setOnClickListener(SignInPressed);
    }

    private void login(final String username, final String password,final View v){

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
                    startActivity(signInSuccess);
                }
                else{
                    Snackbar.make(v, "Incorrect Password", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        };
        asyncTask.execute(username);
    }

}



