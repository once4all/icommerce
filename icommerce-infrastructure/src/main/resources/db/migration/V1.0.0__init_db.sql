create database icommerce;

set search_path = icommerce;

create sequence category_seq;
create sequence brand_seq;
create sequence colour_seq;
create sequence product_seq;
create sequence user_information_seq;
create sequence cart_seq;
create sequence cart_item_seq;
create sequence booking_seq;
create sequence booking_item_seq;

drop table if exists category;
create table category
(
    id     int primary key,
    name   varchar(2000),
    active boolean
);

drop table if exists brand;
create table brand
(
    id     int primary key,
    name   varchar(2000),
    active boolean
);

drop table if exists colour;
create table colour
(
    id     int primary key,
    name   varchar(2000),
    active boolean
);

drop table if exists user_information;
create table user_information
(
    id            int primary key,
    name          varchar(50)  not null,
    username      varchar(20)  not null,
    password      varchar(500) not null,
    role          varchar(20)  not null,
    refresh_token varchar(1000),
    active        boolean
);

drop table if exists product;
create table product
(
    id            int primary key,
    code          varchar(100),
    name          varchar(1000) not null,
    description   text,
    price         numeric       not null,
    status        varchar(20)   not null,
    created_date  timestamp     not null,
    modified_date timestamp
);

drop table if exists product_brand;
create table product_brand
(
    product_id int not null references product (id),
    brand_id   int not null references brand (id),
    active     boolean,
    primary key (product_id, brand_id)
);

drop table if exists product_category;
create table product_category
(
    product_id  int not null references product (id),
    category_id int not null references category (id),
    active      boolean,
    primary key (product_id, category_id)
);

drop table if exists product_colour;
create table product_colour
(
    product_id int not null references product (id),
    colour_id  int not null references colour (id),
    active     boolean,
    primary key (product_id, colour_id)
);

drop table if exists product_image;
create table product_image
(
    product_id int not null references product (id),
    image_url  varchar(1000),
    active     boolean
);


drop table if exists product_attribute;
create table product_attribute
(
    product_id      int not null references product (id),
    attribute_key   varchar(1000),
    attribute_value varchar(1000),
    active          boolean,
    primary key (product_id, attribute_key)
);

drop table if exists cart;
create table cart
(
    id      int primary key,
    user_id int         not null references user_information (id),
    status  varchar(20) not null
);

drop table if exists cart_item;
create table cart_item
(
    id         int primary key,
    cart_id    int not null references cart (id),
    product_id int not null references product (id),
    colour_id  int references colour (id),
    quantity   int not null
);

drop table if exists booking;
create table booking
(
    id           int primary key,
    cart_id      int     not null references cart (id),
    user_id      int     not null references user_information (id),
    total_price  numeric not null,
    created_date timestamp
);

drop table if exists booking_item;
create table booking_item
(
    id          int primary key,
    booking_id  int     not null references booking (id),
    product_id  int     not null references product (id),
    colour_id   int references colour (id),
    quantity    int     not null,
    total_price numeric not null
);
--
insert into user_information(id, name, username, password, role, active)
values (nextval('user_information_seq'), 'User test', 'test',
        '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'USER', true);
--
insert into category(id, name, active)
values (nextval('category_seq'), 'Car', true);
insert into category(id, name, active)
values (nextval('category_seq'), 'TV', true);
insert into category(id, name, active)
values (nextval('category_seq'), 'Phone', true);
--
insert into brand(id, name, active)
values (nextval('brand_seq'), 'BMW', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Mercedes', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Toyota', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Honda', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Hyundai', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Kia', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Sony', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'LG', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Samsung', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Apple', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'Xiaomi', true);
insert into brand(id, name, active)
values (nextval('brand_seq'), 'HTC', true);
--
insert into colour(id, name, active)
values (nextval('colour_seq'), 'Black', true);
insert into colour(id, name, active)
values (nextval('colour_seq'), 'White', true);
insert into colour(id, name, active)
values (nextval('colour_seq'), 'Blue', true);
insert into colour(id, name, active)
values (nextval('colour_seq'), 'Red', true);
insert into colour(id, name, active)
values (nextval('colour_seq'), 'Green', true);
--
do
$$
    declare
        product_id  int;
        brand_id    int;
        category_id int;
        colour_id   int;
    begin
        product_id := (select nextval('product_seq'));
        insert into product(id, code, name, description, price, status, created_date, modified_date)
        VALUES (product_id, null, 'BMW 320i', null, 100000, 'ACTIVE', current_timestamp, null);
        --
        category_id := (select * from category where name = 'Car');
        insert into product_category(product_id, category_id, active) VALUES (product_id, category_id, true);
        --
        brand_id := (select * from brand where name = 'BMW');
        insert into product_brand(product_id, brand_id, active) VALUES (product_id, brand_id, true);
        --
        colour_id := (select * from colour where name = 'Black');
        insert into product_colour(product_id, colour_id, active) VALUES (product_id, colour_id, true);
        colour_id := (select * from colour where name = 'White');
        insert into product_colour(product_id, colour_id, active) VALUES (product_id, colour_id, true);
    end
$$;
