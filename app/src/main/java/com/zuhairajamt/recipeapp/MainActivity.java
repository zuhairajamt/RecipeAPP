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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    String[] daftar;
    ListView listView;
    Menu menu;
    protected Cursor cursor;
    Database database;
    public static MainActivity ma;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FloatingActionButton fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FoodFragment()).commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pindah = new Intent(MainActivity.this, com.zuhairajamt.recipeapp.CreateFoodActivity.class);
                startActivity(pindah);
            }
        });
        ma = this;
        database = new Database(this);
        RefreshList();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectedFragment = null;

                switch (item.getItemId()){
                    case R.id.nav_food:
                        selectedFragment = new FoodFragment();
                        break;
                    case R.id.nav_drink:
                        selectedFragment = new DrinkFragment();
                        break;
                    case R.id.nav_restaurant:
                        selectedFragment = new RestaurantFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

                return true;
            }
        });
    }

    public void RefreshList() {
        SQLiteDatabase db = database.getReadableDatabase();
        cursor = db.rawQuery("select * from makanan", null);
        daftar = new String[cursor.getCount()];
        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            daftar[i] = cursor.getString(0).toString();
        }

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, daftar));

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
            }
        });
        ((ArrayAdapter) listView.getAdapter()).notifyDataSetInvalidated();
    }
}