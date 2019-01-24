package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by crown on 2018-02-08.
 */


public class AttendanceData{

    private String which_string;
    private int which;
    private int weeks;

    public void setWhich(int which) {
        this.which = which;
        this.which_string = which + "주차";
    }
    public void setWeeks(int weeks) {this.weeks = weeks;}

    public int getWhich() {return which;}
    public String getWhichString() {return which_string;}
    public String getWeeks() {
        String atd = "";
        switch(weeks) {
            case 0:
                atd = "-";
                break;
            case 1:
                atd = "출석";
                break;
            case 2:
                atd = "지각";
                break;
            case 3:
                atd = "부재";
                break;
        }
        return atd;
    }

}
