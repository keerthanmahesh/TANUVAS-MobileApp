package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

public class Options {
    private String option_content;
    private int option_id, question_id;

    public Options(String option_content, int option_id, int question_id) {
        this.option_content = option_content;
        this.option_id = option_id;
        this.question_id = question_id;
    }
    public Options(){}

    public String getOption_content() {
        return option_content;
    }

    public void setOption_content(String option_content) {
        this.option_content = option_content;
    }

    public int getOption_id() {
        return option_id;
    }

    public void setOption_id(int option_id) {
        this.option_id = option_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }
}
