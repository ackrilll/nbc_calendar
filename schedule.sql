create table schedule
(
    id         bigint auto_increment
        primary key,
    todo       varchar(500) not null,
    charge     varchar(50)  not null,
    password   varchar(20)  not null,
    createDate datetime     null,
    updateDate datetime     null
);