<?php 


/*switch($_SERVER['REQUEST_METHOD']){
   case 'GET':
	getGFXAccount();
    break;
   case 'POST':
    break;
   case 'PUT':
    break;
   case 'DELETE':
    break;
}

function getGFXAccount(){	*/
	$input = true;
	
	//Gets the input
	if(isset($_GET["email"])){
		$email = $_GET["email"];
	}
	if(isset($_GET["password"])){
		$password =  $_GET["password"];
	}
	
	$validemail = true;
	$validpass = true;
	
	if(!isset($email)){
		$input = false;
		$validemail = false;
	}
	if(!isset($password)){
		$input = false;
		$validpass = false;
	}
	
	
	if(!$input){//Erro no input, retorna erro
		$response["status"] = "false";
		$response["message"] = "Erro ao receber dados de login email =".$validemail." password = ".$validpass;
		
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
		
		if($result && mysql_num_rows($result) > 0){
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			
			$response["status"] = "true";
			$response["userId"] = $row["user_id"];
			$response["houseIds"] = array();
			//{"status":"true", "userId" = "12", "houseIds": [{"houseId":"1234"}, {"houseId":"1234"}]}

		}
		else{
			$response["status"] = "false";
		}
			
		echo json_encode($response);
	}
//}
?>