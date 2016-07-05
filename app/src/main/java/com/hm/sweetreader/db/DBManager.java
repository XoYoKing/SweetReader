package com.hm.sweetreader.db;

import android.content.Context;
import android.database.Cursor;

import com.hm.sweetreader.entity.DataEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/26
 * time：   16:41
 * purpose：
 */
public class DBManager {

    protected static volatile DBManager dbManager;
    private AppDataBase appDataBase;

    private DBManager(Context context) {
        appDataBase = new AppDataBase(context);
    }

    public static DBManager newInstance(Context context) {
        if (dbManager == null) {
            synchronized (DBManager.class) {
                if (dbManager == null) {
                    dbManager = new DBManager(context);
                }
            }
        }
        return dbManager;
    }

    public void insertBook(String name, String url, String iconPath, String tag, int page,long page_total) {
        Cursor cursors = null;

        try {
            cursors = appDataBase.query(String.format(DataEntityTable.SELECT_BY_NAME, name));
            if (cursors.getCount() > 0) {
                return;
            }

            appDataBase.execSQL(String.format(DataEntityTable.INSERT, name, url, iconPath, tag, page,page_total));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursors != null) {
                cursors.close();
            }
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
    }

    public String selectBookTag(String name) {
        Cursor cursor = null;
        String tag = null;
        try {
            cursor = appDataBase.query(String.format(DataEntityTable.SELECT_BY_NAME, name));
            if (null == cursor || cursor.getCount() == 0) {
                return null;
            }
            if (cursor.moveToNext()) {
                tag = cursor.getString(cursor.getColumnIndex(DataEntityTable.TAG));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
        return tag;
    }

    public List<DataEntity> selectAll() {
        Cursor cursor = null;
        String name = null, url = null, icon = null, tag = null;
        long page = 0;
        long page_total=0;
        List<DataEntity> list = new ArrayList<>();
        DataEntity dataEntity = null;
        try {
            cursor = appDataBase.query(String.format(DataEntityTable.SELECT));
            if (null == cursor || cursor.getCount() == 0) {
                return null;
            }
            while (cursor.moveToNext()) {
                name = cursor.getString(cursor.getColumnIndex(DataEntityTable.NAME));
                url = cursor.getString(cursor.getColumnIndex(DataEntityTable.URL));
                icon = cursor.getString(cursor.getColumnIndex(DataEntityTable.ICON));
                tag = cursor.getString(cursor.getColumnIndex(DataEntityTable.TAG));
                page = cursor.getInt(cursor.getColumnIndex(DataEntityTable.PAGE));
                page_total=cursor.getInt(cursor.getColumnIndex(DataEntityTable.PAGE_TOTAL));
                dataEntity = new DataEntity(name, url, icon, tag, page,page_total);
                list.add(dataEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
        return list;
    }

    public long selectPageTotal(String name){
        Cursor cursor = null;
        long pageTotal = 0;
        try {
            cursor = appDataBase.query(String.format(DataEntityTable.SELECT_BY_NAME, name));
            if (null == cursor || cursor.getCount() == 0) {
                return 0;
            }
            if (cursor.moveToNext()) {
                pageTotal = cursor.getLong(cursor.getColumnIndex(DataEntityTable.PAGE_TOTAL));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
        return pageTotal;
    }
    public String selectIconPath(String name) {
        Cursor cursor = null;
        String iconPath = null;
        try {
            cursor = appDataBase.query(String.format(DataEntityTable.SELECT_BY_NAME, name));
            if (null == cursor || cursor.getCount() == 0) {
                return null;
            }
            if (cursor.moveToNext()) {
                iconPath = cursor.getString(cursor.getColumnIndex(DataEntityTable.ICON));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
        return iconPath;
    }

    public void updatePageByName(String name, long page) {
        try {
            appDataBase.execSQL(String.format(DataEntityTable.UPDATE_PAGE_BY_NAME, page, name));
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
    }

    public void delectBook(String name) {
        try {
            appDataBase.execSQL(String.format(DataEntityTable.DELETE_BY_NAME, name));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (appDataBase != null) {
                appDataBase.close();
            }
        }
    }
}
