package com.example.anhtu_coffee.Activity;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.example.anhtu_coffee.Adapter.MenuAdapter;
import com.example.anhtu_coffee.Object.Menu;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityMenuBinding;

import java.util.ArrayList;
import java.util.List;

public class MenuActivity extends AppCompatActivity {
    ActivityMenuBinding binding;
    SQLiteDatabase db;
    Cursor cursor;
    List<Menu> menuList = new ArrayList<>();
    MenuAdapter menu_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_menu);
        initData();


        String sql = "select * from _menu order by tenhang";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        menuList.clear();
        while (!cursor.isAfterLast()) {
            String tenhang = cursor.getString(0);
            int loaihang = cursor.getInt(1);
            int gia = cursor.getInt(2);
            float khuyenmai = cursor.getFloat(3);
            String thanhphan = cursor.getString(4);
            String linkanh = cursor.getString(5);

            Menu menu = new Menu(tenhang, thanhphan, linkanh, gia, khuyenmai);
            menuList.add(menu);
            cursor.moveToNext();
        }

        menu_adapter = new MenuAdapter(menuList);
        binding.lvMenu.setAdapter(menu_adapter);

        binding.btnAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMenu();
            }
        });
        binding.lvMenu.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                PopupMenu popupMenu = new PopupMenu(getBaseContext(), view);
                MenuInflater menuInflater = popupMenu.getMenuInflater();
                menuInflater.inflate(R.menu.popup_menu_edit, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.mndelete:
                                AlertDialog alertDialog = new AlertDialog.Builder(MenuActivity.this)
                                        .setTitle(R.string.delete)
                                        .setMessage(R.string.xac_nhan_xoa)
                                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String sqldelete = "delete from _menu where tenhang='" + menuList.get(position).getTenHang() + "'";
                                                try {
                                                    db.execSQL(sqldelete);
                                                    loadData();
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
                                break;
                            case R.id.mnedit:
                                editMenu(menuList.get(position).getTenHang(), menuList.get(position).getGia(), menuList.get(position).getKhuyenMai(), menuList.get(position).getThanhPhan(), menuList.get(position).getLinkAnh());
                                break;
                        }


                        return false;
                    }
                });

                return false;
            }
        });

        binding.txtSearchMenu.setVisibility(View.GONE);
        binding.btnSearchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSearchMenu.setVisibility(View.VISIBLE);
            }
        });
        binding.btnSearchGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchMenu.setText("");
                binding.txtSearchMenu.setVisibility(View.GONE);
            }
        });
        binding.etSearchMenu.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sql="";
                if(binding.etSearchMenu.getText().toString().equals(""))
                {
                    sql = "select * from _menu order by tenhang";
                }
                else
                {
                    sql = "select * from _menu where tenhang like '%"+binding.etSearchMenu.getText().toString()+"%' order by tenhang";
                }

                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                menuList.clear();
                while (!cursor.isAfterLast()) {
                    String tenhang = cursor.getString(0);
                    int loaihang = cursor.getInt(1);
                    int gia = cursor.getInt(2);
                    float khuyenmai = cursor.getFloat(3);
                    String thanhphan = cursor.getString(4);
                    String linkanh = cursor.getString(5);

                    Menu menu = new Menu(tenhang, thanhphan, linkanh, gia, khuyenmai);
                    menuList.add(menu);
                    cursor.moveToNext();

                }
                menu_adapter.notifyDataSetChanged();
            }
        });


    }

    private void editMenu(String tenHang, int gia, float khuyenMai, String thanhPhan, String linkAnh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_menu_layout, null);
        builder.setView(customDialog);
        EditText ettenhang = customDialog.findViewById(R.id.et_tenhang_menu_add);
        EditText etgia = customDialog.findViewById(R.id.et_gia_menu_add);
        EditText etkhuyenmai = customDialog.findViewById(R.id.et_khuyenmai_menu_add);
        EditText etthanhphan = customDialog.findViewById(R.id.et_thanhphan_menu_add);
        EditText etlinkanh = customDialog.findViewById(R.id.et_linkanh_menu_add);

        ettenhang.setHint(tenHang);
        etgia.setHint(gia + "");
        etkhuyenmai.setHint(khuyenMai + "");
        etthanhphan.setHint(thanhPhan);
        etlinkanh.setHint(linkAnh);

        builder.setTitle(R.string.update_item);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (ettenhang.getText().toString().equals("") || etgia.getText().toString().equals("") || etkhuyenmai.getText().toString().equals("") || etthanhphan.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                }
                else if (Integer.parseInt(etgia.getText().toString()) < 1 || Float.parseFloat(etkhuyenmai.getText().toString())<0) {
                    Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_LONG).show();
                }
                else {
                    String sqlcheckmenu = "select COUNT (tenhang) from _menu where tenhang = '" + ettenhang.getText().toString() + "'";
                    Toast.makeText(getBaseContext(), sqlcheckmenu, Toast.LENGTH_LONG).show();
                    Cursor cursor_menu = db.rawQuery(sqlcheckmenu, null);
                    cursor_menu.moveToFirst();
                    if (cursor_menu.getInt(0) > 0 && !ettenhang.getText().toString().equals(tenHang)) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    }
                    else {
                        String sql = "delete from _menu where tenhang = '" + tenHang + "'";
                        db.execSQL(sql);
                        if (etlinkanh.getText().toString().equals(""))
                            sql = "insert into _menu (tenhang, loaihang, gia, khuyenmai, thanhphan, linkanh) values ('" + ettenhang.getText().toString() + "', 0, " + etgia.getText().toString() + ", " +
                                    "" + etkhuyenmai.getText().toString() + ", '" + etthanhphan.getText().toString() + "', null)";
                        else
                            sql = "insert into _menu (tenhang, loaihang, gia, khuyenmai, thanhphan, linkanh) values ('" + ettenhang.getText().toString() + "', 0, " + etgia.getText().toString() + ", " +
                                    "" + etkhuyenmai.getText().toString() + ", '" + etthanhphan.getText().toString() + "', '" + etlinkanh.getText().toString() + "')";

                        db.execSQL(sql);

                        Toast.makeText(getBaseContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                        loadData();
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

    private void addMenu() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MenuActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_menu_layout, null);
        builder.setView(customDialog);
        builder.setTitle(R.string.add_item);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText ettenhang = customDialog.findViewById(R.id.et_tenhang_menu_add);
                EditText etgia = customDialog.findViewById(R.id.et_gia_menu_add);
                EditText etthanhphan = customDialog.findViewById(R.id.et_thanhphan_menu_add);
                EditText etkhuyenmai = customDialog.findViewById(R.id.et_khuyenmai_menu_add);
                EditText etlinkanh = customDialog.findViewById(R.id.et_linkanh_menu_add);


                if (ettenhang.getText().toString().equals("") || etgia.getText().toString().equals("") || etthanhphan.getText().toString().equals("") || etkhuyenmai.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(etgia.getText().toString()) < 1|| Float.parseFloat(etkhuyenmai.getText().toString())<0) {
                    Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_LONG).show();
                } else {
                    String sqlchecktable = "select * from _menu where tenhang = '" + ettenhang.getText().toString() + "'";
                    cursor = db.rawQuery(sqlchecktable, null);
                    if (cursor.getCount() > 0) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            String sql = "";
                            if (etlinkanh.getText().toString().equals(""))

                                sql = "insert into _menu(tenhang, loaihang, gia, khuyenmai, thanhphan, linkanh) values ('" + ettenhang.getText().toString() + "', 0, " + etgia.getText().toString() + ", " +
                                        "" + etkhuyenmai.getText().toString() + ", '" + etthanhphan.getText().toString() + "', null)";
                            else
                                sql = "insert into _menu(tenhang, loaihang, gia, khuyenmai, thanhphan, linkanh) values ('" + ettenhang.getText().toString() + "', 0, " + etgia.getText().toString() + ", " +
                                        "" + etkhuyenmai.getText().toString() + ", '" + etthanhphan.getText().toString() + "', '" + etlinkanh.getText().toString() + "')";
                            db.execSQL(sql);

                            Toast.makeText(getBaseContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
                            loadData();
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_SHORT).show();
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

    private void initData() {
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
//String sql = "drop table _table";
        String sql = "CREATE TABLE IF NOT EXISTS _menu" +
                "(tenhang TEXT,  loaihang integer, gia integer, khuyenmai float, thanhphan TEXT, linkanh TEXT)";
        db.execSQL(sql);
//        sql = "insert into _menu values('a', 0, 1, 1, 'null', 'https://sevencafe.net/wp-content/uploads/2018/10/cafedenda.jpg')";
//        db.execSQL(sql);
    }

    public void loadData() {
        String sql = "select * from _menu order by tenhang";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        menuList.clear();
        while (!cursor.isAfterLast()) {
            String tenhang = cursor.getString(0);
            int loaihang = cursor.getInt(1);
            int gia = cursor.getInt(2);
            float khuyenmai = cursor.getFloat(3);
            String thanhphan = cursor.getString(4);
            String linkanh = cursor.getString(5);

            Menu menu = new Menu(tenhang, thanhphan, linkanh, gia, khuyenmai);
            menuList.add(menu);
            cursor.moveToNext();

        }
        menu_adapter.notifyDataSetChanged();


    }
}