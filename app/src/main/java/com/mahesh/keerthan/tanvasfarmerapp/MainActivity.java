package com.mahesh.keerthan.tanvasfarmerapp;

import android.animation.Animator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.podcopic.animationlib.library.AnimationType;
import com.podcopic.animationlib.library.StartSmartAnimation;

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
    private TextView textTest;

    @Override
    protected void onStart() {
        super.onStart();

        //StartSmartAnimation.startAnimation(layout, AnimationType.FadeIn,2000,0,false);
        //StartSmartAnimation.startAnimation(findViewById(R.id.text_input_layout),AnimationType.FadeIn,500,0,true);
        //StartSmartAnimation.startAnimation(findViewById(R.id.text_input_layout2),AnimationType.FadeIn,500,50,true);
       // StartSmartAnimation.startAnimation(findViewById(R.id.signInButton),AnimationType.FadeIn,500,100,true);
       //StartSmartAnimation.startAnimation(findViewById(R.id.forgotPass),AnimationType.FadeIn,500,150,true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button SignInButton = findViewById(R.id.signInButton),forgotPass = findViewById(R.id.forgotPass);
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
        RelativeLayout layout = findViewById(R.id.layout1);
        textTest = findViewById(R.id.texttest);
        TextInputLayout textInputLayout = findViewById(R.id.text_input_layout),textInputLayout1 = findViewById(R.id.text_input_layout2);
        layout.animate().alpha(1).setDuration(1500);
        textInputLayout.animate().alpha(1).setDuration(1500);
        textInputLayout1.animate().alpha(1).setDuration(1500).setStartDelay(150);
        SignInButton.animate().alpha(1).setDuration(1500).setStartDelay(300);
        forgotPass.animate().alpha(1).setDuration(1500).setStartDelay(450);
        textTest.animate().alpha(1).setDuration(1500);


    }




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



