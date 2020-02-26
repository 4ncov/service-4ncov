ALTER TABLE `material_supplier_info`
ADD COLUMN `logo` text NULL COMMENT '供应商logo' AFTER `material_supplier_verify_image_urls`;

ALTER TABLE `hospital_info`
ADD COLUMN `logo` text NULL AFTER `hospital_contactor_telephone`;
