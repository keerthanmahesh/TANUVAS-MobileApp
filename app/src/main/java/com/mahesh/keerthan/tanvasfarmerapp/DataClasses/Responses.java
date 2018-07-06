package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.ArrayList;

public class Responses implements Serializable{
    private QuestionClass question;
    private ArrayList<Options> options = new ArrayList<>();


    public void addSingleOption(Options option){
        this.options.add(option);
    }

    public void addMultipleOption(ArrayList<Options> options){
        this.options.addAll(options);
    }

    public QuestionClass getQuestion() {
        return question;
    }

    public void setQuestion(QuestionClass question) {
        this.question = question;
    }

    public ArrayList<Options> getOptions() {
        return options;
    }
    public void clearOptions(){
        options.clear();
    }
}
