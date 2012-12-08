<?php
include("config.php");

//Connect to mysql server
$link = mysql_connect(DB_HOST, DB_USER, DB_PASSWORD);
if(!$link) {
	die('Failed to connect to server: ' . mysql_error());
}

//Select database
$db = mysql_select_db(DB_DATABASE);
if(!$db) {
	die("Unable to select database");
}

//header('Content-type: application/json');
header('Access-Control-Allow-Origin: *');
 
//Recebimento dos dados
switch($_SERVER['REQUEST_METHOD']){
   case 'GET':
    //echo("Ronaldo!");
	$arr = array('data' => 'encoded');
	//echo json_encode($arr);
	//$query = "INSERT INTO teste (text) VALUES ('Get')";
	//$result = mysql_query($query);
	echo("Brilou");
    break;
   case 'POST':
	error_log(date('Y-m-d H-i-s', time())."\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
    $jsonStr = file_get_contents("php://input");
	error_log("String \r\n\r\n".$jsonStr, 3, "/wamp/www/TCC/log_do_tcc.log");
    //echo("json string: ".$jsonStr."\n");
	$input = json_decode($jsonStr, true);
	//echo("------------".count($input));
	error_log("\r\n\r\n------------- \r\nDictionary\r\n\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
	if($input){	
		/*foreach ($input as $k => $v) {
			error_log("[$k] => $v\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
			//echo "\[$k] => $v.\n";
		}*/
		$startTime = $input["s"];
		error_log("Início = ".$startTime."\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
		$length = $input["l"];
		error_log("Duração(min) = ".$length."\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
		$consumption = $input["c"];
		$consumption = ($consumption * ($length/60.0));
		error_log("Consumo (Wh) = ".$consumption."\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
		$powerFactor = $input["fp"];
		error_log("Fator de Potência = ".$powerFactor."\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
		$id = $input["id"];
		//error_log("Id da Casa = ".$id."\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
	}
   //$query = "INSERT INTO teste (text) VALUES ('".$text."')";
	//$result = mysql_query($query);
	//echo("    Post muito no corinthians!");
	error_log("\r\n\r\n\r\n\r\n++++++++++++++++++++++++\r\n\r\n", 3, "/wamp/www/TCC/log_do_tcc.log");
    break;
   case 'PUT':
    break;
   case 'DELETE':
    break;
}

$arduino_energia=$consumption;
$arduino_interval=$length;
$arduino_fp=$powerFactor;
$arduino_house_id=$id;
//$arduino_timestamp=$startTime;
$arduino_timestamp=time();

//recebe potencia em Wh
function calcula_fatura($house_id, $interval, $timestamp, $fp, $energia){
	//Busca ID da Ditribuidora da residencia
	$qry ="SELECT `residencias`.`distribuidora_id`,`residencias`.`tipo_tarifa` FROM `tcc_gfx`.`residencias` WHERE `residencias`.`house_id`='$house_id'";
	$result = mysql_query($qry);
	if ($result){
		$row = mysql_fetch_assoc($result);
		$distribuidora=$row["distribuidora_id"];
		$tipo_tarifa=$row["tipo_tarifa"];
		//echo "Casa:".$house_id."<br/>";
		//echo "Distribuidora:".$distribuidora."<br/>";
		//echo "Tarifa:".$tipo_tarifa."<br/>";		
	}
	else die("Nenhuma distribuidora encontrada para a casa fornecida. house_id=".$house_id);
	
	//Busca Tarifas da Distribuidora
	$qry ="SELECT 
			`distribuidoras`.`tarifa_convencional`,
			`distribuidoras`.`tarifa_ponta`,
			`distribuidoras`.`tarifa_foraponta`,
			`distribuidoras`.`tarifa_intermediaria`
			FROM `tcc_gfx`.`distribuidoras`
			WHERE `distribuidoras`.`distribuidora_id`=".$distribuidora;
	$result = mysql_query($qry);
	if ($result){
		$row = mysql_fetch_assoc($result);
		$tarifa_conv=$row["tarifa_convencional"];
		$tarifa_ponta=$row["tarifa_ponta"];
		$tarifa_foraponta=$row["tarifa_foraponta"];
		$tarifa_int=$row["tarifa_intermediaria"];
		//echo "Tarifa Convencional: ".$tarifa_conv."<br/>";
		//echo "Tarifa Ponta: ".$tarifa_ponta."<br/>";
		//echo "Tarifa Fora Ponta: ".$tarifa_foraponta."<br/>";
		//echo "Tarifa Intermediária: ".$tarifa_int."<br/>";		
	}
	else die("Nenhuma distribuidora encontrada. distribuidora_id=".$distribuidora);
	
	
	//Tarifa Convencional
	if ($tipo_tarifa == '0'){
		$fatura_parcial = $energia*$fp*$tarifa_conv;
	}
	//Tarifa Branca
	else if($tipo_tarifa == '1'){
		$hour=date('H',$timestamp);
		switch ($hour) {
			//ponta
			case ($hour=='18'||$hour=='19'||$hour=='20'):
	    		$fatura_parcial = $energia*$fp*$tarifa_ponta/1000;
	 		break;
	  		//intermediário
	 		case ($hour=='17'||$hour=='21'):
	    		$fatura_parcial = $energia*$fp*$tarifa_int/1000;
	  		break;
	  		//ponta
	 		default:
	    		$fatura_parcial = $energia*$fp*$tarifa_foraponta/1000;
	  		break;
		}
		return $fatura_parcial;
	}
}
//$energia_wh=$arduino_energia*$arduino_interval/60;
$fatura_parcial = calcula_fatura($arduino_house_id,$arduino_interval,$arduino_timestamp, $arduino_fp, $arduino_energia);
//echo "Fatura Parcial: ".$fatura_parcial."R$";
$sqltime = date("Y-m-d H:i:s",$arduino_timestamp);
$qry="
INSERT INTO `tcc_gfx`.`medidas`
	(`house_id`,
	`inicio_medida`,
	`intervalo_demanda`,
	`consumo`,
	`fator_potencia`,
	`fatura_parcial_medida`)
	VALUES
	(
	'$arduino_house_id',
	'$sqltime',
	'$arduino_interval',
	'$arduino_energia',
	'$arduino_fp',
	'$fatura_parcial'
	);";
$result = mysql_query($qry);
	if ($result){
	echo "valor inserido com sucesso!!\n";
	}
echo $qry;
?>