package com.thinkingsoft.security_essentials;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

class DBHelper extends SQLiteOpenHelper {

    DBHelper(Context context) {
        super(context, "securityDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE usuarios (id INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, correo TEXT, uid TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    void insertRow(String nombre, String correo, String uid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into usuarios (nombre,correo,uid) values ('"+nombre+"','"+correo+"','"+uid+"')");
        db.close();
    }
}
