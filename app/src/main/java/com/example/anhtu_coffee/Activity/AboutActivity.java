package com.example.anhtu_coffee.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.ActivityAboutBinding;

public class AboutActivity extends AppCompatActivity {
    ActivityAboutBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getColor(R.color.maincolor));
        binding = DataBindingUtil.setContentView(this, R.layout.activity_about);
        binding.txtAbout.setText(getResources().getString(R.string.copy_right)+"\n"+getResources().getString(R.string.do_an)+"\n"+getResources().getString(R.string.ten_de_tai)+
                "\n"+getResources().getString(R.string.ten_de_tai1)+"\n"+getResources().getString(R.string.ten_de_tai2)+"\n"+getResources().getString(R.string.ten_de_tai3)+"\n \n" +
                getResources().getString(R.string.dev)+"\n"+getResources().getString(R.string.nat)+"\n"+getResources().getString(R.string.student_code)+" 2017603291\n" +
                getResources().getString(R.string.gv_huongdan)+"\n"+getResources().getString(R.string.gv)+"\n"+getResources().getString(R.string.fit)+
                "\n"+getResources().getString(R.string.haui)+"\n \n"+getResources().getString(R.string.ver)+"\n"+getResources().getString(R.string.hn2021));
    }
}