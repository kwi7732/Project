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
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ReplyActivity extends BaseActivity {

	ImageButton button_back;

	ListView listView_reply;
	ReplyAdapter adapter;
	ArrayList<ReplyData> arraylist;

	GetReply getReply;
	InsertReply insertReply;
	ModifyReply modifyReply;
	DeleteReply deleteReply;

	JSONArray jsonArray_reply;

	ViewGroup layout_reply_insert;
	EditText editText_reply;
	ImageView button_send;

	TextView textView_no_reply;

	String reply_to_insert = "";
	String reply_to_modify = "";
	int r_number_to_delete = -1;
	int r_number_to_modify = -1;

	int what;
	int board_number;
	String user_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reply);

		what = getIntent().getExtras().getInt("what");
		board_number = getIntent().getExtras().getInt("board_number");
		user_id = getIntent().getExtras().getString("user_id");

		button_back = (ImageButton)findViewById(R.id.button_back);
		listView_reply = (ListView)findViewById(R.id.listView_reply);
		arraylist = new ArrayList<>();
		adapter = new ReplyAdapter(this, arraylist);
		listView_reply.setAdapter(adapter);

		textView_no_reply = (TextView)findViewById(R.id.textView_no_reply);

		layout_reply_insert = (LinearLayout)findViewById(R.id.layout_reply_insert);
		editText_reply = (EditText)findViewById(R.id.editText_reply);
		button_send = (ImageView)findViewById(R.id.imageView_send);

		editText_reply.setOnFocusChangeListener(new View.OnFocusChangeListener() {//댓글달기에 포커스 없을때 gone처리
			@Override
			public void onFocusChange(View v, boolean hasFocus) {

			}
		});

		button_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				setResult(RESULT_OK, intent);
				finish();
			}
		});


		button_send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//layout_re_reply_insert.setVisibility(View.VISIBLE);
				editText_reply.requestFocus();

				button_send.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if(!editText_reply.getText().toString().equals("")) {
							reply_to_insert = editText_reply.getText().toString();
							insertReply = new InsertReply();
							insertReply.execute();

							editText_reply.setText("");
							InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
							immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
						}
					}
				});


			}
		});

		AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				int temp_position = position;

				final ReplyData data = arraylist.get(position);

				if(data.get_User_Id().equals(user_id) && data.get_Is_Deleted() != 1) { //내가 올린게 맞으면
					PopupMenu popupMenu = new PopupMenu(getApplicationContext(), view);
					getMenuInflater().inflate(R.menu.menu_modify, popupMenu.getMenu());

					popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
						@Override
						public boolean onMenuItemClick(MenuItem item) {
							switch(item.getItemId()) {
								case R.id.menu_modify:
									AlertDialog.Builder dialog = new AlertDialog.Builder(ReplyActivity.this);
									final EditText editText_dialog = new EditText(ReplyActivity.this);

									dialog.setTitle("댓글 수정");
									dialog.setView(editText_dialog);
									editText_dialog.setText(data.get_Content());

									dialog.setPositiveButton("수정", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											String message  = editText_dialog.getText().toString();

											if(!message.equals("")) {
												reply_to_modify = message;
												r_number_to_modify = data.get_R_Number();
												Log.i("asdf", ""+ r_number_to_modify);
												modifyReply = new ModifyReply();
												modifyReply.execute();
											}
											dialog.dismiss();
										}
									}); //수락을 눌렀을 때

									dialog.setNegativeButton("취소", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									}); //거절을 눌렀을 때
									dialog.show();

									break;
								case R.id.menu_delete:
									AlertDialog.Builder dialog_delete = new AlertDialog.Builder(ReplyActivity.this);

									dialog_delete.setTitle("댓글을 삭제하시겠습니까?");

									dialog_delete.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											r_number_to_delete = data.get_R_Number();
											deleteReply = new DeleteReply();
											deleteReply.execute();
											dialog.dismiss();
										}
									}); //수락을 눌렀을 때

									dialog_delete.setNegativeButton("취소", new DialogInterface.OnClickListener() {
										@Override
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										}
									}); //거절을 눌렀을 때
									dialog_delete.show();
									break;
							}
							return false;
						}
					});
					popupMenu.show();
				}

			}
		};
		listView_reply.setOnItemClickListener(onItemClickListener);


		getReply = new GetReply();
		getReply.execute();
	}

	public void addItem(int r_number, String user_id, String user_nick, String user_profile, String content, String writetime, int is_deleted) {
		ReplyData item = new ReplyData();

		item.set_R_Number(r_number);
		item.set_User_Id(user_id);
		item.set_User_Nick(user_nick);
		item.set_User_Profile(user_profile);
		item.set_Content(content);
		item.set_Writetime(writetime);
		item.set_Is_Deleted(is_deleted);

		arraylist.add(item);
	}

	//댓글 불러오는 쓰레드
	class GetReply extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/reply_list.php";

		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		String what_temp;
		String board_number_temp;
		String user_id_temp;

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

				what_temp = String.valueOf(what);
				board_number_temp = String.valueOf(board_number);
				user_id_temp = URLEncoder.encode(user_id, "utf-8");

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
					dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(what_temp);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"board_number\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(board_number_temp);
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
				jsonArray_reply = new JSONArray(str);

				for(int i = 0; i < jsonArray_reply.length(); i++) {
					Log.i("getting_reply_check", ""+i);
					JSONObject jsonObject = jsonArray_reply.getJSONObject(i);
					int r_number = jsonObject.getInt("reply_number");
					String user_id = jsonObject.getString("user_id");
					String user_nick = jsonObject.getString("user_nick");
					String user_profile = jsonObject.getString("user_profile");
					String content = jsonObject.getString("content");
					String writetime = jsonObject.getString("writetime");
					int is_deleted = jsonObject.getInt("is_deleted");

					addItem(r_number, user_id, user_nick, user_profile, content, writetime, is_deleted);
					textView_no_reply.setVisibility(View.GONE);
				}
				adapter.notifyDataSetChanged();
			} catch(JSONException e) {
				e.printStackTrace();
				Log.i("GetReply", "jsonerr");
			}

		}
	}

	//댓글달기
	class InsertReply extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/reply_insert.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		String user_id_temp;

		@Override
		protected void onPreExecute() {
			Log.i("InsertReply", "1");
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

					reply_to_insert = URLEncoder.encode(reply_to_insert, "utf8");
					user_id_temp = URLEncoder.encode(user_id, "utf8");

					DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(what));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"board_number\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(board_number));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"user_id\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(user_id_temp);
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"content\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(reply_to_insert);
					dos.writeBytes(lineEnd);

					dos.flush();
					dos.close();

					Log.i("InsertReply", "" + what + board_number + user_id + reply_to_insert);

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
				Log.i("InsertReply", "fail");
			}
			Log.i("InsertReply", "success");

			return builder.toString();
		}

		@Override
		protected void onPostExecute(String str) {
			Log.i("InseretReply", str);

			if(str.equals("error\n")) {
				Toast.makeText(getApplicationContext(), "다시 시도하여 주십시오", Toast.LENGTH_LONG).show();
			} else {
				Log.i("InseretReply", str);
				Toast.makeText(getApplicationContext(), "댓글 입력 완료", Toast.LENGTH_LONG).show();
				arraylist.clear();
				getReply = new GetReply();
				getReply.execute();
			}
		}
	}

	//댓글수정
	class ModifyReply extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/reply_modify.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		@Override
		protected void onPreExecute() {
			Log.i("ModifyReply", "1");
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

					reply_to_modify = URLEncoder.encode(reply_to_modify, "utf8");

					DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(what));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"reply_number\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(r_number_to_modify));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"content\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(reply_to_modify);
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
				Log.i("Imodify_reply", "fail");
			}
			Log.i("modify_reply", "success");

			return builder.toString();
		}

		@Override
		protected void onPostExecute(String str) {
			Log.i("modify_Reply", str);

			if(str.equals("error\n")) {
				Toast.makeText(getApplicationContext(), "다시 시도하여 주십시오", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "댓글 수정 완료", Toast.LENGTH_LONG).show();
				arraylist.clear();
				getReply = new GetReply();
				getReply.execute();
			}
		}
	}

	//댓글삭제
	class DeleteReply extends AsyncTask<Void, Void, String> {

		String host = "http://121.187.77.28:25000/reply_delete.php";
		String lineEnd = "\r\n";
		String twoHyphens = "--";
		String boundary = "*****";

		@Override
		protected void onPreExecute() {
			Log.i("DeleteReply", "1");
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

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"what\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(what));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"reply_number\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(r_number_to_delete));
					dos.writeBytes(lineEnd);

					dos.writeBytes(twoHyphens + boundary + lineEnd);
					dos.writeBytes("Content-Disposition: form-data; name=\"board_number\"" + lineEnd);
					dos.writeBytes(lineEnd);
					dos.writeBytes(String.valueOf(board_number));
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
				Log.i("delete_reply", "fail");
			}
			Log.i("delete_reply", "success");

			return builder.toString();
		}

		@Override
		protected void onPostExecute(String str) {
			Log.i("delete_Reply", str);

			if(str.equals("error\n")) {
				Toast.makeText(getApplicationContext(), "다시 시도하여 주십시오", Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(), "댓글 삭제 완료", Toast.LENGTH_LONG).show();
				arraylist.clear();
				getReply = new GetReply();
				getReply.execute();
			}
		}
	}


	public class ReplyAdapter extends BaseAdapter {

		Context context;
		ArrayList<ReplyData> arraylist;

		public ReplyAdapter(Context context, ArrayList<ReplyData> arraylist) {
			this.context = context;
			this.arraylist = arraylist;
		}

		@Override
		public int getCount() {
			return arraylist.size();
		}

		@Override
		public Object getItem(int position) {
			return arraylist.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			ImageView imageView_profile;
			TextView textView_nick;
			TextView textView_content;
			TextView textView_writetime;
			TextView imageView_depth;

			final ReplyData data;

			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflater.inflate(R.layout.reply_item, parent, false);
			}

			imageView_profile = (ImageView) convertView.findViewById(R.id.imageView_profile);
			textView_nick = (TextView) convertView.findViewById(R.id.textView_nick);
			textView_content = (TextView) convertView.findViewById(R.id.textView_content);
			textView_writetime = (TextView) convertView.findViewById(R.id.textView_writetime);

			data = arraylist.get(position);
			String url = "http://121.187.77.28:25000/uploads/" + data.get_User_Profile();

			Log.i("adsfsadfasdfsadf", url);

			if (data.get_User_Profile().equals("default_profile")) { //기본이미지면 로컬에서 로드
				BitmapFactory.Options options = new BitmapFactory.Options();
				options.inSampleSize = 2;
				final Bitmap image_profile = BitmapFactory.decodeResource(context.getResources(), R.drawable.default_profile, options);
				imageView_profile.setImageBitmap(image_profile);
			} else { //기본이미지가 아니면 서버에서 로드
				Glide.with(context)
						.load(url)
						.into(imageView_profile);
			}

			textView_nick.setText(data.get_User_Nick());
			textView_content.setText(data.get_Content());
			textView_writetime.setText(data.get_Writetime());

			return convertView;
		}

	}

}
