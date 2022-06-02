package com.zuhairajamt.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class DetailFoodActivity extends AppCompatActivity {

    protected Cursor cursor;
    Database database;
    Button btn_simpan, btn_chose, btn_add_image;
    TextView nama, deskripsi, bahan, carabuat;
    ImageView gambar;

    final int REQUEST_CODE_GALLERY = 999;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_food);
        database = new Database(this);

        nama = findViewById(R.id.tv_nama_makanan);
        deskripsi = findViewById(R.id.tv_deskripsi);
        bahan = findViewById(R.id.tv_bahan_makanan);
        carabuat = findViewById(R.id.tv_cara_memasak);
        gambar = findViewById(R.id.gambar);
        btn_chose = findViewById(R.id.btn_chose);
        btn_add_image = findViewById(R.id.btn_add_image);

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
            byte[] gambar = cursor.getBlob(5);
            //BitmapFactory.decodeByteArray(gambar, 0, gambar.length);
        }

        btn_chose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {

                    ActivityCompat.requestPermissions(
                            DetailFoodActivity.this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            REQUEST_CODE_GALLERY
                    );
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        btn_add_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    SQLiteDatabase db = database.getWritableDatabase();
                    db.execSQL("UPDATE makanan set gambar= '" +
                            imageViewToByte(gambar) + "' WHERE nama = '" +
                            getIntent().getStringExtra("nama") + "'");
                    Toast.makeText(DetailFoodActivity.this, "Gambar berhasil ditambahkan", Toast.LENGTH_SHORT).show();
                    // int image = getIntent().getIntExtra("image", Integer.parseInt(imageViewToByte(gambar) + ""));
                    byte[] image = imageViewToByte(gambar);
                    //gambar.setImageDrawable(getResources().getDrawable(image));
                    gambar.setImageResource(R.drawable.dahar);
                    MainActivity.ma.RefreshList();
                    finish();
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }


    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                gambar.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


}