package com.zuhairajamt.recipeapp;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="recipeapp.db";
    private static final int DATABASE_VERSION = 1;

    public Database(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlmakanan = "create table makanan(nama text null,  favorite int null, deskripsi text null, bahan text null, carabuat text null,gambar blob null);";
        String sqlminuman = "create table minuman(nama text null,  favorite int null, deskripsi text null, bahan text null, carabuat text null,gambar blob null);";
        // String sqlresotran = "create table restoran(nama text null, alamat text null, telp text null, kota text null, gambar text null, rating real null);";
        Log.d("Data","onCreate: " + sqlmakanan);
        Log.d("Data","onCreate: " + sqlminuman);
        // Log.d("Data","onCreate: " + sqlresotran);
        db.execSQL(sqlmakanan);
        db.execSQL(sqlminuman);
        // db.execSQL(sqlresotran);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


}
