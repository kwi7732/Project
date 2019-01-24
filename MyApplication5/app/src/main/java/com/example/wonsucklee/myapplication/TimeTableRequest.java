package com.example.wonsucklee.myapplication;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wonsucklee on 2018. 5. 2..
 */

public class TimeTableRequest extends StringRequest {
    private Map<String, String> parameters;

    public TimeTableRequest(String userID, String url, Response.Listener<String>listener){
        super(Method.POST, url, listener, null );
        parameters=new HashMap<>();
        parameters.put("userID", userID);

    }
    @Override
    public Map<String, String> getParams(){
        return parameters;
    }
}
