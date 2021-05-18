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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbarmainact));

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        initData();
        binding.btnForgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), R.string.ask_manager, Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnNewuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getBaseContext(), R.string.ask_manager, Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.etusername.getText().toString().equals("") || binding.etpassword.getText().toString().equals("")) {
                    Toast.makeText(getBaseContext(), R.string.dont_empty, Toast.LENGTH_SHORT).show();
                } else {

                    int x = login(binding.etusername.getText().toString(), binding.etpassword.getText().toString());
                    if (x > 0) {

                        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                        intent.putExtra("username", binding.etusername.getText().toString());
                        startActivity(intent);
                        if (binding.rememberPass.isChecked()) {
                            String sqlacc = "update login set remember=0";
                            db.execSQL(sqlacc);
                            sqlacc = "update login set remember=1 where user='" + binding.etusername.getText().toString() + "'";
                            db.execSQL(sqlacc);
                        } else {
                            String sqlacc = "update login set remember=0";
                            db.execSQL(sqlacc);
                            binding.etpassword.setText("");
                        }


                    } else {
                        Toast.makeText(getBaseContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void initData() {
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);
//        String sql = "drop table login";
//        db.execSQL(sql);
        String sql = "CREATE TABLE IF NOT EXISTS login" +
                "(_id integer primary key autoincrement, user text, pass text, per integer, remember integer)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS _nhanvien" +
                "(hoten TEXT,  sdt TEXT, user TEXT)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS _table" +
                "(idban integer,  floor integer, empty integer)";
        db.execSQL(sql);
        sql = "CREATE TABLE IF NOT EXISTS _menu" +
                "(tenhang TEXT,  loaihang integer, gia integer, khuyenmai float, thanhphan TEXT, linkanh TEXT)";
        db.execSQL(sql);
        sql = "create table if not exists _khachhang (hoten TEXT, sdt TEXT, chitieu integer)";
        db.execSQL(sql);
        sql = "select count(user) from login";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int a = cursor.getInt(0);
        if (a == 0) {
            Toast.makeText(getBaseContext(), R.string.create_new_account, Toast.LENGTH_LONG).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                    } else {


                        String sql = "insert into _nhanvien (hoten, sdt, user) values ('" + ethoten.getText().toString() + "', '" + etsdt.getText().toString() + "', " +
                                "'" + etuser.getText().toString() + "')";
                        db.execSQL(sql);
                        sql = "insert into login (user, pass, per) values ('" + etuser.getText().toString() + "', 'a', 1)";
                        db.execSQL(sql);
                        Toast.makeText(getBaseContext(), R.string.new_account+"'" + etuser.getText().toString() + "', "+R.string.default_pass+"'a'.", Toast.LENGTH_LONG).show();


                    }

                }

            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        sql = "select * from login where remember=1";
        cursor = db.rawQuery(sql, null);
        if (cursor.getCount() > 0) {
            int i = cursor.getCount();
            cursor.moveToFirst();
            binding.etusername.setText(cursor.getString(cursor.getColumnIndex("user")));
            binding.etpassword.setText(cursor.getString(cursor.getColumnIndex("pass")));
        }
//        sql = "insert into login values (null, 'admin', '123', 1, 0)";
//        db.execSQL(sql);
//        sql = "insert into nhanvien values(null, 'Nguyễn Anh Tú', '0963845802', 'anhtu123')";
//        db.execSQL(sql);

    }

    public int login(String user, String pass) {

        String sql = "select * from login where user='" + user + "' and pass='" + pass + "'";
        Cursor cursor = db.rawQuery(sql, null);

        return cursor.getCount();
    }
}