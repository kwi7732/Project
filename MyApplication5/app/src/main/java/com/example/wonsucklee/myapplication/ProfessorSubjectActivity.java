package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_ATTENDACE_MAIN;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_BOARD_VIEW;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_PROFESSOR_MAIN;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_QUIZ_INSERT;
import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_SUBJECT_DEATIL;

/**
 * Created by crown on 2018-02-08.
 */


public class ProfessorSubjectActivity extends BaseActivity {

    JSONArray jsonArray;

    String user_id;
    String title;
    String is_completed;

    String [] array_is_completed;
    ArrayList<ProfessorSubjectData> arraylist_professor_subject;
    ListView listView_professor_subject;
    ProfessorSubjectAdapter professorSubjectAdapter;

    TextView textView_title;
    ImageView button_back;

    GetSubjectInfo getSubjectInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_subject);

        title = getIntent().getExtras().getString("title");
        user_id = getIntent().getExtras().getString("user_id");
        is_completed = getIntent().getExtras().getString("is_completed");


        arraylist_professor_subject = new ArrayList<>();
        professorSubjectAdapter = new ProfessorSubjectAdapter(getApplicationContext(), title, user_id, arraylist_professor_subject);
        listView_professor_subject = (ListView) findViewById(R.id.listView_professor_subject);
        listView_professor_subject.setAdapter(professorSubjectAdapter);


        textView_title = (TextView) findViewById(R.id.textView_title);
        button_back = (ImageView) findViewById(R.id.button_back);

        textView_title.setText(ClassInformation.eng2kor(title));

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProfessorSubjectData data = arraylist_professor_subject.get(position);

                int which = data.getWhich();


                Intent intent = new Intent(getApplicationContext(), SubjectDetailActivity.class);
                intent.putExtra("subject", title);
                intent.putExtra("which", which);
                startActivityForResult(intent, REQUEST_CODE_TO_SUBJECT_DEATIL);

            }
        };
        listView_professor_subject.setOnItemClickListener(onItemClickListener);

        getSubjectInfo = new GetSubjectInfo();
        getSubjectInfo.execute();

    }

    public void addItem(int which, int is_completed, String question, String question_image) {
        ProfessorSubjectData data = new ProfessorSubjectData();
        data.setWhich(which);
        data.setIsCompleted(is_completed);
        data.setQuestionImage(question_image);
        data.setQuestion(question);

        arraylist_professor_subject.add(data);
    }

    class GetSubjectInfo extends AsyncTask<Void, Void, String> {
        private String myResult;

        String host = "http://121.187.77.28:25000/subject_info.php";


        @Override
        protected void onPreExecute() {
            Log.i("BoardInsertThread", "onPreExecute");
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
                dos.writeBytes("Content-Disposition: form-data; name=\"subject\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(title);
                dos.writeBytes(lineEnd);

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
                Log.d("BoardInsertThread", "exception " + e.getMessage());
                Log.i("BoardInsertThread", "fail");
                // TODO: handle exception
            }
            return myResult;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("BoardInesrtThraed", "result : " + result);
            JSONArray jsonArray;
            try {
                jsonArray = new JSONArray(result);
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    int which = jsonObject.getInt("week");
                    int is_completed_temp = jsonObject.getInt("is_completed");
                    String question = jsonObject.getString("question");
                    String question_image = jsonObject.getString("question_image");

                    addItem(which, is_completed_temp, question, question_image);

                }

                professorSubjectAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

}
