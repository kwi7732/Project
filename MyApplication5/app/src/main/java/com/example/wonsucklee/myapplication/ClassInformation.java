package com.example.wonsucklee.myapplication;

import java.io.Serializable;

/**
 * Created by googolhkl on 2016. 11. 6..
 */

public class ClassInformation implements Serializable {

    private String time;
    private String className;
    private String professor;
    private String isCompleted;

    public ClassInformation(){}
    public ClassInformation(String str){
        String[] arr = str.split(":");
        time = arr[0];
        className = arr[1];
    }

    public ClassInformation(String time, String className, String classColor){
        this.time = time;
        this.className = className;
    }

    public String getTime() {
        return time;
    }
    public String getClassName() {return className;}
    public String getProfessor() {return professor;}
    public String getIsCompleted() {return isCompleted;}
    public static String eng2kor(String title) {
        String result = "";
        switch(title) {
            case "simulation":
                result = "컴퓨터시뮬레이션";
                break;
            case "computer_algorithms":
                result = "컴퓨터알고리즘";
                break;
            case "computer_architectures":
                result = "컴퓨터구조";
                break;
            case "file_processing":
                result = "파일처리";
                break;
            case "operating_system":
                result = "운영체제";
                break;
            case "software_engineering":
                result = "소프트웨어공학";
                break;
            case "compiler":
                result = "컴파일러";
                break;

        }
        return result;
    }


    public void setProfessor(String professor) {this.professor = professor;}
    public void setTime(String time) {this.time = time; }
    public void setClassName(String className) {
        this.className = className;
    }
    public void setIsCompleted(String isCompleted) {this.isCompleted = isCompleted;}
}