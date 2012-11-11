<?php 
	$db_username='gfx';
	$db_password='gfx2012';
	$database='TCC_GFX';

	$connect = mysql_connect("localhost:3306", $db_username, $db_password);
	@mysql_select_db($database) or die('Unable to select database');
	mysql_query("SET NAMES 'utf8'");

	$time = 1352309353;
	$timestamp = date('Y-m-d H:i:s', $time);
	
	$query = "INSERT INTO teste (timestamp, datetime) VALUES (FROM_UNIXTIME(".$time."),FROM_UNIXTIME(".$time.")";
	$result = mysql_query($query);
	
	//echo ("r=".$result);
	$query = "SELECT * FROM teste WHERE id=4";
	$result = mysql_query($query);
	
	if($result && mysql_num_rows($result) > 0){
		$row = mysql_fetch_array($result, MYSQL_ASSOC);
		
		echo ("timestamp = ".$row["timestamp"]);
	}
	
	//echo ($timestamp);
?>