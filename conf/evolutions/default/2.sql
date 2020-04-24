# --- !Ups
create table people2(
    id  serial primary key,
    name text not null,
    mail text not null,
    tel text
);

# --- !Downs
drop table people2;