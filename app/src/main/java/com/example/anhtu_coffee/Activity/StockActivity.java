package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.anhtu_coffee.Adapter.StockAdapter;
import com.example.anhtu_coffee.Adapter.TableAdaper;
import com.example.anhtu_coffee.Object.Stock;
import com.example.anhtu_coffee.Object.Table;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityStockBinding;

import java.util.ArrayList;
import java.util.List;

public class StockActivity extends AppCompatActivity {
    ActivityStockBinding binding;
    SQLiteDatabase db;
    Cursor cursor;
    StockAdapter stock_adapter;
    List<Stock> stockList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stock);
        initData();
        String sql = "select * from _stock order by nguyenlieu";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        stockList.clear();

        while (!cursor.isAfterLast()) {
            String nguyenLieu = cursor.getString(0);
            int slCon = cursor.getInt(1);
            String ncc = cursor.getString(2);
            String donvitinh = cursor.getString(3);
            Stock stock = new Stock(nguyenLieu, ncc, donvitinh, slCon);
            stockList.add(stock);
            cursor.moveToNext();
        }

        stock_adapter = new StockAdapter(stockList);
        binding.lvStock.setAdapter(stock_adapter);


        binding.btnAddStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStock();
            }
        });
        binding.lvStock.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                AlertDialog alertDialog = new AlertDialog.Builder(StockActivity.this)
                                        .setTitle(R.string.delete)
                                        .setMessage(R.string.xac_nhan_xoa)
                                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String sqldelete = "delete from _stock where nguyenlieu='" + stockList.get(position).getNguyenLieu() + "'";
                                                try {
                                                    db.execSQL(sqldelete);
                                                    loadData();
                                                    Toast.makeText(getBaseContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                                                } catch (Exception e) {
                                                    e.printStackTrace();
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
                                editStock(stockList.get(position).getNguyenLieu(), stockList.get(position).getSlcon() + "", stockList.get(position).getNcc(), stockList.get(position).getDonvitinh());
                                break;
                        }


                        return false;
                    }
                });


                return false;

            }
        });

        binding.txtSearchStock.setVisibility(View.GONE);
        binding.btnSearchStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSearchStock.setVisibility(View.VISIBLE);
            }
        });
        binding.btnSearchGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchStock.setText("");
                binding.txtSearchStock.setVisibility(View.GONE);
            }
        });
        binding.etSearchStock.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sql = "";
                if (binding.etSearchStock.getText().toString().equals("")) {
                    sql = "select * from _stock order by nguyenlieu";
                } else {
                    sql = "select * from _stock where nguyenlieu like '%" + binding.etSearchStock.getText().toString() + "%' order by nguyenlieu";
                }

                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                stockList.clear();
                while (!cursor.isAfterLast()) {
                    String nguyenLieu = cursor.getString(0);
                    int slCon = cursor.getInt(1);
                    String ncc = cursor.getString(2);
                    String donvitinh = cursor.getString(3);
                    Stock stock = new Stock(nguyenLieu, ncc, donvitinh, slCon);
                    stockList.add(stock);
                    cursor.moveToNext();

                }
                stock_adapter.notifyDataSetChanged();
            }
        });
    }

    private void editStock(String nguyenLieu, String s, String ncc, String donvitinh) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StockActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_stock_layout, null);
        builder.setView(customDialog);
        EditText etnguyenlieu = customDialog.findViewById(R.id.et_nguyenlieu_stock_add);
        EditText etncc = customDialog.findViewById(R.id.et_ncc_stock_add);
        EditText etslcon = customDialog.findViewById(R.id.et_slcon_stock_add);
        EditText etdonvitinh = customDialog.findViewById(R.id.et_donvitinh_stock_add);

        etnguyenlieu.setHint(nguyenLieu);
        etncc.setHint(ncc);
        etslcon.setHint(s);
        etdonvitinh.setHint(donvitinh);
        builder.setTitle(R.string.update_material);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (etnguyenlieu.getText().toString().equals("") || etncc.getText().toString().equals("") || etslcon.getText().toString().equals("") || etdonvitinh.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(etslcon.getText().toString()) < 0) {
                    Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_LONG).show();
                } else {
                    String sqlchecktable = "select * from _stock where nguyenlieu = '" + etnguyenlieu.getText().toString() + "'";
                    cursor = db.rawQuery(sqlchecktable, null);
                    if (cursor.getCount() > 0 && !etnguyenlieu.getText().toString().equals(nguyenLieu)) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    } else {


                        try {
                            String sql = "delete from _stock where nguyenlieu='" + nguyenLieu + "'";
                            db.execSQL(sql);
                            sql = "insert into _stock (nguyenlieu, slcon, ncc, donvitinh) values ('" + etnguyenlieu.getText().toString() + "', " + etslcon.getText().toString() + ", " +
                                    "'" + etncc.getText().toString() + "', '" + etdonvitinh.getText().toString() + "')";
                            db.execSQL(sql);
                            Toast.makeText(getBaseContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                            loadData();
                        } catch (Exception e) {
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

    private void addStock() {
        AlertDialog.Builder builder = new AlertDialog.Builder(StockActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_stock_layout, null);
        builder.setView(customDialog);
        builder.setTitle(R.string.add_material);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etnguyenlieu = customDialog.findViewById(R.id.et_nguyenlieu_stock_add);
                EditText etncc = customDialog.findViewById(R.id.et_ncc_stock_add);
                EditText etslcon = customDialog.findViewById(R.id.et_slcon_stock_add);
                EditText etdonvitinh = customDialog.findViewById(R.id.et_donvitinh_stock_add);


                if (etnguyenlieu.getText().toString().equals("") || etncc.getText().toString().equals("") || etslcon.getText().toString().equals("") || etdonvitinh.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(etslcon.getText().toString()) < 0) {
                    Toast.makeText(getBaseContext(), R.string.failed, Toast.LENGTH_LONG).show();
                } else {
                    String sqlchecktable = "select * from _stock where nguyenlieu = '" + etnguyenlieu.getText().toString() + "'";
                    cursor = db.rawQuery(sqlchecktable, null);
                    if (cursor.getCount() > 0) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    } else {
                        try {
                            String sql = "insert into _stock (nguyenlieu, slcon, ncc, donvitinh) values ('" + etnguyenlieu.getText().toString() + "', " + etslcon.getText().toString() + ", " +
                                    "'" + etncc.getText().toString() + "', '" + etdonvitinh.getText().toString() + "')";
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
//        String sql = "drop table _stock";
//        db.execSQL(sql);
        String sql = "CREATE TABLE IF NOT EXISTS _stock" +
                "(nguyenlieu TEXT,  slcon integer, ncc TEXT, donvitinh TEXT,  _id integer not null primary key autoincrement)";
        db.execSQL(sql);
    }

    public void loadData() {
        String sql = "select * from _stock order by nguyenlieu";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        stockList.clear();
        while (!cursor.isAfterLast()) {
            String nguyenLieu = cursor.getString(0);
            int slCon = cursor.getInt(1);
            String ncc = cursor.getString(2);
            String donvitinh = cursor.getString(3);
            Stock stock = new Stock(nguyenLieu, ncc, donvitinh, slCon);
            stockList.add(stock);
            cursor.moveToNext();

        }
        stock_adapter.notifyDataSetChanged();


    }
}