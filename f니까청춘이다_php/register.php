<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);
	
	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");
	
	$image=$_POST['image'];
	$id = $_POST['userID'];
	$password = $_POST['userPassword'];
	
	$name = urldecode($_POST['userName']);
	$major = urldecode($_POST['userMajor']);
	
	$target_dir="/uploads";
	
	
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	$file_name = rand()."_".time().".png";	
	$target_dir = $_SERVER['DOCUMENT_ROOT']."/uploads/".$file_name;
	
	if(file_put_contents($target_dir, base64_decode($image))){
	
	}
	else echo "fail!";

	$sql = "INSERT INTO users(user_id, password, name, major, image_path)
			VALUES ('$id', '$password', '$name', '$major' , '$file_name')";

	echo $conn->query($sql);
	?>