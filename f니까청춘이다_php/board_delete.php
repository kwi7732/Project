<?php
	$host = 'localhost';
	$user = 'root';
	$pw = 'root';
	$dbName = 'jsea';
	$conn = new mysqli($host, $user, $pw, $dbName);


	$id = $_POST['userID'];
	$board_num = $_POST['board_number'];
	$what = $_POST['what'];
	$grade = $_POST['grade'];
	

if($what == 1){
		$delete = "DELETE FROM free_board where (f_num = $board_num and f_user_id = '$id')";
	echo $conn -> query($delete);
}

else if($what == 2){

	if($grade == 1){
	$delete = "DELETE FROM grade1_board where (g1_num = $board_num and g1_user_id = '$id')";
	echo $conn -> query($delete);
	}
	
	
	else if($grade == 2){
	$delete = "DELETE FROM grade2_board where (g2_num = $board_num and g2_user_id = '$id')";
	echo $conn -> query($delete);
	}
	else if($grade == 3){
	$delete = "DELETE FROM grade3_board where (g3_num = $board_num and g3_user_id = '$id')";
	echo $conn -> query($delete);
	}
	else if($grade >= 4){
	$delete = "DELETE FROM grade4or5_board where (g45_num = $board_num and g45_user_id = '$id')";
	echo $conn -> query($delete);
	}
	else echo "?!";
	
}


else if($what == 3){
		$delete = "DELETE FROM living_alone_board where (la_num = $board_num and la_user_id = '$id')";
	echo $conn -> query($delete);
}


else if($what == 4){
		$delete = "DELETE FROM second_hand_board where (sh_num = $board_num and sh_user_id = '$id')";
	echo $conn -> query($delete);
}

else echo "Error";


?>

