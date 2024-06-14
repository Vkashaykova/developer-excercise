INSERT INTO `products` (product_name,price, currency) VALUES ('apple',50,'c'),
                                                            ('banana',40,'c'),
                                                            ('tomato',30,'c'),
                                                            ('potato',26,'c');

INSERT INTO orders (total_price, timestamp) VALUES (199, NOW());

INSERT INTO orders_items (order_id, product_id)  VALUES (1, 1),
                                                            (1, 2),
                                                            (1, 2),
                                                            (1, 4),
                                                            (1, 3),
                                                            (1, 2),
                                                            (1, 4);


