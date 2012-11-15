##
##adiciona todas as Foreign Keys do Schema relativos a medidas
##
use TCC_GFX;
ALTER TABLE medidas_dia 
ADD CONSTRAINT FK_collected_for
FOREIGN KEY (user_id) REFERENCES medidas(user_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;

ALTER TABLE medidas_dia 
ADD CONSTRAINT FK_collected_at
FOREIGN KEY (house_id) REFERENCES medidas(house_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;

ALTER TABLE medidas_mes 
ADD CONSTRAINT FK_gathered_for
FOREIGN KEY (user_id) REFERENCES medidas_dia(user_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;

ALTER TABLE medidas_mes 
ADD CONSTRAINT FK_gathered_at
FOREIGN KEY (house_id) REFERENCES medidas_dia(house_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;