package com.diezsiete.lscapp.data.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.diezsiete.lscapp.di.ApplicationContext;
import com.diezsiete.lscapp.di.DatabaseInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DbHelper extends SQLiteOpenHelper {

    @Inject
    public DbHelper(@ApplicationContext Context context,
                    @DatabaseInfo String dbName,
                    @DatabaseInfo Integer version) {
        super(context, dbName, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
