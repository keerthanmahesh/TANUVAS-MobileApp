package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FarmerClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.AddFarmerFragment;
import com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses.SelectDateFragment;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RealPathUtil;

import java.io.ByteArrayOutputStream;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class AddFarmerFragment2Activity extends AppCompatActivity implements View.OnClickListener {


    private Calendar calendar;
    private TextView dateView;
    private int year, month, day,village_id,district_id,age;
    private Button calender_button;
    private static int id1;
    private EditText aadharTV,phoneTV;
    private String phone_number,aadhar_number,outputDate;
    private ImageView imageButton;
    private Toolbar toolbar;
    private EditText firstNameTV,lastNameTV,address1TV,address2TV;
    private TextView dobTV,ageTV;
    private RadioGroup radioGroup;
    private boolean verified = true;
    private Villages village_selected;
    private District district_selected;
    private Uri ImageURI;
    private FarmerClass farmerClass;

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
        bindViews();
        Intent intent = getIntent();
        if(intent.hasExtra("farmer")){
            farmerClass = (FarmerClass) intent.getSerializableExtra("farmer");
            setViews();
        }
    }

    private void setViews(){
        firstNameTV.setText(farmerClass.getFirst_name());
        lastNameTV.setText(farmerClass.getLast_name());
        phoneTV.setText(farmerClass.getPhone_number());
        aadharTV.setText(farmerClass.getAadhar_number());
        address1TV.setText(farmerClass.getAddress_1());
        address2TV.setText(farmerClass.getAddress_2());
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date inputDate = formatter.parse(farmerClass.getDob(), new ParsePosition(0));
        formatter = new SimpleDateFormat("dd/MM/yyyy");
        dateView.setText(formatter.format(inputDate));
        ageTV.setText(farmerClass.getAge()+"");
        if(farmerClass.getGender().equals("Male"))
            radioGroup.check(R.id.radiobuttonMale);
        else if(farmerClass.getGender().equals("Female"))
            radioGroup.check(R.id.radiobuttonFemale);
        else
            radioGroup.check(R.id.radiobuttonOthers);
        byte[] b = farmerClass.getProfileImageDrawble();
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        imageButton.setImageBitmap(bmp);
    }

    public void bindViews(){
        toolbar = (Toolbar) findViewById(R.id.toolbar123);
        toolbar.setTitleTextAppearance(this,R.style.AmericanTypewriterSemibold);
        toolbar.setTitle("Add Farmer");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(Activity.RESULT_CANCELED);
                supportFinishAfterTransition();
            }
        });
        //setSupportActionBar(toolbar);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = AddFarmerFragment2Activity.this.getSharedPreferences("com.keerthan.tanuvas.selectedArea",Context.MODE_PRIVATE);
        String temp = sharedPreferences.getString("selectedVillage","");
        village_selected = gson.fromJson(temp,Villages.class);

        temp = sharedPreferences.getString("selectedDistrict","");
        district_selected = gson.fromJson(temp,District.class);
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
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        ageTV = findViewById(R.id.ageTextView);
        dobTV = dateView;
        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    first_name = firstNameTV.getText().toString();
                    last_name = lastNameTV.getText().toString();
                    aadhar_number = aadharTV.getText().toString();
                    phone_number = phoneTV.getText().toString();
                    address_1 = address1TV.getText().toString();
                    address_2 = address2TV.getText().toString();
                    dob = dobTV.getText().toString();
                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    switch (selectedId) {
                        case R.id.radiobuttonMale:
                            gender = "Male";
                            verified = true;
                            break;

                        case R.id.radiobuttonFemale:
                            gender = "Female";
                            verified = true;
                            break;

                        case R.id.radiobuttonOthers:
                            gender = "Others";
                            verified = true;
                            break;
                        default:
                            verified = false;
                    }
                    if(isComplete()){
                        Intent returnIntent = new Intent();
                        FarmerClass farmerClass = new FarmerClass(UUID.randomUUID().toString(),first_name,last_name,phone_number,aadhar_number,address_1,address_2,gender,outputDate,village_id,district_id);
                        farmerClass.setAge(age);
                        imageButton.setDrawingCacheEnabled(true);
                        Bitmap bitmap = imageButton.getDrawingCache();
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] b = baos.toByteArray();
                        farmerClass.setProfileImageDrawble(b);
                        returnIntent.putExtra("farmer",farmerClass);
                        setResult(Activity.RESULT_OK,returnIntent);
                        supportFinishAfterTransition();
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

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        supportFinishAfterTransition();
    }


    private Boolean isComplete(){

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        if(TextUtils.isEmpty(first_name)){
            firstNameTV.setError("Please Enter The First Name");
            return false;
        }
        if(TextUtils.isEmpty(last_name)){
            lastNameTV.setError("Please Enter The Second Name");
            return false;
        } if(phone_number.length() != 10){
            phoneTV.setError("Invalid Phone");
            return false;
        } if(aadhar_number.length() != 12){
            aadharTV.setError("Invalid Aadhar Number");
            return false;
        } if(TextUtils.isEmpty(address_1)){
            address1TV.setError("Please Enter an Address");
            return false;
        } if(TextUtils.isEmpty(address_2)){
            address2TV.setError("Please Enter an Address");
            return false;
        }

        if(dob.equals("Date Of Birth")){
            Snackbar.make(findViewById(android.R.id.content), "Please Enter The Date", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return false;
        }
        String age = ageTV.getText().toString();
        this.age = Integer.parseInt(age);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        switch (selectedId){
            case R.id.radiobuttonMale:
                gender="Male";
                break;

            case R.id.radiobuttonFemale:
                gender = "Female";
                break;

            case R.id.radiobuttonOthers:
                gender = "Others" ;
                break;
            default: Snackbar.make(findViewById(android.R.id.content), "Please Enter The Gender", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
                return false;
        }

        Date inputDate = formatter.parse(dob, new ParsePosition(0));
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        outputDate = formatter.format(inputDate);
        village_id = village_selected.getVillage_id();
        district_id = village_selected.getDistrict_id();
        return true;

    }
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
                    ImageURI = data.getData();
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
