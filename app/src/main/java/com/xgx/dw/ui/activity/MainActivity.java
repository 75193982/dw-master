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
import com.xgx.dw.bean.UserAllInfo;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.dao.StoreBeanDaoHelper;
import com.xgx.dw.dao.TransformerBeanDaoHelper;
import com.xgx.dw.dao.UserBeanDaoHelper;
import com.xgx.dw.presenter.impl.MainPresenterImpl;
import com.xgx.dw.presenter.impl.UserPresenterImpl;
import com.xgx.dw.presenter.interfaces.IMainPresenter;
import com.xgx.dw.presenter.interfaces.IUserPresenter;
import com.xgx.dw.ui.view.interfaces.IMainView;
import com.xgx.dw.ui.view.interfaces.IUserView;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.ApiLevelHelper;
import com.xgx.dw.utils.Logger;
import com.xgx.dw.utils.MyUtils;
import com.xgx.dw.vo.request.UpdateVersionRequest;
import com.xgx.dw.vo.response.UpdateVersionResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;

public class MainActivity extends BaseActivity implements IMainView, IUserView {
    private final String ACTION_FILTER = "com.xgx.dw.main";
    @Bind(R.id.wei_xin_s)
    ImageView weiXinS;
    @Bind(R.id.tab_weiXin)
    TextView tabWeiXin;
    @Bind(R.id.tab_weiXin_s)
    TextView tabWeiXinS;
    @Bind(R.id.ll_wx)
    LinearLayout llWx;
    @Bind(R.id.address_book_s)
    ImageView addressBookS;
    @Bind(R.id.tab_address)
    TextView tabAddress;
    @Bind(R.id.tab_address_s)
    TextView tabAddressS;
    @Bind(R.id.ll_address)
    LinearLayout llAddress;
    @Bind(R.id.find_s)
    ImageView findS;
    @Bind(R.id.tab_find)
    TextView tabFind;
    @Bind(R.id.tab_find_s)
    TextView tabFindS;
    @Bind(R.id.ll_find)
    LinearLayout llFind;
    @Bind(R.id.me_s)
    ImageView meS;
    @Bind(R.id.tab_me)
    TextView tabMe;
    @Bind(R.id.tab_me_s)
    TextView tabMeS;
    @Bind(R.id.ll_me)
    LinearLayout llMe;
    @Bind(R.id.ll_tab)
    LinearLayout llTab;
    @Bind(R.id.line)
    View line;
    @Bind(R.id.viewPage)
    ViewPager viewPage;
    @Bind(R.id.coordinatorLayout)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private List<ImageView> mListImage = new ArrayList();
    private List<TextView> mListText = new ArrayList();
    private IMainPresenter mMainPresenter;
    private OnFABClickListener mOnFABClickListener;
    private int[] viewId;
    private String currentUserType;

    public void checkVersionCallBack(UpdateVersionResult paramUpdateVersionResult) {
        Intent localIntent = new Intent(this, UploadDialogActivity.class);
        localIntent.putExtra("response", paramUpdateVersionResult);
        startActivity(localIntent);
    }


    public void initContentView() {
        setContentView(R.layout.activity_main);
    }

    public void initPresenter() {
        mMainPresenter = new MainPresenterImpl(this);
    }

    public void initView() {

        fab.setVisibility(View.VISIBLE);
        fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_erweima_searching_black_24dp));
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CaptureActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });
        Setting setting = new Setting(this);
        currentUserType = setting.loadString(G.currentUserType);
        FragmentAdapter localFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), currentUserType);
        if ("20,30,32".contains(currentUserType)) {//普通用户 不显示资源管理按钮
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
        } else if (currentUserType.equals("31")) {//供电局调试账户 不显示资源管理按钮
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
        mMainPresenter.checkVersion(localUpdateVersionRequest, 1);


    }

    @OnClick({R.id.ll_wx, R.id.ll_address, R.id.ll_find, R.id.ll_me})
    public void onClick(View paramView) {
        mMainPresenter.switchNavigation(paramView.getId());
    }

    protected void onResume() {
        super.onResume();
    }

    public void setOnItemClickListener(OnFABClickListener paramOnFABClickListener) {
        mOnFABClickListener = paramOnFABClickListener;
    }

    public void setViewPageListener() {
        viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int paramAnonymousInt) {
            }

            public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat, int paramAnonymousInt2) {
                if (paramAnonymousFloat > 0.0F) {
                    ((ImageView) MainActivity.this.mListImage.get(paramAnonymousInt1)).setAlpha(1.0F - paramAnonymousFloat);
                    ((ImageView) MainActivity.this.mListImage.get(paramAnonymousInt1 + 1)).setAlpha(paramAnonymousFloat);
                    ((TextView) MainActivity.this.mListText.get(paramAnonymousInt1)).setAlpha(1.0F - paramAnonymousFloat);
                    ((TextView) MainActivity.this.mListText.get(paramAnonymousInt1 + 1)).setAlpha(paramAnonymousFloat);
                }
            }

            public void onPageSelected(int paramAnonymousInt) {
                if (MainActivity.this.mListText.get(paramAnonymousInt).getText().toString().equals("特殊操作")) {
                    fab.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_bluetooth_searching_black_24dp));
                    fab.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View view) {
                            if (MainActivity.this.mOnFABClickListener != null) {
                                MainActivity.this.mOnFABClickListener.OnFABClickListener(view);
                            }
                        }
                    });
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
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
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
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
                        public void onClick(View view) {
                            Intent intent = new Intent(getContext(), CaptureActivity.class);
                            startActivityForResult(intent, REQUEST_CODE);
                        }
                    });
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
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

    public void switchAddressBook() {
        if ("20,30,32".contains(currentUserType)) {
            viewPage.setCurrentItem(0, false);
        } else {
            viewPage.setCurrentItem(1, false);
        }
        addressBookS.setAlpha(1.0F);
        tabAddressS.setAlpha(1.0F);
    }

    public void switchAlpha(int paramInt) {
        for (int i = 0; i < viewId.length; i++) {
            if (paramInt != viewId[i]) {
                ((ImageView) mListImage.get(i)).setAlpha(0.0F);
                ((TextView) mListText.get(i)).setAlpha(0.0F);
            }
        }
    }

    public void switchFind() {
        if ("31".contains(currentUserType)) {
            viewPage.setCurrentItem(0, false);
        } else if ("20,30,32".contains(currentUserType)) {
            viewPage.setCurrentItem(1, false);
        } else {
            viewPage.setCurrentItem(2, false);
        }

        findS.setAlpha(1.0F);
        tabFindS.setAlpha(1.0F);
    }

    public void switchMe() {
        if ("20,30,32".contains(currentUserType)) {
            viewPage.setCurrentItem(2, false);
        } else {
            viewPage.setCurrentItem(3, false);
        }
        meS.setAlpha(1.0F);
        tabMeS.setAlpha(1.0F);
    }

    public void switchWX() {
        viewPage.setCurrentItem(0, false);
        weiXinS.setAlpha(1.0F);
        tabWeiXinS.setAlpha(1.0F);
    }

    @Override
    public void saveTransformer(boolean b) {

    }


    public class MainBoradcastReceiver extends BroadcastReceiver {
        public MainBoradcastReceiver() {
        }

        public void onReceive(Context paramContext, Intent paramIntent) {
        }
    }

    public static abstract interface OnFABClickListener {
        public abstract void OnFABClickListener(View paramView);

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
                        decryptString = AES.decrypt("1396198677119910", result);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Logger.e(getContext(), "扫描结果：" + decryptString);
                    try {
                        UserAllInfo userAllInfo = new Gson().fromJson(decryptString, UserAllInfo.class);
                        UserBean bean = userAllInfo.getUser();
                        String type = LoginInformation.getInstance().getUser().getType();
                        //自动登录 比较 ime账号
                        if (bean.getEcodeType().equals("0") && type.equals("admin")) {
                            if ("admin".contains(type)) {
                                startActivity(new Intent(getContext(), CreateUserOneAcvitity.class).putExtra("ime", bean.getIme()));
                            } else {
                                showToast("当前账号没有权限创建营业厅管理员");
                            }
                            startActivity(new Intent(getContext(), CreateUserOneAcvitity.class).putExtra("ime", bean.getIme()));
                        } else if (bean.getEcodeType().equals("1")) {
                            if ("admin,31".contains(type)) {
                                startActivity(new Intent(getContext(), CreateUserTwoAcvitity.class).putExtra("ime", bean.getIme()));
                            } else {
                                showToast("当前账号没有权限创建台区管理员");
                            }
                        } else if (bean.getEcodeType().equals("2")) {
                            startActivity(new Intent(getContext(), CreateUserThreeAcvitity.class).putExtra("ime", bean.getIme()));
                        } else if (bean.getEcodeType().equals("3")) {
                            //保存用户 方便登录
                            IUserPresenter presenter = new UserPresenterImpl();
                            presenter.saveUser(MainActivity.this, bean, Integer.valueOf(bean.getType()), false);
                            if (userAllInfo.getStoreBean().getId() != null) {
                                StoreBeanDaoHelper.getInstance().addData(userAllInfo.getStoreBean());
                            }
                            if (userAllInfo.getTransformerBean().getId() != null) {
                                TransformerBeanDaoHelper.getInstance().addData(userAllInfo.getTransformerBean());
                            }
                        } else if (bean.getEcodeType().equals("4") && ("admin,30,31").contains(type)) {
                            //保存用户 方便登录
                            startActivity(new Intent(getContext(), BuySpotActivity.class).putExtra("userAllInfo", userAllInfo));
                        } else if (bean.getEcodeType().equals("5")) {
                            //保存用户 方便登录
                            IUserPresenter presenter = new UserPresenterImpl();
                            presenter.saveUser(MainActivity.this, bean, Integer.valueOf(bean.getType()), false);
                            if (userAllInfo.getStoreBean().getId() != null) {
                                StoreBeanDaoHelper.getInstance().addData(userAllInfo.getStoreBean());
                            }
                            if (userAllInfo.getTransformerBean().getId() != null) {
                                TransformerBeanDaoHelper.getInstance().addData(userAllInfo.getTransformerBean());
                            }

                            if (!PricingDaoHelper.getInstance().hasKey(userAllInfo.getPricings().getId())) {
                                PricingDaoHelper.getInstance().addData(userAllInfo.getPricings());
                                showToast("扫描购电信息成功，请查看购电记录完成购电");
                            } else {
                                showToast("已经扫描过该条购电记录");

                            }
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
                                    decryptString = AES.decrypt(G.appsecret, result);
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
}