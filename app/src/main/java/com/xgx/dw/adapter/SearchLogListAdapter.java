package com.xgx.dw.adapter;

import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hymane.expandtextview.ExpandTextView;
import com.xgx.dw.R;
import com.xgx.dw.SearchDlLog;
import com.xgx.dw.ui.view.interfaces.TextViewExpandableAnimation;

import java.util.List;

public class SearchLogListAdapter extends BaseQuickAdapter<SearchDlLog, BaseViewHolder> {
    public SearchLogListAdapter(List<SearchDlLog> paramList) {
        super(R.layout.searchlog_list_item, paramList);
    }

    protected void convert(BaseViewHolder baseViewHolder, SearchDlLog bean) {
        TextViewExpandableAnimation title = baseViewHolder.getView(R.id.title);
        title.setText(Html.fromHtml(bean.getContent()));
    }
}
