package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anhtu_coffee.Adapter.HangAdapter;
import com.example.anhtu_coffee.Adapter.MenuAdapter;
import com.example.anhtu_coffee.Object.Menu;
import com.example.anhtu_coffee.Object.Stock;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityBanHangBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BanHangActivity extends AppCompatActivity {
    ActivityBanHangBinding binding;
    SQLiteDatabase db;
    Cursor cursor;
    List<String> list_table = new ArrayList<>();
    List<String> tablelist = new ArrayList<>();
    String selected_table = "";
    List<Menu> hanglist = new ArrayList<>();
    int chonban = 0;
    HangAdapter hangAdapter;
    int tongtien = 0;
    int tongsl = 0;
    String date = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbanhang));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_ban_hang);
        initData();
        chonban = 0;
        selected_table = tablelist.get(binding.cbbBan.getSelectedItemPosition());
//        Toast.makeText(getBaseContext(), selected_table, Toast.LENGTH_SHORT).show();

        binding.cbbBan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                chonban = 1;

                if (tablelist.get(position) == "0") {
                    selected_table = tablelist.get(position) + "";

                } else {
                    String sql = "delete from _chitiethoadon  where ban='" + selected_table + "'";
                    db.execSQL(sql);
                    selected_table = tablelist.get(position) + "";
                }

                tinhtongtien();
                tinhtonggiohang();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                chonban = 0;
                Toast.makeText(getBaseContext(), R.string.choose_table_first, Toast.LENGTH_SHORT).show();
            }
        });

        String sql = "select * from _menu order by tenhang";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        hanglist.clear();
        while (!cursor.isAfterLast()) {
            String tenhang = cursor.getString(0);
            int loaihang = cursor.getInt(1);
            int gia = cursor.getInt(2);
            float khuyenmai = cursor.getFloat(3);
            String thanhphan = cursor.getString(4);
            String linkanh = cursor.getString(5);

            Menu menu = new Menu(tenhang, thanhphan, linkanh, gia, khuyenmai);
            hanglist.add(menu);
            cursor.moveToNext();
        }
        hangAdapter = new HangAdapter(hanglist);
        binding.lvHang.setAdapter(hangAdapter);


        //lấy ngày
        Calendar calendar = Calendar.getInstance();
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        int year = calendar.get(Calendar.YEAR);
        if (month.length() < 2)
            month = "0" + month;
        if (day.length() < 2)
            day = "0" + day;
        date = year + month + day;
        int[] hangchon = new int[hanglist.size()];
        for (int i = 0; i < hanglist.size(); i++) {
            hangchon[i] = 0;
        }
        binding.lvHang.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (chonban != 0) {

                    String sql = "select count (tenhang) from _chitiethoadon where tenhang='" + hanglist.get(position).getTenHang() + "' and ban=" + selected_table;
                    //Toast.makeText(getBaseContext(), sql, Toast.LENGTH_SHORT).show();
                    Cursor cursor1 = db.rawQuery(sql, null);
                    cursor1.moveToFirst();
                    int check = cursor1.getInt(0);
                    if (check == 0) {
                        String sqladd = "insert into _chitiethoadon values(" + selected_table + ", '" + hanglist.get(position).getTenHang() + "', 1)";
                        //Toast.makeText(getBaseContext(), sqladd, Toast.LENGTH_SHORT).show();
                        db.execSQL(sqladd);
                    } else {
                        String sqladd = "update _chitiethoadon set soluong = soluong+1 where tenhang='" + hanglist.get(position).getTenHang() + "'";
                        //Toast.makeText(getBaseContext(), sqladd, Toast.LENGTH_SHORT).show();
                        db.execSQL(sqladd);
                    }
                } else {
                    Toast.makeText(getBaseContext(), R.string.choose_table_first, Toast.LENGTH_SHORT).show();
                }
                tinhtongtien();
                tinhtonggiohang();
            }
        });

        binding.btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CartActivity.class);
                intent.putExtra("ban", selected_table);

                intent.putExtra("code", "1");
                startActivity(intent);

            }
        });

        binding.txtSearchHang.setVisibility(View.GONE);
        binding.btnSearchHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSearchHang.setVisibility(View.VISIBLE);
            }
        });
        binding.btnSearchGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchHang.setText("");
                binding.txtSearchHang.setVisibility(View.GONE);
            }
        });
        binding.etSearchHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sql = "";
                if (binding.etSearchHang.getText().toString().equals("")) {
                    sql = "select * from _menu order by tenhang";
                } else {
                    sql = "select * from _menu where tenhang like '%" + binding.etSearchHang.getText().toString() + "%' order by tenhang";
                }

                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                hanglist.clear();
                while (!cursor.isAfterLast()) {
                    String tenhang = cursor.getString(0);
                    int loaihang = cursor.getInt(1);
                    int gia = cursor.getInt(2);
                    float khuyenmai = cursor.getFloat(3);
                    String thanhphan = cursor.getString(4);
                    String linkanh = cursor.getString(5);

                    Menu menu = new Menu(tenhang, thanhphan, linkanh, gia, khuyenmai);
                    hanglist.add(menu);
                    cursor.moveToNext();
                }
                hangAdapter.notifyDataSetChanged();
            }
        });
        binding.btnBanhang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tongsl == 0) {
                    Toast.makeText(getBaseContext(), R.string.choose_item_first, Toast.LENGTH_SHORT).show();
                } else {

                    if (selected_table.equals("0")) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BanHangActivity.this);
                        final View customDialog = getLayoutInflater().inflate(R.layout.add_customer_layout, null);
                        builder.setView(customDialog);
                        EditText etif = customDialog.findViewById(R.id.et_info_customer_add);
                        EditText etsdt = customDialog.findViewById(R.id.et_sdt_customer_add);

                        builder.setTitle(R.string.takeaway_info);
                        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (etif.getText().toString().equals("") || etsdt.getText().toString().equals("")) {

                                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();

                                } else {
                                    try {
                                        String sql = "insert into _hoadon values ('Đ/c: " + etif.getText().toString() + " sđt: " + etsdt.getText().toString() + "', 'cho', " + tongtien + ", -1, '" + date + "')";

                                        db.execSQL(sql);
                                        sql = "update _chitiethoadon set ban='Đ/c: " + etif.getText().toString() + " sđt: " + etsdt.getText().toString() + "' where ban='0'";
                                        db.execSQL(sql);

                                        Toast.makeText(getBaseContext(), R.string.order_success, Toast.LENGTH_SHORT).show();

                                        String sql1 = "select count(sdt) from _khachhang where sdt='" + etsdt.getText().toString() + "'";
                                        cursor = db.rawQuery(sql1, null);
                                        cursor.moveToFirst();
                                        if (cursor.getInt(0) < 1) {
                                            sql = "insert into _khachhang values('" + etif.getText().toString() + "', '" + etsdt.getText().toString() + "', " + tongtien + ")";
                                            db.execSQL(sql);
                                        } else {
                                            sql = "update _khachhang set chitieu = chitieu+" + tongtien + " where sdt = '" + etsdt.getText().toString() + "'";
                                            db.execSQL(sql);
                                        }


                                        //sql = "insert into _khachhang values()";
                                        // db.execSQL(sql);
                                        onBackPressed();
                                    } catch (Exception e) {
                                        Toast.makeText(getBaseContext(), R.string.dont_use_symbol, Toast.LENGTH_LONG).show();
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
                    } else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(BanHangActivity.this);
                        final View customDialog = getLayoutInflater().inflate(R.layout.add_customer_layout, null);
                        builder.setView(customDialog);
                        EditText etif = customDialog.findViewById(R.id.et_info_customer_add);
                        EditText etsdt = customDialog.findViewById(R.id.et_sdt_customer_add);

                        builder.setTitle(R.string.customer_info);
                        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {


                                if (etif.getText().toString().equals("") || etsdt.getText().toString().equals("")) {

                                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();

                                } else {
                                    try {

                                        String sql = "";

                                        String sql1 = "select count(sdt) from _khachhang where sdt='" + etsdt.getText().toString() + "'";
                                        cursor = db.rawQuery(sql1, null);
                                        cursor.moveToFirst();
                                        if (cursor.getInt(0) < 1) {
                                            sql = "insert into _khachhang values('" + etif.getText().toString() + "', '" + etsdt.getText().toString() + "', " + tongtien + ")";
                                            db.execSQL(sql);
                                        } else {
                                            sql = "update _khachhang set chitieu = chitieu+" + tongtien + " where sdt = '" + etsdt.getText().toString() + "'";
                                            db.execSQL(sql);
                                        }
                                        sql = "insert into _hoadon values ('" + selected_table + "', 'cho', " + tongtien + ", -1, '" + date + "')";

                                        db.execSQL(sql);
                                        sql = "update _table set empty=1 where idban='" + selected_table + "'";
                                        db.execSQL(sql);
                                        Toast.makeText(getBaseContext(), R.string.order_success, Toast.LENGTH_SHORT).show();

                                        //sql = "insert into _khachhang values()";
                                        // db.execSQL(sql);
                                        onBackPressed();
                                    } catch (Exception e) {
                                        Toast.makeText(getBaseContext(), R.string.dont_use_symbol, Toast.LENGTH_LONG).show();
                                    }


                                }

                            }

                        });
                        builder.setNegativeButton(R.string.skip, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sql = "insert into _hoadon values ('" + selected_table + "', 'cho', " + tongtien + ", -1, '" + date + "')";

                                db.execSQL(sql);
                                sql = "update _table set empty=1 where idban='" + selected_table + "'";
                                db.execSQL(sql);
                                Toast.makeText(getBaseContext(), R.string.order_success, Toast.LENGTH_SHORT).show();
                                onBackPressed();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        tinhtonggiohang();
        tinhtongtien();
    }

    public void tinhtonggiohang() {
        String sql = "select sum(soluong) from _chitiethoadon where ban='" + selected_table + "'";
        Cursor cursor_tongsl = db.rawQuery(sql, null);
        cursor_tongsl.moveToFirst();
        binding.tongSoLuong.setText("" + cursor_tongsl.getInt(0));
        tongsl = cursor_tongsl.getInt(0);
    }

    public void tinhtongtien() {

        String sql = "select sum(soluong * (gia*(100-khuyenmai)/100)) from _chitiethoadon join _menu on _chitiethoadon.tenhang = _menu.tenhang where ban='" + selected_table + "'";
        Cursor cursor_tongtien = db.rawQuery(sql, null);
        cursor_tongtien.moveToFirst();

//        if (cursor_tongtien!=null && cursor_tongtien.getCount()>0)
//        {
//
//                tongtien+= cursor_tongtien.getInt(0) * cursor.getInt(1);
//                cursor_tongtien.moveToNext();
//
//            cursor_tongtien.moveToNext();
//        }
        tongtien = cursor_tongtien.getInt(0);
        binding.txtTongtien.setText(String.format("%,d", tongtien) + getResources().getString(R.string.vnd));
    }

    private void initData() {
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        //String sql = "drop table _hoadon";
        String sql = "create table if not exists _hoadon (ban TEXT, ghichu TEXT, thanhtien integer, thanhtoan integer, ngay integer)";
        db.execSQL(sql);
        //sql = "drop table _chitiethoadon";
        sql = "create table if not exists _chitiethoadon (ban TEXT, tenhang TEXT, soluong integer)";
        db.execSQL(sql);


        sql = "select idban, floor from _table where empty=0 order by idban";

        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        list_table.add(getResources().getString(R.string.takeaway));

        tablelist.add("0");
        while (!cursor.isAfterLast()) {
            list_table.add(getResources().getString(R.string.table) + " " + cursor.getInt(0) + " " + getResources().getString(R.string.floor) + " " + cursor.getInt(1) + "");
            tablelist.add(cursor.getInt(0) + "");
            cursor.moveToNext();

        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, list_table);

        binding.cbbBan.setAdapter(arrayAdapter);


    }
}