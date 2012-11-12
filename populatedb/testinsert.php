<html>
<head>
<title>My First PHP Page</title>
</head>
<body>
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
$consumo="425.23";
$fp=0.95;
$tt=1;
$im=time();
$ut=time();
$qry = "INSERT INTO `medidas_`(`consumo`, `fator_potencia`, `tipo_tarifa`, `inicio_medida`, `updated`) VALUES ('$consumo','$fp','$tt',1,'$ut')";
echo $qry;	
$result = @mysql_query($qry);
	if($result) echo "sucesso!";
	else echo "fail";
?>
</body>
</html>
