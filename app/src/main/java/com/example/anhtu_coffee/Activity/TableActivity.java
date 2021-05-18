package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.anhtu_coffee.Adapter.TableAdaper;
import com.example.anhtu_coffee.Interface.ITable;
import com.example.anhtu_coffee.Object.Table;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityTableBinding;

import java.util.ArrayList;
import java.util.List;

public class TableActivity extends AppCompatActivity {
    ActivityTableBinding binding;
    SQLiteDatabase db;
    List<Table> tableList = new ArrayList<>();
    TableAdaper table_adapter;
    Cursor cursor;
    int id_cu, tangcu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_table);
        initData();
        String sql = "select * from _table order by idban";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        tableList.clear();

        while (!cursor.isAfterLast()) {
            int idban = cursor.getInt(0);
            int floor = cursor.getInt(1);
            int empty = cursor.getInt(2);
            Table table = new Table(idban, floor, empty);
            tableList.add(table);
            cursor.moveToNext();
        }

        table_adapter = new TableAdaper(tableList);
        binding.lvTable.setAdapter(table_adapter);

        binding.btnAddTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTable();
            }
        });

        binding.lvTable.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                AlertDialog alertDialog = new AlertDialog.Builder(TableActivity.this)
                                        .setTitle(R.string.delete)
                                        .setMessage(R.string.xac_nhan_xoa)
                                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String sqldelete = "delete from _table where idban=" + tableList.get(position).getId_table();
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
                                editTable(tableList.get(position).getId_table() + "", tableList.get(position).getFloor() + "");
                                break;
                        }


                        return false;
                    }
                });


                return false;

            }
        });

        binding.txtSearchTable.setVisibility(View.GONE);
        binding.btnSearchTable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSearchTable.setVisibility(View.VISIBLE);
            }
        });
        binding.btnSearchGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchTable.setText("");
                binding.txtSearchTable.setVisibility(View.GONE);
            }
        });
        binding.etSearchTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sql="";
                if(binding.etSearchTable.getText().toString().equals(""))
                {
                    sql = "select * from _table order by idban";
                }
                else
                {
                    sql = "select * from _table where idban like '%"+binding.etSearchTable.getText().toString()+"%' order by idban";
                }

                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                tableList.clear();
                while (!cursor.isAfterLast()) {
                    int idban = cursor.getInt(0);
                    int floor = cursor.getInt(1);
                    int empty = cursor.getInt(2);
                    Table table = new Table(idban, floor, empty);
                    tableList.add(table);
                    cursor.moveToNext();

                }
                table_adapter.notifyDataSetChanged();
            }
        });
    }

    private void initData() {

        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
//String sql = "drop table _table";
        String sql = "CREATE TABLE IF NOT EXISTS _table" +
                "(idban integer,  floor integer, empty integer)";
        db.execSQL(sql);
//        sql = "insert into _table values(null, 1, 1, 0)";
//        db.execSQL(sql);

    }

    public void loadData() {
        String sql = "select * from _table order by idban";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        tableList.clear();
        while (!cursor.isAfterLast()) {
            int idban = cursor.getInt(0);
            int floor = cursor.getInt(1);
            int empty = cursor.getInt(2);
            Table table = new Table(idban, floor, empty);
            tableList.add(table);
            cursor.moveToNext();

        }
        table_adapter.notifyDataSetChanged();


    }

    public void editTable(String id, String floor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_table_layout, null);
        builder.setView(customDialog);
        EditText etid = customDialog.findViewById(R.id.et_id_table_add);
        EditText etfloor = customDialog.findViewById(R.id.et_floor_table_add);
        CheckBox cbempty = customDialog.findViewById(R.id.cb_empty_table_add);
        etid.setHint(id);
        etfloor.setHint(floor);
        builder.setTitle(R.string.update_table);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (etid.getText().toString().equals("") || etfloor.getText().toString().equals("")) {

                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(etfloor.getText().toString()) < 1) {
                    Toast.makeText(getBaseContext(), R.string.check_floor, Toast.LENGTH_LONG).show();
                } else {
                    String sqlchecktable = "select * from _table where idban = '" + etid.getText().toString() + "'";
                    cursor = db.rawQuery(sqlchecktable, null);
                    if (cursor.getCount() > 0 && !etid.getText().toString().equals(id)) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    } else {
                        String sql = "delete from _table where idban=" + id;

                        db.execSQL(sql);
                        int empty;
                        if (cbempty.isChecked())
                            empty = 0;
                        else empty = 1;
                        sql = "insert into _table values(" + etid.getText().toString() + ", " + etfloor.getText().toString() + ", "+empty+")";

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

    public void addTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TableActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_table_layout, null);
        builder.setView(customDialog);
        builder.setTitle(R.string.add_table);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText etid = customDialog.findViewById(R.id.et_id_table_add);
                EditText etfloor = customDialog.findViewById(R.id.et_floor_table_add);
                CheckBox cbempty = customDialog.findViewById(R.id.cb_empty_table_add);

                if (etid.getText().toString().equals("") || etfloor.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(etfloor.getText().toString()) < 1) {
                    Toast.makeText(getBaseContext(), R.string.check_floor, Toast.LENGTH_LONG).show();
                } else {
                    String sqlchecktable = "select * from _table where idban = '" + etid.getText().toString() + "'";
                    cursor = db.rawQuery(sqlchecktable, null);
                    if (cursor.getCount() > 0) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    } else {
                        int empty;
                        if (cbempty.isChecked())
                            empty = 0;
                        else empty = 1;
                        String sql = "insert into _table values (" + etid.getText().toString() + ", " + etfloor.getText().toString() + ", " + empty + ")";
                        db.execSQL(sql);

                        Toast.makeText(getBaseContext(), R.string.add_success, Toast.LENGTH_SHORT).show();
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


}