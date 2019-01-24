package com.example.wonsucklee.myapplication;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by crown on 2018-06-07.
 */

public class BeaconService {
    private BeaconManager beaconManager;
    private Region region;

    ArrayList <ClassInformation> subject_list_arrayList;

    SimpleDateFormat dayFormat;
    SimpleDateFormat timeFormat;

    private boolean isConnected;
    int count;

    public BeaconService(Context context) {
        beaconManager=new BeaconManager(context);
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                Log.i("BeaconManager", "onEnter");
                //getApplicationContext().startActivity(new Intent(getApplicationContext(),PopupActivity.class).putExtra("uuid",String.valueOf(list.get(0).getProximityUUID())).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onExitedRegion(Region region) {
            }
        });

        //add this below:
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {

            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                if(!list.isEmpty()){
                    Beacon nearestBeacon=list.get(0);
                    Log.d("Airport","Nearest places: "+nearestBeacon.getRssi());

                    if(!isConnected&&nearestBeacon.getRssi()>-80){
                        isConnected=true;
                        count = 0;

                        Log.d("BeaconService","들어옴");

                        try {
                            Log.i("BeaconService", getDay()); //요일 얻기
                            Log.i("BeaconService", getTime()); //요일 얻기
                            Log.i("BeaconService", getWeeks()); //주차 얻기
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        //String time = sdf.format(date);
                        //Log.i("BeaconTime", time);
                        /*
                        AlertDialog.Builder dialog=new AlertDialog.Builder(MainActivity.this);
                        showNotification("들어옴","연결됨"+list.get(0).getRssi());
                        dialog .setTitle("알림")
                                .setMessage("연결")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int which) {

                                    }
                                }).create().show();*/
                    }else if(nearestBeacon.getRssi()<-80){

                        Log.i("1111", "" + count);
                        if(count == 0) {
                            Log.d("Airport","나감");
                            //showNotification("나감","연결끊김");
                        }
                        count++;
                        isConnected=false;
                    }


                }
            }
        });


        region=new Region("ranged region",
                UUID.fromString("E20A39F4-73F5-4BC4-A12F-17D1AD07A961"),5,2);
        //연결할 비콘의 Id와 메이저 마이너 코드 값

        Log.d("비콘 시작", "BeaconService_onStartCommand");

        //SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }


    public void set_Subject(ArrayList<ClassInformation> subject_list_arrayList) {
        this.subject_list_arrayList = subject_list_arrayList;

        Log.i("BeaconService", "onSetSubject" + this.subject_list_arrayList.size());
    }

    public String getTime() throws Exception {

        String time = "";

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat timeFormat = new SimpleDateFormat("hhmm");

        try {
            time = timeFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return time;
    }

    public String getDay() throws Exception {

        String day = "";
        String day_temp = "";

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat dayFormat = new SimpleDateFormat("E");

        try {
            day = dayFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (day_temp) {
            case "일":
                day = "sun";
                break;
            case "월":
                day = "mon";
                break;
            case "화":
                day = "tue";
                break;
            case "수":
                day = "wed";
                break;
            case "목":
                day = "thu";
                break;
            case "금":
                day = "fri";
                break;
            case "토":
                day = "sat";
                break;
        }
        return day;
    }

    public String getWeeks() throws Exception {

        String week = "";
        String week_temp = "";

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat weekFormat = new SimpleDateFormat("w");

        try {
            week_temp = weekFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        switch (week_temp) {
            case "10" :
                week = "1";
                break;
            case "11" :
                week = "2";
                break;
            case "12" :
                week = "3";
                break;
            case "13" :
                week = "4";
                break;
            case "14" :
                week = "5";
                break;
            case "15" :
                week = "6";
                break;
            case "16" :
                week = "7";
                break;
            case "17" :
                week = "8";
                break;
            case "18" :
                week = "9";
                break;
            case "19" :
                week = "10";
                break;
            case "20" :
                week = "11";
                break;
            case "21" :
                week = "12";
                break;
            case "22" :
                week = "13";
                break;
            case "23":
                week = "14";
                break;
            case "24" :
                week = "15";
                break;
        }

        return week;
    }


    /**
    class TaskTimerStart extends AsyncTask<String, String, String> {

        public static final String RESULT_SUCCESS = "1";
        public static final String RESULT_FAIL = "0";


        public int time;


        public TaskTimerStart() {
            time = 4;
        }

        public int getTime() {
            return time;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.i("Timer", "prepare");
        }

        @Override
        protected String doInBackground(String... params) {
            Log.i("Timer", "doInBackground");
            while (time > 0) {

                if (isCancelled()) {
                    break;
                }


                if (!game_running) {

                } else {
                    try {
                        Thread.sleep(1000);
                        time--;
                        publishProgress();
                    } catch (InterruptedException e) {
                        Log.i("Timer", "exception");
                        e.printStackTrace();
                        return RESULT_FAIL;
                    }
                }
            }


            return RESULT_SUCCESS;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            count_ok = true;
            Log.i("Timer", "onPostExecute");
        }
    }
*/

}
