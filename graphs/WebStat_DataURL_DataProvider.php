<?php
// Set the content type in the header to text/xml.
header('Content-type: text/xml');

// Include DBConn.php
include('Includes/DBConn.php');

// Use the connectToDB() function provided in DBConn.php, and establish the connection between PHP and the World database in our MySQL setup.
$link = connectToDB();

// Buscar medidas dos últimos 30 dias;
$currenttime = time();
$initialtime = $currenttime - 60*60*24;
//$strQuery = 'SELECT Name AS Country, Population FROM country ORDER BY Population DESC LIMIT 10';
$strQuery = 'SELECT timestamp AS Time, potencia FROM `medidas` WHERE timestamp BETWEEN \''.$initialtime.'\' AND \''.$currenttime.'\' ORDER BY timestamp ASC';

// Execute the query, or else return the error message.
$result = mysql_query($strQuery) or die(mysql_error());

// If we get a valid response - 
if ($result) {
    
    // Create the chart's XML string. We can add attributes here to customize our chart.
    $strXML = "<chart caption=\"KWh Energy Consumption\" subCaption=\"GFX Arduino Meter\" yAxisMaxValue=\"100\" bgColor=\"406181,
     6DA5DB\"  bgAlpha=\"100\" baseFontColor=\"FFFFFF\" canvasBgAlpha=\"0\" canvasBorderColor=\"FFFFFF\" divLineColor=\"FFFFFF\" 
     divLineAlpha=\"100\" numVDivlines=\"10\" vDivLineisDashed=\"1\" showAlternateVGridColor=\"1\" lineColor=\"BBDA00\" anchorRadius=\"4\" 
     anchorBgColor=\"BBDA00\" anchorBorderColor=\"FFFFFF\" anchorBorderThickness=\"2\" showValues=\"0\" numberSuffix=\"KWh\" toolTipBgColor=\"406181\" 
     toolTipBorderColor=\"406181\" alternateHGridAlpha=\"5\">";
    
    while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".date("H:i:s",$ors['Time'])."' value='".$ors['potencia']."' />";
    }
}   
// Close the chart's XML string.
$strXML .= "        <styles>
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