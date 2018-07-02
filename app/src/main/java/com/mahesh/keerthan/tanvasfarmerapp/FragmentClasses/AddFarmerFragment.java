package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.Activities.AddFarmerFragment2Activity;
import com.mahesh.keerthan.tanvasfarmerapp.Adapters.questionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RealPathUtil;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;
import com.ramotion.foldingcell.FoldingCell;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.zip.Inflater;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

import static android.app.Activity.RESULT_OK;


public class AddFarmerFragment extends Fragment implements View.OnClickListener, questionAdapter.ItemClickListener{


    private Calendar calendar;
    private TextView dateView;
    private int year, month, day;
    private View view;
    private Button calender_button;
    private static int id1;
    private Villages village_selected;
    private District district_selected;
    private EditText aadharTV,phoneTV;
    private String phone_number,aadhar_number;
    private ImageView imageButton;
    public static final int PICK_IMAGE = 1;
    private String realPath,first_name,last_name,dob,address_1,address_2,gender = "Others";;
    private FoldingCell fc,fc1,fc2;
    private EditText firstNameTV,lastNameTV,address1TV,address2TV;
    private TextView dobTV,ageTV;
    private RadioGroup radioGroup;
    private boolean verified = true;
    private ScrollView scrollView;
    private ArrayList<QuestionClass> mainQuestions = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.add_farmer_fragment,container,false);
        /*calender_button = (Button) view.findViewById(R.id.DOBbuttonAddfarmer);
        calender_button.setOnClickListener(this);
        dateView = view.findViewById(R.id.DOBtextView);
        calendar = Calendar.getInstance();*/
        getActivity().invalidateOptionsMenu();
        village_selected = (Villages) getArguments().getSerializable("village");
        //imageButton = view.findViewById(R.id.profPicImageButton);
        district_selected = (District) getArguments().getSerializable("district");
        //TextView village_name = view.findViewById(R.id.villageName);
        //TextView district_name = view.findViewById(R.id.districtName);
        //village_name.setText("Village: "+village_selected.getEn_village_name());
        //district_name.setText("District: " + district_selected.getEn_district_name());
        //Button donebtn = view.findViewById(R.id.donebtn);
        scrollView = view.findViewById(R.id.scrollView123);


        View.OnClickListener pickImageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        };
        //imageButton.setOnClickListener(pickImageListener);
        //fc = (FoldingCell) view.findViewById(R.id.folding_cell);
        fc1 = (FoldingCell) view.findViewById(R.id.folding_cell1);
        //fc2 = (FoldingCell) view.findViewById(R.id.folding_cell2);
        /*fc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.toggle(false);
            }
        });*/
        fc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc1.toggle(false);
            }
        });
        /*fc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollView.setSmoothScrollingEnabled(true);
                fc2.toggle(true);
                scrollView.post(new Runnable() {
                    @Override
                    public void run() {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });

            }
        });*/

        new getOthersQuestions().execute();
        CardView cardView = view.findViewById(R.id.card_view);
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntent(v);
            }
        });

        CardView cardView2 = view.findViewById(R.id.cardViewOthers);
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntentOthers(v);
            }
        });

        /*firstNameTV = (EditText) view.findViewById(R.id.firstName);
        lastNameTV = (EditText) view.findViewById(R.id.lastName);

        aadharTV = (EditText) view.findViewById(R.id.adhaarNumber);
        phoneTV = (EditText) view.findViewById(R.id.mobileNumber);
        address1TV = (EditText) view.findViewById(R.id.address1);

        address2TV = (EditText) view.findViewById(R.id.address2);

        dobTV = view.findViewById(R.id.DOBtextView);

        donebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fc.fold(false);
                if(!firstNameTV.getText().toString().isEmpty()&&!lastNameTV.getText().toString().isEmpty()){
                    first_name = firstNameTV.getText().toString();
                    last_name = lastNameTV.getText().toString();
                    aadhar_number = aadharTV.getText().toString();
                    phone_number = phoneTV.getText().toString();
                    address_1 = address1TV.getText().toString();
                    address_2 = address2TV.getText().toString();
                    dob = dobTV.getText().toString();
                    radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
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
                    TextView nameTv = view.findViewById(R.id.nameTV);
                    nameTv.setAlpha(0);
                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);

                    float d = getActivity().getResources().getDisplayMetrics().density;
                    int left = (int)(20 * d);
                    int top = (int)(130 * d);
                    layoutParams.setMargins(left,top,0,0);
                    nameTv.setLayoutParams(layoutParams);
                    nameTv.setText(firstNameTV.getText().toString()+" "+lastNameTV.getText().toString());
                    TextView aadharCardTV = view.findViewById(R.id.aadharTV);
                    aadharCardTV.setAlpha(0);
                    TextView phoneCardTV = view.findViewById(R.id.phoneTV123);
                    phoneCardTV.setAlpha(0);
                    TextView genderandageTV = view.findViewById(R.id.genderandage);
                    genderandageTV.setAlpha(0);
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
        });*/


        return view;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
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

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
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

            if(dob.equals("Date Of Birth")){
                Snackbar.make(view, "Please Enter The Date", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return super.onOptionsItemSelected(item);
            }
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
                default: Snackbar.make(view, "Please Enter The Gender", Snackbar.LENGTH_LONG)
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

    @Override
    public void onItemClick(View view, int position) {

    }



    public void animateIntent(View v){
        Intent intent = new Intent(getActivity(), AddFarmerFragment2Activity.class);
        String transitionName = getString(R.string.transition_string);
        String transitionName1 = getString(R.string.profpic_transition);
        View view_start = view.findViewById(R.id.card_view);
        View profpic_start = view.findViewById(R.id.cardViewProfileImage);
        android.support.v4.util.Pair<View, String> p1 = android.support.v4.util.Pair.create((View)view_start, transitionName);
        android.support.v4.util.Pair<View, String> p2 = android.support.v4.util.Pair.create((View)profpic_start, transitionName1);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2);
        ActivityCompat.startActivity(getActivity(),intent,optionsCompat.toBundle());
    }

    public void animateIntentOthers(View v){
        Intent intent = new Intent(getActivity(),AddFarmerFragmentOthers.class);
        String transitionName = getString(R.string.transition_string3);
        View view_start = view.findViewById(R.id.cardViewOthers);
        intent.putExtra("othersQuestions",  mainQuestions);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),view_start,transitionName);
        ActivityCompat.startActivity(getActivity(),intent,optionsCompat.toBundle());
    }

    private class getOthersQuestions extends AsyncTask<Void,Void,JSONArray> {

        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mainQuestions.clear();
            dialog = ProgressDialog.show(getActivity(), "Loading....", "We appreciate your patience", true, false);
        }

        @Override
        protected JSONArray doInBackground(Void... voids) {
            OkHttpClient client = new OkHttpClient();
            try {
                JSONArray array = new JSONArray(APICall.GET(client, RequestBuilder.buildURL("fetchQuestions.php", null, null)));
                JSONObject object;
                for (int i = 0; i < array.length(); i++) {
                    object = array.getJSONObject(i);
                    QuestionClass question = new QuestionClass(
                            object.getInt("question_id"),
                            object.getString("question_content"),
                            object.getString("question_type"),
                            object.getInt("main_or_sub"),
                            object.getInt("has_sub_ques"),
                            object.getInt("main_ques_id"),
                            object.getInt("sub_ques_id"));
                    mainQuestions.add(question);
                }

                return array;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            dialog.dismiss();
        }
    }
}



