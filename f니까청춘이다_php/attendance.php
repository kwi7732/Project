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
	
	
	$beacon = $_POST['beacon_num']; //비콘 번호
	$week_num = $_POST['attendance_week']; // 몇주차
	$atd_time = $_POST['attendance_time']; // 입장 시간
	$atd_day = $_POST['attendance_day']; // 요일
	$status;
	$user_id;
	
	
	$c_flag = $conn -> query("SELECT class_flag FROM compiler");
	$row = $c_flag->fetch_assoc(); // 값을 가진 하나의 행을 추출하여 row에 입력
	
	if($row['class_flag'] == 1){
		echo "휴강";//class_flag = 1은 휴강
	}
	
	else{
	
	if($_POST['user_id'] == "") {
		$user_id = "아이디가 없습니다.";
	} else {
		$user_id = $_POST['user_id'];
	}

	if($_POST['title'] == "") {
		$title = "과목이 없습니다.";
	} else {
		$title = $_POST['title'];
	}
	
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$atd_time_int = (int)$atd_time; // 시간 문자열을 정수형으로 변환
	$period = 1000; // 초기값 1교시 10시 이하 + 1교시씩 증가
	$cnt = 0;
	
	// 몇교시인지 추출하는 반복문
	for($i = 1; $i < 10; $i++){
		if($atd_time_int <= $period + $cnt){
			$period = $i;
			break;
		}
		else $cnt += 100;
	}
	
	$class_time = "$atd_day"."$period";
	$week = $week_num."week";

	//class_time을 list_of_class에서 c_title 추출!
	$query_class = "select * from list_of_classes where (c_time like '%$class_time%' AND c_beacon = '$beacon')";
	if($result = $conn -> query($query_class)) {
	    $row = $result -> fetch_array();
	    
	    $start = $row[5];
	    //1 = 출석, 2 = 지각, 3 = 결석, 4 = 부재
	    if($atd_time_int - $start < 10){
	    $status = 1;
	    }else $status = 2;
	
	    
	    $query_search = "select ".$week." from ".$row[1]." where user_id = '$user_id'";
	    if($result_search = $conn -> query($query_search)) {
	    	$row_search = $result_search -> fetch_array();
	    	
	    	if($row_search[0] == 0) {
	    		$query_atd = "update ".$row[1]." set ".$week." = ".$status." where user_id = '$user_id'";
	    
	    		if($conn -> query($query_atd)) {
	    
	        		if($status == 1) {
	            		echo "출석";
	        		} else {
	            		echo "지각";
	      		  	}
	        
	    		} else {
	        		echo "fail";
	    		}
	    	} else {
	    		echo "already_checked";
	    	}
		} else {
			echo "fail_search";
		}

 ////// /////// /////// ////// //////// /////// /////// /////// /////// /////// /////// /////// /////// ///////
	    
	} else {
	    echo "fail!";
	}
	
}
	$conn->close();

	
?>