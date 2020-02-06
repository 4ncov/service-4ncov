SET FOREIGN_KEY_CHECKS=0;

alter table material_supplied
change material_id material_id bigint(10) unsigned DEFAULT NULL COMMENT '对应物料的ID';

SET FOREIGN_KEY_CHECKS=1;

alter table material_supplied
add column material_supplied_name varchar(100) not null comment '物资名称';

alter table material_supplied
add column material_supplied_category varchar(50) not null comment '物资类别';

alter table material_supplied
add column material_supplied_standard varchar(100) not null comment '执行标准';