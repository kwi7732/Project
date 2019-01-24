package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

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

public class SubjectDetailActivity extends BaseActivity {

	SubjectDetailAdapter subjectDetailAdapteradapter;
	ArrayList<SubjectDetailData> arraylist_subject_detail;
	ListView listView_subject_detail;

	JSONArray jsonArray_subject_detail;

	ImageView button_back;
	TextView textView_title;
	Button button_answersheet;
	TextView textView_id;
	TextView textView_name;
	TextView textView_atd;
	ImageView imageView_profile;

	String title;
	String subject;
	int week;
	String user_id;

	GetSubjectDetail getSubjectDetail;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.subject_detail);

		subject = getIntent().getExtras().getString("subject");
		week = getIntent().getExtras().getInt("which");

		title = ClassInformation.eng2kor(subject) + "(" + week + "주차)";

		button_back = (ImageView)findViewById(R.id.button_back);
		textView_title = (TextView) findViewById(R.id.textView_title);

		listView_subject_detail = (ListView)findViewById(R.id.listView_subject_detail);
		arraylist_subject_detail = new ArrayList<>();
		subjectDetailAdapteradapter = new SubjectDetailAdapter(this, arraylist_subject_detail);
		listView_subject_detail.setAdapter(subjectDetailAdapteradapter);

		button_answersheet = (Button) findViewById(R.id.button_answersheet);
		textView_id = (TextView) findViewById(R.id.textView_id);
		textView_name = (TextView) findViewById(R.id.textView_name);
		textView_atd = (TextView) findViewById(R.id.textView_atd);
		imageView_profile = (ImageView) findViewById(R.id.imageView_profile);

		textView_title.setText(title);

		button_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});

		getSubjectDetail = new GetSubjectDetail();
		getSubjectDetail.execute();
	}

	public void addItem(String user_id, String user_name, String user_profile, int attendance, String question, String question_image, String answer) {
		SubjectDetailData item = new SubjectDetailData();
		item.set_User_Id(user_id);
		item.set_User_Name(user_name);
		item.set_User_Profile(user_profile);
		item.set_Attendance(attendance);
		item.set_Question(question);
		item.set_Question_Image(question_image);
		item.set_Answer(answer);

		arraylist_subject_detail.add(item);
	}

	//댓글 불러오는 쓰레드
	class GetSubjectDetail extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/subject_detail.php";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		@Override
		protected void onPreExecute() {
			Log.i("GetReplyList", "1");
		}

		@Override
		protected String doInBackground(Void... params) {
			StringBuilder builder = new StringBuilder();

			try {
				URL connectUrl = new URL(host);
				HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();


				if(conn != null) {
					conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setUseCaches(false);
					conn.setRequestMethod("POST");
					conn.setRequestProperty("Connection", "Keep-Alive");
					conn.setRequestProperty("ENCTYPE", "multipart/form-data");
					conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
					conn.connect();

					DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

					//텍스트전송
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"week\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(week));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"subject\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(subject);
					dos.writeBytes(lineEnd);

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
				Log.i("GedReplyList", "fail");
			}
			Log.i("GetReplyList", "success");

			return builder.toString();
		}

		@Override
		protected void onPostExecute(String str) {
			Log.i("GetReplyList", "6");
			Log.i("reply", str);

			try {
				jsonArray_subject_detail = new JSONArray(str);

				for(int i = 0; i < jsonArray_subject_detail.length(); i++) {
					Log.i("getting_reply_check", ""+i);
					JSONObject jsonObject = jsonArray_subject_detail.getJSONObject(i);
					String user_id = jsonObject.getString("user_id");
					String user_name = jsonObject.getString("user_name");
					String user_profile = jsonObject.getString("user_profile");
					int attendance = jsonObject.getInt("attendance");
					String question = jsonObject.getString("question");
					String question_image = jsonObject.getString("question_image");
					String answer = jsonObject.getString("answer");

					addItem(user_id, user_name, user_profile, attendance, question, question_image, answer);
				}
				subjectDetailAdapteradapter.notifyDataSetChanged();
			} catch(JSONException e) {
				e.printStackTrace();
				Log.i("GetReply", "jsonerr");
			}

		}
	}

}
