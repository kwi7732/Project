package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class SubjectDetailAdapter extends BaseAdapter {

    ArrayList<SubjectDetailData> arrayList;
    Context context;

    public SubjectDetailAdapter(Context context, ArrayList<SubjectDetailData> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
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

        SubjectDetailData data = arrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subject_detail_item, parent, false);
            holder = new ViewHolder();

            holder.textView_id = (TextView) convertView.findViewById(R.id.textView_id);
            holder.textView_name = (TextView) convertView.findViewById(R.id.textView_name);
            holder.textView_atd = (TextView) convertView.findViewById(R.id.textView_atd);
            holder.imageView_profile = (ImageView) convertView.findViewById(R.id.imageView_profile);
            holder.button_answersheet = (Button) convertView.findViewById(R.id.button_answersheet);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String user_id = data.get_User_Id();
        final String user_name = data.get_User_Name();
        String atd = data.get_Attendance();
        final String question = data.get_Question();
        final String question_image = data.get_Question_Image();
        final String answer = data.get_Answer();

        holder.textView_id.setText(user_id);
        holder.textView_name.setText(user_name);
        holder.textView_atd.setText(atd);

        String profile_url = "http://121.187.77.28:25000/uploads/" + data.get_User_Profile();

        Glide.with(context)
                .load(profile_url)
                .into(holder.imageView_profile);

        holder.button_answersheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position_temp = (int) view.getTag();

                SubjectDetailData data = arrayList.get(position_temp);
                Intent intent = new Intent(context, ProfessosrQuizViewActivity.class);
                intent.putExtra("user_name", data.get_User_Name());
                intent.putExtra("question", data.get_Question());
                intent.putExtra("question_image", data.get_Question_Image());
                intent.putExtra("answer", data.get_Answer());

                context.startActivity(intent);
            }
        });

        holder.button_answersheet.setTag(position);

        return convertView;
    }

    class ViewHolder {
        ImageView imageView_profile;
        TextView textView_id;
        TextView textView_name;
        TextView textView_atd;
        Button button_answersheet;
    }
}
