DELIMITER $$
DROP TRIGGER IF EXISTS `after_update_medidas_dia`$$
CREATE TRIGGER `after_update_medidas_DIA`  
    AFTER UPDATE ON `medidas_dia` FOR EACH ROW  
    BEGIN  
		INSERT INTO medidas_mes (
			`user_id`,
			`house_id`,
			`mes_medida`,
			`consumo`,
			`fator_potencia_medio`,
			`tipo_tarifa`)  
		VALUES (
			NEW.user_id,
			NEW.house_id,
			NEW.dia_medida, 
			NEW.consumo,
			NEW.fator_potencia_medio,
			NEW.tipo_tarifa
		)
		ON DUPLICATE KEY update
		consumo = consumo + NEW.consumo,
		#fator_potencia_medio = (fator_potencia_medio*(DAYOFMONTH(NEW.dia_medida)-'1') + NEW.fator_potencia_medio)/DAYOFMONTH(NEW.dia_medida);
		fator_potencia_medio=NEW.fator_potencia_medio;
END;$$