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
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.anhtu_coffee.Adapter.CartAdapter;
import com.example.anhtu_coffee.Adapter.StockAdapter;
import com.example.anhtu_coffee.Object.Cart;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityCartBinding;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    ActivityCartBinding binding;
    SQLiteDatabase db;
    List<Cart> list = new ArrayList<>();
    Cursor cursor;
    CartAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbanhang));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);

        String sql = "select _chitiethoadon.tenhang, soluong, (gia*(100-khuyenmai)/100) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban=" + getIntent().getStringExtra("ban")+" order by _chitiethoadon.tenhang";
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
        sql = "select sum (soluong* (gia*(100-khuyenmai)/100)) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban=" + getIntent().getStringExtra("ban");
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int tongtien = cursor.getInt(0);
        binding.txtTongCart.setText(String.format("%,d", tongtien)+getResources().getString(R.string.vnd));
        binding.lvCart.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(CartActivity.this)
                        .setTitle(R.string.delete)
                        .setMessage(R.string.xac_nhan_xoa)
                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sqldelete = "delete from _chitiethoadon where tenhang='" + list.get(position).getTenhang() + "' and ban="+getIntent().getStringExtra("ban");
                                try {
                                    db.execSQL(sqldelete);
                                    String sql = "select _chitiethoadon.tenhang, soluong, (gia*(100-khuyenmai)/100) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban=" + getIntent().getStringExtra("ban")+" order by _chitiethoadon.tenhang";
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
                                    adapter.notifyDataSetChanged();
                                    sql = "select sum (soluong* (gia*(100-khuyenmai)/100)) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban=" + getIntent().getStringExtra("ban");
                                    cursor = db.rawQuery(sql, null);
                                    cursor.moveToFirst();
                                    int tongtien = cursor.getInt(0);
                                    binding.txtTongCart.setText(String.format("%,d", tongtien)+getResources().getString(R.string.vnd));
                                    Toast.makeText(getBaseContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getBaseContext(), R.string.canceled, Toast.LENGTH_SHORT).show();
                            }
                        })
                        .create();
                alertDialog.show();
                return false;
            }
        });

    }
}