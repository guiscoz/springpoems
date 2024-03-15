alter table poems
change column created_at created_at timestamp default current_timestamp();
alter table poems
change column last_update last_update timestamp default current_timestamp();