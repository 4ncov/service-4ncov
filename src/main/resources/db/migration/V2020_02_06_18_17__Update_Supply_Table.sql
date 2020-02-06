alter table material_supplied
drop column material_supplied_picture_url_1;

alter table material_supplied
drop column material_supplied_picture_url_2;

alter table material_supplied
drop column material_supplied_picture_url_3;

alter table material_supplied
add column material_supplied_image_urls text;

alter table material_supplied
drop foreign key fk_material_supplied_region_code_id;

alter table material_supplied
drop column material_supplied_region_code;

alter table material_supplied
change column material_supplied_count material_supplied_quantity double not null comment '物资供应数量, 可为小数';

alter table material_supplied
change column material_supplied_status material_supplied_status varchar(50) not null comment '提供物资的状态：\n1 未匹配\n2 处理中（例如已经对接上）\n3 已完成（所有物质都已经发送完成）';