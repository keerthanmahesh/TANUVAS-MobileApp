package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.SelectDateFragment;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RealPathUtil;

import java.util.Calendar;


public class AddFarmerFragment2Activity extends AppCompatActivity implements View.OnClickListener {


    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private Button calender_button;
    private static int id1;
    private EditText aadharTV,phoneTV;
    private String phone_number,aadhar_number;
    private ImageView imageButton;

    private EditText firstNameTV,lastNameTV,address1TV,address2TV;
    private TextView dobTV,ageTV;
    private RadioGroup radioGroup;
    private boolean verified = true;

    public static final int PICK_IMAGE = 1;
    private String realPath,first_name,last_name,dob,address_1,address_2,gender = "Others";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_farmer_fragment_2);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.Back));
    }

    /*public void bindViews(){
        calender_button = (Button) findViewById(R.id.DOBbuttonAddfarmer);
        calender_button.setOnClickListener(this);
        dateView = findViewById(R.id.DOBtextView);
        calendar = Calendar.getInstance();
        imageButton = findViewById(R.id.profPicImageButton);
        TextView village_name = findViewById(R.id.villageName);
        TextView district_name = findViewById(R.id.districtName);
        village_name.setText("Village: "+village_selected.getEn_village_name());
        district_name.setText("District: " + district_selected.getEn_district_name());
        Button donebtn = findViewById(R.id.donebtn);
        firstNameTV = (EditText) findViewById(R.id.firstName);
        lastNameTV = (EditText) findViewById(R.id.lastName);

        aadharTV = (EditText) findViewById(R.id.adhaarNumber);
        phoneTV = (EditText) findViewById(R.id.mobileNumber);
        address1TV = (EditText) findViewById(R.id.address1);

        address2TV = (EditText) findViewById(R.id.address2);

        dobTV = dateView;
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!firstNameTV.getText().toString().isEmpty()&&!lastNameTV.getText().toString().isEmpty()){
                    first_name = firstNameTV.getText().toString();
                    last_name = lastNameTV.getText().toString();
                    aadhar_number = aadharTV.getText().toString();
                    phone_number = phoneTV.getText().toString();
                    address_1 = address1TV.getText().toString();
                    address_2 = address2TV.getText().toString();
                    dob = dobTV.getText().toString();
                    radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    switch (selectedId){
                        case R.id.radiobuttonMale:
                            gender="Male";
                            verified = true;
                            break;

                        case R.id.radiobuttonFemale:
                            gender = "Female";
                            verified = true;
                            break;

                        case R.id.radiobuttonOthers:
                            gender = "Others" ;
                            verified = true;
                            break;
                        default: verified = false;
                    }
                    if(!TextUtils.isEmpty(gender) && !dobTV.getText().toString().equals("Date Of Birth")){
                        ageTV = view.findViewById(R.id.ageTextView);
                        genderandageTV.setText(ageTV.getText().toString() + " yrs, "+ gender);
                        genderandageTV.animate().translationY(-200).setDuration(2000).alpha(1).setStartDelay(100);
                        verified = true;
                    }else verified = false;

                    if(TextUtils.isEmpty(phone_number)){
                        phoneCardTV.setText("Phone Not Entered!");
                        verified = false;
                    }
                    else {
                        phoneCardTV.setText("Phone: +91 " + phone_number);
                        verified = true;
                    }

                    if(TextUtils.isEmpty(aadhar_number)){
                        aadharCardTV.setText("Aadhar Not Entered!");
                        verified = false;
                    }
                    else {
                        aadharCardTV.setText("Aadhar: " + aadhar_number);
                        verified = true;
                    }
                    if(imageButton.getDrawable().getConstantState() != getActivity().getResources().getDrawable(R.drawable.profpic1).getConstantState()){
                        ImageView profileImageCard = view.findViewById(R.id.cardViewProfileImage);
                        profileImageCard.setImageDrawable(imageButton.getDrawable());
                        verified = true;
                    }else verified = false;

                    if(TextUtils.isEmpty(address_1) || TextUtils.isEmpty(address_2)) verified = false; else verified = true;
                    nameTv.animate().translationY(-200).setDuration(2000).alpha(1);
                    aadharCardTV.animate().setDuration(2000).alpha(1).setStartDelay(100);
                    phoneCardTV.animate().setDuration(2000).alpha(1).setStartDelay(100);

                    if (verified) view.findViewById(R.id.verified).animate().alpha(1).setDuration(2000); else view.findViewById(R.id.verified).setAlpha(0);
                }
            }
        });

        View.OnClickListener pickImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        };
        imageButton.setOnClickListener(pickImageListener);

    }*/

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.DOBbuttonAddfarmer){
            android.support.v4.app.DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getSupportFragmentManager(), "DatePicker");
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

    public boolean checkPermissionREAD_EXTERNAL_STORAGE(
            final Context context) {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    showDialog("External storage", context,
                            Manifest.permission.READ_EXTERNAL_STORAGE);

                } else {
                    ActivityCompat
                            .requestPermissions(
                                    (Activity) context,
                                    new String[] { Manifest.permission.READ_EXTERNAL_STORAGE },
                                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }

        } else {
            return true;
        }
    }

    public void showDialog(final String msg, final Context context,
                           final String permission) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission necessary");
        alertBuilder.setMessage(msg + " permission is necessary");
        alertBuilder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions((Activity) context,
                                new String[] { permission },
                                MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // do your stuff
                } else {
                    Toast.makeText(this, "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                    final Uri ImageURI = data.getData();
                    if(checkPermissionREAD_EXTERNAL_STORAGE(this)){
                        realPath = RealPathUtil.getRealPathFromURI_API19(this,data.getData());
                    }
                    realPath = RealPathUtil.getRealPathFromURI_API19(this,data.getData());
                    imageButton.setImageURI(ImageURI);

                }
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE);
    }
}
