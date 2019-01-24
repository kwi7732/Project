<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");

    $board_number = $_POST['board_number'];
    $reply_number = $_POST['reply_number'];
	$user_id = $_POST['user_id'];
	$what = (int) $_POST['what'];
	$grade = (int) $_POST['grade'];
	$content = urldecode($_POST['content']);

	$modify;

	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	if($what == 1){
		$modify = "UPDATE free_board_reply set content = '$content' where reply_number = ".$reply_number;
	} else if($what == 2){
		if($grade == 1){
			$modify = "UPDATE grade1_board_reply set content = '$content' where reply_number = ".$reply_number;
		} else if($grade == 2){
			$modify = "UPDATE grade2_board_reply set content = '$content' where reply_number = ".$reply_number;
		} else if($grade == 3){
			$modify = "UPDATE grade3_board_reply set content = '$content' where reply_number = ".$reply_number;
		} else if($grade >= 4){
			$modify = "UPDATE grade4_board_reply set content = '$content' where reply_number = ".$reply_number;
		}

	} else if($what == 3){
		$modify = "UPDATE living_alone_board_reply set content = '$content' where reply_number = ".$reply_number;
	} else if($what == 4){
		$modify = "UPDATE second_hand_board_reply set content = '$content' where reply_number = ".$reply_number;
	}
	
	if($result = $conn -> query($modify)) {
		echo "1";
	} else {
		echo "2";
	}

	
?>

