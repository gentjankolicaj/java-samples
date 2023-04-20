-- liquibase formatted sql
-- changeset liquibase:2

create TABLE `liquibase_sample`.`product`(
    `id` BIGINT not null auto_increment, name varchar(50) not null, description varchar(100) not null,price decimal,  primary key(`id`));