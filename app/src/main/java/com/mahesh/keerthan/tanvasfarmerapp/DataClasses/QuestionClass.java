package com.mahesh.keerthan.tanvasfarmerapp.DataClasses;

public class QuestionClass {

    private int question_id;
    private String question_content;
    private String question_type;
    private int main_or_sub;
    private int has_sub_ques;
    private int main_ques_id;
    private int sub_ques_id;

    public QuestionClass(int question_id, String question_content, String question_type, int main_or_sub, int has_sub_ques, int main_ques_id, int sub_ques_id) {
        this.question_id = question_id;
        this.question_content = question_content;
        this.question_type = question_type;
        this.main_or_sub = main_or_sub;
        this.has_sub_ques = has_sub_ques;
        this.main_ques_id = main_ques_id;
        this.sub_ques_id = sub_ques_id;
    }

    public int getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(int question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_content() {
        return question_content;
    }

    public void setQuestion_content(String question_content) {
        this.question_content = question_content;
    }

    public String getQuestion_type() {
        return question_type;
    }

    public void setQuestion_type(String question_type) {
        this.question_type = question_type;
    }

    public int getMain_or_sub() {
        return main_or_sub;
    }

    public void setMain_or_sub(int main_or_sub) {
        this.main_or_sub = main_or_sub;
    }

    public int getHas_sub_ques() {
        return has_sub_ques;
    }

    public void setHas_sub_ques(int has_sub_ques) {
        this.has_sub_ques = has_sub_ques;
    }

    public int getMain_ques_id() {
        return main_ques_id;
    }

    public void setMain_ques_id(int main_ques_id) {
        this.main_ques_id = main_ques_id;
    }

    public int getSub_ques_id() {
        return sub_ques_id;
    }

    public void setSub_ques_id(int sub_ques_id) {
        this.sub_ques_id = sub_ques_id;
    }
}
