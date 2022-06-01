package com.zuhairajamt.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.zuhairajamt.recipeapp.Database;
import com.zuhairajamt.recipeapp.R;

public class DetailFoodActivity extends AppCompatActivity {

    protected Cursor cursor;
    Database database;
    Button btn_simpan;
    TextView nama, deskripsi, bahan, carabuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        database = new Database(this);

        nama = findViewById(R.id.tv_nama_makanan);
        deskripsi = findViewById(R.id.tv_deskripsi);
        bahan = findViewById(R.id.tv_bahan_makanan);
        carabuat = findViewById(R.id.tv_cara_memasak);

        btn_simpan = findViewById(R.id.btn_simpan);

        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM makanan WHERE nama = '" +
                getIntent().getStringExtra("nama") + "'", null);
        cursor.moveToFirst();

        if(cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            nama.setText(cursor.getString(0).toString());
            deskripsi.setText(cursor.getString(2).toString());
            bahan.setText(cursor.getString(3).toString());
            carabuat.setText(cursor.getString(4).toString());
        }


    }



}