package com.example.asofar_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLITE  extends SQLiteOpenHelper {

    final String CreaTablaConfig = "CREATE TABLE config (iddb TEXT, cajadb TEXT, ipserverdb TEXT, puertodb TEXT)";
    final String insertTablaConfig = "INSERT INTO config VALUES ('1','1001','192.168.100.15','8080')";


    public SQLITE(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

                db.execSQL(CreaTablaConfig);
                 db.execSQL(insertTablaConfig);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE CreaTablaConfig");
        onCreate(db);
    }
}
