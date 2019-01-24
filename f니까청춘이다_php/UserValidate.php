<?php
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);
	
	$id = $_POST['userID'];
	
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	
	
	$result = $conn -> query("SELECT * FROM users WHERE user_id = '$id'");

	//result값으로 아이디 존재 여부 확인 및 비밀번호 일치 확인	
	
	
	echo mysqli_num_rows($result);
	
	
	
	//print((json_encode($data)));
	?>