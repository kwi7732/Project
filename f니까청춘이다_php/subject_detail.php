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
	$week = (int) $_POST['week'];
	$week_name = $week."week";
	$array = array();
	
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$question;
	$question_image;
	
	$table_name = $subject."_quiz";
	$query_quiz_question = "select * from ".$table_name." where week = ".$week;
	

	if($result_quiz_question = $conn -> query($query_quiz_question)) {
		$row_quiz_question = $result_quiz_question -> fetch_assoc();
		
		$question = $row_quiz_question['question'];
		$question_image = $row_quiz_question['image_path'];
		
	} else {
		echo "fail_question";
	}
	
	
	$query_subject = "select user_id, ".$week_name." from ".$subject."";
	
	if($result_subject = $conn -> query($query_subject)) {
		while($row_subject = $result_subject -> fetch_assoc()) {
			$query_user = "select * from users where user_id = '".$row_subject['user_id']."'";
			
			if($result_user = $conn -> query($query_user)) {
				$row_user = $result_user -> fetch_assoc();
				
				$query_quiz_answer = "select * from quiz_reply where (user_id = ".$row_subject['user_id']." and week = ".$week.")";
				if($result_quiz_answer = $conn -> query($query_quiz_answer)) {
					$row_quiz_answer = $result_quiz_answer -> fetch_assoc();
					
					array_push($array,
						array(
							"user_id" => $row_subject['user_id'],
							"user_name" => $row_user['name'],
							"user_profile" => $row_user['image_path'],
							"attendance" => $row_subject[$week_name],
							"question" => $question,
							"question_image" => $question_image,
							"answer" => $row_quiz_answer['answer']	
					));
				} else {
					echo "fail_answer";
				}
			
					
			} else {
				echo "fail_user";
			}
		}
	} else {
		echo "fail_subject";
	} 
	

	$json = json_encode($array, JSON_UNESCAPED_UNICODE);
	print($json);
			
	$conn->close();
	
	
?>