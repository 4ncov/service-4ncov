ALTER TABLE `material_required`
DROP COLUMN `material_required_picture_url_1`,
DROP COLUMN `material_required_picture_url_2`,
DROP COLUMN `material_required_picture_url_3`,
ADD COLUMN `material_supplied_image_urls` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `gmt_modified`,
ADD COLUMN `material_supplied_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物资名称' AFTER `material_supplied_image_urls`,
ADD COLUMN `material_supplied_category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '物资类别' AFTER `material_supplied_name`,
ADD COLUMN `material_supplied_standard` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '执行标准' AFTER `material_supplied_category`,
ADD COLUMN `material_supplied_organization_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '供货方机构名称' AFTER `material_supplied_standard`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE;

SET FOREIGN_KEY_CHECKS=0;

ALTER TABLE `material_required`
MODIFY COLUMN `id` bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT FIRST;

SET FOREIGN_KEY_CHECKS=1;