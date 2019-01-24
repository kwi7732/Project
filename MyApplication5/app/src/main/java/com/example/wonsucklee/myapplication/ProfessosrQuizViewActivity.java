package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by crown on 2018-02-08.
 */


public class ProfessosrQuizViewActivity extends BaseActivity {

    ImageButton button_back;
    TextView textView_back;

    TextView textView_question;
    TextView textView_answer;
    ImageView imageView_content;
    Button button_insert;

    String title;
    String user_id;
    String question;
    String question_image;
    String answer;

    View layout_quiz;
    View layout_no_quiz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.professor_quiz_view);

        button_back = (ImageButton) findViewById(R.id.button_back);
        textView_back = (TextView) findViewById(R.id.textView_back);
        textView_question = (TextView) findViewById(R.id.textView_question);
        imageView_content = (ImageView) findViewById(R.id.imageView_content);
        textView_answer = (TextView) findViewById(R.id.textView_answer);

        layout_quiz = (RelativeLayout) findViewById(R.id.layout_quiz);
        layout_no_quiz = (RelativeLayout) findViewById(R.id.layout_no_quiz);

        button_insert = (Button) findViewById(R.id.button_insert);

        user_id = getIntent().getExtras().getString("user_name");
        title = user_id + "의 답안지";
        question = getIntent().getExtras().getString("question");
        question_image = getIntent().getExtras().getString("question_image");
        answer = getIntent().getExtras().getString("answer");

        String profile_url = "http://121.187.77.28:25000/uploads/" + question_image;

        Glide.with(getApplicationContext())
                .load(profile_url)
                .into(imageView_content);

        textView_back.setText(title);
        textView_question.setText(question);
        textView_answer.setText(answer);

        button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });


    }
}
