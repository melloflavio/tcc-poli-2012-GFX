<?php 

	//echo '{"status"="true", "id"="123"}';
	
	$input = true;
	
	//Gets the input
	if(isset($_GET["email"])){
		$email = $_GET["email"];
	}
	if(isset($_GET["password"])){
		$password =  $_GET["password"];
	}
	
	if(!isset($email)){
		$input = false;
	}
	if(!isset($password)){
		$input = false;
	}
	
	
	if(!$input){//Erro no input, retorna erro
		$response["status"] = "false";
		$response["message"] = "Erro ao receber dados de login";
		
		echo json_encode($response);
		}
	else {//Input ok, efetua query de login
		$db_username='gfx';
		$db_password='gfx2012';
		$database='TCC_GFX';

		$connect = mysql_connect("localhost:3306", $db_username, $db_password);
		@mysql_select_db($database) or die('Unable to select database');
		mysql_query("SET NAMES 'utf8'");
	
		$query = "SELECT user_id FROM users WHERE email='".$email."' AND password='".$password."'";
		$result = mysql_query($query);
		
		if(mysql_num_rows($result) > 0){
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			
			$response["status"] = "true";
			$response["id"] = $row["user_id"];
		}
		else{
			$response["status"] = "false";
		}
			
		echo json_encode($response);
	}
?>