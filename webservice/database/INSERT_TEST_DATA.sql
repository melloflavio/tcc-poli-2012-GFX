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

INSERT INTO `users` (`user_id`, `email`, `password`) VALUES
(1, '@', 'qwerty'),
(2, '@2', 'qwerty');

INSERT INTO `distribuidoras`(`distribuidora_id`, `sigla_distribuidora`, `razao_social`, `date_from`, `date_to`, `tarifa_convencional`, `tarifa_ponta`, `tarifa_foraponta`, `tarifa_intermediaria`, `cidade`, `estado`) VALUES (1,' ELETROPAULO','Eletropaulo Metropolitana Eletricidade de São Paulo S/A',date('2010-07-04'),date('2013-07-04'),0.29114,0.472416,0.274660,0.373538,'Sao Paulo','SP');

INSERT INTO `residencias` (`house_id`, `nome_casa`, `logradouro`, `cidade`, `estado`, `user_id`,`tipo_tarifa`,`distribuidora_id`) VALUES
(1, 'Casa Uno', 'Alberto Faria, 1946', 'Sao Paulo', 'SP', 1, 1, 1),
(2, 'Maloca Due', 'Arruda Botelho', 'Sobradinho', 'MG', 1, 1, 1),
(3, 'Saudosa Maloca', 'Foi Aqui Seu Moço', 'Sampa', 'SP', 2, 1, 1);

