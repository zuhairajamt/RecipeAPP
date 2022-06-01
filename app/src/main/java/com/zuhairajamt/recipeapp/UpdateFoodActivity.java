package com.zuhairajamt.recipeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zuhairajamt.recipeapp.Database;
import com.zuhairajamt.recipeapp.MainActivity;
import com.zuhairajamt.recipeapp.R;

public class UpdateFoodActivity extends AppCompatActivity {
    protected Cursor cursor;
    Database database;
    Button btn_simpan;
    EditText nama, deskripsi, bahan, carabuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);
        database = new Database(this);

        nama = findViewById(R.id.nama_makanan);
        deskripsi = findViewById(R.id.deskripsi);
        bahan = findViewById(R.id.bahan_makanan);
        carabuat = findViewById(R.id.cara_memasak);

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

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = database.getWritableDatabase();
                db.execSQL("UPDATE makanan set nama= '" +
                        nama.getText().toString() + "', deskripsi ='" +
                        deskripsi.getText().toString() + "', bahan ='" +
                        bahan.getText().toString() + "', carabuat ='" +
                        carabuat.getText().toString() + "' WHERE nama = '" +
                        getIntent().getStringExtra("nama") + "'");
                Toast.makeText(UpdateFoodActivity.this, "Makanan berhasil di update", Toast.LENGTH_SHORT).show();
                MainActivity.ma.RefreshList();
                finish();
            }
        });
    }
}