<?php
include("config.php");
$arduino_energia='13,12';
$arduino_interval='15';
$arduino_fp='0.987';
$arduino_house_id='1';
$arduino_timestamp=time();


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

//recebe potencia em Wh
function calcula_fatura($house_id, $interval, $timestamp, $fp, $energia){
	//Busca ID da Ditribuidora da residencia
	$qry ="SELECT `residencias`.`distribuidora_id`,`residencias`.`tipo_tarifa` FROM `tcc_gfx`.`residencias` WHERE `residencias`.`house_id`='$house_id'";
	$result = mysql_query($qry);
	if ($result){
		$row = mysql_fetch_assoc($result);
		$distribuidora=$row["distribuidora_id"];
		$tipo_tarifa=$row["tipo_tarifa"];
		echo "Casa:".$house_id."<br/>";
		echo "Distribuidora:".$distribuidora."<br/>";
		echo "Tarifa:".$tipo_tarifa."<br/>";		
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
		echo "Tarifa Convencional: ".$tarifa_conv."<br/>";
		echo "Tarifa Ponta: ".$tarifa_ponta."<br/>";
		echo "Tarifa Fora Ponta: ".$tarifa_foraponta."<br/>";
		echo "Tarifa Intermedi�ria: ".$tarifa_int."<br/>";		
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
	  		//intermedi�rio
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
$energia_wh=$arduino_energia*$arduino_interval/60;
$fatura_parcial = calcula_fatura($arduino_house_id,$arduino_interval,$arduino_timestamp, $arduino_fp, $arduino_energia);
echo "Fatura Parcial: ".$fatura_parcial."R$";
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
	'$arduino_house_id'
	'$arduino_timestamp',
	'$arduino_interval',
	'$energia_wh',
	'$arduino_fp',
	'$fatura_parcial'
	);";
echo $qry;
?>