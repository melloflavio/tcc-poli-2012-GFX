<html>
	<body>
		<form action="post.php" method="post">
			<input name="data" type="text" />
		</form>
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
    echo("Ronaldo!");
	$arr = array('data' => 'encoded');
	echo json_encode($arr);
	//$query = "INSERT INTO teste (text) VALUES ('Get')";
	//$result = mysql_query($query);
	echo("Brilou");
    break;
   case 'POST':
    $jsonStr = file_get_contents("php://input");
    echo("json string: ".$jsonStr."\n");
	$input = json_decode($jsonStr, true);
	echo("------------".count($input));

	foreach ($input as $k => $v) {
		echo "\[$k] => $v.\n";
	}
   //$query = "INSERT INTO teste (text) VALUES ('".$text."')";
	//$result = mysql_query($query);
	//echo("    Post muito no corinthians!");
    break;
   case 'PUT':
    break;
   case 'DELETE':
    break;
}
?>

</body>
</html>