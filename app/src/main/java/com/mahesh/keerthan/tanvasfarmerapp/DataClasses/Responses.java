package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.util.ArrayList;

public class Responses {
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
