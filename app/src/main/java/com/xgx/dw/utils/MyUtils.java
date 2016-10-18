package com.xgx.dw.utils;


import android.content.Context;
import android.telephony.TelephonyManager;

import com.xgx.dw.SearchDlLog;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.SearchDlDaoHelper;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
//        if (type == 42) {
//            s = "68 B6 01 B6 01 68 88 00 41 01 00 20 0C E1 01 01 01 04 56 14 22 08 16 04 00 28 46 37 00 00 09 26 06 00 00 75 23 07 00 00 51 67 13 00 00 92 28 10 00 95 88 04 00 82 86 00 00 68 81 00 00 22 69 01 00 21 51 01 00 EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE 02 16 59 14 22 05 87 16";
//        } else if (type == 43) {
//            s = "68 56 01 56 01 68 88 00 41 01 00 20 0C E3 01 01 01 03 18 15 22 08 16 42 03 00 33 00 00 26 00 00 81 02 00 00 60 03 00 34 00 00 24 00 00 01 03 88 06 96 06 64 07 83 06 70 23 70 23 70 23 20 00 00 10 00 00 00 02 00 EE EE EE EE EE EE EE EE EE EE EE EE EE EE EE 04 26 21 15 22 05 AE 16";
//        }
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
            case 4://倍率录入
                resultString.append("倍率录入成功");

                break;
            case 5://电价录入
                resultString.append("电价录入成功");
                break;
            case 6://电价录入
                resultString.append("电费录入成功");
                setCurrentOrderId();
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
                    mBuffer = mBuffer.subList(48, 116);
                    getHexDlTime(resultString, mBuffer);
                    SearchDlLog bean = new SearchDlLog();
                    bean.setId(UUID.randomUUID().toString());
                    bean.setType("1");
                    bean.setUserId(LoginInformation.getInstance().getUser().getUserId());
                    bean.setContent(resultString.toString());
                    SearchDlDaoHelper.getInstance().addData(bean);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有电量信息");
                }
                break;
            case 43://电压电流功率查询
                //获取时间格式
                try {
                    mBuffer = mBuffer.subList(23, 75);
                    getHexDyTime(resultString, mBuffer);
                    SearchDlLog bean = new SearchDlLog();
                    bean.setId(UUID.randomUUID().toString());
                    bean.setType("2");
                    bean.setUserId(LoginInformation.getInstance().getUser().getUserId());
                    bean.setContent(resultString.toString());
                    SearchDlDaoHelper.getInstance().addData(bean);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有功率信息");
                }
                break;
            case 47://倍率查询
                //获取时间格式
                try {
                    mBuffer = mBuffer.subList(9, 19);
                    getBl(resultString, mBuffer);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有倍率信息");
                }
                break;
            case 48://电价查询
                //获取时间格式
                try {
                    mBuffer = mBuffer.subList(8, 24);
                    getDj(resultString, mBuffer);
                } catch (Exception e) {
                    resultString = new StringBuilder();
                    resultString.append("当前没有电价信息");
                }
                break;
        }
        return resultString.toString();
    }

    private static void getDj(StringBuilder resultString, List<String> mBuffer) {
        resultString.append(String.format("尖电价：<big><font color='blue'>%.3f</font></big>元<br/>", Float.valueOf(mBuffer.get(12) + mBuffer.get(13) + mBuffer.get(14) + mBuffer.get(15)) / 1000));
        resultString.append(String.format("峰电价：<big><font color='blue'>%.3f</font></big>元<br/>", Float.valueOf(mBuffer.get(8) + mBuffer.get(9) + mBuffer.get(10) + mBuffer.get(11)) / 1000));
        resultString.append(String.format("平电价：<big><font color='blue'>%.3f</font></big>元<br/>", Float.valueOf(mBuffer.get(4) + mBuffer.get(5) + mBuffer.get(6) + mBuffer.get(7)) / 1000));
        resultString.append(String.format("谷电价：<big><font color='blue'>%.3f</font></big>元<br/>", Float.valueOf(mBuffer.get(0) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3)) / 1000));
    }

    private static void getBl(StringBuilder resultString, List<String> mBuffer) {
        try {
            resultString.append(String.format("电压倍率：<big><font color='blue'>%s</font></big><br/>", Integer.valueOf(mBuffer.get(8) + mBuffer.get(9), 16)));
        } catch (Exception e) {
            resultString.append(String.format("电压倍率：<big><font color='blue'>%s</font></big><br/>", "0"));
        }
        try {
            resultString.append(String.format("电流倍率：<big><font color='blue'>%s</font></big><br/>", Integer.valueOf(mBuffer.get(6) + mBuffer.get(7), 16)));
        } catch (Exception e) {
            resultString.append(String.format("电流倍率：<big><font color='blue'>%s</font></big><br/>", "0"));
        }
        resultString.append(String.format("额定负荷：%.4f<br/>", Float.valueOf(mBuffer.get(0) + mBuffer.get(1) + mBuffer.get(2)) / 10000));
        resultString.append(String.format("额定电流(A)：%.1f<br/>", Float.valueOf(mBuffer.get(3)) / 10));
        resultString.append(String.format("额定电压(V)：%.1f<br/>", Float.valueOf(mBuffer.get(4) + mBuffer.get(5)) / 10));
    }


    private static void getHexDlTime(StringBuilder resultString, List<String> mBuffer) {
        String gStr = mBuffer.get(22).startsWith("0") ? "0" : "";
        String pStr = mBuffer.get(27).startsWith("0") ? "0" : "";
        String fStr = mBuffer.get(32).startsWith("0") ? "0" : "";
        String jStr = mBuffer.get(37).startsWith("0") ? "0" : "";
        String zStr = mBuffer.get(42).startsWith("0") ? "0" : "";
        resultString.append(String.format("抄表时间为：%s年%s月%s日 %s时%s分<br/>", mBuffer.get(46), mBuffer.get(47), mBuffer.get(48), mBuffer.get(49), mBuffer.get(50)));
        resultString.append(String.format("费率数M（1≤M≤12）：%s<br/>", Integer.valueOf(mBuffer.get(45)) + ""));
        resultString.append(String.format("当前正向有功总电能(kwh)：<big><font color='blue'>%s%.4f</font></big><br/>", Integer.valueOf(mBuffer.get(40) + mBuffer.get(41)) + zStr, Float.valueOf(Integer.valueOf(mBuffer.get(42) + mBuffer.get(43) + mBuffer.get(44))) / 10000));
        resultString.append(String.format("当前尖有功电能(kwh)：<big><font color='blue'>%s%.4f</font></big><br/>", Integer.valueOf(mBuffer.get(35) + mBuffer.get(36)) + jStr, Float.valueOf(Integer.valueOf(mBuffer.get(37) + mBuffer.get(38) + mBuffer.get(39))) / 10000));
        resultString.append(String.format("当前峰有功电能(kwh)：<big><font color='blue'>%s%.4f</font></big><br/>", Integer.valueOf(mBuffer.get(30) + mBuffer.get(31)) + fStr, Float.valueOf(Integer.valueOf(mBuffer.get(32) + mBuffer.get(33) + mBuffer.get(34))) / 10000));
        resultString.append(String.format("当前平有功电能(kwh)：<big><font color='blue'>%s%.4f</font></big><br/>", Integer.valueOf(mBuffer.get(25) + mBuffer.get(26)) + pStr, Float.valueOf(Integer.valueOf(mBuffer.get(27) + mBuffer.get(28) + mBuffer.get(29))) / 10000));
        resultString.append(String.format("当前谷有功电能(kwh)：<big><font color='blue'>%s%.4f</font></big><br/><br/>", Integer.valueOf(mBuffer.get(20) + mBuffer.get(21)) + gStr, Float.valueOf(Integer.valueOf(mBuffer.get(22) + mBuffer.get(23) + mBuffer.get(24))) / 10000));
        resultString.append(String.format("当前无功总电能(kvarh)：<big><font color='blue'>%.2f</font></big><br/>", Float.valueOf(mBuffer.get(16) + mBuffer.get(17) + mBuffer.get(18) + mBuffer.get(19)) / 100));
        resultString.append(String.format("当前尖无功电能(kvarh)：<big><font color='blue'>%.2f</font></big><br/>", Float.valueOf(mBuffer.get(12) + mBuffer.get(13) + mBuffer.get(14) + mBuffer.get(15)) / 100));
        resultString.append(String.format("当前峰无功电能(kvarh)：<big><font color='blue'>%.2f</font></big><br/>", Float.valueOf(mBuffer.get(8) + mBuffer.get(9) + mBuffer.get(10) + mBuffer.get(11)) / 100));
        resultString.append(String.format("当前平无功电能(kvarh)：<big><font color='blue'>%.2f</font></big><br/>", Float.valueOf(mBuffer.get(4) + mBuffer.get(5) + mBuffer.get(6) + mBuffer.get(7)) / 100));
        resultString.append(String.format("当前谷无功电能(kvarh)：<big><font color='blue'>%.2f</font></big><br/>", Float.valueOf(mBuffer.get(0) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3)) / 100));


    }

    private static void getHexDfTime(StringBuilder resultString, List<String> mBuffer) {
        //46 00 00 76
        if (mBuffer.get(0).startsWith("0")) {
            //厘单位 需要除以100
            resultString.append(String.format("<br/>当前剩余电费：   <big><font color='blue'>%.3f</font></big>      元<br/>", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3)) / 1000));

        } else if (mBuffer.get(0).startsWith("4")) {
            resultString.append(String.format("<br/>当前剩余电费：   <big><font color='blue'>%.3f</font></big>   元<br/>", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3))));

        } else if (mBuffer.get(0).startsWith("5")) {
            resultString.append(String.format("<br/>当前剩余电费：  <big><font color='blue'>%.3f</font></big>   元<br/>", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3))));

        } else if (mBuffer.get(0).startsWith("1")) {
            resultString.append(String.format("<br/>当前剩余电费：  <big><font color='blue'>%.3f</font></big>   元<br/>", Float.valueOf(mBuffer.get(0).substring(1) + mBuffer.get(1) + mBuffer.get(2) + mBuffer.get(3)) / 1000));

        }
    }

    /**
     * 电压电流
     *
     * @param resultString
     * @param mBuffer
     */
    private static void getHexDyTime(StringBuilder resultString, List<String> mBuffer) {
        resultString.append(String.format("抄表时间：%s年%s月%s日 %s时%s分<br/><br/>", mBuffer.get(47), mBuffer.get(48), mBuffer.get(49), mBuffer.get(50), mBuffer.get(51)));
        resultString.append(String.format("有功总功率<big><font color='blue'>%.4f</font></big>kw<br/>", Float.valueOf(mBuffer.get(44) + mBuffer.get(45) + mBuffer.get(46)) / 10000));
        resultString.append(String.format("A相有功功率<big><font color='blue'>%.4f</font></big>kw<br/>", Float.valueOf(mBuffer.get(41) + mBuffer.get(42) + mBuffer.get(43)) / 10000));
        resultString.append(String.format("B相有功功率<big><font color='blue'>%.4f</font></big>kw<br/>", Float.valueOf(mBuffer.get(38) + mBuffer.get(39) + mBuffer.get(40)) / 10000));
        resultString.append(String.format("C相有功功率<big><font color='blue'>%.4f</font></big>kw<br/><br/>", Float.valueOf(mBuffer.get(35) + mBuffer.get(36) + mBuffer.get(37)) / 10000));

        resultString.append(String.format("总无功功率<big><font color='blue'>%.4f</font></big>kw<br/>", Float.valueOf(mBuffer.get(32) + mBuffer.get(33) + mBuffer.get(34)) / 10000));
        resultString.append(String.format("A相无功功率<big><font color='blue'>%.4f</font></big>kw<br/>", Float.valueOf(mBuffer.get(29) + mBuffer.get(30) + mBuffer.get(31)) / 10000));
        resultString.append(String.format("B相无功功率<big><font color='blue'>%.4f</font></big>kw<br/>", Float.valueOf(mBuffer.get(26) + mBuffer.get(27) + mBuffer.get(28)) / 10000));
        resultString.append(String.format("C相无功功率<big><font color='blue'>%.4f</font></big>kw<br/><br/>", Float.valueOf(mBuffer.get(23) + mBuffer.get(24) + mBuffer.get(25)) / 10000));

        resultString.append(String.format("总功率因数<big><font color='blue'>%.1f%%</font></big><br/>", Float.valueOf(mBuffer.get(21) + mBuffer.get(22)) / 10));
        resultString.append(String.format("A相功率因数<big><font color='blue'>%.1f%%</font></big><br/>", Float.valueOf(mBuffer.get(19) + mBuffer.get(20)) / 10));
        resultString.append(String.format("A相功率因数<big><font color='blue'>%.1f%%</font></big><br/>", Float.valueOf(mBuffer.get(19) + mBuffer.get(20)) / 10));
        resultString.append(String.format("B相功率因数<big><font color='blue'>%.1f%%</font></big><br/>", Float.valueOf(mBuffer.get(17) + mBuffer.get(18)) / 10));
        resultString.append(String.format("C相功率因数<big><font color='blue'>%.1f%%</font></big><br/><br/>", Float.valueOf(mBuffer.get(15) + mBuffer.get(16)) / 10));

        resultString.append(String.format("A相电压<big><font color='blue'>%dV</font></big><br/>", Integer.valueOf(mBuffer.get(13) + mBuffer.get(14)) / 10));
        resultString.append(String.format("B相电压<big><font color='blue'>%dV</font></big><br/>", Integer.valueOf(mBuffer.get(11) + mBuffer.get(12)) / 10));
        resultString.append(String.format("C相电压<big><font color='blue'>%dV</font></big><br/><br/>", Integer.valueOf(mBuffer.get(9) + mBuffer.get(10)) / 10));

        resultString.append(String.format("A相电流<big><font color='blue'>%.3fA</font></big><br/>", Float.valueOf(mBuffer.get(6) + mBuffer.get(7) + mBuffer.get(8)) / 1000));
        resultString.append(String.format("B相电流<big><font color='blue'>%.3fA</font></big><br/>", Float.valueOf(mBuffer.get(3) + mBuffer.get(4) + mBuffer.get(5)) / 1000));
        resultString.append(String.format("C相电流<big><font color='blue'>%.3fA</font></big><br/>", Float.valueOf(mBuffer.get(0) + mBuffer.get(1) + mBuffer.get(2)) / 1000));


        //（00 34 00 A相无功功率00.3400KW）（00 24 00 B相无功功率00.2400KW）（00 01 03 C相无功功率03.0100KW）
    }


    public static void main(String args[]) throws Exception {
        // System.out.println(decodeHex367(42, " b6 01 b6 01 68 88 00 41 02 00 20 0c e1 01 01 01 04 34 11 07 09 16 04 00 13 02 39 00 00 21 48 06 00 00 65 54 07 00 00 31 39 14 00 00 94 59 10 00 44 18 05 00 45 91 00 00 65 86 00 00 18 81 01 00 14 59 01 00 ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee ee"));
        // System.out.println(decodeHex367(41, "68 5A 00 5A 00 68 88 00 41 01 00 20 0C E6 01 01 40 02 40 90 70 08 09 29 19 11 04 05 cf 16"));
        // System.out.println(changeDjStr("21.2345"));
        "003C".replaceFirst("0", "");
        System.out.println(Integer.valueOf("4", 16));
    }


    public static String changeDjStr(String dj) {
        String str = "";
        try {
            int i = (int) (Float.valueOf(dj) * 1000);
            str = i + "";
            if (str.length() == 1) {
                str = "0" + str + " 00 00 00";
            } else if (str.length() == 2) {
                str = str + " 00 00 00";
            } else if (str.length() == 3) {
                str = str.substring(1, 3) + " 0" + str.substring(0, 1) + " 00 00";
            } else if (str.length() == 4) {
                str = str.substring(2, 4) + " " + str.substring(0, 2) + " 00 00";
            } else if (str.length() == 5) {
                str = str.substring(3, 5) + " " + str.substring(1, 3) + " 0" + str.substring(0, 1) + " 00";
            } else if (str.length() == 6) {
                str = str.substring(4, 6) + " " + str.substring(2, 4) + " " + str.substring(0, 2) + " 00";
            } else if (str.length() == 7) {
                str = str.substring(5, 7) + " " + str.substring(3, 5) + " " + str.substring(1, 3) + " 0" + str.substring(0, 1);
            } else if (str.length() == 8) {
                str = str.substring(6, 8) + " " + str.substring(4, 6) + " " + str.substring(2, 4) + " " + str.substring(0, 2);
            } else if (str.length() > 8) {
                str = str.substring(6, 8) + " " + str.substring(4, 6) + " " + str.substring(2, 4) + " " + str.substring(0, 2);
            }
        } catch (Exception e) {
            str = "00 00 00 00";
        }

        return str;
    }

    public static String changeDlStr(String dl) {
        String str = "";
        try {
            str = dl;
            if (str.length() == 0) {
                str = "00 00 00";
            } else if (str.length() == 1) {
                str = "0" + str + " 00 00";
            } else if (str.length() == 2) {
                str = str + " 00 00";
            } else if (str.length() == 3) {
                str = str.substring(1, 3) + " 0" + str.substring(0, 1) + " 00";
            } else if (str.length() == 4) {
                str = str.substring(2, 4) + " " + str.substring(0, 2) + " 00";
            } else if (str.length() == 5) {
                str = str.substring(3, 5) + " " + str.substring(1, 3) + " 0" + str.substring(0, 1);
            } else if (str.length() == 6) {
                str = str.substring(4, 6) + " " + str.substring(2, 4) + " " + str.substring(0, 2);
            }
            str = str + " 40";
        } catch (Exception e) {
            str = "00 00 00 40";
        }

        return str;
    }

    public static String getJyCode(String temp) {
        List<String> mBuffer = Arrays.asList(temp.split(" "));
        int sum = 0;
        try {
            for (int i = 0; i < mBuffer.size(); i++) {
                sum += Integer.valueOf(mBuffer.get(i), 16);
            }
            sum = sum - (sum / 256) * 256;
        } catch (Exception e) {

        }

        return Integer.toHexString(sum);
    }

    public static String getNewOrderId() {
        String username = LoginInformation.getInstance().getUser().getUserName();
        int currentOrderId = BaseApplication.getSetting().loadInt(username + G.currentOrderId);
        //首先转换成16进制
        if (currentOrderId == -1) {
            setCurrentOrderId();
            currentOrderId = 0;
        }
        return toHexString(currentOrderId + 1);

    }

    public static void setCurrentOrderId() {
        String username = BaseApplication.getSetting().loadString(G.currentUsername);
        int newId = BaseApplication.getSetting().loadInt(username + G.currentOrderId);
        try {
            newId = newId + 1;
        } catch (Exception e) {
            newId = 1;
        }
        BaseApplication.getSetting().saveInt(username + G.currentOrderId, newId);
    }

    private static String toHexString(int temp) {
        String dlbl = "";

        try {
            dlbl = Integer.toHexString(temp);
            //自动补全4位
            if (dlbl.length() == 0) {
                dlbl = "01 00 00 00";
            } else if (dlbl.length() == 1) {
                dlbl = "0" + dlbl + " 00 00 00";
            } else if (dlbl.length() == 2) {
                dlbl = dlbl + " 00 00 00";
            } else if (dlbl.length() == 3) {
                dlbl = dlbl.substring(1, 3) + " 0" + dlbl.substring(0, 1) + " 00 00";
            } else if (dlbl.length() == 4) {
                dlbl = dlbl.substring(2, 4) + " " + dlbl.substring(0, 2) + " 00 00";
            } else if (dlbl.length() == 5) {
                dlbl = dlbl.substring(3, 5) + " " + dlbl.substring(1, 3) + " 0" + dlbl.substring(0, 1) + " 00";
            } else if (dlbl.length() == 6) {
                dlbl = dlbl.substring(4, 6) + " " + dlbl.substring(2, 4) + " " + dlbl.substring(0, 2) + " 00";
            } else if (dlbl.length() == 7) {
                dlbl = dlbl.substring(5, 7) + " " + dlbl.substring(3, 5) + " " + dlbl.substring(1, 3) + " 0" + dlbl.substring(0, 1);
            } else if (dlbl.length() >= 8) {
                dlbl = dlbl.substring(6, 8) + " " + dlbl.substring(4, 6) + " " + dlbl.substring(2, 4) + dlbl.substring(0, 2);
            }
        } catch (Exception e) {
            dlbl = "01 00 00 00";
        }
        return dlbl;
    }

    public static String getuniqueId(Context context) {

        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        String imei = tm.getDeviceId();

//        String simSerialNumber = tm.getSimSerialNumber();
//
//        String androidId = android.provider.Settings.Secure.getString(
//
//                context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
//
//        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) imei.hashCode() << 32) | simSerialNumber.hashCode());

        //       String uniqueId = deviceUuid.toString();
        return imei;
    }

}
