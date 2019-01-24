package com.example.wonsucklee.myapplication;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by crown on 2018-02-08.
 */


/**
 안드로이드의 Activity의 상단을 보면 ActionBar라는 것이 있었다.
 그런데 안드로이드 API21 부터 ActionBar는 deprecated되고 ToolBar라는 것이 추가 되었다.
 ToolBar란 기존의 ActionBar를 대체하는 View의 일종이다.
 ToolBar란 View이기 때문에 기존의 ActionBar에서는 할수 없던 것, 또는 하기 어려웠던 것들을 쉽게 코드로 제어 할 수 있다. (위치제어 등…)

 ActionBar : View가 아니다. 따라서 위치나 내부 아이템을 제어하기 힘들다.
 ToolBar : View다. 따라서 기타 View처럼 제어하기가 슆다.



 */

public class Frag_MainActivity extends AppCompatActivity{
    public static final int REQUEST_CODE_TO__FRAG_ATTENDACE_MAIN = 0;
    public static final int REQUEST_CODE_TO__FRAG_F_BOARD_MAIN = 1;
    public static final int REQUEST_CODE_TO__FRAG_LA_BOARD_MAIN = 3;
    public static final int REQUEST_CODE_TO__FRAG_SH_BOARD_MAIN = 4;
    public static final int REQUEST_CODE_TO__SCHEDULE = 5;

    public static final int REQUEST_CODE_TO_ATTENDACE_MAIN = 1000;
    public static final int REQUEST_CODE_TO_BOARD_MAIN = 1001;
    public static final int REQUEST_CODE_TO_BOARD_INSERT = 1002;
    public static final int REQUEST_CODE_TO_BOARD_MODIFY = 1003;
    public static final int REQUEST_CODE_TO_BOARD_VIEW = 1004;
    public static final int REQUEST_CODE_TO_REPLY = 1005;
    public static final int REQUEST_CODE_TO_MESSAGE = 1006;
    public static final int REQUEST_CODE_TO_SCHEDULE = 1007;
    public static final int REQUEST_CODE_TO_FRIEND = 1008;
    public static final int REQUEST_CODE_TO_PROFESSOR_MAIN = 1009;
    public static final int REQUEST_CODE_TO_QUIZ_INSERT = 1010;
    public static final int REQUEST_CODE_TO_SUBJECT_DEATIL = 1011;

    Toolbar toolbar;
    DrawerLayout drawerLayout;


    ImageView imageView_profile;
    ActionBar actionBar;
    ImageView button_search;
    ImageView button_message;

    Button btn_logout;

    ListView listView_menu;
    ArrayList<MenuData> arrayList_menu;
    Menu_Adapter menuAdapter;

    private static final int MENU_SIZE = 9;
    private static final String [] MENU_ARRAY = {"출석현황", "게시판","개인 시간표"};
    private static final String [] BOARD_MENU_ARRAY = {"자유게시판", "자취방게시판", "중고게시판"};

    boolean menu_change_on;

    private int user_number;
    private String user_id;
    private String user_name;
    private String user_major;
    private String user_profile;
    private String token;

    private BeaconManager beaconManager;
    private Region region;

    ArrayList <ClassInformation> subject_list_arrayList;

    SimpleDateFormat dayFormat;
    SimpleDateFormat timeFormat;

    private boolean isConnected;
    private int count;

    AttendanceCheckThread attendanceCheckThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frag_main);

        user_id = getIntent().getExtras().getString("user_id");
        user_name = getIntent().getExtras().getString("user_name");
        user_major = getIntent().getExtras().getString("user_major");
        user_profile = getIntent().getExtras().getString("user_profile");
        token = getIntent().getExtras().getString("token");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        button_search = (ImageView) findViewById(R.id.button_search);
        button_message = (ImageView) findViewById(R.id.button_message);

        imageView_profile = (ImageView) findViewById(R.id.imageView_profile);

        btn_logout = (Button) findViewById(R.id.btn_logout);
        listView_menu = (ListView) findViewById(R.id.listView_menu);
        arrayList_menu = new ArrayList<>();
        menuAdapter = new Menu_Adapter(getApplicationContext(), arrayList_menu);
        listView_menu.setAdapter(menuAdapter);

        // 툴바 생성 및 세팅하는 부분
        //Toolbar의 왼쪽에 버튼을 추가하고 버튼의 아이콘을 바꾼다.
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        actionBar.setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        String profile_url = "http://121.187.77.28:25000/uploads/" + user_profile;

        Glide.with(this).
                load(profile_url)
                .bitmapTransform(new CropCircleTransformation(getApplicationContext()))
                .into(imageView_profile);

        button_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(Frag_MainActivity.this, FriendActivity.class);
                intent.putExtra("user_id", user_id);
                intent.putExtra("user_name", user_name);
                intent.putExtra("user_major", user_major);
                intent.putExtra("token", token);
                startActivityForResult(intent, REQUEST_CODE_TO_FRIEND);
            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences auto_login = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto_login.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();

                finish();
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                int temp_position = position;
                MenuData data = arrayList_menu.get(temp_position);

                int type_temp = data.get_Type();
                String title_temp = data.get_Title();

                Log.i("FragMainActivity", "position : " + position);
                Log.i("FragMainActivity", "type_temp : " + type_temp);
                Log.i("FragMainActivity", "title_temp : " + title_temp);

                if(type_temp == 1) { //상위메뉴
                    switch(title_temp) {
                        case "출석현황":
                            //출석현황
                            change_fragment(REQUEST_CODE_TO_ATTENDACE_MAIN, REQUEST_CODE_TO__FRAG_ATTENDACE_MAIN);
                            break;
                        case "게시판": //하위 게시판 펼치기 또는 접기\
                            menu_change_on = change_menu(temp_position, 1, menu_change_on);
                            break;
                        case "개인 시간표": //시간표
                            change_fragment(REQUEST_CODE_TO_SCHEDULE, REQUEST_CODE_TO__SCHEDULE);
                            /*
                            Log.i("FragMainActivity", "시간표메뉴");
                            Intent intent =new Intent(Frag_MainActivity.this, TimeTableActivity.class);
                            intent.putExtra("user_id", user_id);
                            startActivityForResult(intent, REQUEST_CODE_TO_SCHEDULE);
                            */
                            break;

                    }
                } else if(type_temp == 2) { //하위메뉴
                    if(data.get_Title().equals(BOARD_MENU_ARRAY[0])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_F_BOARD_MAIN);
                    } else if(data.get_Title().equals(BOARD_MENU_ARRAY[1])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_LA_BOARD_MAIN);
                    } else if(data.get_Title().equals(BOARD_MENU_ARRAY[2])) {
                        change_fragment(REQUEST_CODE_TO_BOARD_MAIN, REQUEST_CODE_TO__FRAG_SH_BOARD_MAIN);
                    }
                }

            }
        };
        listView_menu.setOnItemClickListener(onItemClickListener); //리스트뷰 아이템 클릭시

        menu_Init();
        change_fragment(REQUEST_CODE_TO_ATTENDACE_MAIN, 0);

        beaconManager =new BeaconManager(getApplicationContext());
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
                            if(user_id.charAt(0) != '0') {
                                attendanceCheckThread = new AttendanceCheckThread();
                                attendanceCheckThread.set_day(getDay());
                                attendanceCheckThread.set_week(getWeeks());
                                attendanceCheckThread.set_atd_time(getTime());
                                attendanceCheckThread.execute();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

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

    public void onResume() {
        super.onResume();
    }


    public void menu_Init() {
        arrayList_menu.clear();

        MenuData data;

        for(int i = 0; i < MENU_ARRAY.length; i++) {
            data = new MenuData();
            data.set_Title(MENU_ARRAY[i]);
            data.set_Type(1);
            arrayList_menu.add(data);
        }

        menuAdapter.notifyDataSetChanged();
    }
    //뒤로가기 버튼을 눌렀을 때

    public void add_menu_item(String title, int type) {
        MenuData data = new MenuData();
        data.set_Title(title);
        data.set_Type(type);

        arrayList_menu.add(data);
    }

    public boolean change_menu(int position, int type, boolean menu_change_on) {
        arrayList_menu.clear();

        if(menu_change_on) { //하위메뉴 펼쳐져 있으면 닫는다.
            for(int i = 0; i < MENU_ARRAY.length; i++) {
                add_menu_item(MENU_ARRAY[i], 1);
            }
            menu_change_on = false;
        } else {
            switch(type) {
                case 1:
                    if(position == 0) {

                    } else if(position == 1) { //하위 게시판 보여주기
                        for(int i = 0; i < MENU_ARRAY.length; i++) {
                            if(i < position) {
                                add_menu_item(MENU_ARRAY[i], 1);
                            } else if(i == position) {
                                add_menu_item(MENU_ARRAY[i], 1);

                                for(int j = 0; j < BOARD_MENU_ARRAY.length; j++) {
                                    add_menu_item(BOARD_MENU_ARRAY[j], 2);
                                }
                            }
                            else {
                                add_menu_item(MENU_ARRAY[i], 1);
                            }
                        }
                    }
                    break;
                case 2:
                    break;
            }
            menu_change_on = true;
        }

        menuAdapter.notifyDataSetChanged();
        return menu_change_on;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) { //이미 열려져있었다면 닫는다
            drawer.closeDrawer(GravityCompat.START);
        } else { //아니라면 뒤로가기
            super.onBackPressed();
        }
    }

    // 햄버거 버튼 클릭 시 드로어 열리도록 하는 곳
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        switch (id){
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void change_fragment(int frag_num, int what) {
        switch(frag_num) {
            case REQUEST_CODE_TO_ATTENDACE_MAIN:
                button_search.setVisibility(View.GONE);
                set_Title(what);

                Fragment fragment_attendance = new Frag_Attendace_Main();
                Bundle bundle_attendance = new Bundle(3); // 파라미터는 전달할 데이터 개수
                bundle_attendance.putString("user_id", user_id); // key , value
                bundle_attendance.putString("user_name", user_name);
                bundle_attendance.putString("user_major", user_major);
                bundle_attendance.putString("user_profile", user_profile);
                fragment_attendance.setArguments(bundle_attendance);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment_attendance)
                        .commit();
                break;
            case REQUEST_CODE_TO_BOARD_MAIN:
                button_search.setVisibility(View.VISIBLE);
                set_Title(what);

                Fragment fragment_board = new Frag_Board_Main();
                Bundle bundle_board = new Bundle(4); // 파라미터는 전달할 데이터 개수
                bundle_board.putInt("what", what); // key , value\
                bundle_board.putString("user_id", user_id); // key , value
                bundle_board.putString("user_name", user_name);
                bundle_board.putString("user_major", user_major);
                bundle_board.putString("user_profile", user_profile);
                fragment_board.setArguments(bundle_board);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment_board)
                        .commit();
                break;
            case REQUEST_CODE_TO_SCHEDULE:
                button_search.setVisibility(View.GONE);
                set_Title(what);

                Fragment fragment_timetable = new Frag_TimeTable_Main();
                Bundle bundle_timetable = new Bundle(3); // 파라미터는 전달할 데이터 개수
                bundle_timetable.putString("user_id", user_id); // key , value
                bundle_timetable.putString("user_name", user_name);
                bundle_timetable.putString("user_major", user_major);
                bundle_timetable.putString("user_profile", user_profile);
                fragment_timetable.setArguments(bundle_timetable);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frag_container, fragment_timetable)
                        .commit();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void set_Title(int what) {
        switch(what) {
            case 0:
                getSupportActionBar().setTitle("출석 현황");
                break;
            case 1:
                getSupportActionBar().setTitle("자유 게시판");
                break;
            case 3:
                getSupportActionBar().setTitle("자취방 게시판");
                break;
            case 4:
                getSupportActionBar().setTitle("중고 게시판");
                break;
            case 5:
                getSupportActionBar().setTitle("개인 시간표");
                break;
        }
    }

    public String getTime() throws Exception {

        String time = "";

        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HHmm");

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
            day_temp = dayFormat.format(date);
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


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void showNotification(String title, String message){
        Intent notifyIntent;
        notifyIntent=new Intent(this, Frag_MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,
                notifyIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification=new Notification.Builder(this)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |=Notification.DEFAULT_SOUND;
        NotificationManager notificationManager=
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification);

    }



    class AttendanceCheckThread extends AsyncTask<Void, Void, String> {

        String host = "http://121.187.77.28:25000/attendance.php";
        String myResult;

        String beacon_number = "E20A39F4-73F5-4BC4-A12F-17D1AD07A961";
        String day;
        String week;
        String atd_time;

        public void set_week(String week) {
            this.week = week;
            Log.i("AttendanceCheckThread", week);
        }

        public void set_atd_time(String atd_time) {
            this.atd_time = atd_time;
            Log.i("AttendanceCheckThread", atd_time);
        }

        public void set_day(String day) {
            this.day = day;
            Log.i("AttendanceCheckThread", day);
        }

        @Override
        protected void onPreExecute() {
            Log.i("AttendanceCheckThread", "onPreExecute");
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";


                URL connectUrl = new URL(host); // url 설정
                HttpURLConnection conn = (HttpURLConnection) connectUrl
                        .openConnection(); //접속
                //설정=
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.connect();


                // write data
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                //텍스트전송
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"beacon_num\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(beacon_number);
                dos.writeBytes(lineEnd);
                /*
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"attendance_day\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(day);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"attendance_week\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(week);
                dos.writeBytes(lineEnd);
                */

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"attendance_day\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("tue");
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"attendance_week\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("12");
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"attendance_time\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes("1000");
                dos.writeBytes(lineEnd);

                Log.i("AttendanceCheckThread", day);
                Log.i("AttendanceCheckThread", week);
                Log.i("AttendanceCheckThread", atd_time);

                // close streams
                dos.flush(); // finish upload...
                dos.close();

                // get response

                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                }
                myResult = builder.toString();
                conn.disconnect();
            } catch (Exception e) {
                Log.d("AttendanceCheckThread", "exception " + e.getMessage());
                Log.i("AttendanceCheckThread", "fail");
                // TODO: handle exception
            }

            return myResult;

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        protected void onPostExecute(String result) {
            Log.i("BeaconService", "result : " + result);

            AlertDialog.Builder dialog=new AlertDialog.Builder(Frag_MainActivity.this);

            if(result.equals("출석\n")) {
                showNotification("출석체크","출석 완료 하였습니다");
                dialog .setTitle("출석체크 알림")
                        .setMessage("출석 완료 하였습니다")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        }).create().show();
            } else if(result.equals("지각\n")) {
                showNotification("출석체크","지각 처리 되었습니다");
                dialog .setTitle("출석체크 알림")
                        .setMessage("지각 처리 되었습니다")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        }).create().show();
            } else if(result.equals("부재\n")) {
                showNotification("출석체크","부재 처리 되었습니다");
                dialog .setTitle("출석체크 알림")
                        .setMessage("부재 처리 되었습니다")
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {

                            }
                        }).create().show();

            }
        }
    }

    /**
     * 0 : 데이터 없음
     * 1 : 출석완료
     * 2 : 지각
     * 3 : 결석
     * 4 : 부재
     *
     */


}
