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

$currenttime = time()+60*60*8;
$potencia = 550;
$timestamp = time(); 

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
	$fator_potencia = 0.9732*(rand(9370000,9740000)/10000000);
	$potencia = generatevalue($timestamp,$potencia);
	//$potencia = number_format($potencia, '3');
	$intervalo = '2';
	$potencia_wh = $potencia*$intervalo/60;
	
	//insert into DB
	$sqltime = date("Y-m-d H:i:s",$timestamp);
	//$qry = "INSERT INTO medidas_(consumo,fator_potencia,tipo_tarifa,inicio_medida) VALUES('$potencia','$fator_potencia','1','$sqltime')";
	$fatura_parcial = calcula_fatura('2',$intervalo,$timestamp, $fator_potencia, $potencia_wh);
	$qry="INSERT INTO `tcc_gfx`.`medidas`
			(`house_id`,
			`inicio_medida`,
			`intervalo_demanda`,
			`consumo`,
			`fator_potencia`,
			`fatura_parcial_medida`)
			VALUES
			(
			'2',
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
	$timestamp = $timestamp + 10;
	
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
$oldvalue = 50;
//geração da curva de demanda
function generatevalue ($timestamp,$oldvalue){
	$now = date("Gi",$timestamp); // gera hora atual
	//$factor = 1 + fprand(0,0.15,3); //gera fator randomico para macroeventos
	if ($oldvalue == 0){
		$oldvalue = 120;
	}

	if ($now > 000 && $now <= 200){
		$tchuca = rand(8560000,9430000)/10000000;
		$potencia = $oldvalue *$tchuca; // DEcrescimento entre 5% e %10
	}
	elseif ($now > 200 && $now <= 400)
	{
		$tchuca = rand(9660000,9530000)/10000000;
		$potencia = $oldvalue*$tchuca; // ok
	}
	elseif ($now > 400 && $now <= 600)
	{
		$tchuca = rand(920000,1060000)/1000000;
		$potencia = 50*$tchuca; // ok
	}
	elseif ($now > 600 && $now <= 730)
	{
		$tchuca = 1 + rand(1135000,1450000)/10000000;
		$potencia = $oldvalue * $tchuca; // ok
	}
	elseif ($now > 730 && $now <= 800)
	{
		$tchuca = 1 + rand(335000,650000)/10000000;
		$potencia = $oldvalue * $tchuca; // ok
	}
	elseif ($now > 800 && $now <= 1000)
	{
		$tchuca = 1-rand(240000,850000)/10000000;
		$potencia = $oldvalue *$tchuca; // DEcrescimento entre 10% e %15
	}
	elseif ($now > 1000 && $now <= 1200)
	{
		$tchuca = rand(920000,1060000)/1000000;
		$potencia = $oldvalue*$tchuca; // ok
	}
	elseif ($now > 1200 && $now <= 1400)
	{
		$tchuca = 1+rand(20000,60000)/1000000;
		$potencia = $oldvalue * $tchuca; // DEcrescimento entre 10% e %15
	}
	elseif ($now > 1400 && $now <= 1600)
	{
		$tchuca = rand(820000,910000)/1000000;
		$potencia = $oldvalue * $tchuca; // crescimento entre 15% e %27
	}
	elseif ($now > 1600 && $now <= 1700)
	{
		$tchuca = 1 + rand(935000,1500000)/10000000;
		$potencia = $oldvalue * $tchuca; // 
	}
	elseif ($now > 1700 && $now <= 1800)
	{
		$tchuca = 1 + rand(1635000,1900000)/10000000;
		$potencia = $oldvalue * $tchuca; // 
	}
	elseif ($now > 1800 && $now <= 2000)
	{
		$tchuca = 1 + rand(735000,1350000)/10000000;
		$potencia = $oldvalue * $tchuca;
	}
	elseif ($now > 2000 && $now <= 2200)
	{
		$tchuca = rand(928000,1062000)/1000000;
		$potencia = $oldvalue*$tchuca; // ok
	}
	elseif ($now > 2200 && $now <= 2359)
	{
		$tchuca = rand(9560000,9730000)/10000000;
		$potencia = $oldvalue * $tchuca; // DEcrescimento entre 10% e %15
	}
	else
	{
		$tchuca = rand(998000,1002000)/1000000;
		$potencia = $oldvalue*$tchuca; // ok
	}
	return $potencia;
}

?>

</body>
</html>
