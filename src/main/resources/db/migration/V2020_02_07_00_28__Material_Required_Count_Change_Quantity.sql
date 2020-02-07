ALTER TABLE `material_required` DROP FOREIGN KEY `fk_material_required_region_code_id`;

ALTER TABLE `material_required`
DROP COLUMN `material_required_region_code`,
CHANGE COLUMN `material_required_count` `material_required_quantity` double NOT NULL AFTER `material_required_received_address`,
MODIFY COLUMN `material_id` bigint(10) UNSIGNED NULL COMMENT '所属物料ID' AFTER `material_required_quantity`;