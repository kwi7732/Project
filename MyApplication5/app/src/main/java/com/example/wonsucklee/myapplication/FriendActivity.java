package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_MESSAGE;

public class FriendActivity extends BaseActivity {

	ImageButton button_back;

	ImageView imageView_profile;
	TextView textView_name;
	TextView textView_major;

	FriendAdapter adapter;
	ListView listView_friend_list;
	ArrayList<FriendData> arraylist;

	String user_id;
	String user_name;
	String user_major;
	String user_profile;
	String token;

	JSONArray jsonArray;

	GetFriendList getFriendList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend);

		button_back = (ImageButton) findViewById(R.id.button_back);

		imageView_profile = (ImageView)findViewById(R.id.imageView_profile);
		textView_name = (TextView)findViewById(R.id.textView_name);
		textView_major = (TextView)findViewById(R.id.textView_major);

		listView_friend_list = (ListView) findViewById(R.id.listView_friend_list);

		user_id = getIntent().getExtras().getString("user_id");
		user_name = getIntent().getExtras().getString("user_name");
		user_major = getIntent().getExtras().getString("user_major");
		token = getIntent().getExtras().getString("token");

		arraylist = new ArrayList<>();
		adapter = new FriendAdapter(getApplicationContext(), arraylist); //fragment는 getActivity로 context 얻기
		listView_friend_list.setAdapter(adapter);

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

				FriendData data = arraylist.get(position);

				Intent intent = new Intent(getApplicationContext(), MessageActivity.class);
				intent.putExtra("user_id", user_id);
				intent.putExtra("user_name", user_name);
				intent.putExtra("user_major", user_major);
				intent.putExtra("token", token);

				intent.putExtra("other_user_id", data.get_User_Id());
				intent.putExtra("other_user_name", data.get_User_Name());
				intent.putExtra("other_user_major", data.get_User_Major());
				intent.putExtra("other_user_profile", data.get_User_Profile());
				startActivityForResult(intent, REQUEST_CODE_TO_MESSAGE);

			}
		};
		listView_friend_list.setOnItemClickListener(onItemClickListener);

		getFriendList = new GetFriendList();
		getFriendList.execute();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
	}

	public void addItem(String user_id, String user_name, String user_profile, String user_major) {
		FriendData item = new FriendData();
		item.set_User_Id(user_id);
		item.set_User_Name(user_name);
		item.set_User_Profile(user_profile);
		item.set_User_Major(user_major);

		arraylist.add(item);
	}

	//친구목록 불러오는 쓰레드
	class GetFriendList extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/friend_list.php";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		@Override
		protected void onPreExecute() {
			Log.i("GetFriendList", "1");
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
					Log.i("IdSearch", "4");
					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(user_id);
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
			Log.i("IdSearch", "6");
			try {
				Log.i("IdSearch", str);
				jsonArray = new JSONArray(str);
				Log.i("IdSearch", "8");
				for(int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					String user_profile = jsonObject.getString("user_profile");
					String user_id = jsonObject.getString("user_id");
					String user_name = jsonObject.getString("user_name");
					String user_major = jsonObject.getString("user_major");

					addItem(user_id, user_name, user_profile, user_major);
				}

				adapter.notifyDataSetChanged();

			} catch(JSONException e) {
				e.printStackTrace();
				Log.i("IdSearch", "jsonerr");
			}

			Log.i("viewPager", "친구");
		}
	}
}
