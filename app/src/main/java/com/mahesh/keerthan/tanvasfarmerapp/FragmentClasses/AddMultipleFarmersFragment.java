package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.HomeActivity;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RealPathUtil;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import java.io.File;
import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class AddMultipleFarmersFragment extends Fragment implements InputFarmersDialog.Callback,View.OnClickListener {

    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private View view;
    private Button calender_button;
    private static int id1;
    private Villages village_selected;
    private District district_selected;
    private Context context;
    private int village_id,district_id;
    private int left;
    private EditText firstNameTV,lastNameTV,aadharTV,address1TV,address2TV,phoneTV;
    private TextView dobTV;
    private String first_name,last_name,aadhar_number,address_1,address_2,phone_number,outputDate,gender;
    private RadioGroup radioGroup;
    private String saveornext = null;
    private ImageButton imageButton;
    public static final int PICK_IMAGE = 1;
    private String realPath;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = getActivity();
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("numberoffarmers",1);
        editor.apply();

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
        district_selected = (District) getArguments().getSerializable("district");
        TextView village_name = view.findViewById(R.id.villageName);
        TextView district_name = view.findViewById(R.id.districtName);
        village_name.setText("Village: "+village_selected.getEn_village_name());
        district_name.setText("District: " + district_selected.getEn_district_name());
        imageButton = view.findViewById(R.id.profPicImageButton);
        View.OnClickListener pickImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        };
        imageButton.setOnClickListener(pickImageListener);
        showDialog();
        return view;
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,PICK_IMAGE);
    }

    public static AddMultipleFarmersFragment newInstance(Villages villageSelected, District districtSelected){
        AddMultipleFarmersFragment newFarmer = new AddMultipleFarmersFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("village",villageSelected);
        bundle.putSerializable("district",districtSelected);
        newFarmer.setArguments(bundle);
        return newFarmer;
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.DOBbuttonAddfarmer){
            android.support.v4.app.DialogFragment newFragment = new SelectDateFragment();
            newFragment.show(getChildFragmentManager(), "DatePicker");
        }
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
    public void Done(int farmers) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("numberoffarmers",farmers);
        editor.apply();
        //number = farmers;
        getActivity().invalidateOptionsMenu();
    }

    public void showDialog(){
        InputFarmersDialog dialog = new InputFarmersDialog();
        dialog.setTargetFragment(this,1);
        dialog.show(getActivity().getSupportFragmentManager(),"InputFarmersDialog");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save ) {
            if(!getValues())
                if(!isThereError())
                    new updateFarmers().execute(UUID.randomUUID().toString(),first_name,last_name,aadhar_number,phone_number,address_1,address_2,gender,outputDate,Integer.toString(village_id),Integer.toString(district_id),realPath,"0");
        }
        if(id == R.id.action_next){
            if(!getValues())
                if(!isThereError())
                    new updateFarmers().execute(UUID.randomUUID().toString(),first_name,last_name,aadhar_number,phone_number,address_1,address_2,gender,outputDate,Integer.toString(village_id),Integer.toString(district_id),realPath,"1");
            /*number--;
            getActivity().invalidateOptionsMenu();*/

        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE).getInt("numberoffarmers",1)/*number*/ == 1){
            MenuItem save_item = menu.findItem(R.id.action_save);
            save_item.setVisible(true);
        }
        else if(context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE).getInt("numberoffarmers",1)/* number*/ > 1){
            MenuItem save_item = menu.findItem(R.id.action_save);
            save_item.setVisible(false);
            MenuItem next_item = menu.findItem(R.id.action_next);
            next_item.setVisible(true);
        }

        MenuItem settings_item = menu.findItem(R.id.action_settings);
        settings_item.setVisible(false);
    }

    /*public void updateFarmer(final String first_name, final String last_name, final String aadhar, final String phone, final String address_1, final String address_2, final String gender, final String dob, final int village_id, final int dirstrict_id,final int saveornext){
        final String u_id = UUID.randomUUID().toString();
        AsyncTask<String,Void,String> asyncTask = new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder().url("http://192.168.1.45/~vandit/saveFarmer.php?u_id=" + u_id+"&first_name="+first_name+"&last_name="+last_name+"&aadhar_number="+aadhar+"&phone_number="+phone+"&address_1="+address_1+"&address_2="+address_2+"&gender="+gender+"&dob="+dob+"&village_id="+village_id+"&district_id="+dirstrict_id).build();

                try{
                    Response response = client.newCall(request).execute();
                    String message = response.body().string();
                    return message;


                }catch( IOException e){
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if(s.equals("New record created successfully")){
                    Snackbar.make(view, "New Farmer Added Successfully", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    if(saveornext == 0){
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new QuestionFragment()).commit();
                        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                        navigationView.setCheckedItem(R.id.Questions);
                    }
                    else{
                        firstNameTV.setText("");
                        lastNameTV.setText("");
                        aadharTV.setText("");
                        phoneTV.setText("");
                        address1TV.setText("");
                        address2TV.setText("");
                        dobTV.setText("Date Of Birth");
                        radioGroup.clearCheck();
                        SharedPreferences sharedPreferences = context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE);
                        left = sharedPreferences.getInt("numberoffarmers",1);
                        left--;
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("numberoffarmers",left);
                        editor.apply();
                        getActivity().invalidateOptionsMenu();
                    }

                }
            }
        };
        asyncTask.execute(u_id);
    }*/

    private Boolean getValues(){
        firstNameTV = (EditText) view.findViewById(R.id.firstName);
        first_name = firstNameTV.getText().toString();
        lastNameTV = (EditText) view.findViewById(R.id.lastName);
        last_name = lastNameTV.getText().toString();
        aadharTV = (EditText) view.findViewById(R.id.adhaarNumber);
        aadhar_number = aadharTV.getText().toString();
        phoneTV = (EditText) view.findViewById(R.id.mobileNumber);
        phone_number = phoneTV.getText().toString();
        address1TV = (EditText) view.findViewById(R.id.address1);
        address_1 = address1TV.getText().toString();
        address2TV = (EditText) view.findViewById(R.id.address2);
        address_2 = address2TV.getText().toString();
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);

        int selectedId = radioGroup.getCheckedRadioButtonId();
        gender = "Others";
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
                return true;
        }
        dobTV = view.findViewById(R.id.DOBtextView);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dob = dobTV.getText().toString();
        if(dob.equals("Date Of Birth")){
            Snackbar.make(view, "Please Enter The Date", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            return true;
        }
        Date inputDate = formatter.parse(dob, new ParsePosition(0));
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        outputDate = formatter.format(inputDate);
        village_id = village_selected.getVillage_id();
        district_id = village_selected.getDistrict_id();
        return false;
    }
    private Boolean isThereError(){
        if(TextUtils.isEmpty(first_name)){
            firstNameTV.setError("Please Enter The First Name");
            return true;
        }
        if(TextUtils.isEmpty(last_name)){
            lastNameTV.setError("Please Enter The Second Name");
            return true;
        } if(phone_number.length() != 10){
            phoneTV.setError("Invalid Phone");
            return true;
        } if(aadhar_number.length() != 12){
            aadharTV.setError("Invalid Aadhar Number");
            return true;
        } if(TextUtils.isEmpty(address_1)){
            address1TV.setError("Please Enter an Address");
            return true;
        } if(TextUtils.isEmpty(address_2)){
            address2TV.setError("Please Enter an Address");
            return true;
        }
        return false;
    }

    private class updateFarmers extends AsyncTask<String,Integer,String>{


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
                saveornext = strings[12];

                File sourceFile = new File(imagePath);
                final MediaType MEDIA_TYPE_JPG = MediaType.parse("image/jpg");
                String filename = imagePath.substring(imagePath.lastIndexOf("/") + 1);
                RequestBody requestBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("image",filename,RequestBody.create(MEDIA_TYPE_JPG,sourceFile))
                        .build();
                OkHttpClient client = new OkHttpClient();
                res = APICall.POST(client, RequestBuilder.buildURL("saveFarmer.php",
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
                if(saveornext.equals("0")){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new QuestionFragment()).commit();
                    //NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    //navigationView.setCheckedItem(R.id.Questions);
                }
                else{
                    firstNameTV.setText("");
                    lastNameTV.setText("");
                    aadharTV.setText("");
                    phoneTV.setText("");
                    address1TV.setText("");
                    address2TV.setText("");
                    dobTV.setText("Date Of Birth");
                    radioGroup.clearCheck();
                    SharedPreferences sharedPreferences = context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE);
                    left = sharedPreferences.getInt("numberoffarmers",1);
                    left--;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("numberoffarmers",left);
                    editor.apply();
                    getActivity().invalidateOptionsMenu();
                }
            }else if(s.contains("Duplicate entry")){
                if(s.contains("aadhar_number"))
                    aadharTV.setError("Aadhar Number Already Exists");
                else if(s.contains("phone_number"))
                    phoneTV.setError("Mobile Already Exists");
            }

        }
    }
}
