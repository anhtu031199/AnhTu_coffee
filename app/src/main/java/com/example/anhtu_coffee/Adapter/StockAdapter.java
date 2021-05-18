package com.example.anhtu_coffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhtu_coffee.Object.Stock;
import com.example.anhtu_coffee.Object.Table;
import com.example.anhtu_coffee.R;

import java.util.List;

public class StockAdapter extends BaseAdapter {
    List<Stock> stockList;

    public StockAdapter(List<Stock> stockList) {
        this.stockList = stockList;
    }

    @Override
    public int getCount() {
        return stockList.size();
    }

    @Override
    public Object getItem(int position) {
        return stockList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.stock_item,parent, false);
        Stock stock = stockList.get(position);
        TextView nguyenlieu, ncc, slcon;
        ImageView img;
        nguyenlieu = view.findViewById(R.id.txt_nguyenlieu_stock);
        ncc = view.findViewById(R.id.txt_ncc_stock);
        slcon = view.findViewById(R.id.txt_slcon_stock);

        img = view.findViewById(R.id.img_item_stock);
        nguyenlieu.setText(stock.getNguyenLieu());
        ncc.setText("NCC: "+stock.getNcc());
        slcon.setText(stock.getSlcon()+" "+stock.getDonvitinh());

        if(stock.getSlcon()<=0)
        {
            slcon.setText("Hết");
            slcon.setTextColor(view.getResources().getColor(R.color.red));
            img.setImageResource(R.drawable.stock0);
        }
        else {
            slcon.setTextColor(view.getResources().getColor(R.color.green));
            img.setImageResource(R.drawable.stock1);
        }

        return view;
    }
}
