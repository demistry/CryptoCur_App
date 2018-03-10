package com.android.cryptocurapp.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.android.cryptocurapp.models.CryptoCurContract;

/**
 * Created by ILENWABOR DAVID on 19/10/2017.
 */

public class CryptoCurDatabase extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "CryptoCurDatabase";
    public CryptoCurDatabase(Context context){
        super(context,DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String databaseDefined = "CREATE TABLE "+ CryptoCurContract.CRYPTOCUR_TABLE+ " ("+
                CryptoCurContract.CRYPTOCUR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                CryptoCurContract.CRYPTOCURRENCY_COLUMN + " TEXT,"+
                CryptoCurContract.BASECURRENCY_COLUMN + " TEXT," +
                CryptoCurContract.EXCHANGERATE_COLUMN +" TEXT);";
        db.execSQL(databaseDefined);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
