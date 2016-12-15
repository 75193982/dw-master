package com.xgx.dw;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.xgx.dw.SearchDlLog;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SearchDlLog.
*/
public class SearchDlLogDao extends AbstractDao<SearchDlLog, String> {

    public static final String TABLENAME = "SearchDlLog";

    /**
     * Properties of entity SearchDlLog.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Content = new Property(1, String.class, "content", false, "CONTENT");
        public final static Property Type = new Property(2, String.class, "type", false, "TYPE");
        public final static Property UserId = new Property(3, String.class, "userId", false, "USER_ID");
        public final static Property CreateTime = new Property(4, String.class, "createTime", false, "CREATE_TIME");
    };


    public SearchDlLogDao(DaoConfig config) {
        super(config);
    }
    
    public SearchDlLogDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SearchDlLog' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'CONTENT' TEXT," + // 1: content
                "'TYPE' TEXT," + // 2: type
                "'USER_ID' TEXT," + // 3: userId
                "'CREATE_TIME' TEXT);"); // 4: createTime
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SearchDlLog'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SearchDlLog entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(2, content);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(3, type);
        }
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(4, userId);
        }
 
        String createTime = entity.getCreateTime();
        if (createTime != null) {
            stmt.bindString(5, createTime);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SearchDlLog readEntity(Cursor cursor, int offset) {
        SearchDlLog entity = new SearchDlLog( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // content
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // userId
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4) // createTime
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SearchDlLog entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setContent(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setUserId(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setCreateTime(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(SearchDlLog entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(SearchDlLog entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}
