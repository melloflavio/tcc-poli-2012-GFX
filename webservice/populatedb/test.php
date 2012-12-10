<?php

//include SQL connection data. EDIT config.php!!!
include("config.php");
include("calculafatura.php");
ini_set('max_execution_time', 600);

	// Floating point random number function
function fprand($intMin,$intMax,$intDecimals) {
	if($intDecimals) {
		$intPowerTen=pow(10,$intDecimals);
		return rand($intMin,$intMax*$intPowerTen)/$intPowerTen;
	}
	else
	return rand($intMin,$intMax);
}
$tchuca = 1+rand(0,4000)/100000;
echo $tchuca;


?>
