<?php
$username='melloflavio';
$password='ffm1207';
$database='TCC_GFX';

$connect = mysql_connect("localhost:3306", $username, $password);
@mysql_select_db($database) or die('Unable to select database');
mysql_query("SET NAMES 'utf8'");

//header('Content-type: application/json');
header('Access-Control-Allow-Origin: *');
 
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
?>