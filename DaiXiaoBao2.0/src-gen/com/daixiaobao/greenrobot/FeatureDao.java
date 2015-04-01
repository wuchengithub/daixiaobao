package com.daixiaobao.greenrobot;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.daixiaobao.greenrobot.Feature;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table daixiaobao_feature.
*/
public class FeatureDao extends AbstractDao<Feature, Long> {

    public static final String TABLENAME = "daixiaobao_feature";

    /**
     * Properties of entity Feature.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property FeatureId = new Property(1, String.class, "featureId", false, "FEATURE_ID");
        public final static Property FeatureName = new Property(2, String.class, "featureName", false, "FEATURE_NAME");
        public final static Property Attrb_id = new Property(3, String.class, "attrb_id", false, "ATTRB_ID");
    };


    public FeatureDao(DaoConfig config) {
        super(config);
    }
    
    public FeatureDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'daixiaobao_feature' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'FEATURE_ID' TEXT NOT NULL ," + // 1: featureId
                "'FEATURE_NAME' TEXT," + // 2: featureName
                "'ATTRB_ID' TEXT);"); // 3: attrb_id
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'daixiaobao_feature'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, Feature entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
        stmt.bindString(2, entity.getFeatureId());
 
        String featureName = entity.getFeatureName();
        if (featureName != null) {
            stmt.bindString(3, featureName);
        }
 
        String attrb_id = entity.getAttrb_id();
        if (attrb_id != null) {
            stmt.bindString(4, attrb_id);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public Feature readEntity(Cursor cursor, int offset) {
        Feature entity = new Feature( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.getString(offset + 1), // featureId
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // featureName
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // attrb_id
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, Feature entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setFeatureId(cursor.getString(offset + 1));
        entity.setFeatureName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setAttrb_id(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(Feature entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(Feature entity) {
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
