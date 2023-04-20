-- liquibase formatted sql
-- changeset liquibase:1

create TABLE `liquibase_sample`.`user`(
    `id` BIGINT not null auto_increment, name varchar(50) not null, surname varchar(100) not null, primary key(`id`));
