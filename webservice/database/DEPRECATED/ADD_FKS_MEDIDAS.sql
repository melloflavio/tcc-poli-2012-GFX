##
##adiciona todas as Foreign Keys do Schema relativos a medidas
##
use TCC_GFX;

ALTER TABLE medidas 
DROP FOREIGN KEY FK_medido_at;

ALTER TABLE medidas_dia 
DROP FOREIGN KEY FK_collected_for;

ALTER TABLE medidas_dia 
DROP FOREIGN KEY FK_collected_at;

ALTER TABLE medidas_mes 
DROP FOREIGN KEY FK_gathered_for;

ALTER TABLE medidas_mes 
DROP FOREIGN KEY FK_gathered_at;

ALTER TABLE residencias 
DROP FOREIGN KEY FK_owner;

ALTER TABLE medidas 
ADD CONSTRAINT FK_medido_at
FOREIGN KEY (house_id) REFERENCES residencias(house_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;

-- ALTER TABLE medidas_dia 
-- ADD CONSTRAINT FK_collected_for
-- FOREIGN KEY (user_id) REFERENCES medidas(user_id)  
-- ON UPDATE CASCADE  
-- ON DELETE CASCADE;
-- 
ALTER TABLE medidas_dia 
ADD CONSTRAINT FK_collected_at
FOREIGN KEY (house_id) REFERENCES medidas(house_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;

-- ALTER TABLE medidas_mes 
-- ADD CONSTRAINT FK_gathered_for
-- FOREIGN KEY (user_id) REFERENCES medidas_dia(user_id)  
-- ON UPDATE CASCADE  
-- ON DELETE CASCADE;

ALTER TABLE medidas_mes 
ADD CONSTRAINT FK_gathered_at
FOREIGN KEY (house_id) REFERENCES medidas_dia(house_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;

ALTER TABLE residencias 
ADD CONSTRAINT FK_owner
FOREIGN KEY (user_id) REFERENCES users(user_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;
