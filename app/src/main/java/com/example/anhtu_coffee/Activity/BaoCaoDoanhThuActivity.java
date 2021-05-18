package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.anhtu_coffee.Adapter.HoadonAdapter;
import com.example.anhtu_coffee.Object.HoaDon;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityBaoCaoDoanhThuBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class BaoCaoDoanhThuActivity extends AppCompatActivity {
    ActivityBaoCaoDoanhThuBinding binding;
    final Calendar myCalendar = Calendar.getInstance();
    int start=0;
    int end=0;
    SQLiteDatabase db;
    Cursor cursor;
    List<HoaDon> hoaDonList = new ArrayList<>();
    HoadonAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.sttbanhang));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bao_cao_doanh_thu);
        db = openOrCreateDatabase("anhtucoffee.db", MODE_PRIVATE, null);

        DatePickerDialog.OnDateSetListener datestart = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelstart();
            }
        };
        DatePickerDialog.OnDateSetListener dateend = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelend();
            }
        };


        binding.dateStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BaoCaoDoanhThuActivity.this, datestart, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.dateEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(BaoCaoDoanhThuActivity.this, dateend, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        binding.btnThongke.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initData();
            }
        });

    }
    public  void initData()
    {
        String sql = "select * from _hoadon where ngay BETWEEN "+start+" and "+end+" order by ngay";

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

       sql = "select sum(thanhtien) from _hoadon where ngay between "+start+" and "+end+" order by ngay";

//        sql = "select sum(thanhtien) from _hoadon";
        cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        int sldon = cursor.getInt(0);
        binding.txtTongCart.setText(String.format("%,d", sldon)+" "+getResources().getString(R.string.vnd));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initData();
    }
    private void updateLabelstart() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String myFormat1 = "yyyyMMdd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        start = Integer.parseInt(sdf1.format(myCalendar.getTime()));
        binding.dateStart.setText(sdf.format(myCalendar.getTime()));
    }
    private void updateLabelend() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        String myFormat1 = "yyyyMMdd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        SimpleDateFormat sdf1 = new SimpleDateFormat(myFormat1, Locale.US);
        end = Integer.parseInt(sdf1.format(myCalendar.getTime()));
        binding.dateEnd.setText(sdf.format(myCalendar.getTime()));
    }
}