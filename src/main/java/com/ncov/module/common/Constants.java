package com.ncov.module.common;

/**
 * @author JackJun
 * @date 2020/1/29 12:55 上午
 */
public class Constants {

    public static final String GLOBAL_UPLOAD_PATH = "D:\\UploadFile";
    public static final String GLOBAL_OTHER_UPLOAD_PATH = "/usr/data/upload";

    public static final String SPRING_PROFILE_DEVELOPMENT = "dev";
    public static final String SPRING_PROFILE_LOCAL = "local";

    public static final String SPRING_PROFILE_KEY = "spring.profiles.active";

    public enum ResponseProp {
        /**
         * prop:分页属性
         */
        PAGE("page"),
        /**
         * prop:数据属性
         */
        DATA("data");

        private String propName;

        ResponseProp(String prop) {
            this.propName = prop;
        }

        public String getPropName() {
            return propName;
        }
    }


}
