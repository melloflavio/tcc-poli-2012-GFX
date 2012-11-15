CREATE TABLE IF NOT EXISTS `medidas_mes` (
  `user_id` int(11) NOT NULL,
  `house_id` int(11) NOT NULL,
  `mes_medida` date NOT NULL,
  `consumo` double NOT NULL,
  `fator_potencia` double NOT NULL,
  `tipo_tarifa` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`house_id`,`mes_medida`),
  KEY `FK_measured_at` (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;