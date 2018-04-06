package com.xgx.dw.net;

/**
 * URL路径处理类
 *
 * @author Ht
 */
public class URLs {
    public static final String HOST = "http://192.168.0.116:80/";
    // public static final String HOST = "http://221.176.209.130:8089/";
    public static final String PROJECT_NAME = "";
    public static final String API = "";

    // 归属地查询
    public static final String TEST_API = "get";

    // 用户模块
    /**
     * 第三方登陆
     */
    public static final String OAUTH_SIGNIN = "xxx";
    public static final String UPDATE_VERSION = "commons/apkVersionCompare.do";
    public static final String USER_SIGNIN = "auth";
    public static final String COUNTY_LIST = "mtzCounty/countyList"; //营业厅列表
    public static final String COUNTY_SAVE = "mtzCounty/save"; //保存或者编辑营业厅

    public static final String PRICE_LIST = "mtzPrice/priceList";//电价列表
    public static final String PRICE_SAVE = "mtzPrice/save";//保存或者编辑电价列表

    public static final String TAIQU_LIST = "mtzTaiqu/taiquList";//台区列表
    public static final String TAIQU_SAVE = "mtzTaiqu/save";//保存或者编辑台区列表
    public static final String MTUSER_LIST = "mtzUser/mtzUserList"; //用户资料管理
    public static final String MTUSER_SAVE = "mtzUser/save"; //用户资料保存

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
