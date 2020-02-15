alter table material_required
add column deleted tinyint(1) default 0;

alter table material_supplied
add column deleted tinyint(1) default 0;

alter table hospital_info
add column deleted tinyint(1) default 0;

alter table material_supplier_info
add column deleted tinyint(1) default 0;

alter table user_info
add column `status` varchar(50) not null,
add column `deleted` tinyint(1) default 0;