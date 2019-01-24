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
	
	
	$user_id = $_POST['user_id'];
	$subject = $_POST['subject'];
	$answer = urldecode($_POST['answer']);
	$week = (int) $_POST['week'];
	
	$array;
	
	if(! $conn) {
		die("fail to connecting db");
	}

	$query_answer = "insert into quiz_reply (user_id, subject, week, answer) values ('$user_id', '$subject', ".$week.", '$answer')";

	if($result_answer = $conn -> query($query_answer)) {
		echo 1;
	} else {
		echo 0;
	}

	
	$conn->close();
	
	
?>