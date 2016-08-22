package com.xgx.dw.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.xgx.dw.ui.fragment.FragmentFactory;

/**
 * Created by Administrator on 2016/8/13.
 */
public class FragmentAdapter extends FragmentStatePagerAdapter {
    private FragmentFactory mFragmentFactory = new FragmentFactory(4);

    public FragmentAdapter(FragmentManager paramFragmentManager) {
        super(paramFragmentManager);
    }

    public int getCount() {
        return this.mFragmentFactory.getSize();
    }

    public Fragment getItem(int paramInt) {
        return this.mFragmentFactory.createFragment(paramInt);
    }
}
