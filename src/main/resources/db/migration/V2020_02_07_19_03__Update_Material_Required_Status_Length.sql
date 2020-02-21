alter table material_required
change column material_required_status material_required_status varchar(50) not null
comment '物资需求的状态：\n1 未匹配\n2 处理中（例如已经对接上）\n3 已完成（所有物质都已经满足）';