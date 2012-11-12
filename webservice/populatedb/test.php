<html>
<head>
<title>My First PHP Page</title>
</head>
<body>
<?php
//$timepost = $_POST['date']; 
$timestamp = //unix time stamp
$hour = 0;
$minute = 0;
$second = 0;
$day = 0;
$month = 0;

$timestamp = strtotime("12/25/2012");
echo $timestamp;
echo 
$month = substr($timepost,"0","2");
$day = substr($timepost,"3","2");;
$year = substr($timepost,"6","4");

echo $year." ".$month." ".$day."\nn";
//$timepost = mkti
//for ($time=)
function isleapyear($year)
{
	return ((($year % 4) == 0) && ((($year % 100) != 0) || (($year %400) == 0)));
}
function monthdays($currentmonth,$currentyear)
{
	$bigmonths = array ("1","3","5","7","8","10","12");
	$smallmonths = array ("4","6","9","11");
	if (in_array($currentmonth,$bigmonths)){
		$monthdays=31;
	}
	elseif (in_array($currentmonth,$smallmonths)){
		$monthdays=30;
	}
	elseif ($currentmonth="2"){
		$monthdays=29;
		if (isleapyear($currentyear)){
			$monthdays=28;
		}
	}
	return $monthdays;
}
echo monthdays(2,2012);

$timestamp = date("U", mktime(18, 55, 0, 8, 21, 2012));
echo "<p>".$timestamp."</p>";
//echo "August 21, 2012 is on a " . date("U", mktime(18, 55, 0, 8, 21, 2012));
echo "<br></br>current timestamp is: ".$timestamp;
echo "<br></br>current date is: ".date(DATE_RSS,$timestamp);
echo "<br></br>current hour is: ".date("Gi",$timestamp)."\n\n";

// Floating point random number function
function fprand($intMin,$intMax,$intDecimals) {
  if($intDecimals) {
        $intPowerTen=pow(10,$intDecimals);
        return rand($intMin,$intMax*$intPowerTen)/$intPowerTen;
  }
  else
        return rand($intMin,$intMax);
}


// Example of fprand function
for($i=0; $i<=5; $i++) {
  echo "Three random numbers with $i decimals are: ";
  for($ii=1; $ii<=3; $ii++)
        echo fprand(1,10,$i).' ';
  echo '<br>';
}
$factor = 1 + fprand(0,0.15,3);
for ($i=0;$i<30;$i++){
	$factor = 1+fprand(0,0.15,3);
	echo $factor."<br/>";
}

?>
</body>
</html>
