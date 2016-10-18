package com.xgx.dw.ui.fragment;

import android.support.v4.app.Fragment;

public class FragmentFactory {
    private Fragment mFragment;
    private int mSize;

    public FragmentFactory(int paramInt) {
        this.mSize = paramInt;
    }

    public Fragment createFragment(int paramInt, String type) {
        if (type.equals("20") || type.equals("30") || type.equals("32")) {
            switch (paramInt) {
                case 0:
                    this.mFragment = new BuyItemFragment();
                    break;
                case 1:
                    this.mFragment = new SpecialOperationFragment();
                    break;
                case 2:
                    this.mFragment = new DataSearchFragment();
                    break;
            }
        } else if (type.equals("31")) {
            switch (paramInt) {
                case 0:
                    this.mFragment = new SpecialOperationFragment();
                    break;
            }
        } else {
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
        }
        return this.mFragment;


    }

    public int getSize() {
        return this.mSize;
    }
}


