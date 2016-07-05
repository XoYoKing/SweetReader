package com.hm.sweetreader.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class AppDataBase extends AbstractDataBase {

    public static final int VERSION = 1;

    private static final String DB_NAME = "apps_data.db";

    private AppDataBaseUpgrader dbUpgrader;

    public AppDataBase(Context mContext) {
        super(mContext, DB_NAME, VERSION);
        dbUpgrader = new AppDataBaseUpgrader(mContext);
    }

    @Override
    public void onDataBaseCreate(SQLiteDatabase db) {
        db.execSQL(DataEntityTable.CREATE_TABLE);
    }

    @Override
    public void onDataBaseUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dbUpgrader.onUpgrade(db, oldVersion, newVersion);
    }

}
