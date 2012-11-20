DELIMITER $$
DROP TRIGGER IF EXISTS `after_insert_medidas`$$
CREATE TRIGGER `after_insert_medidas`  
    AFTER INSERT ON `medidas` FOR EACH ROW  
    BEGIN  
		INSERT INTO medidas_dia (
			`user_id`,
			`house_id`,
			`dia_medida`,
			`consumo`,
			`fator_potencia_dia`,
			`soma_intervalos`,
			`tipo_tarifa`)  
		VALUES (
			NEW.user_id,
			NEW.house_id,
			DATE(NEW.inicio_medida), 
			NEW.consumo*NEW.intervalo_demanda*NEW.fator_potencia/'60',
			NEW.fator_potencia,
			NEW.intervalo_demanda,
			NEW.tipo_tarifa
		)
		ON DUPLICATE KEY update
		fator_potencia_dia = (fator_potencia_dia*soma_intervalos + NEW.fator_potencia*NEW.intervalo_demanda)/(soma_intervalos + NEW.intervalo_demanda),
		soma_intervalos = soma_intervalos + NEW.intervalo_demanda,
		consumo = consumo + NEW.consumo*NEW.intervalo_demanda/'60';
		
	INSERT INTO medidas_mes (
			`user_id`,
			`house_id`,
			`mes_medida`,
			`consumo`,
			`fator_potencia_mes`,
			`soma_intervalos`,
			`tipo_tarifa`)  
		VALUES (
			NEW.user_id,
			NEW.house_id,
			LAST_DAY(DATE(NEW.inicio_medida)), 
			NEW.consumo*NEW.intervalo_demanda*NEW.fator_potencia/'60',
			NEW.fator_potencia,
			NEW.intervalo_demanda,
			NEW.tipo_tarifa
		)
		ON DUPLICATE KEY update
		consumo = consumo + NEW.consumo*NEW.intervalo_demanda/'60',
		soma_intervalos = soma_intervalos + NEW.intervalo_demanda,
		fator_potencia_mes = (fator_potencia_mes*soma_intervalos + NEW.fator_potencia*NEW.intervalo_demanda)/(soma_intervalos + NEW.intervalo_demanda);
    END;$$