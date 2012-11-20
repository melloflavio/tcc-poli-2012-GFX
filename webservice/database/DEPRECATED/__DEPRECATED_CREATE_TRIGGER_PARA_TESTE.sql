DELIMITER $$
DROP TRIGGER `after_insert_medidas`$$
CREATE TRIGGER `after_insert_medidas`  
    AFTER INSERT ON `medidas` FOR EACH ROW  
    BEGIN  
		INSERT INTO medidas_dia (
			`user_id`,
			`house_id`,
			`dia_medida`,
			`consumo`,
			`fator_potencia`,
			`tipo_tarifa`)  
		VALUES (
			NEW.user_id,
			NEW.house_id,
			DATE(NEW.inicio_medida), 
			NEW.consumo*NEW.intervalo_demanda/'1440',
			NEW.fator_potencia,
			NEW.tipo_tarifa
		)
		ON DUPLICATE KEY update
		consumo = consumo + NEW.consumo*NEW.intervalo_demanda/'1440',
		fator_potencia = fator_potencia + NEW.fator_potencia*NEW.intervalo_demanda/'1440';
    END;$$