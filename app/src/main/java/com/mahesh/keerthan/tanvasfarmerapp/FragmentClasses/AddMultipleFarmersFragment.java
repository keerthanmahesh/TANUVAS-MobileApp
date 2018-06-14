package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.HomeActivity;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.io.IOException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

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
        showDialog();
        return view;
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
    public void Done(int farmers) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("com.keerthan.tanuvas.numberoffarmers",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("numberoffarmers",farmers);
        editor.apply();
        //number = farmers;
        dateView.setText(farmers+"");
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
            getValues();
            updateFarmer(first_name,last_name,aadhar_number,phone_number,address_1,address_2,gender,outputDate,village_id,district_id,0);
        }
        if(id == R.id.action_next){
            getValues();
            updateFarmer(first_name,last_name,aadhar_number,phone_number,address_1,address_2,gender,outputDate,village_id,district_id,1);
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

    public void updateFarmer(final String first_name, final String last_name, final String aadhar, final String phone, final String address_1, final String address_2, final String gender, final String dob, final int village_id, final int dirstrict_id,final int saveornext){
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
                        dobTV.setText("");
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
    }

    private void getValues(){
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
        }
        dobTV = view.findViewById(R.id.DOBtextView);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dob = dobTV.getText().toString();
        Date inputDate = formatter.parse(dob, new ParsePosition(0));
        formatter = new SimpleDateFormat("yyyy-MM-dd");
        outputDate = formatter.format(inputDate);
        village_id = village_selected.getVillage_id();
        district_id = village_selected.getDistrict_id();
    }
}
