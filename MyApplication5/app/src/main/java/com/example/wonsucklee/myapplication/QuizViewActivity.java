package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by crown on 2018-02-08.
 */


public class QuizViewActivity extends BaseActivity {

    ImageButton button_back;
    TextView textView_back;

    TextView textView_question;
    TextView textView_answer;
    EditText editText_answer;
    ImageView imageView_content;
    Button button_insert;

    int week;
    String title;
    String user_id;
    String question;
    String answer;

    QuizViewThread quizViewThread;
    QuizAnswerThread quizAnswerThread;

    AlertDialog.Builder builder;

    View layout_quiz;
    View layout_no_quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quiz_view);

        button_back = (ImageButton) findViewById(R.id.button_back);
        textView_back = (TextView) findViewById(R.id.textView_back);
        textView_question = (TextView) findViewById(R.id.textView_question);
        imageView_content = (ImageView) findViewById(R.id.imageView_content);
        textView_answer = (TextView) findViewById(R.id.textView_answer);
        editText_answer = (EditText) findViewById(R.id.editText_answer);

        layout_quiz = (RelativeLayout) findViewById(R.id.layout_quiz);
        layout_no_quiz = (RelativeLayout) findViewById(R.id.layout_no_quiz);

        button_insert = (Button) findViewById(R.id.button_insert);

        week = getIntent().getExtras().getInt("which");
        title = getIntent().getExtras().getString("title");
        user_id = getIntent().getExtras().getString("user_id");

        builder = new AlertDialog.Builder(this);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });


        button_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer = editText_answer.getText().toString();

                quizAnswerThread = new QuizAnswerThread();
                quizAnswerThread.execute();
            }
        });

        quizViewThread = new QuizViewThread();
        quizViewThread.execute();
    }


    class QuizViewThread extends AsyncTask<Void, Void, String> {
        private String myResult;

        String host = "http://121.187.77.28:25000/quiz_view.php";

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

                FileInputStream mFileInputStream = null;

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

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"subject\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(title);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"week\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(String.valueOf(week));
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

            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);

                int is_questioned = jsonObject.getInt("is_questioned");
                int is_answered = jsonObject.getInt("is_answered");
                String question = jsonObject.getString("question");
                String answer = jsonObject.getString("answer");
                String image_content = jsonObject.getString("image_content");

                if(is_questioned == 1) { //퀴즈존재o
                    if(is_answered == 1) { //답변 했으면
                        layout_no_quiz.setVisibility(View.GONE);
                        textView_question.setText(question);
                        textView_answer.setText(answer);
                        textView_answer.setVisibility(View.VISIBLE);
                        editText_answer.setVisibility(View.INVISIBLE);
                        button_insert.setVisibility(View.GONE);

                        String image_url = "http://121.187.77.28:25000/uploads/" + image_content;

                        Glide.with(getApplicationContext())
                                .load(image_url)
                                .into(imageView_content);

                    } else { //답변 안했으면
                        layout_no_quiz.setVisibility(View.GONE);
                        textView_question.setText(question);
                        textView_answer.setVisibility(View.INVISIBLE);
                        editText_answer.setVisibility(View.VISIBLE);
                        button_insert.setVisibility(View.VISIBLE);

                        String image_url = "http://121.187.77.28:25000/uploads/" + image_content;

                        Glide.with(getApplicationContext())
                                .load(image_url)
                                .into(imageView_content);
                    }
                } else { //퀴즈존재x
                    layout_quiz.setVisibility(View.GONE);
                    layout_no_quiz.setVisibility(View.VISIBLE);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
    }

    class QuizAnswerThread extends AsyncTask<Void, Void, String> {
        private String myResult;

        String host = "http://121.187.77.28:25000/quiz_answer.php";

        String answer_temp;

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


                answer_temp = URLEncoder.encode(answer, "utf-8");

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

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(user_id);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"subject\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(title);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"answer\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(answer_temp);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"week\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(String.valueOf(week));
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

            if(result.equals("1\n")) {
                Log.i("BoardInseretThread", "upload 완료");
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "업로드 에러..", Toast.LENGTH_LONG).show();
            }

        }
    }
}
