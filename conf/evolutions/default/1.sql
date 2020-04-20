# --- !Ups
create table people(
    id  serial primary key,
    name text not null,
    mail text not null,
    tel text
);
insert into people (name, mail, tel)values('Terry', 'terry@letitride.jp', '000-0000-0000');
insert into people (name, mail, tel)values('Mike', 'mike@letitride.jp', '111-1111-1111');
insert into people (name, mail, tel)values('Mika', 'mika@letitride.jp', '222-2222-2222');

# --- !Downs
drop table people;