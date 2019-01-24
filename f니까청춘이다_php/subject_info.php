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
	

	$subject = $_POST['subject'];
	$table_name = $subject."_quiz";
	$array = array();
	$array_is_completed;
	$array_week = array();
	$array_question = array();
	$array_question_image = array();
	$pattern = ',';
	
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$query_subject = "select * from list_of_classes where c_title = '$subject'";
	
	if($result_subject = $conn -> query($query_subject)) {
		$row_subject = $result_subject -> fetch_assoc();
		
		$is_completed_temp = $row_subject['is_completed'];
		$array_is_completed = explode($pattern, $is_completed_temp);
	} else {
		echo "subject_fail";
	}
	
	$query_quiz = "select * from ".$table_name."";
	if($result_quiz = $conn -> query($query_quiz)) {
		while($row_quiz = $result_quiz -> fetch_assoc()) {
			$week = $row_quiz['week'];
			$array_question[$week] = $row_quiz['question'];
			$array_question_image[$week] = $row_quiz['image_path'];
		}
	} else {
		echo "quiz_fail";
	}
	
	for($i = 0; $i < 15; $i++) {
		$j = $i + 1;
		array_push($array, 
			array(
				"week" => $j,
				"is_completed" => $array_is_completed[$i], 
				"question" => $array_question[$i],
				"question_image" => $array_question_image[$i]			
		));
	}
	$json = json_encode($array, JSON_UNESCAPED_UNICODE);
	print($json);
			
	$conn->close();
	
	
?>