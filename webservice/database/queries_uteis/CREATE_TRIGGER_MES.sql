DELIMITER $$
DROP TRIGGER IF EXISTS `after_insert_medidas_dia`$$
CREATE TRIGGER `after_insert_medidas_DIA`  
    AFTER INSERT ON `medidas_dia` FOR EACH ROW  
    BEGIN  
		INSERT INTO medidas_mes (
			`user_id`,
			`house_id`,
			`mes_medida`,
			`consumo`,
			`fator_potencia`,
			`tipo_tarifa`)  
		VALUES (
			NEW.user_id,
			NEW.house_id,
			NEW.dia_medida, 
			NEW.consumo/DAYOFMONTH(LAST_DAY(NEW.dia_medida)),
			NEW.fator_potencia/DAYOFMONTH(LAST_DAY(NEW.dia_medida)),
			NEW.tipo_tarifa
		)
		ON DUPLICATE KEY update
		consumo = consumo + NEW.consumo/DAYOFMONTH(LAST_DAY(NEW.dia_medida)),
		fator_potencia = fator_potencia + NEW.fator_potencia/DAYOFMONTH(LAST_DAY(NEW.dia_medida));
    END;$$