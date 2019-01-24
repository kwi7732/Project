package com.example.wonsucklee.myapplication;

import android.app.Activity;

/**
 * Created by crown on 2018-02-08.
 */


public class ProfessorSubjectData extends BaseActivity {

    private int which;
    private String which_string;
    private int is_completed;
    private String question_image;
    private String question;

    public void setWhich(int which) {
        this.which_string = which + "주차";
        this.which = which;
    }
    public void setIsCompleted(int is_completed) {this.is_completed = is_completed;}
    public void setQuestionImage(String question_image) {this.question_image = question_image;}
    public void setQuestion(String question) {this.question = question;}

    public int getWhich () {return which;}
    public String getWhichString() {return which_string;}
    public int get_iscompleted() {return is_completed;}
    public String getIsCompleted() {
        String atd = "";
        switch(is_completed) {
            case 0:
                atd = "-";
                break;
            case 1:
                atd = "수업완료";
                break;
            case 2:
                atd = "휴  강";
                break;
        }
        return atd;
    }

    public String getQuestoinImage() {return question_image;}
    public String getQuestion() {return question;}

}
