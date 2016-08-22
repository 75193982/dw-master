package com.xgx.dw.ui.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgx.dw.R;
import com.xgx.dw.app.BaseApplication;
import com.xgx.dw.base.BaseActivity;
import com.xgx.dw.base.FragmentAdapter;
import com.xgx.dw.presenter.impl.MainPresenterImpl;
import com.xgx.dw.presenter.interfaces.IMainPresenter;
import com.xgx.dw.ui.view.interfaces.IMainView;
import com.xgx.dw.utils.ApiLevelHelper;
import com.xgx.dw.vo.request.UpdateVersionRequest;
import com.xgx.dw.vo.response.UpdateVersionResult;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements IMainView {
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

    public void checkVersionCallBack(UpdateVersionResult paramUpdateVersionResult) {
        Intent localIntent = new Intent(this, UploadDialogActivity.class);
        localIntent.putExtra("response", paramUpdateVersionResult);
        startActivity(localIntent);
    }


    public void initContentView() {
        setContentView(R.layout.activity_main);
    }

    public void initPresenter() {
        this.mMainPresenter = new MainPresenterImpl(this);
    }

    public void initView() {
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (MainActivity.this.mOnFABClickListener != null) {
                    MainActivity.this.mOnFABClickListener.OnFABClickListener(paramAnonymousView);
                }
            }
        });
        fab.setVisibility(View.GONE);
        FragmentAdapter localFragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        this.viewPage.setAdapter(localFragmentAdapter);
        this.weiXinS.setAlpha(1.0F);
        this.tabWeiXinS.setAlpha(1.0F);
        setViewPageListener();
        MainBoradcastReceiver localMainBoradcastReceiver = new MainBoradcastReceiver();
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("com.xgx.dw.main");
        registerReceiver(localMainBoradcastReceiver, localIntentFilter);
        this.viewId = new int[]{R.id.ll_wx, R.id.ll_address, R.id.ll_find, R.id.ll_me};
        this.mListImage.add(this.weiXinS);
        this.mListImage.add(this.addressBookS);
        this.mListImage.add(this.findS);
        this.mListImage.add(this.meS);
        this.mListText.add(this.tabWeiXinS);
        this.mListText.add(this.tabAddressS);
        this.mListText.add(this.tabFindS);
        this.mListText.add(this.tabMeS);
        UpdateVersionRequest localUpdateVersionRequest = new UpdateVersionRequest();
        localUpdateVersionRequest.versionCode = BaseApplication.getVersionCode();
        this.mMainPresenter.checkVersion(localUpdateVersionRequest, 1);
    }

    @OnClick({R.id.ll_wx, R.id.ll_address, R.id.ll_find, R.id.ll_me})
    public void onClick(View paramView) {
        this.mMainPresenter.switchNavigation(paramView.getId());
    }

    protected void onResume() {
        super.onResume();
    }

    public void setOnItemClickListener(OnFABClickListener paramOnFABClickListener) {
        this.mOnFABClickListener = paramOnFABClickListener;
    }

    public void setViewPageListener() {
        this.viewPage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
                if (paramAnonymousInt == 2) {
                    ViewCompat.animate(fab).scaleX(1.0F).scaleY(1.0F).setInterpolator(new LinearOutSlowInInterpolator()).setListener(new ViewPropertyAnimatorListenerAdapter() {
                        public void onAnimationEnd(View paramAnonymous2View) {
                            if ((MainActivity.this.isFinishing()) || ((ApiLevelHelper.isAtLeast(17)) && (MainActivity.this.isDestroyed()))) {
                                return;
                            }
                            fab.setVisibility(View.VISIBLE);
                        }
                    }).start();
                } else {
                    ViewCompat.animate(fab).scaleX(0.0F).scaleY(0.0F).setInterpolator(new FastOutSlowInInterpolator()).setStartDelay(100L).setListener(new ViewPropertyAnimatorListenerAdapter() {
                        public void onAnimationEnd(View paramAnonymous2View) {
                            if ((MainActivity.this.isFinishing()) || ((ApiLevelHelper.isAtLeast(17)) && (MainActivity.this.isDestroyed()))) {
                                return;
                            }
                            fab.setVisibility(View.GONE);
                        }
                    }).start();
                }
            }
        });
    }

    public void switchAddressBook() {
        this.viewPage.setCurrentItem(1, false);
        this.addressBookS.setAlpha(1.0F);
        this.tabAddressS.setAlpha(1.0F);
    }

    public void switchAlpha(int paramInt) {
        for (int i = 0; i < this.viewId.length; i++) {
            if (paramInt != this.viewId[i]) {
                ((ImageView) this.mListImage.get(i)).setAlpha(0.0F);
                ((TextView) this.mListText.get(i)).setAlpha(0.0F);
            }
        }
    }

    public void switchFind() {
        this.viewPage.setCurrentItem(2, false);
        this.findS.setAlpha(1.0F);
        this.tabFindS.setAlpha(1.0F);
    }

    public void switchMe() {
        this.viewPage.setCurrentItem(3, false);
        this.meS.setAlpha(1.0F);
        this.tabMeS.setAlpha(1.0F);
    }

    public void switchWX() {
        this.viewPage.setCurrentItem(0, false);
        this.weiXinS.setAlpha(1.0F);
        this.tabWeiXinS.setAlpha(1.0F);
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
}
