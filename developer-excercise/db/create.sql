create table `vegetable-fruit`
(
    `vegetable-fruit-id` int auto_increment
        primary key,
    name                 varchar(50)              not null,
    price                decimal (10,2)                 not null,
    currency             varchar(3) default 'asw' not null
);

create table orders
(
    order_id          int auto_increment
        primary key,
    order_number      int not null,
    `vegetable-fruit` int not null,
    constraint `orders_vegetable-fruit_vegetable-fruit-id_fk`
        foreign key (`vegetable-fruit`) references `vegetable-fruit` (`vegetable-fruit-id`)
);

