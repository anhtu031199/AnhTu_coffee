package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.anhtu_coffee.Adapter.HoadonAdapter;
import com.example.anhtu_coffee.Object.HoaDon;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityQuanLyKhachHangBinding;

import java.util.ArrayList;
import java.util.List;

public class QuanLyKhachHangActivity extends AppCompatActivity {
    ActivityQuanLyKhachHangBinding binding;
    SQLiteDatabase db;
    Cursor cursor;
    List<HoaDon> hoaDonList = new ArrayList<>();
    HoadonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbanhang));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quan_ly_khach_hang);
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        initData();
    }
    public  void initData()
    {
        String sql = "select * from _khachhang";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        hoaDonList.clear();

        while (!cursor.isAfterLast()) {
            String hoten = cursor.getString(0);
            String sdt = cursor.getString(1);
            int chitieu = cursor.getInt(2);

            HoaDon hoaDon = new HoaDon(hoten+" "+sdt, chitieu, 0, 0, "0");
            hoaDonList.add(hoaDon);
            cursor.moveToNext();
        }

        adapter = new HoadonAdapter(hoaDonList);
        binding.lvKhachhang.setAdapter(adapter);

        sql = "select count(sdt) from _khachhang";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int sldon = cursor.getInt(0);
        binding.txtTongKhachhang.setText(sldon+" "+getResources().getString(R.string.customer));
    }
}