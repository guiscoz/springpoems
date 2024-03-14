create table poems (
  id bigint not null auto_increment,
  title varchar(255) not null,
  content text not null,
  created_at date not null,
  last_update date not null,
  author_id bigint not null,
  active boolean not null,
  primary key (id),
  foreign key (author_id) references users (id)
);