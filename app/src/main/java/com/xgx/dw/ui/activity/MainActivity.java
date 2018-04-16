package com.xgx.dw.ui.activity;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.UserBean;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.base.BaseActivity;
import com.xgx.dw.base.FragmentAdapter;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Purchase;
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.SpotPricingBeanDaoHelper;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.presenter.impl.MainPresenterImpl;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IMainPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IMainView;
import com.xgx.dw.ui.view.interfaces.IUserView;
import com.xgx.dw.upload.UploadResponse;
import com.xgx.dw.utils.ApiLevelHelper;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.vo.request.UpdateVersionRequest;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements IMainView, IUserView {

    private final String ACTION_FILTER = "com.xgx.dw.main";
    @BindView(R.id.wei_xin_s)
    ImageView weiXinS;
    @BindView(R.id.tab_weiXin)
    TextView tabWeiXin;
    @BindView(R.id.tab_weiXin_s)
    TextView tabWeiXinS;
    @BindView(R.id.ll_wx)
    LinearLayout llWx;
    @BindView(R.id.address_book_s)
    ImageView addressBookS;
    @BindView(R.id.tab_address)
    TextView tabAddress;
    @BindView(R.id.tab_address_s)
    TextView tabAddressS;
    @BindView(R.id.ll_address)
    LinearLayout llAddress;
    @BindView(R.id.find_s)
    ImageView findS;
    @BindView(R.id.tab_find)
    TextView tabFind;
    @BindView(R.id.tab_find_s)
    TextView tabFindS;
    @BindView(R.id.ll_find)
    LinearLayout llFind;
    @BindView(R.id.me_s)
    ImageView meS;
    @BindView(R.id.tab_me)
    TextView tabMe;
    @BindView(R.id.tab_me_s)
    TextView tabMeS;
    @BindView(R.id.ll_me)
    LinearLayout llMe;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    @BindView(R.id.line)
    View line;
    @BindView(R.id.viewPage)
    ViewPager viewPage;
    @BindView(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    private List<ImageView> mListImage = new ArrayList();
    private List<TextView> mListText = new ArrayList();
    private IMainPresenter mMainPresenter;
    private OnFABClickListener mOnFABClickListener;
    private int[] viewId;
    private String currentUserType;

    public FloatingActionButton getFab() {
        return fab;
    }

    @Override
    public void checkVersionCallBack(UploadResponse paramUpdateVersionResult) {
        try {
            Intent localIntent = new Intent(this, UploadDialogActivity.class);
            localIntent.putExtra("response", paramUpdateVersionResult);
            startActivity(localIntent);
        } catch (Exception e) {
            Logger.i(e.getMessage());
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void initContentView() {
        setContentView(R.layout.activity_main);
    }

    @Override
    public void initPresenter() {
        mMainPresenter = new MainPresenterImpl(this);
    }

    @Override
    public void initView() {

        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_erweima_searching_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        currentUserType = LoginInformation.getInstance().getUser().getType();
        FragmentAdapter localFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), currentUserType);
        if ((G.userRole + "").equals(currentUserType) || (G.testRole + "").equals(currentUserType) || (G.test2Role + "").equals(currentUserType)) {//普通用户 不显示资源管理按钮
            viewId = new int[]{R.id.ll_address, R.id.ll_find, R.id.ll_me};
            llWx.setVisibility(View.GONE);
            mListImage.add(addressBookS);
            mListImage.add(findS);
            mListImage.add(meS);
            mListText.add(tabAddressS);
            mListText.add(tabFindS);
            mListText.add(tabMeS);
            addressBookS.setAlpha(1.0F);
            tabAddressS.setAlpha(1.0F);
        } else if (currentUserType.equals((G.test1Role + ""))) {//供电局调试账户 不显示资源管理按钮
            viewId = new int[]{R.id.ll_find};
            llWx.setVisibility(View.GONE);
            llAddress.setVisibility(View.GONE);
            llMe.setVisibility(View.GONE);
            mListImage.add(findS);
            mListText.add(tabFindS);
            findS.setAlpha(1.0F);
            tabFindS.setAlpha(1.0F);
        } else {
            viewId = new int[]{R.id.ll_wx, R.id.ll_address, R.id.ll_find, R.id.ll_me};
            mListImage.add(weiXinS);
            mListImage.add(addressBookS);
            mListImage.add(findS);
            mListImage.add(meS);
            mListText.add(tabWeiXinS);
            mListText.add(tabAddressS);
            mListText.add(tabFindS);
            mListText.add(tabMeS);
            weiXinS.setAlpha(1.0F);
            tabWeiXinS.setAlpha(1.0F);
        }
        viewPage.setAdapter(localFragmentAdapter);

        setViewPageListener();
        MainBoradcastReceiver localMainBoradcastReceiver = new MainBoradcastReceiver();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.xgx.dw.main");
        registerReceiver(localMainBoradcastReceiver, localIntentFilter);
        UpdateVersionRequest localUpdateVersionRequest = new UpdateVersionRequest();
        localUpdateVersionRequest.versionCode = BaseApplication.getVersionCode();
        //   mMainPresenter.checkVersion(localUpdateVersionRequest, 1);


    }

    @OnClick({R.id.ll_wx, R.id.ll_address, R.id.ll_find, R.id.ll_me})
    public void onClick(View paramView) {
        mMainPresenter.switchNavigation(paramView.getId());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setOnItemClickListener(OnFABClickListener paramOnFABClickListener) {
        mOnFABClickListener = paramOnFABClickListener;
    }

    public void setViewPageListener() {
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int paramAnonymousInt) {
            }

            @Override
            public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {
                if (paramAnonymousFloat > 0.0F) {
                    ((ImageView) MainActivity.this.mListImage.get(paramAnonymousInt1)).setAlpha(1.0F - paramAnonymousFloat);
                    ((ImageView) MainActivity.this.mListImage.get(paramAnonymousInt1 + 1)).setAlpha(paramAnonymousFloat);
                    ((TextView) MainActivity.this.mListText.get(paramAnonymousInt1)).setAlpha(1.0F - paramAnonymousFloat);
                    ((TextView) MainActivity.this.mListText.get(paramAnonymousInt1 + 1)).setAlpha(paramAnonymousFloat);
                }
            }

            @Override
            public void onPageSelected(int paramAnonymousInt) {
                if (MainActivity.this.mListText.get(paramAnonymousInt).getText().toString().equals("特殊操作")) {
                    final Setting setting = new Setting(getContext());
                    boolean isWifi = setting.loadBoolean("isWifi");
                    if (isWifi) {
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_signal_wifi_4_bar_black_24dp));
                    } else {
                        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bluetooth_searching_black_24dp));
                    }

                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (MainActivity.this.mOnFABClickListener != null) {
                                MainActivity.this.mOnFABClickListener.OnFABClickListener(view);
                            }
                        }
                    });
                    fab.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            if (MainActivity.this.mOnFABClickListener != null) {
                                MainActivity.this.mOnFABClickListener.OnFABLongClickListener(v);
                            }
                            return false;
                        }
                    });
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View paramAnonymous2View) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if ((MainActivity.this.isFinishing()) || ((ApiLevelHelper.isAtLeast(17)) && (MainActivity.this.isDestroyed()))) {
                                    return;
                                }
                            }
                            fab.setVisibility(View.VISIBLE);
                        }
                    }).start();

                } else if (MainActivity.this.mListText.get(paramAnonymousInt).getText().toString().equals("资料管理")) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_erweima_searching_black_24dp));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View paramAnonymous2View) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if ((MainActivity.this.isFinishing()) || ((ApiLevelHelper.isAtLeast(17)) && (MainActivity.this.isDestroyed()))) {
                                    return;
                                }
                            }
                            fab.setVisibility(View.VISIBLE);
                        }
                    }).start();
                } else if (MainActivity.this.mListText.get(paramAnonymousInt).getText().toString().equals("用户购电")) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_erweima_searching_black_24dp));
                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View paramAnonymous2View) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if ((MainActivity.this.isFinishing()) || ((ApiLevelHelper.isAtLeast(17)) && (MainActivity.this.isDestroyed()))) {
                                    return;
                                }
                            }
                            fab.setVisibility(View.VISIBLE);
                        }
                    }).start();
                } else {
                    ViewCompat.animate(fab).scaleX(0.0F).scaleY(0.0F).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(100L).setListener(new ViewPropertyAnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(View paramAnonymous2View) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                                if ((MainActivity.this.isFinishing()) || ((ApiLevelHelper.isAtLeast(17)) && (MainActivity.this.isDestroyed()))) {
                                    return;
                                }
                            }
                            fab.setVisibility(View.GONE);
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    public void switchAddressBook() {
        if ((G.userRole + "").equals(currentUserType) || (G.testRole + "").equals(currentUserType) || (G.test2Role + "").equals(currentUserType)) {
            viewPage.setCurrentItem(0, false);
        } else {
            viewPage.setCurrentItem(1, false);
        }
        addressBookS.setAlpha(1.0F);
        tabAddressS.setAlpha(1.0F);
    }

    @Override
    public void switchAlpha(int paramInt) {
        for (int i = 0; i < viewId.length; i++) {
            if (paramInt != viewId[i]) {
                ((ImageView) mListImage.get(i)).setAlpha(0.0F);
                ((TextView) mListText.get(i)).setAlpha(0.0F);
            }
        }
    }

    @Override
    public void switchFind() {
        if ((G.test1Role + "").equals(currentUserType)) {
            viewPage.setCurrentItem(0, false);
        } else if ((G.userRole + "").equals(currentUserType) || (G.test2Role + "").equals(currentUserType) || (G.testRole + "").equals(currentUserType)) {
            viewPage.setCurrentItem(1, false);
        } else {
            viewPage.setCurrentItem(2, false);
        }

        findS.setAlpha(1.0F);
        tabFindS.setAlpha(1.0F);
    }

    @Override
    public void switchMe() {
        if (((G.userRole + "").equals(currentUserType) || (G.test2Role + "").equals(currentUserType) || (G.testRole + "").equals(currentUserType))) {
            viewPage.setCurrentItem(2, false);
        } else {
            viewPage.setCurrentItem(3, false);
        }
        meS.setAlpha(1.0F);
        tabMeS.setAlpha(1.0F);
    }

    @Override
    public void switchWX() {
        viewPage.setCurrentItem(0, false);
        weiXinS.setAlpha(1.0F);
        tabWeiXinS.setAlpha(1.0F);
    }


    @Override
    public void saveTransformer(boolean b, String id) {

    }


    public class MainBoradcastReceiver extends BroadcastReceiver {
        public MainBoradcastReceiver() {
        }

        @Override
        public void onReceive(Context paramContext, Intent paramIntent) {
        }
    }

    public interface OnFABClickListener {
        void OnFABClickListener(View paramView);

        void OnFABLongClickListener(View paramView);
    }

    private static final int REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY = 666;

    int REQUEST_CODE = 1001;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) {
                    return;
                }
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    String decryptString = "";
                    try {
                        // decryptString = AES.decrypt("1396198677119910", result);
                        decryptString = result;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Logger.e(getContext(), "扫描结果：" + decryptString);
                    try {
                        UserAllInfo userAllInfo = new Gson().fromJson(decryptString, UserAllInfo.class);
                        UserBean bean = userAllInfo.getUser();
                        String type = LoginInformation.getInstance().getUser().getType();
                        //自动登录 比较 ime账号
                        if (userAllInfo.getEcodeType().equals("0") && type.equals("0")) {
                            if ((G.adminRole + "").equals(type)) {
                                startActivity(new Intent(getContext(), CreateUserOneAcvitity.class).putExtra("ime", bean.getIme()));
                            } else {
                                showToast("当前账号没有权限创建营业厅管理员");
                            }
                            startActivity(new Intent(getContext(), CreateUserOneAcvitity.class).putExtra("ime", bean.getIme()));
                        } else if (userAllInfo.getEcodeType().equals("1")) {
                            if ((G.adminRole + "").equals(type) || (G.depRole + "").contains(type)) {
                                startActivity(new Intent(getContext(), CreateUserTwoAcvitity.class).putExtra("ime", bean.getIme()));
                            } else {
                                showToast("当前账号没有权限创建台区管理员");
                            }
                        } else if (userAllInfo.getEcodeType().equals("2")) {
                            startActivity(new Intent(getContext(), CreateUserThreeAcvitity.class).putExtra("ime", bean.getIme()));
                        } else if (userAllInfo.getEcodeType().equals("3")) {
                            //保存用户 方便登录
                            IUserPresenter presenter = new UserPresenterImpl();
                            presenter.saveOrUpdateUser(userAllInfo.getUser());
                            if (userAllInfo.getTransformerBean().getId() != null) {
                                TransformerBeanDaoHelper.getInstance().addData(userAllInfo.getTransformerBean());
                            }
                            if (userAllInfo.getSpotBeans().getId() != null) {
                                SpotPricingBeanDaoHelper.getInstance().addData(userAllInfo.getSpotBeans());
                            }
                        } else if (userAllInfo.getEcodeType().equals("4") && ("0,30,31,9,10").contains(type)) {
                            if (G.depRole.equals(type) || G.testRole.equals(type)) {
                                if (!LoginInformation.getInstance().getUser().getIsBuy().equals("1")) {
                                    //你当前没有权限扫描购电信息
                                    showToast("你当前没有权限扫描购电信息");
                                    return;
                                }
                                if (type.equals((G.depRole + ""))) {
                                    if (!bean.getStoreId().equals(LoginInformation.getInstance().getUser().getStoreId())) {
                                        //比较下当前是否在营业厅台区下
                                        showToast("该用户不在的营业厅下");
                                        return;
                                    }
                                }
                                if (type.equals((G.taiquRole + ""))) {
                                    if (!bean.getTransformerId().equals(LoginInformation.getInstance().getUser().getTransformerId())) {
                                        //比较下当前是否在营业厅台区下
                                        showToast("该用户不在的台区下");
                                        return;
                                    }
                                }
                            }
                            //保存用户 方便登录
                            try {
                                IUserPresenter presenter = new UserPresenterImpl();
                                presenter.saveOrUpdateUser(bean);
                                if (userAllInfo.getSpotBeans().getId() != null) {
                                    SpotPricingBeanDaoHelper.getInstance().addData(userAllInfo.getSpotBeans());
                                }
                            } catch (Exception e) {

                            }


                            startActivity(new Intent(getContext(), BuySpotActivity.class).putExtra("userAllInfo", userAllInfo));
                        } else if (userAllInfo.getEcodeType().equals("5") || userAllInfo.getEcodeType().equals("6")) {
                            //用户购电
//                            List<Purchase> pricingBeen = PricingDaoHelper.getInstance().queryByUserId(LoginInformation.getInstance().getUser().getUserId(), userAllInfo.getPricings().getIme(), userAllInfo.getPricings().getId());
//                            if (pricingBeen.size() > 0) {
//                                showToast("不能扫描该二维码,已经扫过一次了");
//                            } else {
//                                Log.e("扫描结果-LoginInformation--", LoginInformation.getInstance().getUser().getUserId());
//                                Log.e("扫描结果--pricings--", userAllInfo.getPricings().getUserId());
//                                if (LoginInformation.getInstance().getUser().getUserId().equals(userAllInfo.getPricings().getUserId())) {
//
//                                    IUserPresenter presenter = new UserPresenterImpl();
//                                    if (userAllInfo.getUser() != null) {
//                                        presenter.saveOrUpdateUser(userAllInfo.getUser());
//                                        setLoginInfomation(bean);
//                                    }
//                                    if (userAllInfo.getSpotBeans().getId() != null) {
//                                        SpotPricingBeanDaoHelper.getInstance().addData(userAllInfo.getSpotBeans());
//                                    }
//                                    PricingDaoHelper.getInstance().addData(userAllInfo.getPricings());
//                                    showToast("扫描购电信息成功，请查看购电记录完成购电");
//                                } else {
//                                    showToast("该购电二维码不属于你");
//                                }
//
//                            }
                        } else {
                            showToast("不是有效的二维码");
                        }
                    } catch (Exception e) {
                        showToast("二维码扫描错误，请对准二维码重新扫描");
                    }


                }
                if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_QRCODE_FROM_GALLERY) {
                    final String picturePath = BGAPhotoPickerActivity.getSelectedImages(data).get(0);
                    try {
                        CodeUtils.analyzeBitmap(picturePath, new CodeUtils.AnalyzeCallback() {
                            @Override
                            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {
                                String decryptString = "";
                                try {
                                    // decryptString = AES.decrypt(G.appsecret, result);
                                    decryptString = result;
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                Toast.makeText(getContext(), decryptString, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onAnalyzeFailed() {
                                Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(getContext(), "解析二维码失败", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }
    }

    private void setLoginInfomation(UserBean userBean) {
        Setting setting = new Setting(this);
        setting.saveString(G.currentUsername, userBean.getUserId());
        setting.saveString(G.currentUserType, userBean.getType());
        setting.saveString(G.currentStoreId, userBean.getStoreId());
        setting.saveString(G.currentStoreName, userBean.getStoreName());
        setting.saveString(G.currentTransformId, userBean.getTransformerId());
        setting.saveString(G.currentTransformName, userBean.getTransformerName());
        setting.saveString(G.currentPassword, userBean.getPassword());
        setting.saveString("user", new Gson().toJson(userBean));
        LoginInformation.getInstance().setUser(userBean);
    }
}