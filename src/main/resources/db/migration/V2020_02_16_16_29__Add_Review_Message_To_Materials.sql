alter table material_required
add column review_message varchar(500) default null;

alter table material_supplied
add column review_message varchar(500) default null;