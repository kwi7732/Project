<?php

	header('Content-Type:text/html; charset=utf-8');

	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbname = 'jsea';
	$day;
	$week;
	$c_time;
	$conn = new mysqli($host, $user, $pw, $dbname);
	
	$conn->query("set session character_set_client=utf8;");
	$conn->query("set session character_set_results=utf8;");
	$conn->query("set session character_set_connection=utf8;");

	
	if(!$conn) {
		die("fail to connecting db");
	}
	
	$week = date("W");
	//$date = date('z') + 1;//date('Y-m-d H:i:s',time());
	
	
	if($week > 5){
		$start_class = $week - 9;
	}
	
	///
	//이 구간에 반복문 활용해서 모든 과목 추출해야함 
	///
	
	$qry1 = "select c_title,is_completed from list_of_classes";
	if($rst1 = $conn -> query($qr1)){
		while($row1 = $rst1 -> fetch_assoc()){
		
		$json = json_encode($row1, JSON_UNESCAPED_UNICODE);
		print($json);
		
		}
	}
	else echo 'fail';
	
	
	
	
	
	
	
	
	
	
	
	/*
	//아래서부턴 추출된 과목으로 flag 값을 확인해야함
	$c_flag = $conn -> query("SELECT class_flag FROM compiler");
	$row = $c_flag->fetch_assoc(); // 값을 가진 하나의 행을 추출하여 row에 입력
	
	
	$day = date('w');
	$daily = array('sun','mon','tue','wed','thu','fri','sat');
	$c_time = $daily[$day];
	
	$query_cron = "select * from list_of_classes where (c_time like '%$c_time%')";
	
	if($result = $conn -> query($query_cron)){
	 $row = $result -> fetch_assoc();
	 
	 echo '       '.$row['c_title'];
	}
	
	else{
	echo "There is No subject today.";
	}
	
	
	
	*/
	
	
	$conn->close();
	
	?>