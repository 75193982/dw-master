package com.xgx.dw.ble;

import com.xgx.dw.UserBean;
import com.xgx.dw.app.G;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.utils.Logger;

/**
 * Created by Administrator on 2016/8/20.
 */
public class BlueOperationContact {
    public static String deviceId = "";
    //合闸 发送
    public static String HeZaSend = "68 8A 00 8A 00 68 6A " + deviceId + " 05 E0 01 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String HeZaSendTemp = "6A " + deviceId + " 05 E0 01 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";
    //分闸发送
    public static String TiaoZaSend = "68 8E 00 8E 00 68 4A " + deviceId + " 05 E1 01 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String TiaoZaSendTemp = "4A " + deviceId + " 05 E1 01 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";

    //保电投入 发送
    public static String BaoDianTrSend = "68 8E 00 8E 00 68 4A 00 00 FF FF 21 05 EB 00 00 01 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0C 25 36 17 04 05 E4 16";

    //保电解除 发送
    public static String BaoDianJcSend = "68 8A 00 8A 00 68 6A 00 00 FF FF 21 05 EC 00 00 01 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 40 37 17 04 05 23 16";
    //报警解除发送
    public static String BaoJingJcSend = "68 8A 00 8A 00 68 6A " + deviceId + " 05 E2 00 00 02 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String BaoJingJcSendTemp = "6A " + deviceId + " 05 E2 00 00 02 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";
    //电量查询 发送
    public static String DianLiangCxSend = "68 4A 00 4A 00 68 4B " + deviceId + " 0C E1 01 01 01 04 01 %s %s 16";
    public static String DianLiangCxSendTemp = "4B " + deviceId + " 0C E1 01 01 01 04 01 %s";
    //电压电流功率因数读取数据格式 发送
    public static String DianLvCxSend = "68 4A 00 4A 00 68 4B " + deviceId + " 0C E3 01 01 01 03 01 %s %s 16";
    public static String DianLvCxSendTemp = "4B " + deviceId + " 0C E3 01 01 01 03 01 %s";
    //电费查询发送
    public static String DianFeiCxSend = "68 4A 00 4A 00 68 4B " + deviceId + " 0C E3 01 01 40 02 01 %s %s 16";
    public static String DianFeiCxSendTemp = "4B " + deviceId + " 0C E3 01 01 40 02 01 %s";
    //读取倍率
    public static String BeiLvDuquSend = "68 4A 00 4A 00 68 6B " + deviceId + " 0A E0 01 01 01 03 01 %s %s 16";
    public static String BeiLvDuquSendTemp = "6B " + deviceId + " 0A E0 01 01 01 03 01 %s";
    //读取倍率
    public static String BeiLvLuruSend = "68 B6 00 B6 00 68 6A " + deviceId + " 04 E0 01 01 01 03 %s %s %s 50 00 00 30 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String BeiLvLuruSendTemp = "6A " + deviceId + " 04 E0 01 01 01 03 %s %s %s 50 00 00 30 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";

    //电价录入
    public static String DianjiaLuruSend = "68 CE 00 CE 00 68 6A " + deviceId + " 04 E0 00 00 20 02 04 %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String DianjiaLuruSendTemp = "6A " + deviceId + " 04 E0 00 00 20 02 04 %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";


    //电价录入 第一个s 是 单号 第二个s是 55 是追加 AA 是刷新 第三个s 是购电量 以40 结尾是元  第四个s 是报警电量 以40 结尾是元  第五个s 是跳闸电量 以40 结尾是元 第6个 是 时间 第7个是验证码

    public static String DianfeiLuruSend = "68 CE 00 CE 00 68 6A " + deviceId + " 04 E0 01 01 40 05 %s %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String DianfeiLuruSendTemp = "6A " + deviceId + " 04 E0 01 01 40 05 %s %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";


    public static String beiLvCxSend = "68 4A 00 4A 00 68 6B " + deviceId + " 0A E0 01 01 01 03 01 %s %s 16";
    public static String beiLvCxSendTemp = "6B " + deviceId + " 0A E0 01 01 01 03 01 %s";
    public static String DianjiaCxSend = "68 4A 00 4A 00 68 6B " + deviceId + " 0A E0 00 00 20 02 01 %s %s 16";
    public static String DianjiaCxSendTemp = "6B " + deviceId + " 0A E0 00 00 20 02 01 %s";
    public final static String changeDbTemp = "68 FE 00 FE 00 68 6A 00 00 FF FF 21 04 E0 00 00 02 01 01 00 01 00 00 00 81 1E 00 00 00 00 00 00 00 00 00 00 00 00 04 00 00 00 00 00 00 00 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 42 11 08 03 05 7A 16";
    public final static String faultInquiryDbTemp = "68 32 00 32 00 68 4B 00 00 FF FF 20 0C 65 00 00 01 18 F3 16";
    public final static String dingzhiTemp = "68 32 00 32 00 68 6B 00 00 FF FF 20 0A 64 01 01 02 06 01 16";

    public final static String dingzhiShezhiTemp = "68 76 00 76 00 68 4A 00 00 FF FF 20 04 63 01 01 02 06";
    public final static String adminSend = "68 76 00 76 00 68";
    public final static String adminDzSend = "68 92 00 92 00 68 4A 00 00 FF FF 20 04 63 01 01 02 06";

    public static void reset() {
        //如果是管理员则 按00 00 FF FF 21来

        if (!LoginInformation.getInstance().getUser().getType().equals(G.userRole)) {
            deviceId = "00 00 FF FF 21";
        } else {
            try {
                StringBuilder sb = new StringBuilder();
                String meterNum = LoginInformation.getInstance().getUser().getUserId();
                if (meterNum.length() == 9) {
                    sb.append(meterNum.substring(2, 4) + " " + meterNum.substring(0, 2) + " ");
                    //先获取前两位
                    int device = Integer.valueOf(meterNum.substring(4, 9));
                    sb.append(changeDlStr(Integer.toHexString(device)) + " 20");
                    deviceId = sb.toString();
                }
            } catch (Exception e) {
                Logger.e(e.getMessage());

            }
        }
        //合闸 发送
        HeZaSend = "68 8A 00 8A 00 68 6A " + deviceId + " 05 E0 01 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
        HeZaSendTemp = "6A " + deviceId + " 05 E0 01 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";
        //分闸发送
        TiaoZaSend = "68 8E 00 8E 00 68 4A " + deviceId + " 05 E1 01 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
        TiaoZaSendTemp = "4A " + deviceId + " 05 E1 01 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";

        //保电投入 发送

        //报警解除发送
        BaoJingJcSend = "68 8A 00 8A 00 68 6A " + deviceId + " 05 E2 00 00 02 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
        BaoJingJcSendTemp = "6A " + deviceId + " 05 E2 00 00 02 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";
        //电量查询 发送
        DianLiangCxSend = "68 4A 00 4A 00 68 4B " + deviceId + " 0C E1 01 01 01 04 01 %s %s 16";
        DianLiangCxSendTemp = "4B " + deviceId + " 0C E1 01 01 01 04 01 %s";
        //电压电流功率因数读取数据格式 发送
        DianLvCxSend = "68 4A 00 4A 00 68 4B " + deviceId + " 0C E3 01 01 01 03 01 %s %s 16";
        DianLvCxSendTemp = "4B " + deviceId + " 0C E3 01 01 01 03 01 %s";
        //电费查询发送
        DianFeiCxSend = "68 4A 00 4A 00 68 4B " + deviceId + " 0C E3 01 01 40 02 01 %s %s 16";
        DianFeiCxSendTemp = "4B " + deviceId + " 0C E3 01 01 40 02 01 %s";
        //读取倍率
        BeiLvDuquSend = "68 4A 00 4A 00 68 6B " + deviceId + " 0A E0 01 01 01 03 01 %s %s 16";
        BeiLvDuquSendTemp = "6B " + deviceId + " 0A E0 01 01 01 03 01 %s";
        //读取倍率
        BeiLvLuruSend = "68 B6 00 B6 00 68 6A " + deviceId + " 04 E0 01 01 01 03 %s %s %s 50 00 00 30 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
        BeiLvLuruSendTemp = "6A " + deviceId + " 04 E0 01 01 01 03 %s %s %s 50 00 00 30 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";

        //电价录入
        DianjiaLuruSend = "68 CE 00 CE 00 68 6A " + deviceId + " 04 E0 00 00 20 02 04 %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
        DianjiaLuruSendTemp = "6A " + deviceId + " 04 E0 00 00 20 02 04 %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";
        //电价录入 第一个s 是 单号 第二个s是 55 是追加 AA 是刷新 第三个s 是购电量 以40 结尾是元  第四个s 是报警电量 以40 结尾是元  第五个s 是跳闸电量 以40 结尾是元 第6个 是 时间 第7个是验证码
        DianfeiLuruSend = "68 CE 00 CE 00 68 6A " + deviceId + " 04 E0 01 01 40 05 %s %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
        DianfeiLuruSendTemp = "6A " + deviceId + " 04 E0 01 01 40 05 %s %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";

        beiLvCxSend = "68 4A 00 4A 00 68 6B " + deviceId + " 0A E0 01 01 01 03 01 %s %s 16";
        beiLvCxSendTemp = "6B " + deviceId + " 0A E0 01 01 01 03 01 %s";
        DianjiaCxSend = "68 4A 00 4A 00 68 6B " + deviceId + " 0A E0 00 00 20 02 01 %s %s 16";
        DianjiaCxSendTemp = "6B " + deviceId + " 0A E0 00 00 20 02 01 %s";

    }


    public static String changeDlStr(String dl) {
        String str = "";
        try {
            str = dl;
            if (str.length() == 0) {
                str = "00 00";
            } else if (str.length() == 1) {
                str = "0" + str + " 00";
            } else if (str.length() == 2) {
                str = str + " 00";
            } else if (str.length() == 3) {
                str = str.substring(1, 3) + " 0" + str.substring(0, 1);
            } else if (str.length() == 4) {
                str = str.substring(2, 4) + " " + str.substring(0, 2);
            }
        } catch (Exception e) {
            str = "00 00";
        }

        return str;
    }

    public static void main(String args[]) throws Exception {
        UserBean user = new UserBean();
        user.setType("20");
        user.setUserName("037600496");
        LoginInformation.getInstance().setUser(user);
        reset();
        System.out.println(HeZaSend);
    }
}
