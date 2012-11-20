
<html>
<head>
<title>Populate measures table</title>
</head>
<body>
<?php

//include SQL connection data. EDIT config.php!!!
include("config.php");
ini_set('max_execution_time', 600);

// SETUP

$currenttime = time();
$potencia = 30;
$timestamp = strtotime($_POST['date']); 



echo "<h1>Programa de Inser��o de Dados</h1>";
echo "<h3>Inserindo a partir de: ".$_POST['date']."</h3>";
echo "<p>Initial UTC: ".$timestamp."</p><p>Final UTC: ".$currenttime."</p>";

// BANCO DE DADOS

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

// ROTINA DE INSER��O DE DADOS

while ($timestamp <= $currenttime){
	//generate new power value
	$fator_potencia = 0.9732*(1+fprand(0,0.03,3)-0.015);
	$potencia = generatevalue($timestamp,$potencia);
	$potencia = number_format($potencia, '3');
	//insert into DB
	//echo $timestamp."<br/>";
	$sqltime = date("Y-m-d H:i:s",$timestamp);
	//$qry = "INSERT INTO medidas_(consumo,fator_potencia,tipo_tarifa,inicio_medida) VALUES('$potencia','$fator_potencia','1','$sqltime')";
	$qry="INSERT INTO `tcc_gfx`.`medidas`
			(`house_id`,
			`inicio_medida`,
			`intervalo_demanda`,
			`consumo`,
			`fator_potencia`,
			`fatura_parcial_medida`)
			VALUES
			(
			'1',
			'$sqltime',
			'15',
			'$potencia',
			'$fator_potencia',
			'$fator_potencia*$potencia*0.25'
			);";
	$result = @mysql_query($qry);
	if ($result){
		echo date("Y-m-d H:i:s",$timestamp)."  medida: ".number_format($potencia,2)." Wh  inserido com sucesso!!!<br/>";
	}
	else
	{
		die('Failed to insert values: ' . mysql_error()); 
	}
	//echo $result;
	//increment time in 15 mins
	$timestamp = $timestamp + 60*15;
	
}

//FUNCTIONS 

	// Floating point random number function
function fprand($intMin,$intMax,$intDecimals) {
	if($intDecimals) {
		$intPowerTen=pow(10,$intDecimals);
		return rand($intMin,$intMax*$intPowerTen)/$intPowerTen;
	}
	else
	return rand($intMin,$intMax);
}

//gera��o da curva de demanda
function generatevalue ($timestamp,$oldvalue){
	$now = date("Gi",$timestamp); // gera hora atual
	//$factor = 1 + fprand(0,0.15,3); //gera fator randomico para macroeventos
	if ($oldvalue == 0){
		$oldvalue = 30 * (1 + fprand(0,0.2,3) - 0.10);
	}

	if ($now > 000 && $now <= 200){
		$potencia = $oldvalue * (1 + fprand(0.05,0.10,3) - 0.10); // DEcrescimento entre 5% e %10
	}
	elseif ($now > 200 && $now <= 430)
	{
		$potencia = 20*(1 + fprand(0.87,1.13,3)-0.65); // +-13%
	}
	elseif ($now > 430 && $now <= 600)
	{
		$potencia = 25*( 1+ fprand(0.87,1.13,3)-0.65); // +-13%
	}
	elseif ($now > 600 && $now <= 830)
	{
		$potencia = $oldvalue * (1 + fprand(0.1,0.15,3)); // crescimento entre 10% e %15
	}
	elseif ($now > 830 && $now <= 900)
	{
		$potencia = $oldvalue * (1 + fprand(0.1,0.15,3) - 0.015); // DEcrescimento entre 10% e %15
	}
	elseif ($now > 900 && $now <= 1200)
	{
		$potencia = $oldvalue * (1 + fprand(0.15,0.20,3)); // crescimento entre 15% e %20
	}
	elseif ($now > 1200 && $now <= 1600)
	{
		$potencia = $oldvalue * (1 + fprand(0.10,0.15,3) - 0.15); // DEcrescimento entre 10% e %15
	}
	elseif ($now > 1600 && $now <= 1830)
	{
		$potencia = $oldvalue * (1 + fprand(0.15,0.27,3)); // crescimento entre 15% e %27
	}
	elseif ($now > 1830 && $now <= 2359)
	{
		$potencia = $oldvalue * (1 + fprand(0.1,0.15,3) - 0.15); // DEcrescimento entre 10% e %15
	}
	else
	{
		$potencia = $oldvalue * (1 + fprand(0.10,0.15,3) - 0.15); // DEcrescimento entre 10% e %15
	}
	return $potencia;
}

?>

</body>
</html>
