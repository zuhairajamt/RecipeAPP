package com.zuhairajamt.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreateDrinkActivity extends AppCompatActivity {
    protected Cursor cursor;
    Database database;
    Button btn_simpan;
    EditText nama, deskripsi, bahan, carabuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_drink);
        database = new Database(this);

        nama = findViewById(R.id.nama_minuman);
        deskripsi = findViewById(R.id.deskripsi);
        bahan = findViewById(R.id.bahan_minuman);
        carabuat = findViewById(R.id.cara_buat);

        btn_simpan = findViewById(R.id.btn_simpan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = database.getWritableDatabase();
                db.execSQL("insert into minuman(nama, deskripsi, bahan, carabuat) values('" +
                        nama.getText().toString() + "','" +
                        deskripsi.getText().toString() + "','" +
                        bahan.getText().toString() + "','" +
                        carabuat.getText().toString() + "')");
                Toast.makeText(CreateDrinkActivity.this, "Data Tersimpan", Toast.LENGTH_SHORT).show();
                MainActivity.ma.RefreshList();
                finish();
            }
        });
    }
}