package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseQuestion;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseVillage;
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
    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    private com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseUser firebaseUser;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        Button SignInButton = findViewById(R.id.signInButton),forgotPass = findViewById(R.id.forgotPass);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        View.OnClickListener SignInPressed = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isETFilled()){
                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,"Loading...","We appreciate your patience");
                    passwordText = password.getText().toString();
                    mAuth.signInWithEmailAndPassword(username.getText().toString(),passwordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if(task.isSuccessful()){
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                        databaseReference.child("Users/"+user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                firebaseUser = dataSnapshot.getValue(com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseUser.class);
                                                Gson gson = new Gson();
                                                String json;
                                                SharedPreferences sharedPreferences = getSharedPreferences("com.keerthan.tanuvas.loggedInUser", Context.MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                                json = gson.toJson(firebaseUser);
                                                editor.putString("firebaseUser",json);
                                                editor.commit();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                                        doLogin();
                                    }else {
                                        Exception e = task.getException();
                                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                                        builder.setTitle("Error!").setMessage(e.getMessage()).
                                                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.cancel();
                                                    }
                                                }).setCancelable(true).show();
                                    }
                                }
                            });
                }
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

    private Boolean isETFilled(){
        if(TextUtils.isEmpty(username.getText().toString())){
            username.setError("Please Enter the Email");
            return false;
        }else if(TextUtils.isEmpty(password.getText().toString())){
            password.setError("Please enter the Password");
            return false;
        }
        return true;
    }

    private void doLogin(){
        Intent signInSuccess = new Intent(MainActivity.this, VillageSelect.class);
        startActivity(signInSuccess);
    }

    /*public class login extends AsyncTask<String,Void,JSONObject>{

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
                user = new UserClass(object.getString("u_id"), object.getString("username"), object.getString("password"), object.getString("fullname"), object.getInt("district_id"), object.getString("phone_number"),object.getInt("isSuperAdmin"));
            }catch (JSONException e){
                e.printStackTrace();
            }
            if (user.getPassword().equals(passwordText)){
               doLogin();
            }else{
                password.setError("Incorrect Password");
            }

        }
    }*/
}



