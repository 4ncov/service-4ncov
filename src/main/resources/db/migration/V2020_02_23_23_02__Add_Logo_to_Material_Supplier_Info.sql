ALTER TABLE `4ncov`.`material_supplier_info`
ADD COLUMN `logo` text NULL COMMENT '供应商logo' AFTER `material_supplier_verify_image_urls`;

ALTER TABLE `4ncov`.`material_supplied`
ADD COLUMN `material_supplied_organization_logo` text NULL COMMENT '供货方机构logo' AFTER `material_supplied_organization_name`;

ALTER TABLE `4ncov`.`material_required`
ADD COLUMN `material_required_organization_logo` text NULL COMMENT '供货方机构logo' AFTER `material_required_organization_name`;