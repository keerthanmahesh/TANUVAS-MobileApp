package com.mahesh.keerthan.tanvasfarmerapp.FragmentClasses;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.mahesh.keerthan.tanvasfarmerapp.APICall;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.District;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseUser;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseVillage;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.UserClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Villages;
import com.mahesh.keerthan.tanvasfarmerapp.R;
import com.mahesh.keerthan.tanvasfarmerapp.RequestBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class AddOfficialFragment extends Fragment {

    private View view;
    private ArrayList<District> districts = new ArrayList<>();
    private ArrayList<FirebaseVillage> villages = new ArrayList<>();
    private List<String> districtNames = new ArrayList<>();
    private List<String> villageNames = new ArrayList<>();
    private Spinner districtSpinner;
    private Spinner villageSpinner;
    private Boolean isSuperAdmin = false;
    private FirebaseUser user;
    private ProgressDialog progressDialog ;
    private ProgressDialog progressDialogSecondary;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_official_fragment,container,false);
        districtSpinner = view.findViewById(R.id.spinnerDistrictSelect);
        villageSpinner = view.findViewById(R.id.spinnerVillageSelect);
        progressDialog = ProgressDialog.show(getActivity(),"Loading...","We appreciate your patience");
        final RelativeLayout containerView = view.findViewById(R.id.containerView);
        getDistricts();
        districtSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getVillages(districts.get(position).getDistrict_id());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                getVillages(districts.get(0).getDistrict_id());
            }
        });


        final EditText usernameET = view.findViewById(R.id.addOfficialUsernameEditText), passwordET = view.findViewById(R.id.addOfficialPasswordEditText),
                mobileET = view.findViewById(R.id.addOfficialPhonenumberEditText), fullnameET = view.findViewById(R.id.addOfficialFullnameEditText);
        CheckBox checkBox = view.findViewById(R.id.superAdminCheckBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isSuperAdmin = true;
                else
                    isSuperAdmin = false;
            }
        });

        Button goButton = view.findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = new FirebaseUser(fullnameET.getText().toString(),districts.get(districtSpinner.getSelectedItemPosition()).getDistrict_id(),
                        Long.parseLong(mobileET.getText().toString()),isSuperAdmin? 1:0);

                createUser(usernameET.getText().toString(),passwordET.getText().toString(),user);


            }
        });

        villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                containerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                containerView.setVisibility(View.INVISIBLE);
            }
        });
        return view;
    }


    /** Gets the district names from Firebase */
    private void getDistricts(){


        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("Districts").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.dismiss();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    District district = new District(Integer.parseInt(singleSnapshot.getKey()),singleSnapshot.getValue(String.class));
                    districtNames.add(district.getEn_district_name());
                    districts.add(district);
                }
                Collections.sort(districtNames, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareToIgnoreCase(s2);
                    }
                });
                Collections.sort(districts, new Comparator<District>() {
                    @Override
                    public int compare(District o1, District o2) {
                        return o1.getEn_district_name().compareToIgnoreCase(o2.getEn_district_name());
                    }
                });

                ArrayAdapter<String> districtAdapter = new ArrayAdapter<>(AddOfficialFragment.this.getActivity(),android.R.layout.simple_spinner_dropdown_item,districtNames);
                districtSpinner.setAdapter(districtAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /** Gets the villages from the database corresponding to the district
     * selected in the first spinner*.
     * @param district_id equals district for which villages are to be fetched
     */
    private void getVillages(int district_id){
        final ProgressDialog progressDialogAlpha = ProgressDialog.show(getActivity(),"Loading...","We appreciate your patience");
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference();
        mReference.child("Villages").child("0").orderByChild("district_id").equalTo(district_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialogAlpha.dismiss();
                villageNames.clear();
                villages.clear();
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    FirebaseVillage village = singleSnapshot.getValue(FirebaseVillage.class);
                    villages.add(village);
                    villageNames.add(village.getEn_village_name());
                }
                Collections.sort(villageNames, new Comparator<String>() {
                    @Override
                    public int compare(String s1, String s2) {
                        return s1.compareToIgnoreCase(s2);
                    }
                });
                Collections.sort(villages, new Comparator<FirebaseVillage>() {
                    @Override
                    public int compare(FirebaseVillage o1, FirebaseVillage o2) {
                        return o1.getEn_village_name().compareToIgnoreCase(o2.getEn_village_name());
                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_spinner_dropdown_item,villageNames);
                villageSpinner.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressDialogAlpha.dismiss();
            }
        });
    }

    private void createUser(String email, String password, final FirebaseUser user){
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    final DatabaseReference mRef = FirebaseDatabase.getInstance().getReference();
                    final Map<String,Object> map = new HashMap<>();
                    map.put("Users/"+mAuth.getUid(),user);
                    final String[] key = {null};
                    FirebaseDatabase.getInstance().getReference().child("Villages").child("0").orderByChild("en_village_name").equalTo(villages.get(villageSpinner.getSelectedItemPosition()).getEn_village_name()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                key[0] = snapshot.getKey();
                            }
                            map.put("Villages/0/"+ key[0] +"/allocated",1);
                            map.put("Villages/0/"+ key[0] +"/u_id",mAuth.getUid());
                            mRef.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Successfull").setMessage("User added Successfully").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setCancelable(false).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage(e.getMessage()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }).setCancelable(false).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }else{
                    AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Error").setMessage(task.getException().getMessage()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setCancelable(false).show();
                }
            }
        });
    }
}
