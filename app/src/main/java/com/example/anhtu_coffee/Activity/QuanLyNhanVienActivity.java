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

import com.example.anhtu_coffee.Adapter.NhanVienAdapter;
import com.example.anhtu_coffee.Adapter.StockAdapter;
import com.example.anhtu_coffee.Object.NhanVien;
import com.example.anhtu_coffee.Object.Stock;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityQuanLyNhanVienBinding;

import java.util.ArrayList;
import java.util.List;

public class QuanLyNhanVienActivity extends AppCompatActivity {
    ActivityQuanLyNhanVienBinding binding;
    SQLiteDatabase db;
    Cursor cursor;
    List<NhanVien> nhanVienList = new ArrayList<>();
    NhanVienAdapter adapter_nv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_quan_ly_nhan_vien);
        initData();
        String sql = "select * from _nhanvien join login on _nhanvien.user = login.user order by hoten";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        nhanVienList.clear();
        while (!cursor.isAfterLast()) {
            String hoten = cursor.getString(cursor.getColumnIndex("hoten"));
            String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String pass = cursor.getString(cursor.getColumnIndex("pass"));
            int per = cursor.getInt(cursor.getColumnIndex("per"));

            NhanVien nv = new NhanVien(hoten, sdt, user, pass, per);
            nhanVienList.add(nv);
            cursor.moveToNext();

        }
        adapter_nv = new NhanVienAdapter(nhanVienList);
        binding.lvNv.setAdapter(adapter_nv);
        binding.btnAddNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNV();
            }
        });
        binding.lvNv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
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
                                AlertDialog alertDialog = new AlertDialog.Builder(QuanLyNhanVienActivity.this)
                                        .setTitle(R.string.delete)
                                        .setMessage(R.string.xac_nhan_xoa)
                                        .setPositiveButton(R.string.agree, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                String sqldelete1 = "delete from _nhanvien where user='" + nhanVienList.get(position).getUser()+"'";
                                                String sqldelete2 = "delete from login where user='" + nhanVienList.get(position).getUser()+"'";
                                                try {
                                                    db.execSQL(sqldelete1);
                                                    db.execSQL(sqldelete2);
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
                                editNV(nhanVienList.get(position).getHoTen(),nhanVienList.get(position).getSdt(), nhanVienList.get(position).getUser(), nhanVienList.get(position).getPer());
                                break;
                        }


                        return false;
                    }
                });

                return false;
            }
        });

        binding.txtSearchNv.setVisibility(View.GONE);
        binding.btnSearchNv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.txtSearchNv.setVisibility(View.VISIBLE);
            }
        });
        binding.btnSearchGone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.etSearchNv.setText("");
                binding.txtSearchNv.setVisibility(View.GONE);
            }
        });
        binding.etSearchNv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String sql="";
                if(binding.etSearchNv.getText().toString().equals(""))
                {
                    sql = "select * from _nhanvien join login on _nhanvien.user = login.user order by hoten";
                }
                else
                {
                    sql = "select * from _nhanvien join login on _nhanvien.user = login.user where hoten like '%"+binding.etSearchNv.getText().toString()+"%' order by hoten";
                }

                cursor = db.rawQuery(sql, null);
                cursor.moveToFirst();
                nhanVienList.clear();
                while (!cursor.isAfterLast()) {
                    String hoten = cursor.getString(cursor.getColumnIndex("hoten"));
                    String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
                    String user = cursor.getString(cursor.getColumnIndex("user"));
                    String pass = cursor.getString(cursor.getColumnIndex("pass"));
                    int per = cursor.getInt(cursor.getColumnIndex("per"));

                    NhanVien nv = new NhanVien(hoten, sdt, user, pass, per);
                    nhanVienList.add(nv);
                    cursor.moveToNext();

                }
                adapter_nv.notifyDataSetChanged();
            }
        });

    }

    private void editNV(String hoTen, String sdt, String user, int per) {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyNhanVienActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_nv_layout, null);
        builder.setView(customDialog);
        EditText ethoten = customDialog.findViewById(R.id.et_hoten_nv_add);
        EditText etsdt = customDialog.findViewById(R.id.et_sdt_nv_add);
        EditText etuser = customDialog.findViewById(R.id.et_user_nv_add);
        CheckBox cbper = customDialog.findViewById(R.id.cb_per_nv_add);
        etuser.setEnabled(false);
        ethoten.setEnabled(false);
        ethoten.setText(hoTen);
        etsdt.setHint(sdt);
        etuser.setText(user);

        builder.setTitle(R.string.update_employee);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                if (ethoten.getText().toString().equals("") || etsdt.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                }
                else {
                        int per;
                        if(cbper.isChecked())
                            per=1;
                        else per=0;

                        String sql = "update login set per="+per+" where user='"+user+"'";
                        db.execSQL(sql);
                        sql = "update _nhanvien set sdt='"+etsdt.getText().toString()+"' where user='"+user+"'";
                        db.execSQL(sql);

                        Toast.makeText(getBaseContext(), R.string.update_success, Toast.LENGTH_SHORT).show();
                        loadData();


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

    private void addNV() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuanLyNhanVienActivity.this);
        final View customDialog = getLayoutInflater().inflate(R.layout.add_nv_layout, null);
        builder.setView(customDialog);
        builder.setTitle(R.string.add_employee);
        builder.setPositiveButton(R.string.save, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText ethoten = customDialog.findViewById(R.id.et_hoten_nv_add);
                EditText etsdt = customDialog.findViewById(R.id.et_sdt_nv_add);
                EditText etuser = customDialog.findViewById(R.id.et_user_nv_add);
                CheckBox cbper = customDialog.findViewById(R.id.cb_per_nv_add);


                if (ethoten.getText().toString().equals("") || etsdt.getText().toString().equals("") || etuser.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_LONG).show();
                }
                 else {
                    String sqlchecktable = "select * from _nhanvien where user = '" + etuser.getText().toString() + "'";
                    cursor = db.rawQuery(sqlchecktable, null);
                    if (cursor.getCount() > 0) {
                        Toast.makeText(getBaseContext(), R.string.duplicated, Toast.LENGTH_LONG).show();
                    } else {
                        int per;
                        if(cbper.isChecked())
                        {
                            per=1;
                        }
                        else
                            per=0;
                        String sql = "insert into _nhanvien (hoten, sdt, user) values ('" + ethoten.getText().toString() + "', '" + etsdt.getText().toString() + "', " +
                                "'" + etuser.getText().toString() + "')";
                        db.execSQL(sql);
                        sql = "insert into login (user, pass, per) values ('"+etuser.getText().toString()+"', 'a', "+per+")";
                        db.execSQL(sql);
                        Toast.makeText(getBaseContext(), getResources().getString(R.string.new_account)+" '"+etuser.getText().toString()+"', "+getResources().getString(R.string.default_pass)+" 'a'.", Toast.LENGTH_LONG).show();

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

    private void initData() {
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
        String sql = "CREATE TABLE IF NOT EXISTS _nhanvien" +
                "(hoten TEXT,  sdt TEXT, user TEXT)";

        db.execSQL(sql);
//        sql = "insert into _nhanvien values('Nguyen Anh Tu', '113', 'admin')";
//        db.execSQL(sql);
//        db.execSQL(sql);
    }
    public void loadData() {
        String sql = "select * from _nhanvien join login on _nhanvien.user = login.user order by hoten";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        nhanVienList.clear();
        while (!cursor.isAfterLast()) {
            String hoten = cursor.getString(cursor.getColumnIndex("hoten"));
            String sdt = cursor.getString(cursor.getColumnIndex("sdt"));
            String user = cursor.getString(cursor.getColumnIndex("user"));
            String pass = cursor.getString(cursor.getColumnIndex("pass"));
            int per = cursor.getInt(cursor.getColumnIndex("per"));

            NhanVien nv = new NhanVien(hoten, sdt, user, pass, per);
            nhanVienList.add(nv);
            cursor.moveToNext();

        }
        adapter_nv.notifyDataSetChanged();


    }
}