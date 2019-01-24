package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by crown on 2018-02-08.
 */

public class AttendanceAdapter extends BaseAdapter {

    ArrayList<AttendanceData> arrayList;
    Context context;

    String title;
    String user_id;

    public AttendanceAdapter(Context context, String title, String user_id, ArrayList<AttendanceData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
        this.title = title;
        this.user_id = user_id;
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        AttendanceData data = arrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.attendance_item, parent, false);
            holder = new ViewHolder();

            holder.textView_which = (TextView) convertView.findViewById(R.id.textView_which);
            holder.textView_weeks = (TextView) convertView.findViewById(R.id.textView_weeks);
            holder.button_quiz = (Button) convertView.findViewById(R.id.button_quiz);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView_which.setText(data.getWhichString());
        holder.textView_weeks.setText(data.getWeeks());

        holder.button_quiz.setTag(position);
        holder.button_quiz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                AttendanceData data = arrayList.get(position);
                int which = data.getWhich();

                Intent intent = new Intent(context, QuizViewActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("user_id", user_id);
                intent.putExtra("which", which);

                context.startActivity(intent);
            }
        });

        holder.textView_weeks.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "font1.ttf"));
        holder.textView_which.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(), "font1.ttf"));
        holder.button_quiz.setTypeface(Typeface.createFromAsset(convertView.getContext().getAssets(),"font1.ttf"));


        return convertView;
    }

    class ViewHolder {
        TextView textView_which;
        TextView textView_weeks;
        Button button_quiz;
    }
}
