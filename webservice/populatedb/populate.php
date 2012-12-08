
<html>
<head>
<title>Populate measures table</title>
</head>
<body>
<?php

//include SQL connection data. EDIT config.php!!!
include("config.php");
include("calculafatura.php");
ini_set('max_execution_time', 600);

// SETUP

$currenttime = time();
$potencia = 550;
$timestamp = strtotime($_POST['date']); 

echo "<h1>Programa de Inserção de Dados</h1>";
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

// ROTINA DE INSERÇÃO DE DADOS

echo "<table border=\"2\" align=\"center\" cellpadding=\"10\" cellspacing=\"10\">";
echo "<tr><th>Timestamp</th><th>Energia</th><th>Preço</th><th>Posto</th></tr>";
while ($timestamp <= $currenttime){
	
	//gerar novo valor de energia em W
	$fator_potencia = 0.9732*(1+fprand(0,0.03,3)-0.015);
	$potencia = generatevalue($timestamp,$potencia);
	//$potencia = number_format($potencia, '3');
	$intervalo = '15';
	$potencia_wh = $potencia*$intervalo/60;
	
	//insert into DB
	$sqltime = date("Y-m-d H:i:s",$timestamp);
	//$qry = "INSERT INTO medidas_(consumo,fator_potencia,tipo_tarifa,inicio_medida) VALUES('$potencia','$fator_potencia','1','$sqltime')";
	$fatura_parcial = calcula_fatura('1','15',$timestamp, $fator_potencia, $potencia_wh);
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
			'$intervalo',
			'$potencia_wh',
			'$fator_potencia',
			'$fatura_parcial'
			);";
	$result = @mysql_query($qry);
	if ($result){
			$hour=date('H',$timestamp);
				switch ($hour) {
					//ponta
					case ($hour=='18'||$hour=='19'||$hour=='20'):
			    		$tarifa="ponta";
			 		break;
			  		//intermediário
			 		case ($hour=='17'||$hour=='21'):
			    		$tarifa="intermediária";
			  		break;
			  		//ponta
			 		default:
			    		$tarifa="fora de ponta";
			  		break;
		}
		echo "<tr>";
		echo "<td>".date("Y-m-d H:i:s",$timestamp)."</td>";
		echo "<td>".number_format($potencia,2)."Wh</td>";
		echo "<td>".number_format($fatura_parcial,6)."R$</td>";
		echo "<td>".$tarifa."</td>";
		echo "</tr>";
	}
	else
	{
		die('Failed to insert values: ' . mysql_error()); 
	}
	//echo $result;
	//increment time in 15 mins
	$timestamp = $timestamp + 60*15;
	
}
echo "</table>";

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

//geração da curva de demanda
function generatevalue ($timestamp,$oldvalue){
	$now = date("Gi",$timestamp); // gera hora atual
	//$factor = 1 + fprand(0,0.15,3); //gera fator randomico para macroeventos
	if ($oldvalue == 0){
		$oldvalue = 300 * (1 + fprand(0,0.2,3) - 0.10);
	}

	if ($now > 000 && $now <= 200){
		$potencia = $oldvalue * (1 + fprand(0.05,0.10,3) - 0.10); // DEcrescimento entre 5% e %10
	}
	elseif ($now > 200 && $now <= 430)
	{
		$potencia = 300*(1 + fprand(0.87,1.13,3)-0.65); // +-13%
	}
	elseif ($now > 430 && $now <= 600)
	{
		$potencia = 450*( 1+ fprand(0.87,1.13,3)-0.65); // +-13%
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
