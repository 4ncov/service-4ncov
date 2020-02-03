CREATE TABLE IF NOT EXISTS `user_role` (
  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '角色ID，目前应该有：\n\n1. 管理员\n2. 供应商\n3. 需求方',
  `user_role_desc` VARCHAR(45) NOT NULL COMMENT '角色描述',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '系统中用户角色，目前包括（ID对应）\n\n1. 系统管理员\n2. 物资供应商\n3. 医院负责人\n\n暂时没有考虑普通个人用户';

CREATE TABLE IF NOT EXISTS `user_info` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_nick_name` VARCHAR(10) NOT NULL,
  `user_salt` TEXT(256) NOT NULL COMMENT '目前暂时用不到，因为不需要设置密码，直接和手机号进行关联\n密码使用的随机salt，创建时CSPRNG 生成的随机数。',
  `user_password_SHA256` TEXT(256) NOT NULL COMMENT '目前暂时用不到，因为不需要设置密码，直接和手机号进行关联',
  `gmt_created` VARCHAR(45) NOT NULL,
  `gmt_modified` VARCHAR(45) NULL,
  `user_phone` VARCHAR(20) NOT NULL,
  `user_role_id` INT UNSIGNED NOT NULL COMMENT '用户角色ID',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_user_info_user_role_id`
    FOREIGN KEY (`user_role_id`)
    REFERENCES `user_role` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

CREATE UNIQUE INDEX `user_nick_name_UNIQUE` ON `user_info` (`user_nick_name` ASC);

CREATE TABLE IF NOT EXISTS `hospital_info` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `hospital_name` VARCHAR(200) NOT NULL,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NOT NULL,
  `hospital_uniform_social_credit_code` CHAR(18) NOT NULL,
  `hospital_creator_user_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '该医院负责用户在user_info表中的id,一个医院应该只有一个负责的用户',
  `hospital_detail_address` VARCHAR(100) NOT NULL,
  `hospital_address_code` CHAR(6) NOT NULL COMMENT '六位县级以上区划代码，参见http://www.mca.gov.cn/article/sj/xzqh/2019/2019/201912251506.html',
  `hospital_verify_info_url_1` VARCHAR(512) NOT NULL COMMENT '医院用户应该提供若干用于证明自己能够代表医院的材料，例如单位公章等',
  `hospital_verify_info_url_2` VARCHAR(512) NULL,
  `hospital_verify_info_url_3` VARCHAR(512) NULL,
  `hospital_contactor_name` VARCHAR(45) NOT NULL,
  `hospital_contactor_telephone` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_hospital_info_creator_user_id`
    FOREIGN KEY (`hospital_creator_user_id`)
    REFERENCES `user_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '医院的基本信息，目前设计上只允许一个医院关联一个用户，这是为了避免进一步内部的浪费。\n当然一个用户可以有多个联系人和联系方式';

CREATE UNIQUE INDEX `hospital_organizing_instituion_bar_code_UNIQUE` ON `hospital_info` (`hospital_uniform_social_credit_code` ASC);

CREATE TABLE IF NOT EXISTS `material_supplier_info` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NULL,
  `material_supplier_name` VARCHAR(45) NOT NULL,
  `material_supplier_contactor_name` VARCHAR(45) NOT NULL,
  `material_supplier_uniform_social_credit_code` CHAR(18) NOT NULL,
  `material_supplier_company_address` VARCHAR(100) NOT NULL,
  `material_supplier_verify_info_url_1` VARCHAR(512) NOT NULL,
  `material_supplier_verify_info_url_2` VARCHAR(512) NULL,
  `material_supplier_verify_info_url_3` VARCHAR(512) NULL,
  `material_supplier_creator_user_id` BIGINT(10) UNSIGNED NOT NULL,
  `material_supplier_contactor_phone` VARCHAR(45) NULL,
  `have_logistics` TINYINT NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_material_supplier_info_user_id`
    FOREIGN KEY (`material_supplier_creator_user_id`)
    REFERENCES `user_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '供应商基本信息';

CREATE TABLE IF NOT EXISTS `accepted_materials` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `accepted_material_name` VARCHAR(45) NOT NULL COMMENT '物资名，例如一次性口罩，核酸试剂',
  `accepted_material_standard_name` VARCHAR(45) NOT NULL COMMENT '物资满足的标准名称',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '平台目前能够接受处理的物资列表';

CREATE TABLE IF NOT EXISTS `region_code` (
  `id` CHAR(6) NOT NULL COMMENT '6位区划ID',
  `region_name` VARCHAR(45) BINARY NOT NULL COMMENT '行政区划名称，例如“北京市”',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
COMMENT = '国家规定的行政区划代码表格，方便区域统计用\nhttp://www.mca.gov.cn/article/sj/xzqh/2019/';

CREATE TABLE IF NOT EXISTS `material_supplied` (
  `id` BIGINT(10) NOT NULL,
  `material_supplied_contactor_name` VARCHAR(45) NOT NULL COMMENT '物资联系人',
  `material_supplied_contactor_phone` VARCHAR(20) NOT NULL,
  `material_supplied_address` VARCHAR(200) NOT NULL,
  `material_supplied_region_code` CHAR(6) NOT NULL COMMENT '物资所在区域代码',
  `material_supplied_count` BIGINT(15) NOT NULL COMMENT '物资供应数量，暂时没有设定各种单位',
  `material_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '对应物料的ID',
  `material_supplier_organization_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '物资对应的供应商ID',
  `material_supplied_user_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '信息发布用户ID',
  `material_supplied_comment` VARCHAR(200) NULL COMMENT '物资信息备注',
  `material_supplied_picture_url_1` VARCHAR(512) NULL COMMENT '物资图片',
  `material_supplied_picture_url_2` VARCHAR(512) NULL,
  `material_supplied_picture_url_3` VARCHAR(512) NULL,
  `material_supplied_status` CHAR NOT NULL COMMENT '提供物资的状态：\n1 未匹配\n2 处理中（例如已经对接上）\n3 已完成（所有物质都已经发送完成）',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_material_supplied_supplier_info_id`
    FOREIGN KEY (`material_supplier_organization_id`)
    REFERENCES `material_supplier_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_supplied_user_info_id`
    FOREIGN KEY (`material_supplied_user_id`)
    REFERENCES `user_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_supplied_material_id`
    FOREIGN KEY (`material_id`)
    REFERENCES `accepted_materials` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_supplied_region_code_id`
    FOREIGN KEY (`material_supplied_region_code`)
    REFERENCES `region_code` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '一条来自供应商的信息，可以被分拆给多个用户';

CREATE TABLE IF NOT EXISTS `material_required` (
  `id` BIGINT(10) UNSIGNED NOT NULL,
  `material_required_contactor_name` VARCHAR(45) NOT NULL,
  `material_required_contactor_phone` VARCHAR(45) NOT NULL,
  `material_required_received_address` VARCHAR(100) NOT NULL,
  `material_required_region_code` CHAR(6) NOT NULL,
  `material_required_count` VARCHAR(45) NOT NULL,
  `material_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '所属物料ID',
  `material_required_organization_id` BIGINT(10) UNSIGNED NOT NULL,
  `material_required_user_id` BIGINT(10) UNSIGNED NOT NULL,
  `material_required_comment` VARCHAR(200) NULL,
  `material_required_picture_url_1` VARCHAR(512) NULL,
  `material_required_picture_url_2` VARCHAR(512) NULL,
  `material_required_picture_url_3` VARCHAR(512) NULL,
  `material_required_status` CHAR NOT NULL COMMENT '物资需求的状态：\n1 未匹配\n2 处理中（例如已经对接上）\n3 已完成（所有物质都已经满足）',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_material_required_user_info_id`
    FOREIGN KEY (`material_required_user_id`)
    REFERENCES `user_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_required_accepted_materials_id`
    FOREIGN KEY (`material_id`)
    REFERENCES `accepted_materials` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_required_hospital_info_id`
    FOREIGN KEY (`material_required_organization_id`)
    REFERENCES `hospital_info` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_material_required_region_code_id`
    FOREIGN KEY (`material_required_region_code`)
    REFERENCES `region_code` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '医院发布的需求，由用户发布，可以被多个供应商满足';

CREATE TABLE IF NOT EXISTS `require_supply_match_info` (
  `id` BIGINT(10) UNSIGNED NOT NULL AUTO_INCREMENT,
  `material_supplied_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '对应的物资供给表中的ID',
  `material_required_id` BIGINT(10) UNSIGNED NOT NULL COMMENT '对应的物资需求表中的ID',
  `material_matched_count` BIGINT(20) UNSIGNED NOT NULL COMMENT '物资被满足的数量',
  `gmt_created` DATETIME NOT NULL,
  `gmt_modified` DATETIME NULL,
  `require_supply_match_status` CHAR NOT NULL COMMENT '状态：\n1. 供应商尚未确认\n2. 供应商已经确认\n3. 供应商已经发货\n4. 需求方确认收到',
  `transfer_tracking_number` VARCHAR(45) NULL COMMENT '运输单号，例如快递单',
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_require_supply_match_info_material_required_id`
    FOREIGN KEY (`material_required_id`)
    REFERENCES `material_required` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_require_supply_match_info_material_supplied_id`
    FOREIGN KEY (`material_supplied_id`)
    REFERENCES `material_supplied` (`material_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
COMMENT = '一个供应商和需求的匹配，理论上来说，一个供应商发布的一批物资可以被多个寻求方匹配，一个需求也可以由来自多个供应商的物资满足';
