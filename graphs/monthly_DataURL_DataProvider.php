<?php
// Set the content type in the header to text/xml.
header('Content-type: text/xml');

// Include DBConn.php
include('Includes/DBConn.php');

// Use the connectToDB() function provided in DBConn.php, and establish the connection between PHP and the World database in our MySQL setup.
$link = connectToDB();

// Buscar medidas dos últimos 30 dias;
$currenttime = time();
$initialtime = $currenttime - 60*60*6;
//$strQuery = 'SELECT Name AS Country, Population FROM country ORDER BY Population DESC LIMIT 10';
$strQuery = 'SELECT timestamp AS Time, potencia FROM `medidas` WHERE timestamp BETWEEN \''.$initialtime.'\' AND \''.$currenttime.'\' ORDER BY timestamp ASC';

// Execute the query, or else return the error message.
$result = mysql_query($strQuery) or die(mysql_error());

// If we get a valid response - 
if ($result) {
    
    // Create the chart's XML string. We can add attributes here to customize our chart.
    $strXML = "<chart caption='Monthly Energy Consumption' showValues='0' useRoundEdges='1' palette='3'>";
    
    while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".date("H:i:s",$ors['Time'])."' value='".$ors['potencia']."' />";
    }
}   
// Close the chart's XML string.
$strXML .= "</chart>";  

// Return the valid XML string.
echo $strXML;
?>