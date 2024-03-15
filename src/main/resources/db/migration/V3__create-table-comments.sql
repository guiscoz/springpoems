create table comments (
  id bigint not null auto_increment,
  primary key (id),
  content text not null,
  created_at datetime not null,
  last_update datetime not null,
  user_id bigint not null,
  foreign key (user_id) references users (id),
  poem_id bigint not null,
  foreign key (poem_id) references poems (id),
  active boolean not null
);