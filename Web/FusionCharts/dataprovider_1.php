<?php
// Set the content type in the header to text/xml.
header('Content-type: text/xml');

// Include DBConn.php
include('Includes/DBConn.php');


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
$strQuery = "SELECT
`medidas_mes`.`mes_medida` AS month,
`medidas_mes`.`consumo` AS energia
FROM `tcc_gfx`.`medidas_mes`;";

// Execute the query, or else return the error message.
$result = mysql_query($strQuery) or die(mysql_error());

// If we get a valid response - 
if ($result) {
    // Create the chart's XML string. We can add attributes here to customize our chart.
    $strXML = "<chart caption='Monthly Energy Usage' xAxisName='Month' yAxisName='Energy' showValues='0' useRoundEdges='1' palette='3'>";
    
    while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".date("F",strtotime($ors['month']))."' value='".number_format($ors['energia']/'1000',3,'.','')."' />";
    }
}   
// Close the chart's XML string.
$strXML .= "</chart>";  

// Return the valid XML string.
echo $strXML;
?>