package com.mahesh.keerthan.tanvasfarmerapp;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.mahesh.keerthan.tanvasfarmerapp.Adapters.optionsAdapter;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Options;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.QuestionClass;
import com.mahesh.keerthan.tanvasfarmerapp.DataClasses.Responses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;

public class PickOptionDialog extends Dialog {


    private ArrayList<Options> options = new ArrayList<>();
    private QuestionClass selectedQuestion;
    private ArrayList<Options> selectedOptions = new ArrayList<>();
    private OnResult result;
    private int questionType;

    private static final int QUESTION_TYPE_MCQ = 0;
    private static final int QUESTION_TYPE_YESNO = 1;
    private static final int QUESTION_TYPE_INPUT = 2;
    private static final int QUESTION_TYPE_YESNOINPUT = 3;

    public PickOptionDialog(@NonNull Context context) {
        super(context);
    }

    public void setSelectedQuestion(QuestionClass selectedQuestion) {
        this.selectedQuestion = selectedQuestion;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (selectedQuestion.getQuestion_type()){
            case "MCQ": setContentView(R.layout.dialog_check_box);
                        questionType = QUESTION_TYPE_MCQ;
                        break;
            case "Yesno": setContentView(R.layout.dialog_radio);
                        questionType = QUESTION_TYPE_YESNO;
                        break;
            case "Input": setContentView(R.layout.dialog_check_box);
                        questionType = QUESTION_TYPE_INPUT;
                        break;
            case "Yesnoinput": setContentView(R.layout.dialog_radio);
                        questionType = QUESTION_TYPE_YESNOINPUT;
                        break;
            default: setContentView(R.layout.dialog_check_box);
                    questionType = QUESTION_TYPE_MCQ;
        }
        new fetchOptions().execute(selectedQuestion.getQuestion_id());

    }

    private void setFunc(){
        switch (questionType){
            case QUESTION_TYPE_MCQ: MCQ();
                                    break;
            case QUESTION_TYPE_YESNOINPUT:
            case QUESTION_TYPE_YESNO: yesno();
                                    break;
        }
    }
    private void MCQ(){
        TextView questionTV = findViewById(R.id.questionTV);
        questionTV.setText("Q:" + selectedQuestion.getQuestion_content());
        RecyclerView optionsList = findViewById(R.id.listView1234);
        optionsAdapter adapter = new optionsAdapter(options,getContext());
        optionsList.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter.setResult(new optionsAdapter.OnResult() {
            @Override
            public void finish(ArrayList<Options> selectedOptions) {
                PickOptionDialog.this.selectedOptions.clear();
                PickOptionDialog.this.selectedOptions.addAll(selectedOptions);
            }
        });
        optionsList.setAdapter(adapter);
        Button doneBtn = findViewById(R.id.done_button);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(result!=null){
                    result.finish(selectedOptions);
                }
                dismiss();
            }
        });

        initialiseCancel();

    }


    private void yesno(){
        TextView questionTV = findViewById(R.id.questionTV);
        questionTV.setText("Q: " + selectedQuestion.getQuestion_content());
        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        for(int i=0;i<options.size();i++){
            RadioButton radioButton = new RadioButton(getContext());
            radioButton.setText(options.get(i).getOption_content());
            radioButton.setId(i);
            radioButton.setTextColor(getContext().getResources().getColor(R.color.Black));
            radioButton.setTextSize(20);
            radioGroup.addView(radioButton);
        }
        Button doneBtn = findViewById(R.id.done_button);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedOptionPosition = radioGroup.getCheckedRadioButtonId();
                selectedOptions.add(options.get(selectedOptionPosition));
                if(result!=null){
                    result.finish(selectedOptions);
                }
                dismiss();
            }
        });
        initialiseCancel();



    }

    public void setResult(OnResult result){
        this.result = result;
    }

   public interface OnResult{
        void finish(ArrayList<Options> selectedOptions);
   }

    private void initialiseCancel(){
        Button cancelBtn = findViewById(R.id.cancel_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private class fetchOptions extends AsyncTask<Integer,Void,JSONArray> {

        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getContext(),"Loading...","We appreciate your patience");
        }

        @Override
        protected JSONArray doInBackground(Integer... integers) {
            String question_id = integers[0].toString();
            OkHttpClient client = new OkHttpClient();
            try{
                JSONArray array = new JSONArray(APICall.GET(client, RequestBuilder.buildURL("fetchoptions.php",new String[]{"question_id"},new String[]{question_id})));
                for(int i =0;i<array.length();i++){
                    JSONObject object = array.getJSONObject(i);
                    Options options1 = new Options(object.getString("option_content"),object.getInt("option_id"),object.getInt("question_id"));
                    options.add(options1);
                }

                return array;
            }
            catch (IOException e){
                e.printStackTrace();
            }
            catch (JSONException e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            progressDialog.dismiss();
            setFunc();
        }
    }



}
