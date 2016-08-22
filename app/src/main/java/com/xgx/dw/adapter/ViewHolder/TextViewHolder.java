package com.xgx.dw.adapter.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgx.dw.R;

public class TextViewHolder
        extends RecyclerView.ViewHolder {
    public ImageView icon;
    public LinearLayout item_layout;
    public TextView textView;

    public TextViewHolder(View paramView) {
        super(paramView);
        this.textView = ((TextView) paramView.findViewById(R.id.content));
        this.icon = ((ImageView) paramView.findViewById(R.id.icon));
        this.item_layout = ((LinearLayout) paramView.findViewById(R.id.item_layout));
    }
}
