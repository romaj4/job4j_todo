create table item (
    id serial primary key,
    description text,
    created timestamp,
    done boolean default false
)