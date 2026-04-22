--Жанры
merge into genres g
using (values 
    ('Комедия'),
    ('Драма'),
    ('Мультфильм'),
    ('Триллер'),
    ('Документальный'),
    ('Боевик')
) as data(name)
on g.name = data.name
when not matched then
    insert (name) values (data.name);

--Рейтинги
merge into mpa m
using (values 
    ('G'),
    ('PG'),
    ('PG-13'),
    ('R'),
    ('NC-17')
) as data(name)
on m.name = data.name
when not matched then
    insert (name) values (data.name);
