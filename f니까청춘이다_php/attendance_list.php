<?php
	header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbname = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbname);
	
	$conn->query("set session character_set_client=utf8;");
	$conn->query("set session character_set_results=utf8;");
	$conn->query("set session character_set_connection=utf8;");
	
	$count_of_subject = 6;
	
	if($_POST['user_id'] == "") {
		$user_id = "아이디가 없습니다.";
	} else {
		$user_id = $_POST['user_id'];
	}

	if($_POST['title'] == "") {
		$title = "";
	} else {
		$title = urldecode($_POST['title']);
	}
	
	
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$query_attendance = "select * from ".$title." where user_id = '$user_id'";
	if($result_attendance = $conn->query($query_attendance)) {
		$row_attendance = $result_attendance -> fetch_assoc();


		$json = json_encode($row_attendance, JSON_UNESCAPED_UNICODE);
		print($json);
	} else {
		echo "fail";
	}
	
	
	$conn->close();
	
	
?>