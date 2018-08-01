package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

import java.io.Serializable;
import java.util.ArrayList;

public class FirebaseQuestion implements Serializable {

    String question_content;
    String question_module;
    String question_type;
    String key;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public FirebaseQuestion(String question_content, String question_module, String question_type) {
        this.question_content = question_content;
        this.question_module = question_module;
        this.question_type = question_type;
    }


    public FirebaseQuestion() {
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getQuestion_module() {
        return question_module;
    }

    public void setQuestion_module(String question_module) {
        this.question_module = question_module;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }
}
