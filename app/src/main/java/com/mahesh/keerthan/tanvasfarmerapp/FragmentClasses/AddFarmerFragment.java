package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.DialogFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.HomeActivity;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


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
            district_selected = (District) getArguments().getSerializable("district");
            TextView village_name = view.findViewById(R.id.villageName);
            TextView district_name = view.findViewById(R.id.districtName);
            village_name.setText("Village: "+village_selected.getEn_village_name());
            district_name.setText("District: " + district_selected.getEn_district_name());

            return view;
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

            updateFarmer(first_name,last_name,aadhar_number,phone_number,address_1,address_2,gender,outputDate,village_id,district_id);
        }
        return super.onOptionsItemSelected(item);


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem save_item = menu.findItem(R.id.action_save);
        save_item.setVisible(true);
        MenuItem settings_item = menu.findItem(R.id.action_settings);
        settings_item.setVisible(false);
    }

    public void updateFarmer(final String first_name, final String last_name, final String aadhar, final String phone, final String address_1, final String address_2, final String gender, final String dob, final int village_id, final int dirstrict_id){
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

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mainFragment,new QuestionFragment()).commit();
                    NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                    navigationView.setCheckedItem(R.id.Questions);
                }else if(s.contains("Duplicate entry")){
                    if(s.contains("aadhar_number"))
                        aadharTV.setError("Aadhar Number Already Exists");
                    else if(s.contains("phone_number"))
                        phoneTV.setError("Mobile Already Exists");
                }
            }
        };
        asyncTask.execute(u_id);
    }
}



