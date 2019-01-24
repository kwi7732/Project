package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

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

public class MessageActivity extends BaseActivity {

	ImageView button_back;

	EditText editText_message;
	Button button_send;

	MessageAdapter adapter;
	ListView listView_message;
	ArrayList<MessageData> arraylist;

	JSONArray jsonArray;

	GetMessage getMessage;
	SendMessage sendMessage;

	String user_id;
	String user_name;
	String user_major;
	String token;

	String other_user_id;
	String other_user_name;
	String other_user_major;
	String other_user_profile;

	String message_to_send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.message_list);

		user_id = getIntent().getExtras().getString("user_id");
		user_name = getIntent().getExtras().getString("user_name");
		user_major = getIntent().getExtras().getString("user_major");
		token = getIntent().getExtras().getString("token");

		other_user_id = getIntent().getExtras().getString("other_user_id");
		other_user_name = getIntent().getExtras().getString("other_uesr_name");
		other_user_major = getIntent().getExtras().getString("other_user_major");
		other_user_profile = getIntent().getExtras().getString("other_user_profile");

		button_back = (ImageView)findViewById(R.id.button_back);
		button_send = (Button) findViewById(R.id.button_send);
		editText_message = (EditText) findViewById(R.id.editText_message);

		button_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(editText_message.getText() != null) {
					message_to_send = editText_message.getText().toString();
					editText_message.setText("");

					sendMessage = new SendMessage();
					sendMessage.execute();
				}
			}
		});

		listView_message = (ListView)findViewById(R.id.listView_message);
		arraylist = new ArrayList<>();
		adapter = new MessageAdapter(getApplicationContext(), arraylist, user_id);
		listView_message.setAdapter(adapter);

		button_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_CANCELED, intent);
				finish();
			}
		});


		getMessage = new GetMessage();
		getMessage.execute();

	}


	public void addItem(String room_name, String sender_id, String sender_profile, String message, String writetime) {
		MessageData item = new MessageData();

		item.set_Sender_Id(sender_id);
		item.set_Message(message);
		item.set_Sender_Profile(sender_profile);
		item.set_Writetime(writetime);

		arraylist.add(item);
	}

	class GetMessage extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/message_list.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		@Override
		protected void onPreExecute() {
			Log.i("GetMessage", "1");
		}

		@Override
		protected String doInBackground(Void... params) {
			StringBuilder builder = new StringBuilder();

			try {
				URL connectUrl = new URL(host);
				HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

				Log.i("IdSearch", "2");
				if(conn != null) {
					Log.i("IdSearch", "3");
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
					dos.writeBytes("Content-Disposition: form-data; name=\"other_user_id\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(other_user_id);
					dos.writeBytes(lineEnd);

					dos.flush();
					dos.close();

					Log.i("IdSearch", "5");
					InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
					BufferedReader reader = new BufferedReader(isr);
					builder = new StringBuilder();
					String str;
					while((str = reader.readLine()) != null) {
						builder.append(str + "\n");
					}

					Log.i("GetMessage", builder.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("IdSearch", "fail");
			}
			Log.i("IdSearch", "success");

			return builder.toString();
		}

		@Override
		protected void onPostExecute(String str) {
			Log.i("IdSearch", str);
			try {
				Log.i("IdSearch", "7");
				jsonArray = new JSONArray(str);
				Log.i("IdSearch", "8");
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					String room_name = jsonObject.getString("room_name");
					String sender_id = jsonObject.getString("sender_id");
					String message = jsonObject.getString("message");
					String sender_profile = jsonObject.getString("sender_profile");
					String writetime = jsonObject.getString("writetime");

					addItem(room_name, sender_id, sender_profile, message, writetime);
					adapter.notifyDataSetChanged();

				}

			} catch(JSONException e) {
				e.printStackTrace();
				Log.i("IdSearch", "jsonerr");
			}
		}
	}

	class SendMessage extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/message_insert.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		String message_temp;
		String user_name_temp;
		String user_major_temp;

		@Override
		protected void onPreExecute() {
			Log.i("GetMessage", "1");
		}

		@Override
		protected String doInBackground(Void... params) {
			StringBuilder builder = new StringBuilder();

			try {
				URL connectUrl = new URL(host);
				HttpURLConnection conn = (HttpURLConnection) connectUrl.openConnection();

				message_temp = URLEncoder.encode(message_to_send, "utf-8");
				user_name_temp = URLEncoder.encode(user_name, "utf-8");
				user_major_temp = URLEncoder.encode(user_major, "utf-8");

				Log.i("IdSearch", "2");
				if(conn != null) {
					Log.i("IdSearch", "3");
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
					dos.writeBytes("Content-Disposition: form-data; name=\"sender_id\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(user_id);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"sender_name\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(user_name_temp);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"sender_major\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(user_major_temp);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"receiver_id\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(other_user_id);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"token\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(token);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"message\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(message_temp);
					dos.writeBytes(lineEnd);

					dos.flush();
					dos.close();

					Log.i("IdSearch", "5");
					InputStreamReader isr = new InputStreamReader(conn.getInputStream(), "UTF-8");
					BufferedReader reader = new BufferedReader(isr);
					builder = new StringBuilder();
					String str;
					while((str = reader.readLine()) != null) {
						builder.append(str + "\n");
					}

					Log.i("GetMessage", builder.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("IdSearch", "fail");
			}
			Log.i("IdSearch", "success");

			return builder.toString();
		}

		@Override
		protected void onPostExecute(String str) {
			Log.i("IdSearch", str);

			Log.i("SnedMessage", "result : " + str);


			if(str.equals("1\n")) {
				arraylist.clear();
				getMessage = new GetMessage();
				getMessage.execute();
			}
				/*
				jsonArray = new JSONArray(str);

				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);

					String sender_id = jsonObject.getString("sender_id");
					String receiver_id = jsonObject.getString("receiver_id");
					String message = jsonObject.getString("message");
					String writetime = jsonObject.getString("writetime");

					addItem(sender_id, receiver_id, message, writetime);
					adapter.notifyDataSetChanged();

				}
				*/

		}
	}

}
