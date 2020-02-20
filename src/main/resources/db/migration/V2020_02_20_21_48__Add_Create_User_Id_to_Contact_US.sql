ALTER TABLE contact_us
  ADD COLUMN `create_user_id`  bigint(10) NULL COMMENT '创建用户id' AFTER `deleted`;

