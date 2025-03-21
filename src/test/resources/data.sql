INSERT INTO INGREDIENT (nom, quantite) VALUES
('Tomate', 13),
('OLive',8),
('Fromage',90);

INSERT INTO CLIENT (nom, email) VALUES
('Stiv', 'stiv@tut.by'),
('Masha', 'masha@tut.by');

INSERT INTO PIZZA (nom) VALUES
('Margherita'),
('Reine'),
('4 fromages');

INSERT INTO PIZZA_INGREDIENTS (ingredients_id, pizza_id) VALUES
(1, 1),
(3,1),
(2,2),
(3,2),
(3,3);