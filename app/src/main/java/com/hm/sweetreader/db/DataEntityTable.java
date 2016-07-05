package com.hm.sweetreader.db;

/**
 * project：MyReader
 * author： FLY
 * date：   2016/5/26
 * time：   16:53
 * purpose：
 */
public class DataEntityTable {
    //书名
    private String name;
    //路径
    private String url;
    //封面
    private String icon;
    //缓存文件标识
    private String tag;

    public final static String ID = "_id";
    public final static String NAME = "name";
    public final static String URL = "url";
    public final static String ICON = "iconPath";
    public final static String TAG = "tag";
    public final static String PAGE = "page";
    public final static String PAGE_TOTAL = "pageTotal";

    /**
     * 创建广告运行的表
     */
    public final static String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS MyReader ("
            + ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + NAME
            + " varchar(100) NOT NULL, "
            + URL
            + " varchar(100) NOT NULL, "
            + ICON
            + " varchar(100) NOT NULL,"
            + TAG
            + " varchar(100) NOT NULL,"
            + PAGE
            + " int NOT NULL,"
            + PAGE_TOTAL
            + " int  NOT NULL)";

    /**
     * 根据shortcutId查询
     */
    public final static String SELECT = "select * from MyReader";
    /**
     * 根据packageName查询
     */
    public final static String SELECT_BY_NAME = "select * from MyReader where name = '%s'";

    /**
     * 删除
     */
    public final static String DELETE_BY_NAME = "delete from MyReader where name = '%s'";

    public final static String INSERT = "insert into MyReader(name,url,iconPath,tag,page,pageTotal) values('%s','%s','%s','%s',%d,%d)";

    public final static String UPDATE_BY_NAME = "update MyReader set url = '%s', iconPath = '%s', tag = '%s',page='%d',page_total='%d' where name = '%s'";

    public final static String UPDATE_PAGE_BY_NAME = "update MyReader set page='%d' where name = '%s'";

}
