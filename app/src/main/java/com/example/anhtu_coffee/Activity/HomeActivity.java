package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    String currentUser;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        Intent intent = getIntent();
        currentUser = intent.getStringExtra("username");
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        String sql = "select hoten from _nhanvien where user='" + currentUser + "'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();

        binding.txtwelcome.setText(getResources().getString(R.string.welcome1)+" "+ cursor.getString(0));
        binding.btn7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_about = new Intent(getBaseContext(), AboutActivity.class);

                startActivity(intent_about);
            }
        });
        binding.btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_table = new Intent(getBaseContext(), TableActivity.class);
                startActivity(intent_table);
            }
        });
        binding.btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_stock = new Intent(getBaseContext(), StockActivity.class);
                startActivity(intent_stock);
            }
        });
        binding.btn6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "select per from _nhanvien join login on _nhanvien.user = login.user where _nhanvien.user='" + currentUser + "'";
                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                int per = cursor.getInt(0);

                if (per == 1) {
                    Intent intent_menu = new Intent(getBaseContext(), MenuActivity.class);
                    startActivity(intent_menu);
                } else {
                    Toast.makeText(getBaseContext(), R.string.manager_only, Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.btn8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_tienich = new Intent(getBaseContext(), TienichActivity.class);
                intent_tienich.putExtra("currentuser", currentUser);
                startActivity(intent_tienich);
            }
        });
        binding.btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_banhang = new Intent(getBaseContext(), BanHangActivity.class);
                intent_banhang.putExtra("user", currentUser);
                String sql = "select count(idban) from _table";
                Cursor cursor1 = db.rawQuery(sql, null);
                cursor1.moveToFirst();


                if (cursor1.getInt(0) < 1) {
                    Toast.makeText(getBaseContext(), R.string.add_table_menu, Toast.LENGTH_LONG).show();
                } else {
                    sql = "select count(tenhang) from _menu";
                    Cursor cursor2 = db.rawQuery(sql, null);
                    cursor2.moveToFirst();

                    if (cursor2.getInt(0) < 1) {
                        Toast.makeText(getBaseContext(), R.string.add_table_menu, Toast.LENGTH_LONG).show();

                    }
                    else{
                        startActivity(intent_banhang);
                    }
                }


            }
        });
        binding.btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_hangdoi = new Intent(getBaseContext(), HangDoiActivity.class);

                startActivity(intent_hangdoi);
            }
        });
        binding.btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_thanhtoan = new Intent(getBaseContext(), ThanhToanActivity.class);

                startActivity(intent_thanhtoan);
            }
        });
    }
}