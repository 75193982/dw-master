package com.xgx.dw.bean;

import com.google.gson.Gson;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.Setting;

public class LoginInformation {

    private UserBean user;
    private static LoginInformation instance;


    private LoginInformation() {
    }

    public static LoginInformation getInstance() {
        if (instance == null) {
            synchronized (LoginInformation.class) {
                if (instance == null) {
                    instance = new LoginInformation();
                }
            }
        }

        return instance;
    }


    public static void setInstance(LoginInformation instance) {
        LoginInformation.instance = instance;
    }

    public UserBean getUser() {
        if (user == null) {
            // 进入主页面
            String userString = new Setting(BaseApplication.getInstance()).loadString("user");
            UserBean user = new UserBean();
            try {
                user = new Gson().fromJson(userString, UserBean.class);
            } catch (Exception e) {
            }
        }
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }
}
