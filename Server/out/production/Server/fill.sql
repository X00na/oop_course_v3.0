INSERT INTO `users` (login, password, role)
VALUES ('admin', 'admin', true), ('user', 'user', false);

INSERT INTO `types` (name)
VALUES ('Бытовая техника'), ('Мобильная техника'), ('Крупногабаритная техника');

INSERT INTO `products` (name, info, count, price, date_create, type_id)
VALUES ('Дрель Bosch', 'Хорошая дрель', 11, 190, '2016-11-28 05:26:47', 1),
('Телефон LG G5', 'Отличный телефон', 25, 278, '2016-11-28 05:27:05', 2),
('Холодильник LG', 'Самая лучшая заморозка', 6, 650, '2016-11-28 05:27:26', 3);

INSERT INTO `orders` (count, date_order, info, product_id)
VALUES (1, '2016-11-28 15:12:55', 'первый заказ', 2);

/*INSERT INTO `products` (name, info, count, price, date_create, type_id)
SELECT 'name', 'info', 123, 123, '2016-10-10 12:10:10', (SELECT id FROM types WHERE name = 'Бытовая техника');

SELECT * FROM products;
SELECT * FROM orders;*/