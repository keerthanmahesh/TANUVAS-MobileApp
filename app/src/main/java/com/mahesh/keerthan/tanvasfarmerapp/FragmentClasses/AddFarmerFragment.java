package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.util.Log;
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

import com.google.gson.Gson;
import com.inthecheesefactory.thecheeselibrary.fragment.support.v4.app.StatedFragment;
import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.Activities.AddFarmerFragment2Activity;
import com.mahesh.keerthan.tanvasfarmerapp.Adapters.questionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FarmerClass;
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


public class AddFarmerFragment extends StatedFragment {



    private View view;
    private Villages village_selected;
    private District district_selected;
    private FoldingCell fc,fc1,fc2;
    private ScrollView scrollView;
    private ArrayList<QuestionClass> mainQuestions = new ArrayList<>();
    private FarmerClass farmer = null;
    public static int REQUESTPROFILE = 1;
    public static int REQUESTLANDHOLDING = 2;
    public static int REQUESTOTHERS = 3;
    public static int UPDATEPROFILE = 4;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.add_farmer_fragment,container,false);
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("com.keerthan.tanuvas.selectedArea",Context.MODE_PRIVATE);
        String temp = sharedPreferences.getString("selectedVillage","");
        village_selected = gson.fromJson(temp,Villages.class);
        temp = sharedPreferences.getString("selectedDistrict","");
        district_selected = gson.fromJson(temp,District.class);
        getActivity().invalidateOptionsMenu();
        scrollView = view.findViewById(R.id.scrollView123);
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

        CardView cardView1 = view.findViewById(R.id.cardViewLandHolding);
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateIntentLandHolding(v);
            }
        });


        return view;
    }

    public static float convertDpToPixel(float dp, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float px = dp * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return px;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUESTPROFILE || requestCode == UPDATEPROFILE){
            if(resultCode == Activity.RESULT_OK){
                farmer = (FarmerClass) data.getSerializableExtra("farmer");
                setProfileCard();
            }
        }

    }


    private void setProfileCard(){  
        TextView nameTv = view.findViewById(R.id.nameTV);
        nameTv.setAlpha(0);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        float d = getActivity().getResources().getDisplayMetrics().density;
        int left = (int)(20 * d);
        int top = (int)(130 * d);
        layoutParams.setMargins(left,top,0,0);
        nameTv.setLayoutParams(layoutParams);
        nameTv.setText(farmer.getFirst_name()+" "+farmer.getLast_name());
        TextView aadharCardTV = view.findViewById(R.id.aadharTV);
        aadharCardTV.setAlpha(0);
        TextView phoneCardTV = view.findViewById(R.id.phoneTV123);
        phoneCardTV.setAlpha(0);
        TextView genderandageTV = view.findViewById(R.id.genderandage);
        genderandageTV.setAlpha(0);
        genderandageTV.setText(farmer.getAge() + " yrs, "+ farmer.getGender());
        nameTv.animate().translationY(-200).setDuration(2000).alpha(1);
        aadharCardTV.animate().setDuration(2000).alpha(1).setStartDelay(100);
        phoneCardTV.animate().setDuration(2000).alpha(1).setStartDelay(100);
        genderandageTV.animate().translationY(-200).setDuration(2000).alpha(1).setStartDelay(100);
        view.findViewById(R.id.verified).animate().alpha(1).setDuration(2000);
        ImageView profileImageCard = view.findViewById(R.id.cardViewProfileImage);
        byte[] b = farmer.getProfileImageDrawble();
        Bitmap bmp = BitmapFactory.decodeByteArray(b, 0, b.length);
        profileImageCard.setImageBitmap(bmp);
        phoneCardTV.setText("Phone: +91 " + farmer.getPhone_number());
        aadharCardTV.setText("Aadhar: " + farmer.getAadhar_number());
    }
    /*private class updateFarmer extends AsyncTask<String,Integer,String>{


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
    }*/





    public void animateIntent(View v){
        Intent intent = new Intent(getActivity(), AddFarmerFragment2Activity.class);
        String transitionName = getString(R.string.transition_string);
        String transitionName1 = getString(R.string.profpic_transition);
        View view_start = view.findViewById(R.id.card_view);
        View profpic_start = view.findViewById(R.id.cardViewProfileImage);
        android.support.v4.util.Pair<View, String> p1 = android.support.v4.util.Pair.create((View)view_start, transitionName);
        android.support.v4.util.Pair<View, String> p2 = android.support.v4.util.Pair.create((View)profpic_start, transitionName1);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),p1,p2);
        if(farmer == null){
            ActivityCompat.startActivityForResult(getActivity(),intent,REQUESTPROFILE,optionsCompat.toBundle());
        }else{
            intent.putExtra("farmer",farmer);
            ActivityCompat.startActivityForResult(getActivity(),intent,UPDATEPROFILE,optionsCompat.toBundle());
        }

    }

    public void animateIntentLandHolding(View v){
        Intent intent = new Intent(getActivity(), AddFarmerFragmentLandHolding.class);
        String transitionName = "lololol";
        View view_start = view.findViewById(R.id.cardViewLandHolding);
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),view_start,transitionName);
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



