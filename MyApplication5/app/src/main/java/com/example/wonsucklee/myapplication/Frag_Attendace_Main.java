package com.example.wonsucklee.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_ATTENDACE_MAIN;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_BOARD_VIEW;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_PROFESSOR_MAIN;

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

public class Frag_Attendace_Main extends Fragment {

    GetSubjectList getSubjectList;
    GetProfessorSubjectList getProfessorSubjectList;

    JSONArray jsonArray;

    SubjectList_Adapter subject_list_adapter;
    ArrayList<ClassInformation> subject_list_arrayList;
    ListView listView_subject;

    String user_id;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.attendace_main, container, false);
        user_id = getArguments().getString("user_id");

        Log.i("user_id", user_id);
        listView_subject = (ListView) view.findViewById(R.id.listView_subject);

        subject_list_arrayList = new ArrayList<>();
        subject_list_adapter = new SubjectList_Adapter(getActivity(), subject_list_arrayList);
        listView_subject.setAdapter(subject_list_adapter);

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int arraylist_position = position;

                ClassInformation data = subject_list_arrayList.get(position);
                String title = data.getClassName();
                String professor = data.getProfessor();
                String day_and_time = data.getTime();

                if(user_id.charAt(0) == '0') { //교수
                    String is_completed = data.getIsCompleted();

                    Intent intent = new Intent(getActivity(), ProfessorSubjectActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("title", title);
                    intent.putExtra("professor", professor);
                    intent.putExtra("day_and_time", day_and_time);
                    intent.putExtra("is_completed", is_completed);


                    startActivityForResult(intent, REQUEST_CODE_TO_PROFESSOR_MAIN);
                } else { //학생
                    Intent intent = new Intent(getActivity(), AttendanceActivity.class);
                    intent.putExtra("user_id", user_id);
                    intent.putExtra("title", title);
                    intent.putExtra("professor", professor);
                    intent.putExtra("day_and_time", day_and_time);

                    startActivityForResult(intent, REQUEST_CODE_TO_ATTENDACE_MAIN);
                }

             }
        };
        listView_subject.setOnItemClickListener(onItemClickListener);

        if(user_id.charAt(0) == '0') { //교수
            getProfessorSubjectList = new GetProfessorSubjectList();
            getProfessorSubjectList.execute();
        } else { //학생
            getSubjectList = new GetSubjectList();
            getSubjectList.execute();
        }

        return view;
    }

    public void addItem(String title, String professor, String day_and_time) {
        ClassInformation item = new ClassInformation();
        item.setClassName(title);
        item.setProfessor(professor);
        item.setTime(day_and_time);
        //array_day = day_and_time.split(",");

        subject_list_arrayList.add(item);
    }

    public void addItem(String title, String professor, String day_and_time, String is_completed) {
        ClassInformation item = new ClassInformation();
        item.setClassName(title);
        item.setProfessor(professor);
        item.setTime(day_and_time);

        item.setIsCompleted(is_completed);

        subject_list_arrayList.add(item);
    }

    //교수 과목리스트 불러오는
    class GetProfessorSubjectList extends AsyncTask<Void, Void, String> {

        String host = "http://121.187.77.28:25000/professor_sub_list.php";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        @Override
        protected void onPreExecute() {
            subject_list_arrayList.clear();
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder builder = new StringBuilder();

            HttpURLConnection conn = null;
            try {
                URL connectUrl = new URL(host);
                conn = (HttpURLConnection) connectUrl.openConnection();

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
                dos.writeBytes("Content-Disposition: form-data; name=\"userID\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id);
                dos.writeBytes(lineEnd);


                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                builder = new StringBuilder();
                String str;
                while((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }

                reader.close();
                isr.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("GetBoardList", "fail");
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("GetBoardList", str);

            try {
                jsonArray = new JSONArray(str);

                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.getString("title");
                    String professor = jsonObject.getString("professor");
                    String day_and_time = jsonObject.getString("day");
                    String is_completed = jsonObject.getString("is_completed");

                    addItem(title, professor, day_and_time, is_completed);
                }

                subject_list_adapter.notifyDataSetChanged();

                //((Frag_MainActivity) getActivity()).set_Subject(subject_list_arrayList);
            } catch(JSONException e) {
                e.printStackTrace();
                Log.i("GetBoardList", "jsonerr");
            }
        }
    }

    //학생과목 목록을 불러오는 쓰레드
    class GetSubjectList extends AsyncTask<Void, Void, String> {

        String host = "http://121.187.77.28:25000/subject_list.php";

        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        @Override
        protected void onPreExecute() {
            subject_list_arrayList.clear();
        }

        @Override
        protected String doInBackground(Void... params) {
            StringBuilder builder = new StringBuilder();

            HttpURLConnection conn = null;
            try {
                URL connectUrl = new URL(host);
                conn = (HttpURLConnection) connectUrl.openConnection();

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


                InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(isr);
                builder = new StringBuilder();
                String str;
                while((str = reader.readLine()) != null) {
                    builder.append(str + "\n");
                }

                reader.close();
                isr.close();
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
                Log.i("GetBoardList", "fail");
            }
            return builder.toString();
        }

        @Override
        protected void onPostExecute(String str) {
            Log.i("GetBoardList", str);

            try {
                jsonArray = new JSONArray(str);

                JSONObject jsonObject;

                for(int i = 0; i < jsonArray.length(); i++) {
                    jsonObject = jsonArray.getJSONObject(i);

                    String title = jsonObject.getString("title");
                    String professor = jsonObject.getString("professor");
                    String day_and_time = jsonObject.getString("day");

                    addItem(title, professor, day_and_time);
                }

                subject_list_adapter.notifyDataSetChanged();

                //((Frag_MainActivity) getActivity()).set_Subject(subject_list_arrayList);
            } catch(JSONException e) {
                e.printStackTrace();
                Log.i("GetBoardList", "jsonerr");
            }
        }
    }



}
