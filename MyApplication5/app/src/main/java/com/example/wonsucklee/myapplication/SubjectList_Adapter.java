package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by crown on 2018-02-08.
 */

public class SubjectList_Adapter extends BaseAdapter {

    ArrayList<ClassInformation> arrayList;
    Context context;

    public SubjectList_Adapter(Context context, ArrayList<ClassInformation> arrayList) {
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

        ClassInformation data = arrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.subject_list_item, parent, false);
            holder = new ViewHolder();

            holder.textView_title = (TextView) convertView.findViewById(R.id.textView_title);
            holder.textView_professor = (TextView) convertView.findViewById(R.id.textView_professor);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView_title.setText(data.eng2kor(data.getClassName()));
        holder.textView_professor.setText(data.getProfessor());

        holder.textView_title.setText(data.eng2kor(data.getClassName()));
        holder.textView_professor.setText(data.getProfessor());

        return convertView;
    }

    class ViewHolder {
        TextView textView_title;
        TextView textView_professor;
    }
}
