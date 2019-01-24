<?php
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$reply_number = (int) $_POST['reply_number'];
	$what = (int) $_POST['what'];
	$grade = (int) $_POST['grade'];
	
	$delete;
	$content = "삭제된 댓글입니다";
	
	if($what == 1){
		$delete = "delete from free_board_reply where reply_number = $reply_number";
	} else if($what == 2){

		if($grade == 1){
			"delete from grade1_board_reply where reply_number = $reply_number";
		} else if($grade == 2){
			"delete from grade2_board_reply reply_number = $reply_number";
		} else if($grade == 3){
			"delete from grade3_board_reply where reply_number = $reply_number";
		} else if($grade >= 4){
			"delete from grade4or5_board_reply where reply_number = $reply_number";
		}	
	} else if($what == 3){
		$delete = "delete from living_alone_reply where reply_number = $reply_number";
	} else if($what == 4){
		$delete = "delete from second_hand_board_reply where reply_number = $reply_number";
	}

/*
	if($what == 1){
		$delete = "update free_board_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
	} else if($what == 2){

		if($grade == 1){
			"update grade1_board_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
		} else if($grade == 2){
			"update grade2_board_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
		} else if($grade == 3){
			"update grade3_board_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
		} else if($grade >= 4){
			"update grade4or5_board_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
		}	
	} else if($what == 3){
		$delete = "update living_alone_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
	} else if($what == 4){
		$delete = "update second_hand_board_reply set content = '$content', is_deleted = 1 where reply_number = $reply_number";
	}
	*/

	if($result = $conn -> query($delete)) {
		echo "1";
	} else {
		echo "2";
	}
	
	$conn->close();

?>

