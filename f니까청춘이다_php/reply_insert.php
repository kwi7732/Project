<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");

	$board_number = (int) $_POST['board_number'];
	$user_id = urldecode($_POST['user_id']);
	$content = urldecode($_POST['content']);
	$writetime = time();//date('Y-m-d H:i:s',time());
	$what = (int) $_POST['what'];
	$is_deleted = 0;

	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
		
	//insert 게시글쓰기
	if($what == 1){
		echo "free_board";
		
		$sql = "INSERT INTO free_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
}
	
	else if($what == 2){
	
	if($grade == 1){
	$sql = "INSERT INTO grade1_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
	}
	
	
	else if($grade == 2){
	$sql = "INSERT INTO grade2_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
	}
	else if($grade == 3){
	$sql = "INSERT INTO grade3_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
	}
	else if($grade >= 4){
	$sql = "INSERT INTO grade4or5_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
	}
	else echo "?!";
	
}

	else if($what == 3){
		$sql = "INSERT INTO living_alone_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
}

	else if($what == 4){
		$sql = "INSERT INTO second_hand_board_reply(board_number, user_id, content, writetime, is_deleted)
			VALUES ('$board_number', '$user_id', '$content', '$writetime', '$is_deleted')";
				echo $conn->query($sql);
}

	else echo "???";
	
	
?>