package com.example.wonsucklee.myapplication;

public class MessageData {

	String sender_profile;
	String sender_id;
	String sender_name;
	String message;
	String writetime;

	public String get_Sender_Id() {return sender_id;}
	public String get_Sender_Profile() { return sender_profile;}
	public String get_Sender_Name() {
		return sender_name;
	}
	public String get_Message() {
		return message;
	}
	public String get_Writetime() {
		return writetime;
	}

	public void set_Sender_Id(String sender_id) { this.sender_id = sender_id;}
	public void set_Sender_Profile(String sender_profile) {
		this.sender_profile = sender_profile;
	}
	public void set_Sender_Name(String sender_name) {
		this.sender_name = sender_name;
	}
	public void set_Message(String message) {
		this.message = message;
	}
	public void set_Writetime(String writetime) {
		this.writetime = writetime;
	}
}
