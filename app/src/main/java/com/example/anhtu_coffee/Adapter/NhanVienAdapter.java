package com.example.anhtu_coffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhtu_coffee.Object.NhanVien;
import com.example.anhtu_coffee.Object.Table;
import com.example.anhtu_coffee.R;

import java.util.List;

public class NhanVienAdapter extends BaseAdapter {
    List<NhanVien> nhanVienList;

    public NhanVienAdapter(List<NhanVien> nhanVienList) {
        this.nhanVienList = nhanVienList;
    }

    @Override
    public int getCount() {
        return nhanVienList.size();
    }

    @Override
    public Object getItem(int position) {
        return nhanVienList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.nhanvien_item, parent, false);
        NhanVien nv = nhanVienList.get(position);
        TextView hoten, sdt, user;
        ImageView img = view.findViewById(R.id.img_item_nhanvien);
        hoten = view.findViewById(R.id.txt_hoten_nhanvien);
        sdt = view.findViewById(R.id.txt_sdt_nhanvien);
        user = view.findViewById(R.id.txt_user_nhanvien);


        hoten.setText(nv.getHoTen());
        sdt.setText(nv.getSdt());
        user.setText(nv.getUser()+" / "+nv.getPass());

        if(nv.getPer()==1)
        {
            img.setImageResource(R.drawable.nhanvien2);
        }
        else {
            img.setImageResource(R.drawable.nhanvien);
        }


        return view;
    }
}
