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
	$input = false;
	
	//Gets the input
	if(isset($_GET["houseId"])){
		$houseId = $_GET["houseId"];
		$input = true;
	}
	
	
	
	if(!$input){//Erro no input, retorna erro
		$response["status"] = "false";
		$response["message"] = "Erro ao receber id da residencia";
		
		echo json_encode($response);
		}
	else {//Input ok, efetua query de login
		$db_username='gfx';
		$db_password='gfx2012';
		$database='TCC_GFX';

		$connect = mysql_connect("localhost:3306", $db_username, $db_password);
		@mysql_select_db($database) or die('Unable to select database');
		mysql_query("SET NAMES 'utf8'");
	
		$query = "SELECT * FROM medidas WHERE house_id='".$houseId."'";
		$result = mysql_query($query);
		$num_rows =  mysql_num_rows($result);
		if($result  && $num_rows > 0){
			$response["status"] = "true";
			
			$medidas = array();
			
			for($i = 0 ; $i < $num_rows ; $i++){
				$row = mysql_fetch_array($result, MYSQL_ASSOC);
				
				$medida_pot = $row["potencia"];
				$medida_inicio = $row["inicio_medida"];
				
				$medida["potencia"] = $medida_pot;
				$medida["inicio"] = $medida_inicio;
				
				$medidas[] = $medida;
			}
			
			
			$response["medidas"] = $medidas;
			
			//{"status":"true", "houseInfo": {"nomeCasa":"Maloca" , "logradouro":"Rua dos Bobos, 0", "cidade":"Sao Paulo", "estado":"SP" }}

		}
		else{
			$response["status"] = "false";
		}
			
		echo json_encode($response);
	}
//}
?>