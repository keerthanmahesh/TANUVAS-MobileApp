package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;

public class MainActivity extends AppCompatActivity {


    private UserClass user;
    private EditText username;
    private EditText password;
    private String passwordText;
    private TextView textTest;
    private login login;

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
    protected void onStop() {
        super.onStop();
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
                login = new login();
                login.execute(username.getText().toString());
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
        private boolean isConnectionError = false;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(MainActivity.this,"Loading....","We appreciate your patience",true,false);
        }

        @Override
        protected JSONObject doInBackground(String... strings) {
            String usernamex = strings[0];
            OkHttpClient client = new OkHttpClient();
            try {
                String response = APICall.GET(client, RequestBuilder.buildURL("username.php", new String[]{"username"},new String[]{usernamex}));
                if(response.equals("null"))
                    return null;
                JSONObject array = new JSONObject(response);
                return array;
            }catch (JSONException e){
                e.printStackTrace();
            }catch (IOException e){
               isConnectionError = true;
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONObject object) {
            super.onPostExecute(object);
            loading.dismiss();
            if(object==null){
                if (isConnectionError){
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Connection Error!").setMessage("Please check your internet connection and try again later.").setCancelable(true).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    }).show();
                    return;
                }

                username.setError("Invalid Username");
                return;
            }
            try {
                user = new UserClass(object.getInt("u_id"), object.getString("username"), object.getString("password"), object.getString("fullname"), object.getInt("district_id"), object.getString("phone_number"));
            }catch (JSONException e){
                e.printStackTrace();
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



