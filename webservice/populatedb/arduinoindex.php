<!doctype html>
 
<html lang="en">
<head>
    <meta charset="utf-8" />
    <title>Programa de Inserção de Dados</title>
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.9.1/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.8.2.js"></script>
    <script src="http://code.jquery.com/ui/1.9.1/jquery-ui.js"></script>
    <link rel="stylesheet" href="/resources/demos/style.css" />
    <script>
    $(function() {
    	 $( "#datepicker" ).datepicker({
             changeMonth: true,
             changeYear: true
         });
    });
    </script>
</head>
<body>
<?php 
echo "<h1>Popular Banco de Dados Arduino</h1>";
echo "<h4>Editar o arquivo config.php antes de enviar!!</h4><br/>";
?> 
Popular os dados desde: <form action="arduinopopulate.php" method="post">
<input name="date" type="text" id="datepicker" />
<input id="button" type="submit" />
</form>
 
 
</body>
</html>