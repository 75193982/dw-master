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

    //电量查询 发送
    public static String DianLiangCxSend = "68 4A 00 4A 00 68 4B 00 00 FF FF 21 0C E3 01 01 40 02 04 35 30 17 04 05 26 16";
    //电压电流功率因数读取数据格式 发送
    public static String DianLvCxSend = "68 4A 00 4A 00 68 4B 00 00 FF FF 21 0C E5 01 01 01 03 06 46 32 17 04 05 FF 16";

}
