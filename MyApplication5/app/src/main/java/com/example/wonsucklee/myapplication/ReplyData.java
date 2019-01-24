package com.example.wonsucklee.myapplication;

public class ReplyData {

	int reply_number;
	String user_id;
	String user_nick;
	String user_profile;
	String content;
	String writetime;
	int is_deleted;

	public int get_R_Number() {return reply_number;}
	public String get_User_Id() {return user_id;}
	public String get_User_Nick() {return user_nick;}
	public String get_User_Profile() {return user_profile;}
	public String get_Content() {return content;}
	public String get_Writetime() {return writetime;}
	public int get_Is_Deleted() {return is_deleted;}

	public void set_R_Number(int r_number) {this.reply_number = r_number;}
	public void set_User_Id(String user_id) {this.user_id = user_id;}
	public void set_User_Nick(String user_nick) {this.user_nick = user_nick;}
	public void set_User_Profile(String user_profile) {this.user_profile = user_profile;}
	public void set_Content(String content) {this.content = content;}
	public void set_Writetime(String writetime) {this.writetime = writetime;}
	public void set_Is_Deleted(int is_deleted) {this.is_deleted = is_deleted;}

}
