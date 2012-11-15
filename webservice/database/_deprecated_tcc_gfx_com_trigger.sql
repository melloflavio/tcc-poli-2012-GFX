-- phpMyAdmin SQL Dump
-- version 3.5.1
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Nov 15, 2012 at 02:55 AM
-- Server version: 5.5.24-log
-- PHP Version: 5.3.13

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `tcc_gfx`
--

-- --------------------------------------------------------

--
-- Table structure for table `feriados`
--

CREATE TABLE IF NOT EXISTS `feriados` (
  `data` date NOT NULL,
  `descritivo` text NOT NULL,
  PRIMARY KEY (`data`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `feriados`
--

INSERT INTO `feriados` (`data`, `descritivo`) VALUES
('2012-02-21', 'Carnaval (facultativo)'),
('2012-04-06', 'Sexta-Feira da Paixão'),
('2012-04-21', 'Tiradentes'),
('2012-05-01', 'Dia do Trabalho'),
('2012-06-07', 'Corpus Christi (facultativo)'),
('2012-09-07', 'Independência do Brasil'),
('2012-10-12', 'Nossa Senhora Aparecida'),
('2012-11-01', 'Ano Novo)'),
('2012-11-02', 'Finados'),
('2012-11-15', 'Proclamação da República'),
('2012-11-25', 'Natal');

-- --------------------------------------------------------

--
-- Table structure for table `medidas`
--

CREATE TABLE IF NOT EXISTS `medidas` (
  `user_id` int(11) NOT NULL,
  `house_id` int(11) NOT NULL,
  `inicio_medida` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `intervalo_demanda` tinyint(4) NOT NULL,
  `consumo` double NOT NULL,
  `fator_potencia` double NOT NULL,
  `tipo_tarifa` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`house_id`,`inicio_medida`),
  KEY `FK_measured_at` (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `medidas`
--

INSERT INTO `medidas` (`user_id`, `house_id`, `inicio_medida`, `intervalo_demanda`, `consumo`, `fator_potencia`, `tipo_tarifa`, `updated`) VALUES
(1, 1, '2012-11-15 02:12:42', 5, 1440, 1, 1, '0000-00-00 00:00:00');

--
-- Triggers `medidas`
--
DROP TRIGGER IF EXISTS `after_insert_medidas`;
DELIMITER //
CREATE TRIGGER `after_insert_medidas` AFTER INSERT ON `medidas`
 FOR EACH ROW BEGIN  
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
			NEW.fator_potencia*NEW.intervalo_demanda/'1440',
			NEW.tipo_tarifa
		)
		ON DUPLICATE KEY update
		consumo = consumo + NEW.consumo*NEW.intervalo_demanda/'1440',
		fator_potencia = fator_potencia + NEW.fator_potencia*NEW.intervalo_demanda/'1440';
    END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `medidas_dia`
--

CREATE TABLE IF NOT EXISTS `medidas_dia` (
  `user_id` int(11) NOT NULL,
  `house_id` int(11) NOT NULL,
  `dia_medida` date NOT NULL,
  `consumo` double NOT NULL,
  `fator_potencia` double NOT NULL,
  `tipo_tarifa` int(11) NOT NULL,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`,`house_id`,`dia_medida`),
  KEY `FK_measured_at` (`house_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Dumping data for table `medidas_dia`
--

INSERT INTO `medidas_dia` (`user_id`, `house_id`, `dia_medida`, `consumo`, `fator_potencia`, `tipo_tarifa`, `updated`) VALUES
(1, 1, '2012-11-14', 5, 0.003472222222222222, 1, '0000-00-00 00:00:00');

--
-- Triggers `medidas_dia`
--
DROP TRIGGER IF EXISTS `after_insert_medidas_DIA`;
DELIMITER //
CREATE TRIGGER `after_insert_medidas_DIA` AFTER INSERT ON `medidas_dia`
 FOR EACH ROW BEGIN  
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
    END
//
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `medidas_mes`
--

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

--
-- Dumping data for table `medidas_mes`
--

INSERT INTO `medidas_mes` (`user_id`, `house_id`, `mes_medida`, `consumo`, `fator_potencia`, `tipo_tarifa`, `updated`) VALUES
(1, 1, '2012-11-14', 0.16666666666666666, 0.00011574074074074073, 1, '0000-00-00 00:00:00');

-- --------------------------------------------------------

--
-- Table structure for table `residencias`
--

CREATE TABLE IF NOT EXISTS `residencias` (
  `house_id` int(11) NOT NULL AUTO_INCREMENT,
  `nome_casa` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `logradouro` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `cidade` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `estado` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`house_id`),
  UNIQUE KEY `house_id` (`house_id`),
  KEY `FK_belongs_to` (`user_id`),
  KEY `house_id_2` (`house_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `residencias`
--

INSERT INTO `residencias` (`house_id`, `nome_casa`, `logradouro`, `cidade`, `estado`, `user_id`) VALUES
(1, 'Casa Uno', 'Alberto Faria, 1946', 'Sao Paulo', 'SP', 1),
(2, 'Maloca Due', 'Arruda Botelho', 'Sobradinho', 'MG', 1);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `email` varchar(64) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(16) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id` (`user_id`),
  KEY `user_id_2` (`user_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci AUTO_INCREMENT=3 ;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`user_id`, `email`, `password`) VALUES
(1, '@', 'qwerty'),
(2, '@2', 'qwerty');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `medidas`
--
ALTER TABLE `medidas`
  ADD CONSTRAINT `FK_measured_at` FOREIGN KEY (`house_id`) REFERENCES `residencias` (`house_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_measured_for` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `medidas_dia`
--
ALTER TABLE `medidas_dia`
  ADD CONSTRAINT `FK_collected_at` FOREIGN KEY (`house_id`) REFERENCES `medidas` (`house_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_collected_for` FOREIGN KEY (`user_id`) REFERENCES `medidas` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `medidas_mes`
--
ALTER TABLE `medidas_mes`
  ADD CONSTRAINT `FK_gathered_for` FOREIGN KEY (`user_id`) REFERENCES `medidas_dia` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `FK_gathered_at` FOREIGN KEY (`house_id`) REFERENCES `medidas_dia` (`house_id`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `residencias`
--
ALTER TABLE `residencias`
  ADD CONSTRAINT `FK_belongs_to` FOREIGN KEY (`user_id`) REFERENCES `users` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;