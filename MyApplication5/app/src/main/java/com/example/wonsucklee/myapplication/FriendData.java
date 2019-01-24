package com.example.wonsucklee.myapplication;

/**
 * Created by crown on 2018-02-08.
 */

public class FriendData {
    String user_profile;
    String user_id;
    String user_name;
    String user_major;


    public String get_User_Profile() {
        return user_profile;
    }
    public String get_User_Id() {
        return user_id;
    }
    public String get_User_Name() {
        return user_name;
    }
    public String get_User_Major() {
        return user_major;
    }


    public void set_User_Profile(String user_profile) {
        this.user_profile = user_profile;
    }
    public void set_User_Id(String user_id) {
        this.user_id = user_id;
    }
    public void set_User_Name(String user_name) {
        this.user_name = user_name;
    }
    public void set_User_Major(String user_major) {
        this.user_major = user_major;
    }

}

