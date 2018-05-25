package com.xgx.dw.net;

import com.alibaba.fastjson.JSON;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.Setting;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.utils.Base64SecurityAction;
import com.xgx.dw.utils.BaseTransferEntity;
import com.xgx.dw.utils.MD5Util;

/**
 * URL路径处理类
 *
 * @author Ht
 */
public class URLs {
    // public static final String HOST = "http://192.168.100.9:8089/";
    public static final String HOST = "http://221.176.209.130:8089/";
    public static final String PROJECT_NAME = "";
    public static final String API = "";

    // 归属地查询
    public static final String TEST_API = "get";

    // 用户模块
    /**
     * 第三方登陆
     */
    public static final String OAUTH_SIGNIN = "xxx";
    public static final String UPDATE_VERSION = "mtzApkFile/apkVersionCompare";
    public static final String USER_SIGNIN = "auth";
    public static final String COUNTY_LIST = "sysDept/list"; //营业厅列表
    public static final String COUNTY_SAVE = "sysDept/addOrUpdate"; //保存或者编辑营业厅

    public static final String PRICE_LIST = "mtzPrice/priceList";//电价列表
    public static final String PRICE_INFO = "mtzPrice/getPriceById";//电价详情
    public static final String PRICE_SAVE = "mtzPrice/save";//保存或者编辑电价列表

    public static final String TAIQU_LIST = "mtzTaiqu/taiquList";//台区列表
    public static final String TAIQU_SAVE = "mtzTaiqu/save";//保存或者编辑台区列表
    public static final String MTUSER_LIST = "mtzUser/mtzUserList"; //用户资料管理
    public static final String MTUSER_SAVE = "mtzUser/save"; //用户资料保存
    public static final String MTUSER_CODE = "mtzUser/getCode"; //获取验证码
    public static final String BUY_SPOT = "mtzPurchase/save"; //保存电费单
    public static final String BUY_SPOT_CHANGE_STATUS = "mtzPurchase/changeStatus"; //保存电费单
    public static final String BUY_SPOT_LIST = "mtzPurchase/purchaseList"; //保存电费单
    public static final String OPLOG_SAVE = "mtzOplog/save"; //保存操作日志
    public static final String RECORD_LIST = "recordFront/recordFrontList"; //电量报表

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

    public static String getRequstJsonString(Object obj) {
        String json = JSON.toJSONString(obj);
        String encode = new Base64SecurityAction().doAction(json);
        // md5签名
        String encrypt = MD5Util.encrypt(encode + new Setting(BaseApplication.getInstance()).loadString("randomKey"));
        BaseTransferEntity baseTransferEntity = new BaseTransferEntity();
        baseTransferEntity.setSign(encrypt);
        baseTransferEntity.setObject(encode);
        return JSON.toJSONString(baseTransferEntity);
    }
}
