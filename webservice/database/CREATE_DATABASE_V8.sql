SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE SCHEMA IF NOT EXISTS `tcc_gfx` DEFAULT CHARACTER SET latin1 ;
USE `tcc_gfx` ;

-- -----------------------------------------------------
-- Table `tcc_gfx`.`distribuidoras`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`distribuidoras` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`distribuidoras` (
  `distribuidora_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `sigla_distribuidora` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `razao_social` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `date_from` DATE NOT NULL ,
  `date_to` DATE NOT NULL ,
  `tarifa_convencional` DOUBLE NOT NULL ,
  `tarifa_ponta` DOUBLE NOT NULL ,
  `tarifa_foraponta` DOUBLE NOT NULL ,
  `tarifa_intermediaria` DOUBLE NOT NULL ,
  `cidade` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `estado` VARCHAR(2) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  PRIMARY KEY (`distribuidora_id`, `date_from`, `date_to`) )
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `tcc_gfx`.`feriados`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`feriados` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`feriados` (
  `data` DATE NOT NULL ,
  `descritivo` TEXT NOT NULL ,
  PRIMARY KEY (`data`) )
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `tcc_gfx`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`users` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`users` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `fname` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `lname` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `email` VARCHAR(64) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `password` VARCHAR(32) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  PRIMARY KEY (`user_id`) ,
  UNIQUE INDEX `user_id` (`user_id` ASC) )
ENGINE = InnoDB
AUTO_INCREMENT = 1
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `tcc_gfx`.`residencias`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`residencias` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`residencias` (
  `house_id` INT(11) NOT NULL AUTO_INCREMENT ,
  `nome_casa` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `logradouro` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `cidade` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `estado` VARCHAR(2) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NOT NULL ,
  `user_id` INT(11) NOT NULL ,
  `tipo_tarifa` INT(11) NOT NULL DEFAULT '1',
  `distribuidora_id` INT(11) NOT NULL ,
  PRIMARY KEY (`house_id`) ,
  UNIQUE INDEX `house_id` (`house_id` ASC) ,
  CONSTRAINT `FK_owner`
    FOREIGN KEY (`user_id` )
    REFERENCES `tcc_gfx`.`users` (`user_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `FK_distribuidora`
    FOREIGN KEY (`distribuidora_id` )
    REFERENCES `tcc_gfx`.`distribuidoras` (`distribuidora_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `tcc_gfx`.`medidas`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`medidas` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`medidas` (
  `house_id` INT(11) NOT NULL ,
  `inicio_medida` TIMESTAMP NOT NULL,
  `intervalo_demanda` TINYINT(4) NOT NULL ,
  `consumo` DOUBLE NOT NULL ,
  `fator_potencia` DOUBLE NOT NULL ,
  `fatura_parcial_medida` DOUBLE NOT NULL ,
  PRIMARY KEY (`house_id`, `inicio_medida`) ,
  CONSTRAINT `FK_medida_pertence_a`
    FOREIGN KEY (`house_id` )
    REFERENCES `tcc_gfx`.`residencias` (`house_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `tcc_gfx`.`medidas_dia`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`medidas_dia` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`medidas_dia` (
  `house_id` INT(11) NOT NULL ,
  `dia_medida` DATE NOT NULL ,
  `consumo` DOUBLE NOT NULL ,
  `fator_potencia_dia` DOUBLE NOT NULL ,
  `fatura_parcial_dia` DOUBLE NOT NULL ,
  `soma_intervalos` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`house_id`, `dia_medida`) ,
  CONSTRAINT `FK_dia_pertence_a`
    FOREIGN KEY (`house_id` )
    REFERENCES `tcc_gfx`.`residencias` (`house_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;


-- -----------------------------------------------------
-- Table `tcc_gfx`.`medidas_mes`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tcc_gfx`.`medidas_mes` ;

CREATE  TABLE IF NOT EXISTS `tcc_gfx`.`medidas_mes` (
  `house_id` INT(11) NOT NULL ,
  `mes_medida` DATE NOT NULL ,
  `consumo` DOUBLE NOT NULL ,
  `fator_potencia_mes` DOUBLE NOT NULL ,
  `fatura_mes` DOUBLE NOT NULL ,
  `soma_intervalos` INT(11) NULL DEFAULT NULL ,
  PRIMARY KEY (`house_id`, `mes_medida`) ,
  CONSTRAINT `FK_mes_pertence_a`
    FOREIGN KEY (`house_id` )
    REFERENCES `tcc_gfx`.`residencias` (`house_id` )
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COLLATE = utf8_unicode_ci;

USE `tcc_gfx`;

DELIMITER $$

USE `tcc_gfx`$$
DROP TRIGGER IF EXISTS `tcc_gfx`.`after_insert_medidas` $$
USE `tcc_gfx`$$
CREATE
DEFINER=`root`@`localhost`
TRIGGER `tcc_gfx`.`after_insert_medidas`
AFTER INSERT ON `tcc_gfx`.`medidas`
FOR EACH ROW
BEGIN  
		INSERT INTO medidas_dia (
			`house_id`,
			`dia_medida`,
			`consumo`,
			`fator_potencia_dia`,
			`fatura_parcial_dia`,
			`soma_intervalos`)  
		VALUES (
			NEW.house_id,
			DATE(NEW.inicio_medida), 
			NEW.consumo*NEW.intervalo_demanda*NEW.fator_potencia/'60',
			NEW.fator_potencia,
			NEW.fatura_parcial_medida,
			NEW.intervalo_demanda
		)
		ON DUPLICATE KEY update
		fator_potencia_dia = (fator_potencia_dia*soma_intervalos + NEW.fator_potencia*NEW.intervalo_demanda)/(soma_intervalos + NEW.intervalo_demanda),
		soma_intervalos = soma_intervalos + NEW.intervalo_demanda,
		consumo = consumo + NEW.consumo*NEW.intervalo_demanda/'60',
		fatura_parcial_dia = fatura_parcial_dia + NEW.fatura_parcial_medida;
		
	INSERT INTO medidas_mes (
			`house_id`,
			`mes_medida`,
			`consumo`,
			`fator_potencia_mes`,
			`fatura_mes`,
			`soma_intervalos`)  
		VALUES (
			NEW.house_id,
			LAST_DAY(DATE(NEW.inicio_medida)), 
			NEW.consumo*NEW.intervalo_demanda*NEW.fator_potencia/'60',
			NEW.fator_potencia,
			NEW.fatura_parcial_medida,
			NEW.intervalo_demanda
		)
		ON DUPLICATE KEY update
		consumo = consumo + NEW.consumo*NEW.intervalo_demanda/'60',
		soma_intervalos = soma_intervalos + NEW.intervalo_demanda,
		fator_potencia_mes = (fator_potencia_mes*soma_intervalos + NEW.fator_potencia*NEW.intervalo_demanda)/(soma_intervalos + NEW.intervalo_demanda),
		fatura_mes = fatura_mes + NEW.fatura_parcial_medida;
    END$$


DELIMITER ;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
