package com.zuhairajamt.recipeapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {


    String[] daftar;
    ListView listView;
    protected Cursor cursor;
    Database database;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(MainActivity.this, com.zuhairajamt.recipeapp.CreateFoodActivity.class);
                startActivity(pindah);
            }
        });

        // Initialize and assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Set home selected
        bottomNavigationView.setSelectedItemId(R.id.nav_food);

        // Perform ItemSelectedListener
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;  // Harus null dulu agart line 41 tidak error
                switch (item.getItemId()){
                    case R.id.nav_drink:
                        startActivity(new Intent(getApplicationContext(), DrinkActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.nav_food:
                        return true;
                    case R.id.nav_restaurant:
                        startActivity(new Intent(getApplicationContext(), RestaurantActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                   /* case R.id.nav_fav:
                        startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                        overridePendingTransition(0,0);
                        return true;*/
                }
                return true;
            }
        });

        ma = this;
        database = new Database(this);
        RefreshList();
    }

    public void RefreshList() {
        SQLiteDatabase db = database.getReadableDatabase();
        Button btn_fav = findViewById(R.id.btn_fav);
        ArrayAdapter<String> adapter;

        cursor = db.rawQuery("select * from makanan", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            daftar[i] = cursor.getString(0).toString();
        }

        listView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, daftar);
        listView.setAdapter(adapter);

        //((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();

        btn_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Bingung gagal listview tidak terupdate akhirnya buat activity buat button tapi pakai layout main
                //cursor = db.rawQuery("SELECT * FROM makanan WHERE favorite = 1", null);
                //daftar = new String[cursor.getCount()];
                //cursor.moveToFirst();
                //Toast.makeText(MainActivity.this, "Menampilkan makanan favorit", Toast.LENGTH_SHORT).show();

                //for (int i = 0; i < cursor.getCount(); i++) {
                //    cursor.moveToPosition(i);
                //    daftar[i] = cursor.getString(0).toString();
                //}
                //MainActivity.ma.RefreshList();
                //adapter.notifyDataSetChanged();
                //((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
                //daftar.add(""); //add new item to the list
                //adapter.notifyDataSetChanged(); //notify adapter
                startActivity(new Intent(getApplicationContext(), FavoriteActivity.class));
                overridePendingTransition(0,0);
            }
        });

        //adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
                final String selection = daftar[arg2];
                final CharSequence[] dialogitem = {"Lihat makananan", "Update makananan", "Hapus makananan"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialogInterface, int item) {
                        switch(item) {
                            case 0:
                                Intent i = new Intent(getApplicationContext(), DetailFoodActivity.class);
                                i.putExtra("nama", selection);
                                startActivity(i);
                                break;
                            case 1:
                                Intent in = new Intent(getApplicationContext(), UpdateFoodActivity.class);
                                in.putExtra("nama", selection);
                                startActivity(in);
                                break;
                            case 2:
                                SQLiteDatabase db = database.getWritableDatabase();
                                db.execSQL("delete from makanan where nama = '" + selection + "'");
                                RefreshList();
                                break;
                        }
                    }
                });
                builder.create().show();
                //((ArrayAdapter) listView.getAdapter()).notifyDataSetChanged();
                //adapter.notifyDataSetChanged();
            }
        });
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetInvalidated();
    }
}

