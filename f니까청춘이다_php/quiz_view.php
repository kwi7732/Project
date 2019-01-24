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
	
	
	$user_id = $_POST['user_id'];
	$subject = $_POST['subject'];
	$week = (int) $_POST['week'];
	
	$array;
	
	if(! $conn) {
		die("fail to connecting db");
	}

	$query_question = "select * from ".$subject."_quiz where week = ".$week;
	if($result_question = $conn -> query($query_question)) {
		$count_question = mysqli_num_rows($result_question);

		if($count_question > 0) {
			$row_question = $result_question -> fetch_assoc();
		
			$query_search = "select * from quiz_reply where (subject = '$subject' and user_id = '$user_id' and week = ".$week.")";
	
			if($result_search = $conn -> query($query_search)) {
				$count_search = mysqli_num_rows($result_search);

				if($count_search > 0) { //질문존재o, 답변완료o
					$row_search = $result_search -> fetch_assoc();

					$array = array(
						"is_questioned" => 1,
						"is_answered" => 1,
						"question" => $row_question['question'],
						"answer" => $row_search['answer'],
						"image_content" => $row_question['image_path']
						);
				} else { //질문존재o, 답변완료x
					$array = array(
						"is_questioned" => 1,
						"is_answered" => 0,
						"question" => $row_question['question'],
						"answer" => $row_search['answer'],
						"image_content" => $row_question['image_path']
						);
				}

			}		
		} else { //질문존재x => 답변 또한 x
			$array = array(
				"is_questioned" => 0,
				"is_answered" => 0,
				"question" => $row_question['question'],
				"answer" => $row_search['answer'],
				"image_content" => $row_question['image_path']
				);
		}
	}
	

	$json = json_encode($array, JSON_UNESCAPED_UNICODE);
	print($json);
	
	$conn->close();
	
	
?>