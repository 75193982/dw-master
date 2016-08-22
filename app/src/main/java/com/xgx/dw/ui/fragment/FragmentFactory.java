package com.xgx.dw.ui.fragment;

import android.support.v4.app.Fragment;

public class FragmentFactory {
    private Fragment mFragment;
    private int mSize;

    public FragmentFactory(int paramInt) {
        this.mSize = paramInt;
    }

    public Fragment createFragment(int paramInt) {
        switch (paramInt) {
            case 0:
                this.mFragment = new ResouceMgrFragment();
                break;
            case 1:
                this.mFragment = new BuyItemFragment();
                break;
            case 2:
                this.mFragment = new SpecialOperationFragment();
                break;
            case 3:
                this.mFragment = new DataSearchFragment();
                break;
        }
        return this.mFragment;


    }

    public int getSize() {
        return this.mSize;
    }
}


