/**
 *  Funções para uso do dashboard de geração de gráficos
 * 	Author: Gilberto Cymbaum
 */S
    $('document').ready(
            function(){
            	$( "input[type=submit]" )
                .button()
                .click(function( event ) {
                 	var houseId = $("#form1 input[type='radio']:checked").val();
                	var query = $("#form2 input[type='radio']:checked").val();
                	var data = $("#datepicker").val();
                	alert(houseId+"\n"+query+"\n"+data);
//                     alert("houseId ="+houseId+"\n"+"query = "+query+"\n"+"data ="+data);
                });
            	$( "#datepicker" ).datepicker({
                    changeMonth: true,
                    changeYear: true
                });
            	$( "#radio1" ).buttonset();
            	$( "#radio2" ).buttonset();
                $('input:radio[id=radio1]').click(
                    function(){
                    	//alert(+house);   
                    }
                );
                $('input:radio[id=radio1]').click(
                        function(){
                        	//house='1';
                        	//alert(+house);
                        }
                    );
            }
        );
