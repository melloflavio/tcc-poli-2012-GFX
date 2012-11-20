use TCC_GFX;

ALTER TABLE medidas_dia 
DROP FOREIGN KEY FK_collected_for;

ALTER TABLE medidas_dia 
DROP FOREIGN KEY FK_collected_at;

ALTER TABLE medidas_mes 
DROP FOREIGN KEY FK_gathered_for;

ALTER TABLE medidas_mes 
DROP FOREIGN KEY FK_gathered_at;