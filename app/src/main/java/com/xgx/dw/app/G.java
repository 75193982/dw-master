package com.xgx.dw.app;

import android.annotation.SuppressLint;

import com.xgx.dw.utils.SdCardUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * 全局变量存储类
 *
 * @author Ht
 */
public class G {

    /**
     * 应用程序名
     */
    public static final String APPNAME = "Dw";
    public static final String depRole = "9";
    public static final String taiquRole = "10";
    public static final String adminRole = "1";
    public static final String userRole = "20";
    public static final String testRole = "30";
    public static final String test1Role = "31";
    public static final String test2Role = "32";
    /**
     * 文件根目录
     */
    public static final String STORAGEPATH = SdCardUtil.getNormalSDCardPath() + "/" + APPNAME + "/";

    /**
     * 自动更新文件下载路径
     */
    public static final String UPDATE_APP_SAVE_PATH = STORAGEPATH + APPNAME + ".apk";
    /**
     * 系统图片
     */
    public static final String APPIMAGE = STORAGEPATH + "img/";
    /**
     * 录音文件保存路径
     */
    public static final String APPRECORD = STORAGEPATH + "record/";

    /**
     * 调用拍照request code
     */
    public static final int ACTION_CAMERA = 0x01;
    /**
     * 调用相册request code
     */
    public static final int ACTION_ALBUM = 0x02;
    /**
     * 打开扫码request code
     */
    public static final int ACTION_BARCODE = 0x03;

    /**
     * 打开录音request code
     */
    public static final int ACTION_RECORDER = 0x04;

    /**
     * 打开通讯录request code
     */
    public static final int ACTION_ADDRESSLIST = 0x05;

    /**
     * 当前姓名偏好设置
     */
    public static final String currentUsername = "currentUsername";
    /**
     * 当前姓名偏好设置
     */
    public static final String currentPassword = "currentPassword";
    /**
     * 当前用户类型
     */
    public static final String currentUserType = "currentUserType";
    /**
     * 当前用户营业厅ID
     */
    public static final String currentStoreId = "currentStoreId";
    /**
     * 当前用户营业厅NAME
     */
    public static final String currentStoreName = "currentStoreName";
    /**
     * 当前用户台区ID
     */
    public static final String currentTransformId = "currentTransformId";
    /**
     * 当前用户台区NAME
     */
    public static final String currentTransformName = "currentTransformName";
    /**
     * 当前用户台区NAME
     */
    public static final String currentUserLoginPhone = "currentUserLoginPhone";
    /**
     * 购电单号
     */
    public static final String currentOrderId = "currentOrderId";
    public static String appsecret = "1396198677119910";

    @SuppressLint("SimpleDateFormat")
    public static String getPhoneCurrentTime() {
        SimpleDateFormat date = new SimpleDateFormat("yyyyMMddHHmmss");
        return date.format(Calendar.getInstance().getTime());
    }

}
