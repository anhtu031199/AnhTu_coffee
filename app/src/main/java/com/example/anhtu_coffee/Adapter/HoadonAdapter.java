package com.example.anhtu_coffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhtu_coffee.Object.HoaDon;
import com.example.anhtu_coffee.Object.Menu;
import com.example.anhtu_coffee.R;

import java.util.List;

public class HoadonAdapter extends BaseAdapter {
    List<HoaDon> hoaDonList;

    public HoadonAdapter(List<HoaDon> hoaDonList) {
        this.hoaDonList = hoaDonList;
    }

    @Override
    public int getCount() {
        return hoaDonList.size();
    }

    @Override
    public Object getItem(int position) {
        return hoaDonList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hoadon_item,parent, false);

        HoaDon hoaDon = hoaDonList.get(position);
        TextView ban, thanhtien;

        ban = view.findViewById(R.id.txt_ban_hoadon);
        thanhtien = view.findViewById(R.id.txt_thanhtien_hoadon);
        if(hoaDon.getBan().length()>4)
        {
            ban.setText(hoaDon.getBan());
        }
        else
        {
            ban.setText(view.getResources().getString(R.string.table)+" "+hoaDon.getBan());
        }

        thanhtien.setText(String.format("%,d", hoaDon.getThanhtien())+view.getResources().getString(R.string.vnd));
        return view;
    }
}
