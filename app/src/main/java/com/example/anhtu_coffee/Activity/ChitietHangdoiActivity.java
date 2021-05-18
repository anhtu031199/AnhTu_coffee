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
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.anhtu_coffee.Adapter.CartAdapter;
import com.example.anhtu_coffee.Object.Cart;
import com.example.anhtu_coffee.R;

import com.example.anhtu_coffee.databinding.ActivityChitietHangdoiBinding;

import java.util.ArrayList;
import java.util.List;

public class ChitietHangdoiActivity extends AppCompatActivity {
    ActivityChitietHangdoiBinding binding;
    SQLiteDatabase db;
    List<Cart> list = new ArrayList<>();
    Cursor cursor;
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbanhang));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_chitiet_hangdoi);
        if (getIntent().getStringExtra("code").equals("1"))

            binding.btnCartOption.setText(R.string.done);

        else
            binding.btnCartOption.setText(R.string.pay);

        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        String sql = "select _chitiethoadon.tenhang, soluong, (gia*(100-khuyenmai)/100) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban='" + getIntent().getStringExtra("ban")+"'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        list.clear();
        while (!cursor.isAfterLast()) {
            String ten = cursor.getString(0);
            int sl = cursor.getInt(1);
            int gia = sl * cursor.getInt(2);
            Cart cart = new Cart(ten, sl, gia);
            list.add(cart);
            cursor.moveToNext();

        }
        adapter = new CartAdapter(list);
        binding.lvCart.setAdapter(adapter);
        sql = "select sum (soluong* (gia*(100-khuyenmai)/100)) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban='" + getIntent().getStringExtra("ban")+"'";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int tongtien = cursor.getInt(0);
        binding.txtTongCart.setText(String.format("%,d", tongtien) +getResources().getString( R.string.vnd) +"");



        binding.btnCartOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("code").equals("1"))
                {
                    String sql = "update _hoadon set ghichu='chothanhtoan' where ban='"+getIntent().getStringExtra("ban")+"' and thanhtoan=-1";
                    db.execSQL(sql);
                    Toast.makeText(getBaseContext(), R.string.success, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }
                else
                {
                    String sql = "update _hoadon set ghichu='dathanhtoan', thanhtoan=1 where ban='"+getIntent().getStringExtra("ban")+"' and thanhtoan=-1";
                    db.execSQL(sql);
                    sql = "update _table set empty=0 where idban='"+getIntent().getStringExtra("ban")+"'";
                    db.execSQL(sql);
                    sql = "delete from _chitiethoadon where ban='"+getIntent().getStringExtra("ban")+"'";
                    db.execSQL(sql);
                    Toast.makeText(getBaseContext(), R.string.success, Toast.LENGTH_LONG).show();
                    onBackPressed();
                }

            }
        });
    }
}