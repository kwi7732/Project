<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");

	$id = $_POST['user_id'];
	$question = urldecode($_POST['question']);
	$content = urldecode($_POST['TEXT_CONTENT']);
	$target_dir="/uploads";
	$image_path = $_FILES['uploaded_file']['name'];
	
	$week = (int) $_POST['week'];
	
	$subject = $_POST['subject'];
	

	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	if(!file_exists($target_dir)){
	  mkdir($target_dir, 0777, true);
	}
	
	$file_name = $file_path.basename($image_path);

	$target_dir = $_SERVER['DOCUMENT_ROOT']."/uploads/".$file_name;
	
	
	if($image_path != '') {
		if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $target_dir)){
	 
		} else echo "fail";
	} else {
		$file_name = "default_image.png";
	}
	
	
	$query_search = "select * from ".$subject."_quiz where week = ".$week."";
	if($result_search = $conn -> query($query_search)) {
		$count = mysqli_num_rows($result_search);
		
		if($count <= 0) {
			$sql = "insert into ".$subject."_quiz (user_id, week, question, image_path) values ('$id', ".$week.", '$question', '$file_name')";
			echo $conn -> query($sql);
		}
	}


?>