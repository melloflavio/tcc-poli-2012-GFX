<?php
    
// Include the DBConn.php and FusionCharts.php files, so that we can access their variables and functions.
include('Includes/DBConn.php');
include('Includes/FusionCharts.php');

// Use the connectToDB() function provided in DBConn.php, and establish the connection between PHP and the World database in our MySQL setup.
$link = connectToDB();

// Form the SQL query which will return the Top 10 Most Populous Countries.
$strQuery = 'SELECT Name AS Country, Population FROM country ORDER BY Population DESC LIMIT 10';

// Execute the query, or else return the error message.
$result = mysql_query($strQuery) or die(mysql_error());

// If we get a valid response - 
if ($result) {
    
    // Create the chart's XML string. We can add attributes here to customize our chart.
    $strXML = "<chart caption='Top 10 Most Populous Countries' showValues='0' useRoundEdges='1' palette='3'>";
    
    while($ors = mysql_fetch_array($result)) {
        // Append the names of the countries and their respective populations to the chart's XML string.
        $strXML .= "<set label='".$ors['Country']."' value='".$ors['Population']."' />";
    }
}   
// Close the chart's XML string.
$strXML .= "</chart>";  
?>
        
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
    <head>
        <title>FusionCharts XT with PHP and MySQL</title>
        
        <!-- Include FusionCharts.js to provide client-side interactivity -->
        <script type="text/javascript" src="FusionCharts/FusionCharts.js"></script>
    </head>
    <body>
        <!-- This DIV would be our chart container -->
        <div id="chartContainer">
        <?php
            
            // Set the rendering mode to JavaScript
            FC_SetRenderer('javascript');
            
            // Call the renderChart method, which would return the HTML and JavaScript required to generate the chart
            echo renderChart('FusionCharts/Column2D.swf', // Path to chart type
                    '',     // Empty string when using Data String method
                    $strXML,// Variable which has the chart data
                    'top10_most_populous_countries', // Unique chart ID
                    '660', '400', // Width and height in pixels
                    false,  // Disable debug mode
                    true    // Enable 'Register with JavaScript' (Recommended)
                );
        ?>
        </div>
    </body>
</html>