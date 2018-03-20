package com.xgx.dw.net;

/**
 * URL路径处理类
 *
 * @author Ht
 */
public class URLs {
    //public static final String HOST = "http://221.176.209.130:80/";
    public static final String HOST = "http://192.168.100.5:80/";
    public static final String PROJECT_NAME = "";
    public static final String API = "dw/";

    // 归属地查询
    public static final String TEST_API = "get";

    // 用户模块
    /**
     * 第三方登陆
     */
    public static final String OAUTH_SIGNIN = "xxx";
    public static final String UPDATE_VERSION = "commons/apkVersionCompare.do";
    public static final String USER_SIGNIN = "auth";
    public static final String USER_SIGNUP = "xxx";

    /**
     * 拼接请求路径
     *
     * @PARAM URI
     * @RETURN
     */
    public static String getURL(String uri) {
        return HOST + PROJECT_NAME + API + uri;
    }

}
