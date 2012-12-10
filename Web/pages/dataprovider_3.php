<?php

// Set the content type in the header to text/xml.
header('Content-type: text/xml');

// Include DBConn.php
include('/../Includes/DBConn.php');

// Use the connectToDB() function provided in DBConn.php, and establish the connection between PHP and the World database in our MySQL setup.
$link = connectToDB();

// Form the SQL query which will return the Top 10 Most Populous Countries.
//$strQuery = 'SELECT Name AS Country, Population FROM country ORDER BY Population DESC LIMIT 10';
//$strQuery = 'SELECT Name AS Country, Population FROM country ORDER BY Population DESC LIMIT 10';
/*$strQuery = "SELECT
`medidas_mes`.`house_id`,
`medidas_mes`.`mes_medida`,
`medidas_mes`.`consumo`,
`medidas_mes`.`fator_potencia_mes`,
`medidas_mes`.`fatura_mes`,
`medidas_mes`.`soma_intervalos`
FROM `tcc_gfx`.`medidas_mes`;"*/
//$horas=$_GET['horas'];
$data=$_GET['data'];
$house=$_GET['house'];

//get 23:59 do dia em questão
$date=strtotime($data);
$date=$date+60*60*24-1;
$sqltime = date("Y-m-d H:i:s",$date);


//echo date("F j, Y, G:i", $date);
//echo $sqltime;
//echo $date;
if ($house=='2'){
	$horas = 1;
	$strQuery = "SELECT
		`medidas`.`inicio_medida`,
		`medidas`.`consumo`
		FROM `tcc_gfx`.`medidas`
		WHERE
		`medidas`.`house_id`='".$house."' AND
		`medidas`.`inicio_medida`<= '".date("Y-m-d H:i:s",time())."' AND
		`medidas`.`inicio_medida`>= DATE_SUB('".date("Y-m-d H:i:s",time())."', INTERVAL 3 MINUTE);";
}
else {
	$horas='24';
	$strQuery = "SELECT
		`medidas`.`inicio_medida`,
		`medidas`.`consumo`
		FROM `tcc_gfx`.`medidas`
		WHERE
		`medidas`.`house_id`='".$house."' AND
		`medidas`.`inicio_medida`<= '".$sqltime."' AND
		`medidas`.`inicio_medida`>= DATE_SUB('".$sqltime."', INTERVAL ".$horas." HOUR);";
}

//echo $strQuery;
	
// Execute the query, or else return the error message.
$result = mysql_query($strQuery) or die(mysql_error());

// If we get a valid response - 
if ($result) {
    // Create the chart's XML string. We can add attributes here to customize our chart.
    //$strXML = "<chart caption='Monthly Energy Usage' xAxisName='Month' yAxisName='Energy' showValues='0' useRoundEdges='1' palette='3'>";
	if ($house=='2'){
		$strXML = "<chart caption=\"Consumo de Energia Instant&#226;neo\" subCaption=\"&#250;ltima hora de valores medidos (Wh)\" bgColor=\"406181, 6DA5DB\"  bgAlpha=\"100\" baseFontColor=\"FFFFFF\" canvasBgAlpha=\"0\" canvasBorderColor=\"FFFFFF\" divLineColor=\"FFFFFF\" divLineAlpha=\"100\" numVDivlines=\"10\" vDivLineisDashed=\"1\" showAlternateVGridColor=\"1\" lineColor=\"BBDA00\" anchorRadius=\"4\" anchorBgColor=\"BBDA00\" anchorBorderColor=\"FFFFFF\" anchorBorderThickness=\"2\" showValues=\"0\" numberSuffix=\"Wh\" toolTipBgColor=\"406181\" toolTipBorderColor=\"406181\" alternateHGridAlpha=\"5\">";
		while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".date("G:i:s",strtotime($ors['inicio_medida']))."' value='".number_format($ors['consumo'],3,'.','')."' />";
    	}
	}
	else {
		$strXML = "<chart caption=\"Consumo de Energia Instant&#226;neo\" subCaption=\"&#250;ltimas 24 horas de valores medidos (Wh)\" bgColor=\"406181, 6DA5DB\"  bgAlpha=\"100\" baseFontColor=\"FFFFFF\" canvasBgAlpha=\"0\" canvasBorderColor=\"FFFFFF\" divLineColor=\"FFFFFF\" divLineAlpha=\"100\" numVDivlines=\"10\" vDivLineisDashed=\"1\" showAlternateVGridColor=\"1\" lineColor=\"BBDA00\" anchorRadius=\"4\" anchorBgColor=\"BBDA00\" anchorBorderColor=\"FFFFFF\" anchorBorderThickness=\"2\" showValues=\"0\" numberSuffix=\"Wh\" toolTipBgColor=\"406181\" toolTipBorderColor=\"406181\" alternateHGridAlpha=\"5\">";
		while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".date("G:i",strtotime($ors['inicio_medida']))."' value='".number_format($ors['consumo'],3,'.','')."' />";
    	}
	}	
	
}   
// Close the chart's XML string.
$strXML.= " <styles>
                <definition>
                        <style name=\"LineShadow\" type=\"shadow\" color=\"333333\" distance=\"6\"/>
                </definition>
                <application>
                        <apply toObject=\"DATAPLOT\" styles=\"LineShadow\" />
                </application>  
        </styles>
</chart>";  

// Return the valid XML string.
echo $strXML;
?>