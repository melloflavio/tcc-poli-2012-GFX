use TCC_GFX;

## Remover Restrições

ALTER TABLE medidas_dia 
DROP FOREIGN KEY FK_collected_for;

ALTER TABLE medidas_dia 
DROP FOREIGN KEY FK_collected_at;

ALTER TABLE medidas_mes 
DROP FOREIGN KEY FK_gathered_for;

ALTER TABLE medidas_mes 
DROP FOREIGN KEY FK_gathered_at;

#remover todas as tabelas

DROP TABLE IF EXISTS medidas_mes ;

DROP TABLE IF EXISTS medidas_dia;

DROP TABLE IF EXISTS medidas;

DROP TABLE IF EXISTS residencias;

DROP TABLE IF EXISTS distribuidoras;

DROP TABLE IF EXISTS feriados;

DROP TABLE IF EXISTS users;