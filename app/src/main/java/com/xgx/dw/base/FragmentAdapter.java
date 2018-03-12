package com.xgx.dw.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xgx.dw.app.G;
import com.xgx.dw.app.Setting;
import com.xgx.dw.ui.fragment.FragmentFactory;

/**
 * Created by Administrator on 2016/8/13.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    private FragmentFactory mFragmentFactory = new FragmentFactory(4);
    private String type;

    public FragmentAdapter(FragmentManager paramFragmentManager, String type) {
        super(paramFragmentManager);
        this.type = type;
        if (("20,30,32").contains(type)) {
            mFragmentFactory = new FragmentFactory(3);
        } else if ("31".contains(type)) {
            mFragmentFactory = new FragmentFactory(1);
        }

    }

    @Override
    public int getCount() {
        return this.mFragmentFactory.getSize();
    }

    @Override
    public Fragment getItem(int paramInt) {
        return this.mFragmentFactory.createFragment(paramInt, type);
    }
}
