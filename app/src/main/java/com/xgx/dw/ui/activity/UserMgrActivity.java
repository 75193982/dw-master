package com.xgx.dw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.StoreBean;
import com.xgx.dw.UserBean;
import com.xgx.dw.adapter.StoresAdapter;
import com.xgx.dw.adapter.UserAdapter;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.StorePresenterImpl;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IStoresPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IStoresView;
import com.xgx.dw.ui.view.interfaces.IUserListView;
import com.xgx.dw.ui.view.interfaces.IUserView;
import com.xgx.dw.vo.request.StoresRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

public class UserMgrActivity extends BaseAppCompatActivity implements IUserListView, OnMenuItemClickListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    private UserAdapter adapter;
    private List<UserBean> beans;
    private IUserPresenter presenter;
    @Bind({R.id.list})
    RecyclerView recyclerView;
    private String currentUserType;

    public void initContentView() {
        super.baseSetContentView(R.layout.activity_stores_mgr);
    }

    public void initPresenter() {

        presenter = new UserPresenterImpl();

        presenter.searchUser(this);
    }

    public void initView() {
        getSupportActionBar().setTitle(R.string.user_one);
        getToolbar().setOnMenuItemClickListener(this);
        beans = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        adapter = new UserAdapter(beans);
        adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) recyclerView.getParent(), false);
        adapter.setEmptyView(localView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
                UserBean userbean = (UserBean) baseQuickAdapter.getItem(i);

                if (userbean.getType().equals("10")) {
                    Intent localIntent = new Intent();
                    localIntent.putExtra("bean", userbean);
                    localIntent.setClass(getContext(), CreateUserOneAcvitity.class);
                    startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
                } else if (userbean.getType().equals("11")) {
                    Intent localIntent = new Intent();
                    localIntent.putExtra("bean", userbean);
                    localIntent.setClass(getContext(), CreateUserTwoAcvitity.class);
                    startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
                } else if (userbean.getType().equals("20")) {
                    Intent localIntent = new Intent();
                    localIntent.putExtra("bean", userbean);
                    localIntent.setClass(getContext(), CreateUserThreeAcvitity.class);
                    startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);

                }
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemLongClick(adapter, view, position);
                final UserBean userbean = (UserBean) baseQuickAdapter.getItem(position);

                final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("请注意");
                alertDialog.setMessage("你是否要删除该用户");
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        UserBeanDaoHelper.getInstance().deleteData(userbean.getId());
                        presenter.searchUser(UserMgrActivity.this);

                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REFRESH_RECYCLERVIEW) {
            presenter.searchUser(this);
        }
    }

    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_user_create, paramMenu);
        Setting setting = new Setting(this);
        String usertype = setting.loadString(G.currentUserType);
        if (usertype.equals("11")) {//11表示台区管理员
            getToolbar().getMenu().getItem(0).setVisible(false);
            getToolbar().getMenu().getItem(1).setVisible(false);
        }
        if (usertype.equals("10")) {//10表示营业厅管理员
            getToolbar().getMenu().getItem(0).setVisible(false);
        }

        return true;
    }


    public boolean onMenuItemClick(MenuItem paramMenuItem) {
        Intent intent = new Intent();
        switch (paramMenuItem.getItemId()) {
            case R.id.create_storemgr_user:
                intent.setClass(getContext(), CreateUserOneAcvitity.class);
                startActivityForResult(intent, REFRESH_RECYCLERVIEW);
                break;
            case R.id.create_transformermgr_user:
                intent.setClass(getContext(), CreateUserTwoAcvitity.class);
                startActivityForResult(intent, REFRESH_RECYCLERVIEW);
                break;
            case R.id.create_device_user:
                intent.setClass(getContext(), CreateUserThreeAcvitity.class);
                startActivityForResult(intent, REFRESH_RECYCLERVIEW);
                break;
        }
        return true;
    }


    @Override
    public void getUserList(List<UserBean> userList) {
        adapter.setNewData(userList);
    }
}
