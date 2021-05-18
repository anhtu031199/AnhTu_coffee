package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.example.anhtu_coffee.Adapter.HoadonAdapter;
import com.example.anhtu_coffee.Object.HoaDon;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityThanhToanBinding;

import java.util.ArrayList;
import java.util.List;

public class ThanhToanActivity extends AppCompatActivity {
    ActivityThanhToanBinding binding;
    SQLiteDatabase db;
    Cursor cursor;
    List<HoaDon> hoaDonList = new ArrayList<>();
    HoadonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbanhang));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_thanh_toan);
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        initData();
        binding.lvHoadon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ChitietHangdoiActivity.class);
                intent.putExtra("code", "2");
                intent.putExtra("ban", hoaDonList.get(position).getBan()+"");
                startActivity(intent);
            }
        });
    }
    public  void initData()
    {
        String sql = "select * from _hoadon where ghichu='chothanhtoan' order by ban";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        hoaDonList.clear();

        while (!cursor.isAfterLast()) {
            String ban = cursor.getString(cursor.getColumnIndex("ban"));
            int thanhtien = cursor.getInt(cursor.getColumnIndex("thanhtien"));
            int thanhtoan = cursor.getInt(cursor.getColumnIndex("thanhtoan"));
            int ngay = cursor.getInt(cursor.getColumnIndex("ngay"));
            String ghichu = cursor.getString(cursor.getColumnIndex("ghichu"));

            HoaDon hoaDon = new HoaDon(ban, thanhtien, thanhtoan, ngay, ghichu);
            hoaDonList.add(hoaDon);
            cursor.moveToNext();
        }

        adapter = new HoadonAdapter(hoaDonList);
        binding.lvHoadon.setAdapter(adapter);

        sql = "select count(ban) from _hoadon where ghichu='chothanhtoan'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int sldon = cursor.getInt(0);
        binding.txtTongCart.setText(sldon+" "+getResources().getString(R.string.don_hang )+"");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }
}