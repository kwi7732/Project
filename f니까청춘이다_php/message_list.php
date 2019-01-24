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
	
	$user_id = $_POST['user_id'];
	$other_user_id = $_POST['other_user_id']; 
	$room_id;
	
	$array = array();
	
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}

	
	$user_id_temp = (int) $user_id;
	$other_user_id_temp = (int) $other_user_id;
	
		//메세지룸아이디 = 작은학번 + 큰학번
	if($user_id_temp < $other_user_id_temp) {
		$room_id = $user_id."_".$other_user_id;
	} else {
		$room_id = $other_user_id."_".$user_id;
	}
	
	$query_search = "select * from message where room_name = '$room_id' order by writetime asc";
	
	if($result_search = $conn -> query($query_search)) {
		while($row_search = $result_search -> fetch_assoc()){
		
			$query_user = "select * from users where user_id = '".$row_search['sender_id']."'";
			if($result_user = $conn -> query($query_user)) {
				$row_user = $result_user -> fetch_assoc();
				
				array_push($array,
					array(
					'room_name'=>$row_search['room_name'],
					'sender_id'=>$row_search['sender_id'],
					'sender_profile'=>$row_user['image_path'],
					'message'=>$row_search['message'],
					'writetime'=> date('Y-m-d', $row_search['writetime'])
				));
			
			}
		
		}
			     	      		
		print((json_encode($array, JSON_UNESCAPED_UNICODE)));
	}
	
	$conn->close();

?>