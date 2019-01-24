package com.example.wonsucklee.myapplication;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wonsucklee on 2018. 2. 7..
 */

public class LoginRequest extends StringRequest {

    final static private String URL= "http://121.187.77.28:25000/login.php";
    private Map<String, String> parameters;

    public LoginRequest(String userID, String userPassword, String token, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null );
        parameters=new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("token", token);
    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
