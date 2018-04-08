package com.xgx.dw.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.base.EventCenter;
import com.xgx.dw.bean.Price;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SpecialAdminToolsAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SpecialAdminToolsAdapter(List<String> paramList) {
        super(R.layout.adapter_admin_tools, paramList);
    }

    protected void convert(final BaseViewHolder baseViewHolder, String code) {
        try {
            final MaterialEditText editText = baseViewHolder.getView(R.id.code);
            editText.setFloatingLabelText("报文" + (baseViewHolder.getLayoutPosition() + 1));
            editText.setText(code + "");
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    getData().set(baseViewHolder.getLayoutPosition(), s.toString());
                    EventBus.getDefault().post(new EventCenter<>(EventCenter.ADMIN_TOOLS));
                }
            });
        } catch (Exception e) {

        }

    }
}
