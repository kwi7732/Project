<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");

	$id = $_POST['userID'];
	$title = urldecode($_POST['TITLE']);
	$content = urldecode($_POST['TEXT_CONTENT']);
	$target_dir="/uploads";
	$image_path = $_FILES['uploaded_file']['name'];
	$date = time();//date('Y-m-d H:i:s',time());
	$what = $_POST['what'];
	$grade = $_POST['grade'];
	//$date
	//$num


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
	
	
	
	
	//insert 게시글쓰기
	if($what == 1){
		$sql = "INSERT INTO free_board(f_user_id, f_title, f_content, f_image_path, f_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
}
	
	else if($what == 2){
	
	if($grade == 1){
	$sql = "INSERT INTO grade1_board(g1_user_id, g1_title, g1_content, g1_image_path, g1_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
	}
	
	
	else if($grade == 2){
	$sql = "INSERT INTO grade2_board(g2_user_id, g2_title, g2_content, g2_image_path, g2_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
	}
	else if($grade == 3){
	$sql = "INSERT INTO grade3_board(g3_user_id, g3_title, g3_content, g3_image_path, g3_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
	}
	else if($grade >= 4){
	$sql = "INSERT INTO grade4or5_board(g45_user_id, g45_title, g45_content, g45_image_path, g45_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
	}
	else echo "?!";
	
}

	else if($what == 3){
		$sql = "INSERT INTO living_alone_board(la_user_id, la_title, la_content, la_image_path, la_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
}

	else if($what == 4){
		$sql = "INSERT INTO second_hand_board(sh_user_id, sh_title, sh_content, sh_image_path, sh_date)
			VALUES ('$id', '$title', '$content', '$file_name', '$date')";
				echo $conn->query($sql);
}

	else echo "???";
	
	
	
	
	//view 게시글 클릭했을때 게시판 보기
	//$result = $conn -> query("SELECT * FROM free_board");
	//list 게시판 목록


	//수정 삭제	
	//$modify = $conn -> query("수정 * FROM free_board WHERE 글num");

	//$delete = $conn -> query("삭제 * FROM free_board WHERE 글num");


?>