package com.xgx.dw.base;

import android.os.Bundle;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public abstract class BaseEventBusActivity extends BaseAppCompatActivity {
    protected abstract void onEventComming(EventCenter eventCenter);

    public abstract boolean isBindEventBusHere();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (this.isBindEventBusHere()) {
            EventBus.getDefault().unregister(this);
        }

    }

    @Subscribe
    public void onEventMainThread(EventCenter eventCenter) {
        if (null != eventCenter) {
            this.onEventComming(eventCenter);
        }

    }
}
