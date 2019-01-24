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
	

		$what = $_POST['what'];
		$grade = $_POST['grade'];
		$array = array();
	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	
	


	
		if($what == 1){
		$result = $conn -> query("SELECT * FROM free_board order by f_date desc");
		
	
		}
		      
		else if($what == 2){
		
		if($grade == 1){
		$result = $conn -> query("SELECT * FROM grade1_board order by g1_date desc");
		
	}
	else if($grade == 2){
		$result = $conn -> query("SELECT * FROM grade2_board order by g2_date desc");
	
	}
	else if($grade == 3){
		$result = $conn -> query("SELECT * FROM grade3_board order by g3_date desc");
	
	}
	else if($grade >= 4){
		$result = $conn -> query("SELECT * FROM grade4or5_board order by g4_date desc");
	
	}
	else echo "?!";
		}
		
		
		
		else if($what == 3){
		$result = $conn -> query("SELECT * FROM living_alone_board order by la_date desc");
		
	
		}
		else if($what == 4){
		$result = $conn -> query("SELECT * FROM second_hand_board order by sh_date desc");
		
		
		}

		else echo "???";      
		      
		     
		      
	if(mysqli_num_rows($result)){		
		while($data = mysqli_fetch_array($result)){
		
		$profile_name = $conn -> query("SELECT * FROM users WHERE user_id = '$data[1]'");
		$user_data = mysqli_fetch_array($profile_name);
		
		
		array_push($array,
		array('board_num'=>$data[0],
		'board_user_id'=>$data[1],
		'board_title'=>$data[2],
		'board_text_content'=>$data[3],
		'board_image_content'=>$data[4],
		'board_date'=> date('Y-m-d',$data[5]),
		'user_name'=>$user_data[2],
		'user_major'=>$user_data[3],
		'user_profile'=>$user_data[4]
		));
	}
		
		
		print((json_encode($array, JSON_UNESCAPED_UNICODE)));
		
		
		
		
		
	}

	
	//view 게시글 클릭했을때 게시판 보기
	//$result = $conn -> query("SELECT * FROM free_board");
	//list 게시판 목록


	//수정 삭제	
	//$modify = $conn -> query("수정 * FROM free_board WHERE 글num");

	//$delete = $conn -> query("삭제 * FROM free_board WHERE 글num");


?>