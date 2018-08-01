package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.mahesh.keerthan.tanvasfarmerapp.Adapters.addOptionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.FirebaseQuestion;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AddNewQuestionActivity extends AppCompatActivity {

    private String[] options= new String[] {"MCQ", "Yes/No", "Input Type"};
    private ArrayList<String> optionsArray = new ArrayList<>(Arrays.asList(options));
    private Spinner optionsSpinner;
    private RecyclerView optionsRV;
    private Button addButton;
    private Toolbar toolbar;
    private ArrayList<Options> MCQoptions;
    private String Category, Module;

    public AddNewQuestionActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);

        Intent incomingIntent = getIntent();
        Category = incomingIntent.getStringExtra("Category");
        Module = incomingIntent.getStringExtra("Module");
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.Back));
        toolbar = (Toolbar) findViewById(R.id.toolbarAddQuestion);
        toolbar.setTitleTextAppearance(this,R.style.AmericanTypewriterSemibold);
        toolbar.setTitle("New Question");
        toolbar.inflateMenu(R.menu.home);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Menu menu =  toolbar.getMenu();
        menu.findItem(R.id.action_settings).setVisible(false);
        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,optionsArray);
        optionsSpinner = findViewById(R.id.optionsSpinner);
        optionsRV = findViewById(R.id.addNewQuestionsOptionsList);
        optionsRV.setLayoutManager(new LinearLayoutManager(this));
        addButton = findViewById(R.id.newOptionAddButton);
        optionsSpinner.setAdapter(optionsAdapter);
        optionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    optionsRV.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);

                    Button addOptionBtn = findViewById(R.id.newOptionAddButton);
                    final Integer question_id = new Random().nextInt();
                    final addOptionAdapter adapter = new addOptionAdapter(AddNewQuestionActivity.this, question_id);
                    optionsRV.setAdapter(adapter);
                    adapter.setResult(new addOptionAdapter.OnResult() {
                        @Override
                        public void finish(ArrayList<Options> options) {
                            AddNewQuestionActivity.this.MCQoptions = options;
                        }
                    });
                    menu.findItem(R.id.action_save).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            FirebaseQuestion question = new FirebaseQuestion();
                            EditText questionET = findViewById(R.id.newQuestionContentET);
                            if(TextUtils.isEmpty(questionET.getText().toString())){
                                questionET.setError("Please Enter The Question Content");
                                return false;
                            }
                            question.setQuestion_content(questionET.getText().toString());
                            question.setQuestion_module(Module);
                            question.setQuestion_type("MCQ");
                            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child(Category.equals("Profile")? "Questions" : "QuestionnaireQuestions").child(question_id.toString());
                            mReference.setValue(question);
                            for(Integer i=0;i<MCQoptions.size();i++)
                                mReference.child("Options").child(i.toString()).child("option_content").setValue(MCQoptions.get(i).getOption_content()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            AlertDialog dialog = new AlertDialog.Builder(AddNewQuestionActivity.this).setCancelable(false).setTitle("Successful")
                                                    .setMessage("The Question was added Successfully").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                            finish();
                                                        }
                                                    }).show();
                                        }else {
                                            AlertDialog dialog = new AlertDialog.Builder(AddNewQuestionActivity.this).setCancelable(false).setTitle("Error:")
                                                    .setMessage("The Question Could Not Be Added; Message: "+ task.getException().getMessage()).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }).show();
                                        }
                                    }
                                });
                            return true;
                        }
                    });
                    addOptionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.addOption();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
                else{

                    optionsRV.setVisibility(View.INVISIBLE);
                    addButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

}
