<?php
	header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbname = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbname);
	
	$conn->query("set session character_set_client=utf8;");
	$conn->query("set session character_set_results=utf8;");
	$conn->query("set session character_set_connection=utf8;");
	
	$count_of_subject = 6;
	
    if($_POST['user_id'] == "") {
		$user_id = "아이디가 없습니다.";
	} else {
		$user_id = $_POST['user_id'];
	}
	
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$query_doc = "select * from detail_of_classes where user_id = '$user_id'";
	if($result_doc = $conn->query($query_doc)) {
	
	} else {
		echo "fail";
	}
	
	$row_doc = $result_doc->fetch_array();

	$subject_array = array();
	$result = array();
	
	for($i = 0; $i < $count_of_subject; $i++) {
		$j = $i + 2;
		$subject_temp = $row_doc[$j];
		array_push($subject_array, $subject_temp);
	}
	
	
	for($i = 0; $i < $count_of_subject; $i++) {
		$subject = $subject_array[$i];
		$query_loc = "select * from list_of_classes where c_num = '$subject'";
		if($result_loc = $conn->query($query_loc)) {
		
		} else {
			echo "fail";
		}
		
		$row_loc = $result_loc->fetch_array();
		
		$title_of_class = $row_loc[1];
		$professor = $row_loc[2];
		$day = $row_loc[3];
		
		$class = array(
			"title" => $title_of_class,
			"professor" => $professor,
			"day" => $day);
			
		array_push($result, $class); 
		
	}
	
	$json = json_encode($result, JSON_UNESCAPED_UNICODE);
	print($json);
	
	$conn->close();
	
	
?>