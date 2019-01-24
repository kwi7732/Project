package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MessageAdapter extends BaseAdapter {

	Context context;
	ArrayList<MessageData> arraylist;

	String user_id;

	String sender_id= null;
	String receiver_id = null;
	String writetime = null;
	String message = null;

	//Watch_Message watch_Message;

	public MessageAdapter(Context context, ArrayList<MessageData> arraylist, String user_id) {
		this.user_id = user_id;
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

		ViewHolder holder = null;
		View v = convertView;

		MessageData data = arraylist.get(position);

		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.message_item, parent, false);
			holder = new ViewHolder();

			holder.layout_center = (LinearLayout) v.findViewById(R.id.layout_center);
			holder.viewRight = (View) v.findViewById(R.id.imageViewleft);
			holder.viewLeft = (View) v.findViewById(R.id.imageViewright);
			holder.textView_time = (TextView) v.findViewById(R.id.textView_time);

			holder.layout_left = (LinearLayout) v.findViewById(R.id.layout_left);
			holder.textView_nick_left = (TextView) v.findViewById(R.id.textView_nick_left);
			holder.textView_content_left = (TextView) v.findViewById(R.id.textView_content_left);
			holder.textView_writetime_left = (TextView) v.findViewById(R.id.textView_writetime_left);
			holder.imageView_profile_left = (ImageView) v.findViewById(R.id.imageView_profile_left);

			holder.layout_right = (RelativeLayout) v.findViewById(R.id.layout_right);
			holder.textView_content_right = (TextView) v.findViewById(R.id.textView_content_right);
			holder.textView_writetime_right = (TextView) v.findViewById(R.id.textView_writetime_right);

			holder.layout_left.setVisibility(View.VISIBLE);
			holder.layout_right.setVisibility(View.VISIBLE);
			holder.layout_center.setVisibility(View.VISIBLE);
			holder.viewRight.setVisibility(View.VISIBLE);
			holder.viewLeft.setVisibility(View.VISIBLE);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();

			holder.layout_left.setVisibility(View.VISIBLE);
			holder.layout_right.setVisibility(View.VISIBLE);
			holder.layout_center.setVisibility(View.VISIBLE);
		}


		if(!(data.get_Sender_Id().equals(user_id))) {//내가 안보냈을때 왼쪽

			holder.layout_right.setVisibility(View.GONE);
			holder.layout_center.setVisibility(View.GONE);

			holder.textView_nick_left.setText(data.get_Sender_Name());
			holder.textView_content_left.setText(data.get_Message());
			holder.textView_writetime_left.setText(data.get_Writetime());

			holder.textView_content_left.setBackgroundResource(R.drawable.bubble_left);

			String profile_url = "http://121.187.77.28:25000/uploads/" + data.get_Sender_Profile();

			Glide.with(context)
					.load(profile_url)
					.into(holder.imageView_profile_left);

		} else if (data.get_Sender_Id().equals(user_id)) { //내가 보냈을 때 오른쪽

			holder.layout_left.setVisibility(View.GONE);
			holder.layout_center.setVisibility(View.GONE);

			//textView_nick_right.setText(data.get_Sender_Id());
			holder.textView_content_right.setText(data.get_Message());
			holder.textView_writetime_right.setText(data.get_Writetime());

			holder.textView_content_right.setBackgroundResource(R.drawable.bubble_right);

		}


		return v;
	}

	/**
	 convertView == null 이라는 부분이 생각보다 많이 호출되어 LayoutInflater를 통한 xml을 불러오는 부분이 많이 실행됩니다.
	 LayoutInflater를 통해서 xml을 불러오는 부분이 생각보다 무거운작업이라고 전문가들을 말합니다.
	 따라서 Holder패턴이라는 패턴을 통해서 최적화
	 */

	class ViewHolder {
		TextView textView_time;
		LinearLayout layout_center;
		View viewRight;
		View viewLeft;

		LinearLayout layout_left;
		TextView textView_nick_left;
		ImageView imageView_profile_left;
		TextView textView_content_left;
		TextView textView_writetime_left;

		RelativeLayout layout_right;
		TextView textView_content_right;
		TextView textView_writetime_right;
	}

}
