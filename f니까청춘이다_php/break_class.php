<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");

	
	
	$title = $_POST['TITLE'];
	$week = $_POST['week'];
	


	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	$is_com = $conn -> query("select is_completed from list_of_classes where c_title = '$title'");
	$row = mysqli_fetch_array($is_com);
	
	$position = 2*$week - 2;
	
	
	$result = substr_replace($row[0],'2',$position,1);
	
	
	if($query = $conn -> query("update list_of_classes set is_completed = '$result' where c_title = '$title'")) {
		echo 1;
	} else {
		echo 0;
	}

	$conn -> close();
?>