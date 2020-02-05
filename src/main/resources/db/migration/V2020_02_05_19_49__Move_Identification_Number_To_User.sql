alter table user_info
add column user_identification_number char(18) not null;

alter table material_supplier_info
drop column material_supplier_identification_number;