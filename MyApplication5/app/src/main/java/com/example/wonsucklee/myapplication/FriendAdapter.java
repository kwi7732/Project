package com.example.wonsucklee.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class FriendAdapter extends BaseAdapter{

	Context context;
	ArrayList<FriendData> arraylist;

	public FriendAdapter(Context context, ArrayList<FriendData> arraylist) {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ImageView imageView_profile = null;
		TextView textView_name;
		TextView textView_major;

		FriendData data;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.friend_item, parent, false);
		}

		imageView_profile = (ImageView) convertView.findViewById(R.id.imageView_profile);
		textView_name = (TextView) convertView.findViewById(R.id.textView_name);
		textView_major = (TextView) convertView.findViewById(R.id.textView_major);

		textView_name.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "font1.ttf"));
		textView_major.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "font1.ttf"));

		data = arraylist.get(position);

		String profile_url = "http://121.187.77.28:25000/uploads/" + data.get_User_Profile();

		Glide.with(context)
				.load(profile_url)
				.bitmapTransform(new CropCircleTransformation(context))
				.into(imageView_profile);

		textView_name.setText(data.get_User_Name());
		textView_major.setText(data.get_User_Major());

		return convertView;
	}
}
