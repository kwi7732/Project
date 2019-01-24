package com.example.wonsucklee.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by crown on 2018-02-08.
 */


public class Frag_TimeTable_Main extends Fragment {

    String time = "";
    HashSet<String> set = new HashSet<String>();
    private ArrayList<ClassInformation> ci;
    private Intent intent;


    private String userID;
    private JSONArray jsonArray;

    private boolean timetable = false;

    String STUDENT_URL= "http://121.187.77.28:25000/test.php";
    String PROFESSOR_URL= "http://121.187.77.28:25000/professor_sub_list.php";

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        userID = getArguments().getString("user_id");
        final View view = inflater.inflate(R.layout.activity_timetable, container, false);


        ci = new ArrayList<ClassInformation>();

        if (timetable) {
            return null;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                ClassInformation classInformation;
                JSONObject jsonObject;
                String[] array_day;
                try {
                    Log.i("asdfsadf", response);
                    jsonArray = new JSONArray(response);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);

                        String title = jsonObject.getString("title");
                        String professor = jsonObject.getString("professor");
                        String day_and_time = jsonObject.getString("day");

                        array_day = day_and_time.split(",");
                        String day = array_day[0];

                        classInformation = new ClassInformation();
                        classInformation.setClassName(title);
                        classInformation.setTime(day_and_time);

                        ci.add(classInformation);

                        for (int k = 0; k < ci.size(); k++) {
                            String[] test = ci.get(k).getTime().split(" ");
                            for (int j = 0; j < test.length; j++) {
                                Button button = (Button) view.findViewById(getDaysID(test[j]));
                                button.setText(ci.get(k).eng2kor(ci.get(k).getClassName()));
                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        };

        if(userID.charAt(0) == '0') {
            TimeTableRequest timeTableRequest = new TimeTableRequest(userID, PROFESSOR_URL, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(timeTableRequest);
        } else {
            TimeTableRequest timeTableRequest = new TimeTableRequest(userID, STUDENT_URL, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getActivity());
            queue.add(timeTableRequest);
        }


        return view;
    }

    Integer getDaysID(String str) {
        if (str.equals("mon1"))
            return R.id.addmon1;
        else if (str.equals("mon2"))
            return R.id.addmon2;
        else if (str.equals("mon3"))
            return R.id.addmon3;
        else if (str.equals("mon4"))
            return R.id.addmon4;
        else if (str.equals("mon5"))
            return R.id.addmon5;
        else if (str.equals("mon6"))
            return R.id.addmon6;
        else if (str.equals("mon7"))
            return R.id.addmon7;
        else if (str.equals("mon8"))
            return R.id.addmon8;
        else if (str.equals("mon9"))
            return R.id.addmon9;
        else if (str.equals("tue1"))
            return R.id.addtue1;
        else if (str.equals("tue2"))
            return R.id.addtue2;
        else if (str.equals("tue3"))
            return R.id.addtue3;
        else if (str.equals("tue4"))
            return R.id.addtue4;
        else if (str.equals("tue5"))
            return R.id.addtue5;
        else if (str.equals("tue6"))
            return R.id.addtue6;
        else if (str.equals("tue7"))
            return R.id.addtue7;
        else if (str.equals("tue8"))
            return R.id.addtue8;
        else if (str.equals("tue9"))
            return R.id.addtue9;
        else if (str.equals("wed1"))
            return R.id.addwed1;
        else if (str.equals("wed2"))
            return R.id.addwed2;
        else if (str.equals("wed3"))
            return R.id.addwed3;
        else if (str.equals("wed4"))
            return R.id.addwed4;
        else if (str.equals("wed5"))
            return R.id.addwed5;
        else if (str.equals("wed6"))
            return R.id.addwed6;
        else if (str.equals("wed7"))
            return R.id.addwed7;
        else if (str.equals("wed8"))
            return R.id.addwed8;
        else if (str.equals("wed9"))
            return R.id.addwed9;
        else if (str.equals("thu1"))
            return R.id.addthu1;
        else if (str.equals("thu2"))
            return R.id.addthu2;
        else if (str.equals("thu3"))
            return R.id.addthu3;
        else if (str.equals("thu4"))
            return R.id.addthu4;
        else if (str.equals("thu5"))
            return R.id.addthu5;
        else if (str.equals("thu6"))
            return R.id.addthu6;
        else if (str.equals("thu7"))
            return R.id.addthu7;
        else if (str.equals("thu8"))
            return R.id.addthu8;
        else if (str.equals("thu9"))
            return R.id.addthu9;
        else if (str.equals("fri1"))
            return R.id.addfri1;
        else if (str.equals("fri2"))
            return R.id.addfri2;
        else if (str.equals("fri3"))
            return R.id.addfri3;
        else if (str.equals("fri4"))
            return R.id.addfri4;
        else if (str.equals("fri5"))
            return R.id.addfri5;
        else if (str.equals("fri6"))
            return R.id.addfri6;
        else if (str.equals("fri7"))
            return R.id.addfri7;
        else if (str.equals("fri8"))
            return R.id.addfri8;
        else if (str.equals("fri9"))
            return R.id.addfri9;
        else
            return R.id.addfri9;
    }

}
