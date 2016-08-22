package com.xgx.dw.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xgx.dw.R;
import com.xgx.dw.adapter.ViewHolder.TextViewHolder;
import com.xgx.dw.ui.fragment.dummy.DummyContent;

import java.util.List;

public class DataSearchItemAdapter
        extends RecyclerView.Adapter<TextViewHolder> {
    private static final int ITEM_VIEW_TYPE_HEADER = 0;
    private static final int ITEM_VIEW_TYPE_ITEM = 1;
    private final View header;
    private final List<DummyContent> labels;
    private MyOnItemClickListner onItemClickListner;

    public DataSearchItemAdapter(View paramView, List<DummyContent> paramList) {
        if (paramView == null) {
            throw new IllegalArgumentException("header may not be null");
        }
        this.header = paramView;
        this.labels = paramList;
    }

    public int getItemCount() {
        return 1 + this.labels.size();
    }

    public int getItemViewType(int paramInt) {
        if (isHeader(paramInt)) {
            return 0;
        }
        return 1;
    }

    public MyOnItemClickListner getOnItemClickListner() {
        return this.onItemClickListner;
    }

    public boolean isHeader(int paramInt) {
        return paramInt == 0;
    }

    public void onBindViewHolder(TextViewHolder paramTextViewHolder, final int paramInt) {
        if (isHeader(paramInt)) {
            return;
        }
        String str = ((DummyContent) this.labels.get(paramInt - 1)).getDetails();
        paramTextViewHolder.icon.setImageDrawable(ContextCompat.getDrawable(paramTextViewHolder.icon.getContext(), ((DummyContent) this.labels.get(paramInt - 1)).getRes()));
        paramTextViewHolder.textView.setText(str);
        paramTextViewHolder.item_layout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (DataSearchItemAdapter.this.onItemClickListner != null) {
                    DataSearchItemAdapter.this.onItemClickListner.onrRecyclerViewItemClick(paramInt - 1);
                }
            }
        });
    }

    public TextViewHolder onCreateViewHolder(ViewGroup paramViewGroup, int paramInt) {
        if (paramInt == 0) {
            return new TextViewHolder(this.header);
        }
        return new TextViewHolder(LayoutInflater.from(paramViewGroup.getContext()).inflate(R.layout.fragment_datasearch_item, paramViewGroup, false));
    }

    public void setOnItemClickListner(MyOnItemClickListner paramMyOnItemClickListner) {
        this.onItemClickListner = paramMyOnItemClickListner;
    }

    public static abstract interface MyOnItemClickListner {
        public abstract void onrRecyclerViewItemClick(int paramInt);
    }
}
