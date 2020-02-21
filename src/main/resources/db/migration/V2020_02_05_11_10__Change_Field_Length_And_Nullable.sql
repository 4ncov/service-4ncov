alter table user_info
change column user_nick_name user_nick_name varchar(50) not null;

alter table user_info
change column user_salt user_salt text(256)
COMMENT '目前暂时用不到，因为不需要设置密码，直接和手机号进行关联\n密码使用的随机salt，创建时CSPRNG 生成的随机数。';

alter table user_info
change column user_password_SHA256 user_password_SHA256 text(256);

alter table hospital_info
change column gmt_modified gmt_modified datetime default null;