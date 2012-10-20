<?php
header('Content-type: application/json');
header('Access-Control-Allow-Origin: *');
 
switch($_SERVER['REQUEST_METHOD']){
   case 'GET':
    echo("Ronaldo!");
    break;
   case 'POST':
    break;
   case 'PUT':
    break;
   case 'DELETE':
    break;
}
?>