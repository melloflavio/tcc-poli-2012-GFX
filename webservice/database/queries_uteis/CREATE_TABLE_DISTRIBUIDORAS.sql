CREATE TABLE IF NOT EXISTS `distribuidoras` (
  `distribuidora_id` int(11) NOT NULL AUTO_INCREMENT,
  `tarifa_convencional` double NOT NULL,
  `tarifa_ponta` double NOT NULL,
  `tarifa_foraponta` double NOT NULL,
  `tarifa_intermediaria` double NOT NULL,
  `razao_social` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `cidade` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `estado` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`distribuidora_id`),
  UNIQUE KEY `house_id` (`distribuidora_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;