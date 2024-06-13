create table orders
(
    order_id    int auto_increment
        primary key,
    total_price int      not null,
    timestamp   datetime not null
);

create table products
(
    product_id   int auto_increment
        primary key,
    product_type varchar(50)              not null,
    price        decimal(10, 2)           not null,
    currency     varchar(3) default 'asw' not null
);

create table orders_items
(
    id         int not null
        primary key,
    order_id   int not null,
    product_id int null,
    constraint orders_items_orders_order_id_fk
        foreign key (order_id) references orders (order_id),
    constraint orders_items_products_product_id_fk
        foreign key (product_id) references products (product_id)
);
