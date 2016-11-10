package com.xgx.dw.ui.activity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.xgx.dw.PricingBean;
import com.xgx.dw.R;
import com.xgx.dw.adapter.SpotListAdapter;
import com.xgx.dw.app.G;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.dao.PricingDaoHelper;
import com.xgx.dw.utils.AES;
import com.xgx.dw.utils.MyStringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/9.
 */

public class BuyUserDataActivity extends BaseAppCompatActivity {

    @Bind(R.id.startTimeTv)
    TextView startTimeTv;
    @Bind(R.id.endTimeTv)
    TextView endTimeTv;
    @Bind(R.id.comfirmBtn)
    Button comfirmBtn;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    @Bind(R.id.resultTv)
    TextView resultTv;
    private DatePickerDialog mDataPicker;
    private DatePickerDialog mDataEndTimePicker;
    private List<PricingBean> beans;
    private SpotListAdapter adapter;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_buy_data);
    }

    @Override
    public void initView() {
        setToolbarTitle("购电报表");
        beans = new ArrayList();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, 1, false));
        adapter = new SpotListAdapter(beans);
        this.adapter.openLoadAnimation();
        View localView = LayoutInflater.from(this).inflate(R.layout.base_empty_view, (ViewGroup) this.recyclerView.getParent(), false);
        adapter.setEmptyView(localView);
        recyclerView.setAdapter(this.adapter);
        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, View view, int i) {
            }
        });
    }

    @Override
    public void initPresenter() {
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void getData() {
        String userid = LoginInformation.getInstance().getUser().getId();
        beans = PricingDaoHelper.getInstance().queryByAdminId(userid);
        int num = 0;
        if (beans != null && beans.size() > 0) {
            for (int i = 0; i < beans.size(); i++) {
                String price = "";
                try {
                    price = AES.decrypt(G.appsecret, beans.get(i).getPrice());
                } catch (Exception e) {
                    price = "";
                }
                num += MyStringUtils.toInt(price, 0);
            }
            resultTv.setText("合计 " + num + "元\n购电用户 " + beans.size() + " 位");
        }
        adapter.setNewData(beans);
    }

      @OnClick({R.id.startTimeTv, R.id.endTimeTv, R.id.comfirmBtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.startTimeTv:
                getDatePickerDialog(view);
                mDataPicker.show();
                break;
            case R.id.endTimeTv:
                getDatePickerDialog(view);
                mDataPicker.show();
                break;
            case R.id.comfirmBtn:
                //优先比较 时间
                String starttime = startTimeTv.getText().toString();
                String endTime = endTimeTv.getText().toString();
                int i = compare_date(starttime, endTime);
                if (i == 1) {
                    showToast("请检查查询时间是否正确");
                    return;
                }
                List<PricingBean> tempList = new ArrayList<>();
                for (int j = 0; j < beans.size(); j++) {
                    String createTime = beans.get(j).getCreateTime();
                    int k1 = compare_date(starttime, createTime);
                    int k2 = compare_date(createTime, endTime);
                    if (k1 != 1 && k2 != -1) {
                        tempList.add(beans.get(j));
                    }
                }
                int num = 0;
                if (tempList != null && tempList.size() > 0) {

                    for (int t = 0; t < tempList.size(); t++) {
                        String price = "";
                        try {
                            price = AES.decrypt(G.appsecret, tempList.get(t).getPrice());
                        } catch (Exception e) {
                            price = "";
                        }
                        num += MyStringUtils.toInt(price, 0);
                    }
                }
                resultTv.setText("合计 " + num + "元\n购电用户 " + tempList.size() + " 位");
                adapter.setNewData(tempList);
                break;
        }
    }

    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd  EE");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取日期选择器
     */
    private void getDatePickerDialog(final View targetView) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        mDataPicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, monthOfYear, dayOfMonth);
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd  EE");
                switch (targetView.getId()) {
                    case R.id.startTimeTv:
                        startTimeTv.setText(df.format(calendar.getTime()));
                        break;
                    case R.id.endTimeTv:
                        endTimeTv.setText(df.format(calendar.getTime()));
                        break;
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }
}
