package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.Manifest;
import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.HomeActivity;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RealPathUtil;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;
import com.ramotion.foldingcell.FoldingCell;
import com.yarolegovich.slidingrootnav.SlidingRootNav;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Array;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import javax.xml.transform.Result;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

import static android.app.Activity.RESULT_OK;


public class AddFarmerFragment extends Fragment implements View.OnClickListener{


    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private View view;
    private Button calender_button;
    private static int id1;
    private Villages village_selected;
    private District district_selected;
    private TextView aadharTV,phoneTV;
    private String phone_number,aadhar_number;
    private ImageButton imageButton;
    public static final int PICK_IMAGE = 1;
    private String realPath;
    private FoldingCell fc,fc1,fc2;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.add_farmer_fragment,container,false);
        calender_button = (Button) view.findViewById(R.id.DOBbuttonAddfarmer);
        calender_button.setOnClickListener(this);
        dateView = view.findViewById(R.id.DOBtextView);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        getActivity().invalidateOptionsMenu();
        village_selected = (Villages) getArguments().getSerializable("village");
        imageButton = view.findViewById(R.id.profPicImageButton);
        district_selected = (District) getArguments().getSerializable("district");
        TextView village_name = view.findViewById(R.id.villageName);
        TextView district_name = view.findViewById(R.id.districtName);
        village_name.setText("Village: "+village_selected.getEn_village_name());
        district_name.setText("District: " + district_selected.getEn_district_name());
        View.OnClickListener pickImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        };
        imageButton.setOnClickListener(pickImageListener);
        fc = (FoldingCell) view.findViewById(R.id.folding_cell);
        fc1 = (FoldingCell) view.findViewById(R.id.folding_cell1);
        fc2 = (FoldingCell) view.findViewById(R.id.folding_cell2);
        fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });
        fc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc1.toggle(false);
            }
        });
        fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc2.toggle(false);
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.getView().setFocusableInTouchMode(true);
        this.getView().requestFocus();
        this.getView().setOnKeyListener( new View.OnKeyListener()
        {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event )
            {
                if( keyCode == KeyEvent.KEYCODE_BACK )
                {
                    if(fc.isUnfolded())
                        fc.fold(false);
                    if(fc1.isUnfolded())
                        fc1.fold(false);
                    if(fc2.isUnfolded())
                        fc2.fold(false);

                    return true;
                }
                return false;
            }
        } );
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.DOBbuttonAddfarmer){
            android.support.v4.app.DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getChildFragmentManager(), "DatePicker");
        }
    }


    public static AddFarmerFragment newInstance(Villages villageSelected, District districtSelected){
        AddFarmerFragment newFarmer = new AddFarmerFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("village",villageSelected);
        bundle.putSerializable("district",districtSelected);
        newFarmer.setArguments(bundle);
        return newFarmer;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save ) {
            EditText firstNameTV = (EditText) view.findViewById(R.id.firstName);
            String first_name = firstNameTV.getText().toString();
            EditText lastNameTV = (EditText) view.findViewById(R.id.lastName);
            String last_name = lastNameTV.getText().toString();
            aadharTV = (EditText) view.findViewById(R.id.adhaarNumber);
            aadhar_number = aadharTV.getText().toString();
            phoneTV = (EditText) view.findViewById(R.id.mobileNumber);
            phone_number = phoneTV.getText().toString();
            EditText address1TV = (EditText) view.findViewById(R.id.address1);
            String address_1 = address1TV.getText().toString();
            EditText address2TV = (EditText) view.findViewById(R.id.address2);
            String address_2 = address2TV.getText().toString();
            TextView dobTV = view.findViewById(R.id.DOBtextView);
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            String dob = dobTV.getText().toString();
            if(TextUtils.isEmpty(first_name)){
                firstNameTV.setError("Please Enter The First Name");
                return super.onOptionsItemSelected(item);
            }
            if(TextUtils.isEmpty(last_name)){
                lastNameTV.setError("Please Enter The Second Name");
                return super.onOptionsItemSelected(item);
            } if(phone_number.length() != 10){
                phoneTV.setError("Invalid Phone");
                return super.onOptionsItemSelected(item);
            } if(aadhar_number.length() != 12){
                aadharTV.setError("Invalid Aadhar Number");
                return super.onOptionsItemSelected(item);
            } if(TextUtils.isEmpty(address_1)){
                address1TV.setError("Please Enter an Address");
                return super.onOptionsItemSelected(item);
            } if(TextUtils.isEmpty(address_2)){
                address2TV.setError("Please Enter an Address");
                return super.onOptionsItemSelected(item);
            }
            RadioGroup radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
            int selectedId = radioGroup.getCheckedRadioButtonId();
            String gender = "Others";
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
                default: Snackbar.make(view, "Please Enter The Gender", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                    return super.onOptionsItemSelected(item);
            }

            if(dob.equals("Date Of Birth")){
                Snackbar.make(view, "Please Enter The Date", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return super.onOptionsItemSelected(item);
            }
            Date inputDate = formatter.parse(dob, new ParsePosition(0));
            formatter = new SimpleDateFormat("yyyy-MM-dd");
            String outputDate = formatter.format(inputDate);
            int village_id = village_selected.getVillage_id(),district_id = village_selected.getDistrict_id();

            new updateFarmer().execute(UUID.randomUUID().toString(),first_name,last_name,aadhar_number,phone_number,address_1,address_2,gender,outputDate,Integer.toString(village_id),Integer.toString(district_id),realPath);

        }
        return super.onOptionsItemSelected(item);


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
                    Toast.makeText(getActivity(), "GET_ACCOUNTS Denied",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions,
                        grantResults);
        }
    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem save_item = menu.findItem(R.id.action_save);
        save_item.setVisible(true);
        MenuItem settings_item = menu.findItem(R.id.action_settings);
        settings_item.setVisible(false);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case PICK_IMAGE:
                if(resultCode == RESULT_OK){
                        final Uri ImageURI = data.getData();
                        if(checkPermissionREAD_EXTERNAL_STORAGE(getActivity())){
                            realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(),data.getData());
                        }
                        realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(),data.getData());
                        imageButton.setImageURI(ImageURI);

                }
        }
    }

    private class updateFarmer extends AsyncTask<String,Integer,String>{


        private ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(),"Uploading....",null,true,true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String res = null;
            try{
                String u_id = strings[0],
                        first_name = strings[1],
                        last_name = strings[2],
                        aadhar = strings[3],
                        phone = strings[4],
                        address_1 = strings[5],
                        address_2 = strings[6],
                        gender = strings[7],
                        dob = strings[8],
                        village_id = strings[9],
                        dirstrict_id = strings[10],
                        imagePath = strings[11];

                File sourceFile = new File(imagePath);
                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
                String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image",filename,RequestBody.create(MEDIA_TYPE_JPG,sourceFile))
                        .build();
                OkHttpClient client = new OkHttpClient();
                res = APICall.POST(client,RequestBuilder.buildURL("saveFarmer.php",
                        new String[]{"u_id","first_name","last_name","aadhar_number","phone_number","address_1","address_2","gender","dob","village_id","district_id"},
                        new String[]{u_id,first_name,last_name,aadhar,phone,address_1,address_2,gender,dob,village_id,dirstrict_id}),requestBody);

            }catch (IOException e){
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            dateView.setText(s);
            if(s.equals("New record created successfully")){
                Snackbar.make(view, "New Farmer Added Successfully", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new QuestionFragment()).commit();
                //NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                //navigationView.setCheckedItem(R.id.Questions);
            }else if(s.contains("Duplicate entry")){
                if(s.contains("aadhar_number"))
                    aadharTV.setError("Aadhar Number Already Exists");
                else if(s.contains("phone_number"))
                    phoneTV.setError("Mobile Already Exists");
            }

        }
    }

}



