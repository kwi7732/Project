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
	$array = array();
	
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	$query_friend = "select * from users where is_representative = 1";
	
	if($result_friend = $conn -> query($query_friend)) {
		while($row = $result_friend -> fetch_assoc()) {
		
			if($row['user_id'] != $user_id) {
				array_push($array,
					array('user_id'=>$row['user_id'],
						'user_name'=>$row['name'],
						'user_major'=>$row['major'],
						'user_profile'=>$row['image_path']
				));
			}

		}
	}
	
	print((json_encode($array, JSON_UNESCAPED_UNICODE)));
	$conn -> close();
	

?>