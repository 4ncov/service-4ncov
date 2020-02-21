alter table material_required
add column country varchar(100) default null,
add column province varchar(100) default null,
add column city varchar(100) default null,
add column district varchar(100) default null,
change column material_required_received_address street_address varchar(100) default null;

alter table material_supplied
add column country varchar(100) default null,
add column province varchar(100) default null,
add column city varchar(100) default null,
add column district varchar(100) default null,
change column material_supplied_address street_address varchar(100) default null;
