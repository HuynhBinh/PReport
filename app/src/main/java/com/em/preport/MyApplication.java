package com.em.preport;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import greendao.DaoMaster;
import greendao.DaoSession;

/**
 * Created by USER on 4/22/2015.
 */
public class MyApplication extends Application
{

    public DaoSession daoSession;

    @Override
    public void onCreate()
    {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase()
    {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "preportdb", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession()
    {
        if (daoSession != null)
        {
            return daoSession;
        } else
        {
            setupDatabase();
            return daoSession;
        }
    }

    public void clearDaoSession()
    {
        daoSession = null;
    }

}
