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
	
	define("GOOGLE_API_KEY","AAAA8byFsn4:APA91bFyC5Shyc2CmR0bG0ZAJAdGZa5-SX4hrYyKJGd8mJM2TW7PG2S8A9zjJW1Gq68tBnozGVy-ctuudp4aIsgHALrx_kmLBIOz430z-ADdLnYZErUIrGLg2qel5XNg1SrvkWrcFXRj");
	
	
	function send_notification ($token, $message) {
    
    	$url = 'https://fcm.googleapis.com/fcm/send';
    	
    	$fields = array(
            'to' => $token,
            'data' => $message
        );
 
    	$headers = array(
            'Authorization:key ='.GOOGLE_API_KEY,
            'Content-Type: application/json'
        );
 
       	$ch = curl_init();   
       	curl_setopt($ch, CURLOPT_URL, $url);
       	curl_setopt($ch, CURLOPT_POST, true);
       	curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
       	curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
       	curl_setopt ($ch, CURLOPT_SSL_VERIFYHOST, 0);  
       	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
       	curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
       
       	$result = curl_exec($ch);   
       	if ($result === FALSE) {
           die('Curl failed: ' . curl_error($ch));
       	}
       
      	curl_close($ch);
      	echo $result;
       	return $result;
    }

	$token;
	$sender_id = $_POST['sender_id'];
    $sender_name = urldecode($_POST['sender_name']);
    $sender_major = urldecode($_POST['sender_major']);
    $receiver_id = $_POST['receiver_id'];
    $message = urldecode($_POST['message']);
    $writetime = time();
	$room_id;

	echo $token;
	if(! $conn) {
		die("fail to connecting db");
	}
	
	$sender_id_temp = (int) $sender_id;
	$receiver_id_temp = (int) $receiver_id;
	
	//메세지룸아이디 = 작은학번 + 큰학번
	if($sender_id_temp < $receiver_id_temp) {
		$room_id = "$sender_id"."_"."$receiver_id";
	} else {
		$room_id = "$receiver_id"."_"."$sender_id";
	}
	
	$query_insert = "insert into message (room_name, sender_id, message, writetime) value ('$room_id', '$sender_id', '$message', '$writetime')";

	
	if($result_insert = $conn->query($query_insert)) {
		$query_search = "select * from users where user_id = '$receiver_id'";
		if($result_search = $conn -> query($query_search)) {
			$row_search = $result_search -> fetch_assoc();
			
			$token = $row_search['fcm_token'];
		}
	
		$message_array = array(
				"sender_name" => $sender_name,
				"sender_major" => $sender_major,
				"message" => $message
				);
		send_notification($token, $message_array);
		
		echo "1";
	} else {
		echo "fail";
	}
	
	
	
	
	$conn->close();
	
	
?>