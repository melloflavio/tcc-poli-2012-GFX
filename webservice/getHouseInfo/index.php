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
	
		$query = "SELECT * FROM residencias WHERE house_id='".$houseId."'";
		$result = mysql_query($query);
		
		if($result && mysql_num_rows($result) > 0){
			$row = mysql_fetch_array($result, MYSQL_ASSOC);
			
			$response["status"] = "true";
			
			$nome_casa = $row["nome_casa"];
			$logradouro = $row["logradouro"];
			$cidade = $row["cidade"];
			$estado = $row["estado"];
			
			$house["nomeCasa"] = $nome_casa;
			$house["logradouro"] = $logradouro;
			$house["cidade"] = $cidade;
			$house["estado"] = $estado;

			//Fetching daily consumption
			$today = date('Y-m-d', time());
			$query = "SELECT consumo FROM medidas_dia WHERE house_id='".$houseId."' AND dia_medida='".$today."'";
			$result = mysql_query($query);
			
			if($result && mysql_num_rows($result) > 0){
				$row = mysql_fetch_array($result, MYSQL_ASSOC);
				
				$consumo_dia = $row["consumo"];
				
				$consumo_dia = "".$consumo_dia." kWh";
				
			} else {
				$consumo_dia = " - ";
			}
			$house["consumo_dia"] = $consumo_dia;
			
			
			//Fetching month consumption
			$today = date('Y-m-t', time());
			$query = "SELECT consumo FROM medidas_mes WHERE house_id='".$houseId."' AND mes_medida='".$today."'";
			$result = mysql_query($query);
			
			if($result && mysql_num_rows($result) > 0){
				$row = mysql_fetch_array($result, MYSQL_ASSOC);
				
				$consumo_mes = $row["consumo"];
				
				$consumo_mes = "".$consumo_mes." kWh";
				
			} else {
				$consumo_mes = " - ";
			}
			$house["consumo_mes"] = $consumo_mes;
			
			$response["casa"] = $house;
			
			//{"status":"true", "houseInfo": {"nomeCasa":"Maloca" , "logradouro":"Rua dos Bobos, 0", "cidade":"Sao Paulo", "estado":"SP" }}

		}
		else{
			$response["status"] = "false";
		}
			
		echo json_encode($response);
	}
//}
?>