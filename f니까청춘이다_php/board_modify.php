<?php header('Content-Type:text/html; charset=utf-8');
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);

	$conn -> query("set session character_set_client=utf8;");
	$conn -> query("set session character_set_results=utf8;");
	$conn -> query("set session character_set_connection=utf8;");

	$id = $_POST['userID'];
	$board_num = $_POST['board_number'];
	$what = $_POST['what'];
	$grade = $_POST['grade'];
	$title = urldecode($_POST['TITLE']);
	$content = urldecode($_POST['TEXT_CONTENT']);
	$target_dir="/uploads";
	$image_path = $_FILES['uploaded_file']['name'];
	$date = time();
	$array = array();

	if(! $conn){
		$message = "DB 연동 실패";
		die($message);
	}
	
	if(!file_exists($target_dir)){
	  mkdir($target_dir, 0777, true);
	}
	
	$file_name = $file_path.basename($image_path);

	$target_dir = $_SERVER['DOCUMENT_ROOT']."/uploads/".$file_name;
	
	
	if($image_path != '') {
		if(move_uploaded_file($_FILES['uploaded_file']['tmp_name'], $target_dir)){
		
		} else echo "fail";
	} else {
		$image_path = urldecode($_POST['image_tmp']);
	}
	
	if($what == 1){
		$modify = "UPDATE free_board set f_title = '$title', f_content = '$content',
									 f_image_path = '$image_path',
									 f_date = '$date'
								where (f_num = $board_num and f_user_id = '$id')";
	$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM free_board WHERE (f_num = $board_num and f_user_id = '$id')");
	}
	

	else if($what == 2){
		if($grade == 1){
		$modify = "UPDATE grade1_board set g1_title = '$title', g1_content = '$content',
									 g1_image_path = '$image_path',
									 g1_date = '$date'
								where (g1_num = $board_num and g1_user_id = '$id')";
	$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM grade1_board
							WHERE (g1_num = $board_num and g1_user_id = '$id')");
	}
	
	
		else if($grade == 2){
		$modify = "UPDATE grade2_board set g2_title = '$title', g2_content = '$content',
									 g2_image_path = '$image_path',
									 g2_date = '$date'
								where (g2_num = $board_num and g2_user_id = '$id')";
	$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM grade2_board
							WHERE (g2_num = $board_num and g2_user_id = '$id')");
	}
	
	
		else if($grade == 3){
		$modify = "UPDATE grade3_board set g3_title = '$title', g3_content = '$content',
									 g3_image_path = '$image_path',
									 g3_date = '$date'
								where (g3_num = $board_num and g3_user_id = '$id')";
	$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM grade3_board
							WHERE (g3_num = $board_num and g3_user_id = '$id')");
	}
	
	
		else if($grade >= 4){
		$modify = "UPDATE grade4or5_board set g45_title = '$title', g45_content = '$content',
									 g45_image_path = '$image_path',
									 g45_date = '$date'
								where (g45_num = $board_num and g45_user_id = '$id')";
	$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM grade4or5_board
							WHERE (g45_num = $board_num and g45_user_id = '$id')");
	}
	
		else echo "?!";
	}
	
	
	
	
	
		else if($what == 3){
		$modify = "UPDATE living_alone_board set la_title = '$title', la_content = '$content',
									 la_image_path = '$image_path',
									 la_date = '$date'
								where (la_num = $board_num and la_user_id = '$id')";
		$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM living_alone_board
								WHERE (la_num = $board_num and la_user_id = '$id')");
	}
	
	
	
	
	
		else if($what == 4){
		$modify = "UPDATE second_hand_board set sh_title = '$title', sh_content = '$content',
									 sh_image_path = '$image_path',
									 sh_date = '$date'
								where (sh_num = $board_num and sh_user_id = '$id')";
		$conn -> query($modify);
	
	$result = $conn -> query("SELECT * FROM second_hand_board
								WHERE (sh_num = $board_num and sh_user_id = '$id')");
	}
	
	else echo "123";
	
	
		if(mysqli_num_rows($result)){
		$data = mysqli_fetch_array($result);
		
		
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
?>

