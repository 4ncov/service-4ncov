ALTER TABLE `4ncov`.`material_supplier_info`
ADD COLUMN `logo` text NULL COMMENT '供应商logo' AFTER `material_supplier_verify_image_urls`;

ALTER TABLE `4ncov`.`hospital_info`
ADD COLUMN `logo` text NULL AFTER `hospital_contactor_telephone`;