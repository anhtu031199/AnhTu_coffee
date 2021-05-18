package com.example.anhtu_coffee.Adapter;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.anhtu_coffee.Object.Menu;
import com.example.anhtu_coffee.Object.Table;
import com.example.anhtu_coffee.R;

import java.util.List;

public class MenuAdapter extends BaseAdapter {
    List<Menu> menuList;

    public MenuAdapter(List<Menu> menuList) {
        this.menuList = menuList;
    }

    @Override
    public int getCount() {
        return menuList.size();
    }

    @Override
    public Object getItem(int position) {
        return menuList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.menu_item,parent, false);
        Menu menu = menuList.get(position);
        TextView tenhang, gia, khuyenmai, thanhphan, linkAnh;
        ImageView img;
        tenhang = view.findViewById(R.id.txt_tenhang_menu);
        gia = view.findViewById(R.id.txt_gia_menu);
        thanhphan = view.findViewById(R.id.txt_thanhphan_menu);
        khuyenmai = view.findViewById(R.id.txt_khuyenmai_menu);
        linkAnh = view.findViewById(R.id.txt_linkAnh_menu);

        img = view.findViewById(R.id.img_item_menu);

        tenhang.setText(menu.getTenHang());
        gia.setText(String.format("%,d", menu.getGia())+view.getResources().getString(R.string.vnd));
        thanhphan.setText(menu.getThanhPhan());
        khuyenmai.setText("-"+menu.getKhuyenMai()+"%");
        khuyenmai.setTextColor(view.getResources().getColor(R.color.red));
        linkAnh.setText(menu.getLinkAnh());


        if(linkAnh.getText().toString().equals(""))
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
