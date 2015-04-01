package com.daixiaobao.greenrobot;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.daixiaobao.greenrobot.Attrb;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table daixiaobao_attrb.
*/
public class AttrbDao extends AbstractDao<Attrb, Long> {

    public static final String TABLENAME = "daixiaobao_attrb";

    /**
     * Properties of entity Attrb.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FeatureTypeId = new Property(1, String.class, "featureTypeId", false, "FEATURE_TYPE_ID");
        public final static Property FeatureTypeName = new Property(2, String.class, "featureTypeName", false, "FEATURE_TYPE_NAME");
        public final static Property Categorys_id = new Property(3, String.class, "categorys_id", false, "CATEGORYS_ID");
    };


    public AttrbDao(DaoConfig config) {
        super(config);
    }
    
    public AttrbDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'daixiaobao_attrb' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'FEATURE_TYPE_ID' TEXT NOT NULL ," + // 1: featureTypeId
                "'FEATURE_TYPE_NAME' TEXT," + // 2: featureTypeName
                "'CATEGORYS_ID' TEXT);"); // 3: categorys_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'daixiaobao_attrb'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Attrb entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFeatureTypeId());
 
        String featureTypeName = entity.getFeatureTypeName();
        if (featureTypeName != null) {
            stmt.bindString(3, featureTypeName);
        }
 
        String categorys_id = entity.getCategorys_id();
        if (categorys_id != null) {
            stmt.bindString(4, categorys_id);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Attrb readEntity(Cursor cursor, int offset) {
        Attrb entity = new Attrb( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // featureTypeId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // featureTypeName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // categorys_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Attrb entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFeatureTypeId(cursor.getString(offset + 1));
        entity.setFeatureTypeName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setCategorys_id(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Attrb entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Attrb entity) {
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
