use tcc_gfx;
DROP TABLE medidas_dia;
CREATE TABLE IF NOT EXISTS `medidas_dia` (
  `user_id` int(11) NOT NULL,
  `house_id` int(11) NOT NULL,
  `dia_medida` date NOT NULL,
  `consumo` double NOT NULL,
  `fator_potencia_dia` double NOT NULL,
  `soma_intervalos` int(11),
  `tipo_tarifa` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`house_id`,`dia_medida`),
  KEY `FK_measured_at` (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;