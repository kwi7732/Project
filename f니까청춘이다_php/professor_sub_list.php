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
	
	
    if($_POST['userID'] == "") {
		$user_id = "아이디가 없습니다.";
	} else {
		$user_id = $_POST['userID'];
	}
	
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$array = array();
	
	
	$professor_name = "select name from users where user_id = '$user_id'";
	if($result_name = $conn->query($professor_name)) {
	
	} else {
		echo "fail";
	}
	
	$row_name = $result_name->fetch_array();	//교수 이름 땄음
	
	
	$list_of_classes = "select * from list_of_classes where c_name = '$row_name[0]'"; //교수이름으로 과목 땄음
	if($result_loc = $conn -> query($list_of_classes)){
	
	}else{
		echo "fail";
	}
	
	while($row_loc = $result_loc -> fetch_assoc()){
	
		array_push($array,
			array(
				'title' => $row_loc['c_title'],
				'professor' => $row_loc['c_name'],
				'day' => $row_loc['c_time'],
				'is_completed' => $row_loc['is_completed']
		));
	}
	
	$json = json_encode($array, JSON_UNESCAPED_UNICODE);
	print($json);
	
	$conn->close();

?>