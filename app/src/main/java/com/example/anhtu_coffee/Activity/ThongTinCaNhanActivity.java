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
import android.widget.EditText;
import android.widget.Toast;

import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityThongTinCaNhanBinding;

public class ThongTinCaNhanActivity extends AppCompatActivity {
    ActivityThongTinCaNhanBinding binding;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thong_tin_ca_nhan);
initData();
        Intent intent = getIntent();
        String currentUser = intent.getStringExtra("currentUser");
        String sql1 = "select pass from login where user='" + currentUser + "'";
        Cursor cursor = db.rawQuery(sql1, null);
        cursor.moveToFirst();
        String passcu = cursor.getString(0);
        loadData(currentUser);
        binding.btnDoimk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ThongTinCaNhanActivity.this);
                final View customDialog = getLayoutInflater().inflate(R.layout.doimatkhau_layout, null);
                builder.setView(customDialog);
                builder.setTitle(R.string.change_pass);
                builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText mkcu = customDialog.findViewById(R.id.txt_doimk_mkcu);
                        EditText mkmoi = customDialog.findViewById(R.id.txt_doimk_mkmoi);
                        EditText mkmoi1 = customDialog.findViewById(R.id.txt_doimk_mkmoi1);

                        if (mkcu.getText().toString().equals("") || mkmoi.getText().toString().equals("") || mkmoi1.getText().toString().equals("")) {
                            Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                        } else if (!mkmoi.getText().toString().equals(mkmoi1.getText().toString())) {
                            Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_LONG).show();
                        } else {
                            if (!mkcu.getText().toString().equals(passcu)) {
                                Toast.makeText(getBaseContext(), R.string.wrong_pass, Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    String sql = "update login set pass='" + mkmoi.getText().toString() + "' where user='" + currentUser + "'";
                                    db.execSQL(sql);
                                    Toast.makeText(getBaseContext(), R.string.success, Toast.LENGTH_SHORT).show();
                                }
                                catch (Exception e)
                                {
                                    Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_LONG).show();
                                }

                            }


                        }
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getBaseContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void initData() {
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
//        String sql = "drop table _stock";
//        db.execSQL(sql);

        String sql = "CREATE TABLE IF NOT EXISTS _nhanvien" +
                "(hoten TEXT,  sdt TEXT, user TEXT)";
        db.execSQL(sql);
//        sql = "insert into _nhanvien values('Nguyen Anh Tu', '113', 'admin')";
//        db.execSQL(sql);
    }
    public void loadData(String current)
    {
        String sql = "select * from _nhanvien join login on _nhanvien.user = login.user where _nhanvien.user='"+current+"'";
        Cursor cursor_nv = db.rawQuery(sql, null);
        cursor_nv.moveToFirst();
        binding.txtThongtinHoten.setText(cursor_nv.getString(cursor_nv.getColumnIndex("hoten")));
        binding.txtThongtinSdt.setText(cursor_nv.getString(cursor_nv.getColumnIndex("sdt")));
        binding.txtThongtinTaikhoan.setText(current);
    }
}