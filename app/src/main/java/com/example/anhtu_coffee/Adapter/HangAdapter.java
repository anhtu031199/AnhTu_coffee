package com.example.anhtu_coffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhtu_coffee.Object.Menu;
import com.example.anhtu_coffee.R;

import java.util.List;

public class HangAdapter extends BaseAdapter {
    List<Menu> hangList;

    public HangAdapter(List<Menu> hangList) {
        this.hangList = hangList;
    }

    @Override
    public int getCount() {
        return hangList.size();
    }

    @Override
    public Object getItem(int position) {
        return hangList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.hang_item,parent, false);
        Menu menu = hangList.get(position);
        TextView tenhang, gia;
        ImageView img;
        tenhang = view.findViewById(R.id.txt_tenhang_hang);
        gia = view.findViewById(R.id.txt_gia_hang);


        img = view.findViewById(R.id.img_item_hang);

        tenhang.setText(menu.getTenHang());
        float giamoi = menu.getGia() * (100-menu.getKhuyenMai())/100;
        gia.setText(String.format("%,.0f", giamoi)+"đ");
        if(menu.getKhuyenMai()>0)
        {
            gia.setText(gia.getText().toString()+"  (-"+menu.getKhuyenMai()+"%)");
        }
        if(menu.getLinkAnh()==null)
        {
            Glide.with(view.getContext()).load(R.drawable.menu1).into(img);
        }
        else
        {
            Glide.with(view.getContext()).load(menu.getLinkAnh()).placeholder(R.drawable.menu1).into(img);
        }

        return view;
    }
}
