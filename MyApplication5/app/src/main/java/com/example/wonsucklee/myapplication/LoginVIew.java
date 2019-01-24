package com.example.wonsucklee.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.AlteredCharSequence;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wonsucklee on 2017. 11. 10..
 */


/**
 * ----------------------------------------- FCM ---------------------------------------------------------
 * SharedPreferences 같은 곳에 토큰 저장 후 getToken() 결과랑 비교하신 후 변경되면 서버로 전송하세요.
 단, GCM -> FCM 으로 변경되면서 앱 설치 후 최초 실행 시 FirebaseInstanceId.getInstance().getToken() 값이 null 이 나온 후 FirebaseInstanceIdService 의 onTokenRefresh() 에서 토큰이 발행됩니다.
 getToken() 을 하는 부분과 onTokenRefresh() 두 군데에서 null 여부 체크 및 기존 토큰과 동일한지 여부를 확인하신 후 서버로 전송하시면 됩니다.
 *
 */

public class LoginVIew extends BaseActivity {

    public static final int REQUEST_CODE_TO_FRAG_MAIN = 1000;

    private AlertDialog dialog;
    Button loginButton;
    TextView registerButton;
    private String userID;
    private String userPassword;
    private String token;

    Response.Listener<String> responseListener;

    public static final int MULTIPLE_PERMISSIONS = 101;
    private String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);

        FirebaseMessaging.getInstance().subscribeToTopic("news");
        FirebaseInstanceId.getInstance().getToken();


        loginButton = (Button)findViewById(R.id.loginButton);
        registerButton = (TextView)findViewById(R.id.registerButton);

        final EditText idText=(EditText)findViewById(R.id.idText);
        final EditText passwordText=(EditText)findViewById(R.id.passwordText);

        checkPermissions();

        responseListener=new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    Log.i("LoginView", "on_Response : " + response);
                    if(response != null) {
                        JSONObject jsonObject = new JSONObject(response);
                        String user_id = jsonObject.getString("user_id");
                        String user_name = jsonObject.getString("name");
                        String user_major = jsonObject.getString("major");
                        String user_profile = jsonObject.getString("image_path");

                        Intent intent_login = new Intent(LoginVIew.this, Frag_MainActivity.class);
                        intent_login.putExtra("user_id", user_id);
                        intent_login.putExtra("user_name", user_name);
                        intent_login.putExtra("user_major", user_major);
                        intent_login.putExtra("user_profile", user_profile);
                        intent_login.putExtra("token", token);

                        SharedPreferences auto_login = getSharedPreferences("auto_login", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = auto_login.edit();
                        editor.putString("user_id", userID);
                        editor.putString("user_password", userPassword);
                        editor.commit();

                        startActivityForResult(intent_login, REQUEST_CODE_TO_FRAG_MAIN);

                    } else {
                        AlertDialog.Builder builder=new AlertDialog.Builder(LoginVIew.this);
                        dialog=builder.setMessage("실패했습니다.")
                                .setNegativeButton("확인",null)
                                .create();
                        dialog.show();

                        return;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                userID=idText.getText().toString();
                userPassword=passwordText.getText().toString();

                if(userID.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder(LoginVIew.this);
                    dialog=builder.setMessage("아이디를 적어주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }

                LoginRequest loginRequest=new LoginRequest(userID, userPassword,  token, responseListener);
                RequestQueue queue= Volley.newRequestQueue(LoginVIew.this);
                queue.add(loginRequest);

                passwordText.setText("");
            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginVIew.this, RegisterActivity.class));
            }
        });

        //자동로그인 아이디, 비밀번호 가져오기
        SharedPreferences pref = getSharedPreferences("auto_login", MODE_PRIVATE);
        userID = pref.getString("user_id", "");
        userPassword = pref.getString("user_password", "");

        //fcm 토큰 가져오기
        SharedPreferences pref_token = getSharedPreferences("fcm_token", MODE_PRIVATE);
        token = pref_token.getString("token", "");
        if(token != "") {
            Log.i("저장된 fcm_token : ", token);
        }

        if(userID != "" && userPassword != "") {
            LoginRequest loginRequest=new LoginRequest(userID, userPassword,  token, responseListener);
            RequestQueue queue= Volley.newRequestQueue(LoginVIew.this);
            queue.add(loginRequest);
        }
    }

    public void onResume() {
        super.onResume();
    }


    private boolean checkPermissions() {
        int result;
        List<String> permissionsList = new ArrayList<>();
        for (String pm : permissions) {
            result = ContextCompat.checkSelfPermission(this, pm);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(pm);
            }
        }

        if (!permissionsList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsList.toArray(new String[permissionsList.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(this.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(this.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(this.permissions[3])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }



}