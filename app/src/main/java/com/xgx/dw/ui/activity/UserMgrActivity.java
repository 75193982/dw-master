package com.xgx.dw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar.OnMenuItemClickListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.adapter.UserAdapter;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseEventBusActivity;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IUserListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserMgrActivity extends BaseEventBusActivity implements IUserListView, OnMenuItemClickListener, BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {
    private static int REFRESH_RECYCLERVIEW = 0;
    @BindView(R.id.query)
    EditText query;
    @BindView(R.id.numTv)
    TextView numTv;
    @BindView(R.id.search_clear)
    ImageButton searchClear;
    @BindView(R.id.swipeLayout)
    SwipeRefreshLayout swipeLayout;
    @BindView(R.id.bodyLayout)
    RelativeLayout bodyLayout;
    @BindView(R.id.main_frame)
    FrameLayout mainFrame;
    private UserAdapter adapter;
    private List<UserBean> beans;
    private IUserPresenter presenter;
    @BindView(R.id.list)
    RecyclerView recyclerView;
    private int PAGE_SIZE;
    private long delayMillis = 500;

    public void initContentView() {
        super.baseSetContentView(R.layout.activity_user_mgr);
    }

    public void initPresenter() {

        presenter = new UserPresenterImpl();

        presenter.searchUser(this);
    }

    public void initView() {
        getSupportActionBar().setTitle("用户资料");
        getToolbar().setOnMenuItemClickListener(this);
        beans = new ArrayList();
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
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

                if (userbean.getType().equals(G.depRole)) {
                    Intent localIntent = new Intent();
                    localIntent.putExtra("bean", userbean);
                    localIntent.setClass(getContext(), CreateUserOneAcvitity.class);
                    startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
                } else if (userbean.getType().equals(G.taiquRole)) {
                    Intent localIntent = new Intent();
                    localIntent.putExtra("bean", userbean);
                    localIntent.setClass(getContext(), CreateUserTwoAcvitity.class);
                    startActivityForResult(localIntent, REFRESH_RECYCLERVIEW);
                } else if (userbean.getType().equals(G.userRole)) {
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
                if (userbean.getUserId().equals(LoginInformation.getInstance().getUser().getUserId())) {
                    showToast("不能删除自己");
                    return;
                }

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

        query.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterData(s.toString());
            }
        });
    }

    private void filterData(String filterStr) {
        List<UserBean> filterDateList = new ArrayList<UserBean>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList = beans;
        } else {
            filterDateList.clear();
            for (UserBean bean : beans) {
                try {
                    String name = checkText(bean.getUserName());
                    String userId = checkText(bean.getUserId());
                    if (name.contains(filterStr) || userId.contains(filterStr)) {
                        filterDateList.add(bean);
                    }
                } catch (Exception e) {
                }

            }
        }
        if (filterDateList != null && filterDateList.size() > 0) {
            numTv.setText("共搜索到" + filterDateList.size() + "个用户");
        } else {
            numTv.setText("共搜索到0个用户");

        }
        adapter.setNewData(filterDateList);
    }


    public boolean onCreateOptionsMenu(Menu paramMenu) {
        getMenuInflater().inflate(R.menu.menu_user_create, paramMenu);
        Setting setting = new Setting(this);
        String usertype = setting.loadString(G.currentUserType);
        if (usertype.equals(G.taiquRole)) {//11表示台区管理员
            getToolbar().getMenu().getItem(0).setVisible(false);
            getToolbar().getMenu().getItem(1).setVisible(false);
        }
        if (usertype.equals(G.depRole)) {//10表示营业厅管理员
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
        swipeLayout.setRefreshing(false);
        beans = userList;
        adapter.setNewData(userList);
        if (userList != null && userList.size() > 0) {
            numTv.setText("共搜索到" + userList.size() + "个用户");
        } else {
            numTv.setText("共搜索到0个用户");

        }
    }

    @Override
    protected void onEventComming(EventCenter eventCenter) {
        if (EventCenter.USER_SAVE == eventCenter.getEventCode()) {
            try {
                presenter.searchUser(this);
            } catch (Exception e) {

            }
        }
    }

    @Override
    public boolean isBindEventBusHere() {
        return true;
    }

    @Override
    public void onRefresh() {
        PAGE_SIZE = 1;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                presenter.searchUser(UserMgrActivity.this);
            }
        }, delayMillis);
    }

    @Override
    public void onLoadMoreRequested() {

    }

}
