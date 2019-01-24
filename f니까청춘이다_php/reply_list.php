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
	
    $what = (int) $_POST['what'];
    $board_number = (int) $_POST['board_number'];
	$grade = $_POST['grade'];
	$array = array();
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	
	if($what == 1){
		$result = $conn -> query("SELECT * FROM free_board_reply where board_number = $board_number order by writetime desc");
	} else if($what == 2){
	
		if($grade == 1){
			$result = $conn -> query("SELECT * FROM grade1_board_reply where board_number = $board_number order by writetime desc");
		} else if($grade == 2){
			$result = $conn -> query("SELECT * FROM grade2_board_reply where board_number = $board_number order by writetime desc");
		} else if($grade == 3){
			$result = $conn -> query("SELECT * FROM grade3_board_reply where board_number = $board_number order by writetime desc");
		} else if($grade >= 4){
			$result = $conn -> query("SELECT * FROM grade4or5_board_reply where board_number = $board_number order by writetime desc");
		} else echo "?!";
	
		} else if($what == 3){
			$result = $conn -> query("SELECT * FROM living_alone_board_reply where board_number = $board_number order by writetime desc");
		} else if($what == 4){
			$result = $conn -> query("SELECT * FROM second_hand_board_reply where board_number = $board_number order by writetime desc");
	} else echo "???";      
		      
		     	      
	if(mysqli_num_rows($result)){		
		while($data = $result -> fetch_assoc()){
		
		$profile_name = $conn -> query("SELECT * FROM users WHERE user_id = '".$data['user_id']."'");
		$user_data = $profile_name -> fetch_assoc();
		
		array_push($array,
		array(
		'reply_number'=>$data['reply_number'],
		'board_number'=>$data['board_number'],
		'user_id'=>$data['user_id'],
		'content'=>$data['content'],
		'writetime'=> date('Y-m-d',$data['writetime']),
		'user_nick'=>$user_data['name'],
		'user_profile'=>$user_data['image_path'],
		'is_deleted'=>$data['is_deleted']
		));
	}
		
		
		print((json_encode($array, JSON_UNESCAPED_UNICODE)));
		
	}

?>