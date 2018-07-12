package com.mahesh.keerthan.tanvasfarmerapp.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.mahesh.keerthan.tanvasfarmerapp.Adapters.addOptionAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddNewQuestionActivity extends AppCompatActivity {

    private String[] options= new String[] {"MCQ", "Yes/No", "Input Type"};
    private ArrayList<String> optionsArray = new ArrayList<>(Arrays.asList(options));
    private Spinner optionsSpinner;
    private RecyclerView optionsRV;
    private Button addButton;

    public AddNewQuestionActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_question);
        ArrayAdapter<String> optionsAdapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item,optionsArray);
        optionsSpinner = findViewById(R.id.optionsSpinner);
        optionsRV = findViewById(R.id.addNewQuestionsOptionsList);
        addButton = findViewById(R.id.newOptionAddButton);
        optionsSpinner.setAdapter(optionsAdapter);
        optionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0){
                    optionsRV.setVisibility(View.VISIBLE);
                    addButton.setVisibility(View.VISIBLE);

                    Button addOptionBtn = findViewById(R.id.newOptionAddButton);
                    final addOptionAdapter adapter = new addOptionAdapter(AddNewQuestionActivity.this,0);
                    optionsRV.setAdapter(adapter);
                    addOptionBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            adapter.addOption();
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
