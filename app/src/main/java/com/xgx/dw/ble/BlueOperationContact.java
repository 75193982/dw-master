package com.xgx.dw.ble;

/**
 * Created by Administrator on 2016/8/20.
 */
public class BlueOperationContact {
    //合闸 发送
    public static String HeZaSend = "68 8A 00 8A 00 68 6A 00 00 FF FF 21 05 E0 01 01 02 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 23 12 17 04 05 C8 16";
    //分闸发送
    public static String TiaoZaSend = "68 8E 00 8E 00 68 4A 00 00 FF FF 21 05 E1 01 01 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 02 56 13 17 04 05 DD 16";
    //保电投入 发送
    public static String BaoDianTrSend = "68 8E 00 8E 00 68 4A 00 00 FF FF 21 05 EB 00 00 01 03 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0C 25 36 17 04 05 E4 16";

    //保电解除 发送
    public static String BaoDianJcSend = "68 8A 00 8A 00 68 6A 00 00 FF FF 21 05 EC 00 00 01 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 0D 40 37 17 04 05 23 16";
    //报警解除发送
    public static String BaoJingJcSend = "68 8A 00 8A 00 68 6A 00 00 FF FF 21 05 E2 00 00 02 04 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 03 37 40 17 11 05 1D 16";
    //电量查询 发送
    public static String DianLiangCxSend = "68 4A 00 4A 00 68 4B 00 00 FF FF 21 0C E1 01 01 01 04 02 16 59 14 22 05 0A 16";
    //电压电流功率因数读取数据格式 发送
    public static String DianLvCxSend = "68 4A 00 4A 00 68 4B 00 00 FF FF 21 0C E3 01 01 01 03 04 26 21 15 22 05 E6 16";
    //电费查询发送
    public static String DianFeiCxSend = "68 4A 00 4A 00 68 4B 00 00 FF FF 21 0C E3 01 01 40 02 04 35 30 17 04 05 26 16";
    //读取倍率
    public static String BeiLvDuquSend = "68 4A 00 4A 00 68 6B 00 00 FF FF 21 0A E0 01 01 01 03 01 24 58 15 22 05 33 16";
    //读取倍率
    public static String BeiLvLuruSend = "68 B6 00 B6 00 68 6A 00 00 FF FF 21 04 E0 01 01 01 03 %s %s %s 50 00 00 30 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String BeiLvLuruSendTemp = "6A 00 00 FF FF 21 04 E0 01 01 01 03 %s %s %s 50 00 00 30 01 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";

    //电价录入
    public static String DianjiaLuruSend = "68 CE 00 CE 00 68 6A 00 00 FF FF 21 04 E0 00 00 20 02 04 %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String DianjiaLuruSendTemp = "6A 00 00 FF FF 21 04 E0 00 00 20 02 04 %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";


    //电价录入 第一个s 是 单号 第二个s是 55 是追加 AA 是刷新 第三个s 是购电量 以40 结尾是元  第四个s 是报警电量 以40 结尾是元  第五个s 是跳闸电量 以40 结尾是元 第6个 是 时间 第7个是验证码

    public static String DianfeiLuruSend = "68 CE 00 CE 00 68 6A 00 00 FF FF 21 04 E0 01 01 40 05 %s %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s %s 16";
    public static String DianfeiLuruSendTemp = "6A 00 00 FF FF 21 04 E0 01 01 40 05 %s %s %s %s %s 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 00 01 %s";


    public static String beiLvCxSend = "68 4A 00 4A 00 68 6B 00 00 FF FF 21 0A E0 01 01 01 03 01 24 58 15 22 05 33 16";
    public static String DianjiaCxSend = "68 4A 00 4A 00 68 6B 00 00 FF FF 21 0A E0 00 00 20 02 01 03 57 17 22 05 2F 16";
}
