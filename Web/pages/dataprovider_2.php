<?php
// Set the content type in the header to text/xml.
header('Content-type: text/xml');

// Include DBConn.php
include('/../Includes/DBConn.php');

// Use the connectToDB() function provided in DBConn.php, and establish the connection between PHP and the World database in our MySQL setup.
$link = connectToDB();

$data=$_GET['data'];
$house=$_GET['house'];

//get mes em questão
$date=strtotime($data);
$mes=date("m",$date);
//echo $mes;

$strQuery = "SELECT
`medidas_dia`.`dia_medida` AS day,
`medidas_dia`.`consumo` AS energia
FROM `tcc_gfx`.`medidas_dia`
WHERE
`medidas_dia`.`house_id`='".$house."' AND 
DATE_FORMAT(`medidas_dia`.`dia_medida`,'%m') = ".$mes." AND DATE_FORMAT(`medidas_dia`.`dia_medida`,'%Y') = '2012' 
ORDER BY `medidas_dia`.`dia_medida` ASC;";

//echo $strQuery;

// Execute the query, or else return the error message.
$result = mysql_query($strQuery) or die(mysql_error());

// If we get a valid response - 
if ($result) {
    // Create the chart's XML string. We can add attributes here to customize our chart.
    //$strXML = "<chart caption='Monthly Energy Usage' xAxisName='Month' yAxisName='Energy' showValues='0' useRoundEdges='1' palette='3'>";
    $strXML = "<chart caption=\"Consumo de Energia Di&#225;rio\" subCaption=\"Mostrando valores referentes a ".date("M-Y",$date)."\"  bgColor=\"406181, 6DA5DB\"  bgAlpha=\"100\" baseFontColor=\"FFFFFF\" canvasBgAlpha=\"0\" canvasBorderColor=\"FFFFFF\" divLineColor=\"FFFFFF\" divLineAlpha=\"100\" numVDivlines=\"10\" vDivLineisDashed=\"1\" showAlternateVGridColor=\"1\" lineColor=\"BBDA00\" anchorRadius=\"4\" anchorBgColor=\"BBDA00\" anchorBorderColor=\"FFFFFF\" anchorBorderThickness=\"2\" showValues=\"0\" numberSuffix=\"kWh\" toolTipBgColor=\"406181\" toolTipBorderColor=\"406181\" alternateHGridAlpha=\"5\">";
    //$strXML = "<chart caption='Monthly Unit Sales' xAxisName='Month' yAxisName='Units' showValues='0' decimals='0' formatNumberScale='0' palette='4'>";

	while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".date("d",strtotime($ors['day']))."' value='".number_format($ors['energia']/'1000',3,'.','')."' />";
    }
}   
// Close the chart's XML string.
//$strXML .= "</chart>";  
$strXML.= " <styles><definition><style name=\"LineShadow\" type=\"shadow\" color=\"333333\" distance=\"6\"/></definition><application><apply toObject=\"DATAPLOT\" styles=\"LineShadow\" /></application></styles></chart>";
//$strXML.="<trendlines><line startValue='750' displayValue='Average' color='009900' valueOnRight='1' /></trendlines></chart> ";
// Return the valid XML string.
echo $strXML;
?>