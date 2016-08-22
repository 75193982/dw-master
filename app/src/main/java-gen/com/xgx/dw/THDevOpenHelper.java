package com.xgx.dw;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.github.yuweiguocn.library.greendao.MigrationHelper;

/**
 * Created by Administrator on 2016/8/13.
 */
public class THDevOpenHelper
        extends DaoMaster.OpenHelper {
    public THDevOpenHelper(Context paramContext, String paramString, SQLiteDatabase.CursorFactory paramCursorFactory) {
        super(paramContext, paramString, paramCursorFactory);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        //Log.i("greenDAO", "Upgrading schema from version " + paramInt1 + " to " + paramInt2 + " by dropping all tables");
        MigrationHelper.getInstance().migrate(paramSQLiteDatabase, new Class[]{UserBeanDao.class, TransformerBeanDao.class, StoreBeanDao.class, SpotPricingBeanDao.class});
    }
}