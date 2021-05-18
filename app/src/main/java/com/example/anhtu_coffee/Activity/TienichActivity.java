package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityTienichBinding;

public class TienichActivity extends AppCompatActivity {
    ActivityTienichBinding binding;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tienich);
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        Intent intent = getIntent();
        String currentUser = intent.getStringExtra("currentuser");
        binding.btnTuychon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_tienich1 = new Intent(getBaseContext(), ThongTinCaNhanActivity.class);
                intent_tienich1.putExtra("currentUser", currentUser);
                startActivity(intent_tienich1);
            }
        });
        binding.btnTuychon4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(TienichActivity.this)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logout_confirm)
                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent_tienich4 = new Intent(getBaseContext(), LoginActivity.class);
                                startActivity(intent_tienich4);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.show();

            }
        });
        binding.btnTuychon2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "select per from _nhanvien join login on _nhanvien.user = login.user where _nhanvien.user='" + currentUser + "'";
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                int per = cursor.getInt(0);

                if (per == 1) {
                    Intent intent_tienich2 = new Intent(getBaseContext(), QuanLyNhanVienActivity.class);
                    intent_tienich2.putExtra("currentUser", currentUser);
                    startActivity(intent_tienich2);
                } else {
                    Toast.makeText(getBaseContext(), R.string.manager_only, Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.btnTuychon3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "select per from _nhanvien join login on _nhanvien.user = login.user where _nhanvien.user='" + currentUser + "'";
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                int per = cursor.getInt(0);

                if (per == 1) {
                    Intent intent_tienich3 = new Intent(getBaseContext(), BaoCaoDoanhThuActivity.class);
                    intent_tienich3.putExtra("currentUser", currentUser);
                    startActivity(intent_tienich3);
                } else {
                    Toast.makeText(getBaseContext(), R.string.manager_only, Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.btnTuychon5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sql = "select per from _nhanvien join login on _nhanvien.user = login.user where _nhanvien.user='" + currentUser + "'";
                Cursor cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                int per = cursor.getInt(0);

                if (per == 1) {
                    Intent intent_tienich3 = new Intent(getBaseContext(), QuanLyKhachHangActivity.class);
                    intent_tienich3.putExtra("currentUser", currentUser);
                    startActivity(intent_tienich3);
                } else {
                    Toast.makeText(getBaseContext(), R.string.manager_only, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}