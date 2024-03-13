create table users(
    id bigint not null auto_increment,
    username varchar(100) not null unique,
    password varchar(255) not null,
    email varchar(100) not null,
    member_since date not null,
    first_name varchar(100),
    last_name varchar(100),
    description varchar(255),
    gender enum('F', 'M'),
    birthday date,
    active tinyint not null,
    primary key(id)
);
