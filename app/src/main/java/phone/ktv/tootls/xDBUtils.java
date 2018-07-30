package phone.ktv.tootls;

import org.xutils.DbManager;

public class xDBUtils {
    private DbManager.DaoConfig dConfig;
    private DbManager BusDB;

    public xDBUtils() {
        dConfig = new DbManager.DaoConfig();
        dConfig.setDbName("phone.ktv.db");
        dConfig.setDbVersion(1);//设置数据库版本
        dConfig.setDbOpenListener(new DbManager.DbOpenListener() {
            @Override
            public void onDbOpened(DbManager db) {
                // 开启WAL, 对写入加速提升巨大
                db.getDatabase().enableWriteAheadLogging();
            }
        });

        dConfig.setDbUpgradeListener(new DbManager.DbUpgradeListener() {
            @Override
            public void onUpgrade(DbManager dbManager, int i, int i1) {

            }
        });
    }

    public DbManager.DaoConfig getBusDB() {
        return dConfig;
    }
}
    /****使用xutils3.0的框架,执行Db操作总结****/
//    a.
//    1.使用框架之前,需要在MyApplication文件里面初始化  x.Ext.init(this); x.Ext.setDebug(true),在清单文件application下加上  android:name="
// .MyApplication";
//    2.使用封装好的,Db操作类.
//    3.在实体类中 字段id必须为主键(isId=true),否则操作不了, 主键是自增长(autoGen=true), @Column(name = "_id", isId = true, autoGen = true)
//
//    b.
//    1.add操作  db.save();
//    2.delete操作   db.delete();
//    3.update操作   db.update();
//    4.select操作   db.select(Province.class).where....


