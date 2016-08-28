package com.xgx.dw.utils;


import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Administrator on 2016/8/13.
 */
public class MyUtils {
    public static String convertFileSize(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;



        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else return String.format("%d B", size);
    }

    public static String decodeHex367(int type, String s) {
        StringBuilder resultString = new StringBuilder();
        List<String> mBuffer = Arrays.asList(s.split(" "));
        Collections.reverse(mBuffer);
        switch (type) {
            case 0://合闸
                resultString.append("合闸成功");
                break;
            case 1://分闸
                resultString.append("分闸成功");
                break;
            case 2://保电投入
                resultString.append("保电投入成功");
                break;
            case 3://保电解除
                resultString.append("保电解除成功");
                break;
            case 4://报警解除
                resultString.append("报警解除成功");
                break;
            case 5://倍率录入
                resultString.append("倍率录入成功");
                break;
            case 41://剩余电费
                //获取时间格式
                try {
                    mBuffer = mBuffer.subList(8, 12);
                    getHexDfTime(resultString, mBuffer);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有剩余电费信息");
                }

                break;
            case 42://电量查询
                //获取时间格式
                try {
                    mBuffer = mBuffer.subList(23, 75);
                    getHexDyTime(resultString, mBuffer);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有功率信息");
                }
                break;
            case 43://电压电流功率查询
                //获取时间格式
                try {
                    mBuffer = mBuffer.subList(23, 75);
                    getHexDyTime(resultString, mBuffer);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有功率信息");
                }
                break;
        }
        return resultString.toString();
    }

    private static void getHexDfTime(StringBuilder resultString, List<String> mBuffer) {
        //46 00 00 76
        if (mBuffer.get(0).startsWith("0")) {
            //厘单位 需要除以100
            resultString.append(String.format("当前剩余电量（费）kWh：%.3f元\n", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3)) / 100));

        } else if (mBuffer.get(0).startsWith("4")) {
            resultString.append(String.format("当前剩余电量（费）kWh：%.3f元\n", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3))));

        } else if (mBuffer.get(0).startsWith("5")) {
            resultString.append(String.format("当前剩余电量（费）kWh：-%.3f元\n", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3))));

        }
    }

    /**
     * 电压电流
     *
     * @param resultString
     * @param mBuffer
     */
    private static void getHexDyTime(StringBuilder resultString, List<String> mBuffer) {

        resultString.append(String.format("C相电流%.3fA\n", Float.valueOf(mBuffer.get(0) + mBuffer.get(1) + mBuffer.get(2)) / 1000));
        resultString.append(String.format("B相电流%.3fA\n", Float.valueOf(mBuffer.get(3) + mBuffer.get(4) + mBuffer.get(5)) / 1000));
        resultString.append(String.format("A相电流%.3fA\n", Float.valueOf(mBuffer.get(6) + mBuffer.get(7) + mBuffer.get(8)) / 1000));
        resultString.append(String.format("C相电压%dV\n", Integer.valueOf(mBuffer.get(9) + mBuffer.get(10)) / 10));
        resultString.append(String.format("B相电压%dV\n", Integer.valueOf(mBuffer.get(11) + mBuffer.get(12)) / 10));
        resultString.append(String.format("A相电压%dV\n", Integer.valueOf(mBuffer.get(13) + mBuffer.get(14)) / 10));
        resultString.append(String.format("C相功率因数%.1f%%\n", Float.valueOf(mBuffer.get(15) + mBuffer.get(16)) / 10));
        resultString.append(String.format("B相功率因数%.1f%%\n", Float.valueOf(mBuffer.get(17) + mBuffer.get(18)) / 10));
        resultString.append(String.format("A相功率因数%.1f%%\n", Float.valueOf(mBuffer.get(19) + mBuffer.get(20)) / 10));
        resultString.append(String.format("A相功率因数%.1f%%\n", Float.valueOf(mBuffer.get(19) + mBuffer.get(20)) / 10));
        resultString.append(String.format("总功率因数%.1f%%\n", Float.valueOf(mBuffer.get(21) + mBuffer.get(22)) / 10));
        resultString.append(String.format("C相无功功率%.4fkw\n", Float.valueOf(mBuffer.get(23) + mBuffer.get(24) + mBuffer.get(25)) / 10000));
        resultString.append(String.format("B相无功功率%.4fkw\n", Float.valueOf(mBuffer.get(26) + mBuffer.get(27) + mBuffer.get(28)) / 10000));
        resultString.append(String.format("A相无功功率%.4fkw\n", Float.valueOf(mBuffer.get(29) + mBuffer.get(30) + mBuffer.get(31)) / 10000));
        resultString.append(String.format("总无功功率%.4fkw\n", Float.valueOf(mBuffer.get(32) + mBuffer.get(33) + mBuffer.get(34)) / 10000));
        resultString.append(String.format("C相有功功率%.4fkw\n", Float.valueOf(mBuffer.get(35) + mBuffer.get(36) + mBuffer.get(37)) / 10000));
        resultString.append(String.format("B相有功功率%.4fkw\n", Float.valueOf(mBuffer.get(38) + mBuffer.get(39) + mBuffer.get(40)) / 10000));
        resultString.append(String.format("A相有功功率%.4fkw\n", Float.valueOf(mBuffer.get(41) + mBuffer.get(42) + mBuffer.get(43)) / 10000));
        resultString.append(String.format("总功率%.4fkw\n", Float.valueOf(mBuffer.get(44) + mBuffer.get(45) + mBuffer.get(46)) / 10000));
        resultString.append(String.format("时间为：%s年%s月%s日 %s时%s分\n", mBuffer.get(47), mBuffer.get(48), mBuffer.get(49), mBuffer.get(50), mBuffer.get(51)));


        //（00 34 00 A相无功功率00.3400KW）（00 24 00 B相无功功率00.2400KW）（00 01 03 C相无功功率03.0100KW）
    }


    public static void main(String args[]) throws Exception {
        System.out.println(decodeHex367(43, "123"));
    }
}
