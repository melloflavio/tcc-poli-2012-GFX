use TCC_GFX;
ALTER TABLE medidas_mes 
ADD CONSTRAINT FK_gathered_for
FOREIGN KEY (user_id) REFERENCES medidas_dia(user_id)  
ON UPDATE CASCADE  
ON DELETE CASCADE;