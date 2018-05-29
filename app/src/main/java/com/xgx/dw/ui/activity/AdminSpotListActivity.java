package com.xgx.dw.ui.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.xgx.dw.R;
import com.xgx.dw.adapter.SpotListAdapter;
import com.xgx.dw.base.BaseAppCompatActivity;
import com.xgx.dw.bean.LoginInformation;
import com.xgx.dw.bean.Purchase;
import com.xgx.dw.net.DialogCallback;
import com.xgx.dw.net.LzyResponse;
import com.xgx.dw.net.URLs;

import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/10/16 0016.
 */
public class AdminSpotListActivity extends BaseAppCompatActivity {
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    SpotListAdapter adapter;
    private List<Purchase> beans;
    @BindView(R.id.numTv)
    TextView numTv;

    @BindView(R.id.startTimeTv)
    TextView startTimeTv;
    @BindView(R.id.endTimeTv)
    TextView endTimeTv;
    @BindView(R.id.comfirmBtn)
    TextView comfirmBtn;
    private DatePickerDialog mDataPicker;

    @Override
    public void initContentView() {
        baseSetContentView(R.layout.activity_spot_list);
    }

    @Override
    public void initView() {
        setToolbarTitle("购电记录");
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
            public void SimpleOnItemClick(BaseQuickAdapter baseQuickAdapter, final View view, int i) {


                startActivity(new Intent(getContext(), TestGeneratectivity.class).putExtra("type", 5).putExtra("id", adapter.getItem(i).getOpcode()));
            }

            @Override
            public void onItemLongClick(final BaseQuickAdapter baseQuickAdapter, final View view, final int position) {
                super.onItemLongClick(baseQuickAdapter, view, position);
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
                alertDialog.setTitle("提示");
                alertDialog.setMessage("是否要生成打印单？");
                alertDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, final int i) {
                        dialogInterface.dismiss();
                        //生产订单图片
                        new Handler().post(new Runnable() {
                            @Override
                            public void run() {
                                viewSaveToImage(view, adapter.getItem(position).getOpcode() + "-" + adapter.getItem(position).getCreatetime());
                            }
                        });
                    }
                });
                alertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                alertDialog.show();
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void initPresenter() {
        getDatas();
        //查询电价
//        String userid = getIntent().getStringExtra("id");
//        beans = PricingDaoHelper.getInstance().queryByUserId(userid);
//        if (beans != null && beans.size() > 0) {
//            int num = 0;
//            for (int i = 0; i < beans.size(); i++) {
//                String price = "";
//                try {
//                    price = AES.decrypt(G.appsecret, beans.get(i).getPrice());
//                } catch (Exception e) {
//                    price = "";
//                }
//                num += MyStringUtils.toInt(price, 0);
//            }
//            numTv.setText(num + "元");
//        }
//        adapter.setNewData(beans);
    }

    private void getDatas() {
        Purchase purchase = new Purchase();
        purchase.setUserid(getIntent().getStringExtra("id"));
        OkGo.<LzyResponse<Purchase>>post(URLs.getURL(URLs.BUY_SPOT_LIST))
                .upJson(URLs.getRequstJsonString(purchase))
                .execute(new DialogCallback<LzyResponse<Purchase>>(this) {
                    @Override
                    public void onSuccess(Response<LzyResponse<Purchase>> response) {
                        beans = ((JSONArray) response.body().model).toJavaList(Purchase.class);
                        adapter.setNewData(beans);
                        BigDecimal num = new BigDecimal(0);
                        for (int i = 0; i < beans.size(); i++) {
                            String price = "";
                            try {
                                price = beans.get(i).getAmt();
                            } catch (Exception e) {
                                price = "";
                            }
                            num = new BigDecimal(price).add(num);
                        }
                        numTv.setText(num + "元");
                    }
                });
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
                List<Purchase> tempList = new ArrayList<>();
                for (int j = 0; j < beans.size(); j++) {
                    String createTime = beans.get(j).getCreatetime();
                    int k1 = compare_date(createTime, starttime);
                    int k2 = compare_date(endTime, createTime);
                    if (k1 != -1 && k2 != -1) {
                        tempList.add(beans.get(j));
                    }
                }
                BigDecimal num = new BigDecimal(0);
                for (int k = 0; k < tempList.size(); k++) {
                    String price = "";
                    try {
                        price = tempList.get(k).getAmt();
                    } catch (Exception e) {
                        price = "";
                    }
                    num = new BigDecimal(price).add(num);
                }
                numTv.setText(num + "元");
                adapter.setNewData(tempList);
                break;
        }
    }

    public static int compare_date(String DATE1, String DATE2) {


        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
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

    public void viewSaveToImage(View view, String name) {
        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        view.setDrawingCacheBackgroundColor(Color.WHITE);

        // 把一个View转换成图片
        Bitmap cachebmp = loadBitmapFromView(view);

        // 添加水印
        Bitmap bitmap = Bitmap.createBitmap(createWatermarkBitmap(cachebmp, "中衡电气"));

        FileOutputStream fos;
        try {
            // 判断手机设备是否有SD卡
            boolean isHasSDCard = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);

            if (isHasSDCard) {
                // SD卡根目录
                File sdRoot = Environment.getExternalStorageDirectory();
                String image_dir = Environment.getExternalStorageDirectory() + "/dw/Cache/ecodeEr";

                File image_dirs = new File(image_dir);
                if (!image_dirs.exists()) {
                    image_dirs.mkdirs();
                }
                String filenewpath = image_dir + "/" + name + ".PNG";
                File file = new File(filenewpath);
                fos = new FileOutputStream(file);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + filenewpath)));
                showToast("成功创建打印图片,保存于：" + filenewpath);
            } else throw new Exception("创建文件失败!");

            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.destroyDrawingCache();
    }

    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);

        c.drawColor(Color.WHITE);
        /** 如果不设置canvas画布为白色，则生成透明 */

        v.layout(0, 0, w, h);
        v.draw(c);

        return bmp;
    }

    // 为图片target添加水印
    private Bitmap createWatermarkBitmap(Bitmap target, String str) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();

        // 水印的颜色
        p.setColor(Color.RED);

        // 水印的字体大小
        p.setTextSize(16);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在中间位置开始添加水印
        canvas.drawText(str, w / 2, h / 2, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }
}
