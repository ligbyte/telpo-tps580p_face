package com.stkj.cashier.greendao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;


import com.stkj.cashier.greendao.generate.FacePassPeopleInfoDao;
import com.stkj.cashier.greendao.tool.MigrationHelper;

import org.greenrobot.greendao.database.Database;

public class AppGreenDaoOpenHelper extends DaoMaster.OpenHelper {
    public AppGreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        MigrationHelper.migrate(db, new MigrationHelper.ReCreateAllTableListener() {
            @Override
            public void onCreateAllTables(Database db, boolean ifNotExists) {
                DaoMaster.createAllTables(db, ifNotExists);
            }

            @Override
            public void onDropAllTables(Database db, boolean ifExists) {
                DaoMaster.dropAllTables(db, ifExists);
            }
        }, FacePassPeopleInfoDao.class);
    }
}

