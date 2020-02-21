CREATE TABLE IF NOT EXISTS contact_us (
  `id`  bigint(10) UNSIGNED NOT NULL AUTO_INCREMENT ,
  `content`  varchar(200) NULL COMMENT '联系内容' ,
  `user_phone`  varchar(20) NULL COMMENT '用户手机' ,
  `gmt_created`  datetime NOT NULL ,
  `gmt_modified`  datetime NULL DEFAULT NULL ,
  `deleted`  tinyint(1) NULL DEFAULT 0 ,
  PRIMARY KEY (`id`)
)
;

