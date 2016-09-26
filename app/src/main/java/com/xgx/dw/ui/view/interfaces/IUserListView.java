package com.xgx.dw.ui.view.interfaces;

import com.xgx.dw.UserBean;
import com.xgx.dw.base.IBaseView;

import java.util.List;

/**
 * Created by Administrator on 2016/9/26.
 */
public interface IUserListView extends IBaseView {
    void getUserList(List<UserBean> userList);
}
