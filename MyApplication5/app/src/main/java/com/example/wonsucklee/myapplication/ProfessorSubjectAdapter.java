package com.example.wonsucklee.myapplication;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import static com.example.wonsucklee.myapplication.Frag_MainActivity.REQUEST_CODE_TO_QUIZ_INSERT;

/**
 * Created by crown on 2018-02-08.
 */

public class ProfessorSubjectAdapter extends BaseAdapter {

    ArrayList<ProfessorSubjectData> arrayList;
    Context context;

    int pos_to_change;
    String title;
    String user_id;
    int week;

    ClassCancelThread classCancelThread;

    public ProfessorSubjectAdapter(Context context, String title, String user_id, ArrayList<ProfessorSubjectData> arrayList) {
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

        ProfessorSubjectData data = arrayList.get(position);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.professor_subject_item, parent, false);
            holder = new ViewHolder();

            holder.textView_which = (TextView) convertView.findViewById(R.id.textView_which);
            holder.textView_is_completed = (TextView) convertView.findViewById(R.id.textView_is_completed);
            holder.button_quiz_insert = (Button) convertView.findViewById(R.id.button_quiz_insert);
            holder.button_cancel = (Button) convertView.findViewById(R.id.button_cancel);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView_which.setText(data.getWhichString());
        holder.textView_is_completed.setText(data.getIsCompleted());

        holder.button_quiz_insert.setTag(position);
        holder.button_cancel.setTag(position);

        int is_completed = data.get_iscompleted();

        switch (is_completed) {
            case 0:
                holder.button_cancel.setVisibility(View.VISIBLE);
                break;
            case 1:
            case 2:
                holder.button_cancel.setVisibility(View.INVISIBLE);
                break;
        }

        holder.button_quiz_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                ProfessorSubjectData data = arrayList.get(position);
                int which = data.getWhich();

                Intent intent = new Intent(context, QuizInsertActivity.class);
                intent.putExtra("title", title);
                intent.putExtra("user_id", user_id);
                intent.putExtra("question", data.getQuestion());
                intent.putExtra("question_image", data.getQuestoinImage());
                intent.putExtra("which", which);

                context.startActivity(intent);
            }
        });

        holder.button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (Integer) view.getTag();
                ProfessorSubjectData data = arrayList.get(position);
                week = data.getWhich();

                pos_to_change = position;
                classCancelThread = new ClassCancelThread();
                classCancelThread.execute();

            }
        });

        holder.button_cancel.setFocusable(false);
        holder.button_quiz_insert.setFocusable(false);
        return convertView;
    }

    class ViewHolder {
        TextView textView_which;
        TextView textView_is_completed;
        Button button_quiz_insert;
        Button button_cancel;
    }

    class ClassCancelThread extends AsyncTask<Void, Void, String> {
        private String myResult;

        String host = "http://121.187.77.28:25000/break_class.php";

        @Override
        protected void onPreExecute() {
            Log.i("BoardInsertThread", "onPreExecute");
        }
        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            try {
                String lineEnd = "\r\n";
                String twoHyphens = "--";
                String boundary = "*****";


                URL connectUrl = new URL(host); // url 설정
                HttpURLConnection conn = (HttpURLConnection) connectUrl
                        .openConnection(); //접속
                //설정=
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setUseCaches(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                //conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

                conn.connect();


                // write data
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());


                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"TITLE\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(title);
                dos.writeBytes(lineEnd);

                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"week\"" + lineEnd);
                dos.writeBytes(lineEnd);
                dos.writeBytes(String.valueOf(week));
                dos.writeBytes(lineEnd);


                // close streams
                dos.flush(); // finish upload...
                dos.close();

                // get response
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuilder builder = new StringBuilder();
                String str;
                while ((str = reader.readLine()) != null) {       // 서버에서 라인단위로 보내줄 것이므로 라인단위로 읽는다
                    builder.append(str + "\n");                     // View에 표시하기 위해 라인 구분자 추가
                }
                myResult = builder.toString();
                conn.disconnect();
            } catch (Exception e) {
                Log.d("BoardInsertThread", "exception " + e.getMessage());
                Log.i("BoardInsertThread", "fail");
                // TODO: handle exception
            }
            return myResult;
        }
        @Override
        protected void onPostExecute(String result) {
            Log.i("BoardInesrtThraed", "result : " + result);

            if(result.equals("1\n")) {
                ProfessorSubjectData data = arrayList.get(pos_to_change);
                data.setIsCompleted(2);
                arrayList.set(pos_to_change, data);
                Toast.makeText(context, "휴강완료", Toast.LENGTH_LONG).show();
                notifyDataSetChanged();
            }

        }
    }
}
