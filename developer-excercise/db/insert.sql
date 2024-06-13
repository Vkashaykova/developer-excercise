INSERT INTO `products` (product_type,price, currency) VALUES ('apple',0.50,'c'),
                                                            ('banana',0.40,'c'),
                                                            ('tomato',0.30,'c'),
                                                            ('potato',0.26,'c');

INSERT INTO orders (total_price, timestamp) VALUES (199, NOW());

INSERT INTO orders_items (order_id, product_id)  VALUES (1, 1),
                                                            (1, 2),
                                                            (1, 2),
                                                            (1, 4),
                                                            (1, 3),
                                                            (1, 2),
                                                            (1, 4);


