package com.xgx.dw.adapter;

import android.text.Editable;
import android.text.TextWatcher;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.xgx.dw.R;
import com.xgx.dw.base.EventCenter;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class SpecialAdminToolsDzAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public SpecialAdminToolsDzAdapter(List<String> paramList) {
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

    private String getTitleByPosition(int position) {
        String title = "";
        switch (position) {
            case 0:
                title = "速断电流";
                break;
            case 1:
                title = "速断时间";
                break;
            case 2:
                title = "速断跳闸";
                break;
            case 3:
                title = "零序电流";
                break;
            case 4:
                title = "零序时间";
                break;
            case 5:
                title = "零序跳闸";
                break;
            case 6:
                title = "过流1段电流";
                break;
            case 7:
                title = "过流1段时间";
                break;
            case 8:
                title = "过流2段电流";
                break;
            case 9:
                title = "过流2段时间";
                break;
            case 10:
                title = "过流3段电流";
                break;
            case 11:
                title = "过流3段时间";
                break;
            case 12:
                title = "过流跳闸";
                break;
        }
        return title;
    }
}
