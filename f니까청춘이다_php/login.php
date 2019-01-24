<?php
	header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);
	
	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");
	
	
	$v_id = $_POST['userID'];
	$v_password = $_POST['userPassword'];
	$token = $_POST['token'];
	
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	$insert_token = $conn -> query("update users set fcm_token = '$token' WHERE user_id = '$v_id' AND password = '$v_password'");
	
	$result = $conn -> query("SELECT * FROM users WHERE user_id = '$v_id' AND password = '$v_password'");
	
//result값으로 아이디 존재 여부 확인 및 비밀번호 일치 확인	

	
	
	
	
	//echo mysqli_num_rows($result);
	
	

	
	
	if(mysqli_num_rows($result)){
		$data = mysqli_fetch_array($result);
		print((json_encode($data, JSON_UNESCAPED_UNICODE)));
	}
	

	?>                                                                                                                                                                               