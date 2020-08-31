package com.example.asofar_app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import com.example.asofar_app.utilidades.utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
//crea las tablas
    @Override
    public void onCreate(SQLiteDatabase db) {

        //TABLA QUE REGISTRA EL COGIGO
        db.execSQL( "CREATE TABLE COD_GENERADOS" +
                "(ID_COD INTEGER PRIMARY KEY AUTOINCREMENT," +
                " COD_BARRAS TEXT," +
                " FECHA_CREACION DATE DEFAULT (datetime('NOW','LOCALTIME')))");

        // TABLA QUE REGISTRA EL EQUIPO
        db.execSQL( "CREATE TABLE CAJA" +
                "(ID_COD_CAJA INTEGER PRIMARY KEY," +
                " MAC_EQUIPO TEXT," +
                " FECHA_CREACION DATE DEFAULT (datetime('NOW','LOCALTIME')))");

        //ACTIVA LA CAJA
        db.execSQL("INSERT INTO CAJA (ID_COD_CAJA, MAC_EQUIPO) VALUES(1 ,'')");//68:5a:cf:a5:f3:ec
}
// cada vez que se instala el app se elimina y se vuelve a crear la tabla
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS COD_GENERADOS");
        db.execSQL("DROP TABLE IF EXISTS CAJA");
        onCreate(db);
    }
}
