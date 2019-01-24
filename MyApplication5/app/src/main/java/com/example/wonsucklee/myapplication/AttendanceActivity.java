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


public class AttendanceActivity extends BaseActivity {

    JSONArray jsonArray;

    String user_id;
    String title;

    ArrayList<AttendanceData> arraylist_attendance;
    ListView listView_attendance;
    AttendanceAdapter attendanceAdapter;

    GetAttendanceList getAttendanceList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendance);

        title = getIntent().getExtras().getString("title");
        user_id = getIntent().getExtras().getString("user_id");

        arraylist_attendance = new ArrayList<>();
        attendanceAdapter = new AttendanceAdapter(getApplicationContext(), title, user_id, arraylist_attendance);
        listView_attendance = (ListView) findViewById(R.id.listView_attendance);
        listView_attendance.setAdapter(attendanceAdapter);


        getAttendanceList = new GetAttendanceList();
        getAttendanceList.execute();
    }

    public void addItem(int which, int weeks) {
        AttendanceData data = new AttendanceData();
        data.setWhich(which);
        data.setWeeks(weeks);

        arraylist_attendance.add(data);
    }


    class GetAttendanceList extends AsyncTask<Void, Void, String> {

        String host = "http://121.187.77.28:25000/attendance_list.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        String title_temp;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder builder = new StringBuilder();

            try {
                URL connectUrl = new URL(host);
                HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

                title_temp = URLEncoder.encode(title, "utf-8");

                Log.i("BoardDelete", "2");
                if(conn != null) {
                    Log.i("BoardDelete", "3");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                    conn.connect();

                    DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(user_id);
                    dos.writeBytes(lineEnd);

                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"title\"" + lineEnd);
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(title_temp);
                    dos.writeBytes(lineEnd);

                    dos.flush();
                    dos.close();

                    InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                    BufferedReader reader = new BufferedReader(isr);
                    builder = new StringBuilder();
                    String str;
                    while((str = reader.readLine()) != null) {
                        builder.append(str + "\n");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("Board_delete", "fail");
            }
            Log.i("Board_delete", "success");

            return builder.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("Board_delete", str);

            JSONObject jsonObject;

            try {
                jsonObject = new JSONObject(str);

                int week;

                for(int j = 1; j < 16; j++) {
                    int which = j;
                    String key = j + "week";
                    week = jsonObject.getInt(key);

                    addItem(which, week);

                }

                attendanceAdapter.notifyDataSetChanged();
            } catch(JSONException e) {
                e.printStackTrace();
                Log.i("GetBoardList", "jsonerr");
            }
        }
    }

}
