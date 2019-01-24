<?php
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);
	
	$id = $_POST['id'];
	$password = $_POST['password'];
	$name = $_POST['name'];	
	
	
	echo $id;
	
	
	$sql = "INSERT INTO users(user_id, password, name)
			VALUES ('$id', '$password', '$name')";
	
	if($conn){
	
	echo "MySQL!!!";
	}
	
	else echo "MySQL -_-";
	
	
	if($conn->query($sql) == TRUE){
	echo "mySQL 데이터 입력 성공ㅋ";
	//echo "id=" $id", 비밀번호=" $password ", 이름 =" $name;
	}
	
	else echo "실패!!";
		
	?>