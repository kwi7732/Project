package com.example.wonsucklee.myapplication;

public class SubjectDetailData {

	String user_id;
	String user_name;
	String user_profile;
	int attendance;
	String question;
	String question_image;
	String answer;

	public String get_User_Id() {return user_id;}
	public String get_User_Name() {return user_name;}
	public String get_User_Profile() {return user_profile;}
	public String get_Attendance() {
		String atd = "";
		switch(attendance) {
			case 0:
				atd = "-";
				break;
			case 1:
				atd = "출석";
				break;
			case 2:
				atd = "지각";
				break;
			case 3:
				atd = "부재";
				break;
		}
		return atd;
	}

	public String get_Question() {return question;}
	public String get_Question_Image() {return question_image;}
	public String get_Answer() {
		String no_answer = "답안이 없습니다";

		if(answer == "null") {
			return no_answer;
		}

		return answer;
	}

	public void set_User_Id(String user_id) {this.user_id = user_id;}
	public void set_User_Name(String user_name) {this.user_name = user_name;}
	public void set_User_Profile(String user_profile) {this.user_profile = user_profile;}
	public void set_Attendance(int attendance) {this.attendance = attendance;}
	public void set_Question(String question) {this.question =question;}
	public void set_Question_Image(String question_image) {this.question_image = question_image;}
	public void set_Answer(String answer) {this.answer = answer;}

}
