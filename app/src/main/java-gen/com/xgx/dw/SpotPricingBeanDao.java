package com.xgx.dw;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.xgx.dw.SpotPricingBean;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SpotPricing.
*/
public class SpotPricingBeanDao extends AbstractDao<SpotPricingBean, String> {

    public static final String TABLENAME = "SpotPricing";

    /**
     * Properties of entity SpotPricingBean.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, String.class, "id", true, "ID");
        public final static Property Name = new Property(1, String.class, "name", false, "NAME");
        public final static Property Store_id = new Property(2, String.class, "store_id", false, "STORE_ID");
        public final static Property Storename = new Property(3, String.class, "storename", false, "STORENAME");
        public final static Property Type = new Property(4, String.class, "type", false, "TYPE");
        public final static Property Price_count = new Property(5, String.class, "price_count", false, "PRICE_COUNT");
        public final static Property Pointed_price = new Property(6, String.class, "pointed_price", false, "POINTED_PRICE");
        public final static Property Peek_price = new Property(7, String.class, "peek_price", false, "PEEK_PRICE");
        public final static Property Flat_price = new Property(8, String.class, "flat_price", false, "FLAT_PRICE");
        public final static Property Valley_price = new Property(9, String.class, "valley_price", false, "VALLEY_PRICE");
    };


    public SpotPricingBeanDao(DaoConfig config) {
        super(config);
    }
    
    public SpotPricingBeanDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SpotPricing' (" + //
                "'ID' TEXT PRIMARY KEY NOT NULL ," + // 0: id
                "'NAME' TEXT," + // 1: name
                "'STORE_ID' TEXT," + // 2: store_id
                "'STORENAME' TEXT," + // 3: storename
                "'TYPE' TEXT," + // 4: type
                "'PRICE_COUNT' TEXT," + // 5: price_count
                "'POINTED_PRICE' TEXT," + // 6: pointed_price
                "'PEEK_PRICE' TEXT," + // 7: peek_price
                "'FLAT_PRICE' TEXT," + // 8: flat_price
                "'VALLEY_PRICE' TEXT);"); // 9: valley_price
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SpotPricing'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SpotPricingBean entity) {
        stmt.clearBindings();
 
        String id = entity.getId();
        if (id != null) {
            stmt.bindString(1, id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(2, name);
        }
 
        String store_id = entity.getStore_id();
        if (store_id != null) {
            stmt.bindString(3, store_id);
        }
 
        String storename = entity.getStorename();
        if (storename != null) {
            stmt.bindString(4, storename);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(5, type);
        }
 
        String price_count = entity.getPrice_count();
        if (price_count != null) {
            stmt.bindString(6, price_count);
        }
 
        String pointed_price = entity.getPointed_price();
        if (pointed_price != null) {
            stmt.bindString(7, pointed_price);
        }
 
        String peek_price = entity.getPeek_price();
        if (peek_price != null) {
            stmt.bindString(8, peek_price);
        }
 
        String flat_price = entity.getFlat_price();
        if (flat_price != null) {
            stmt.bindString(9, flat_price);
        }
 
        String valley_price = entity.getValley_price();
        if (valley_price != null) {
            stmt.bindString(10, valley_price);
        }
    }

    /** @inheritdoc */
    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SpotPricingBean readEntity(Cursor cursor, int offset) {
        SpotPricingBean entity = new SpotPricingBean( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // name
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // store_id
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // storename
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // type
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // price_count
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // pointed_price
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // peek_price
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // flat_price
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9) // valley_price
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SpotPricingBean entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setStore_id(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setStorename(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setType(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPrice_count(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setPointed_price(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setPeek_price(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setFlat_price(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setValley_price(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
     }
    
    /** @inheritdoc */
    @Override
    protected String updateKeyAfterInsert(SpotPricingBean entity, long rowId) {
        return entity.getId();
    }
    
    /** @inheritdoc */
    @Override
    public String getKey(SpotPricingBean entity) {
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
