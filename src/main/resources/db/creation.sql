create table positions (
    id mediumint not null auto_increment,
    latitude varchar(255),
    longitude varchar(255),
    chatid int,
    userid varchar(255),
    timezone varchar(100),
    user_timestamp varchar(255),
    vehicle_id mediumint not null,
    vehicle_name varchar(255),
    primary key (id)
);

