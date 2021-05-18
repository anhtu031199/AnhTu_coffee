package com.example.anhtu_coffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anhtu_coffee.Object.Cart;
import com.example.anhtu_coffee.Object.Stock;
import com.example.anhtu_coffee.R;

import java.util.List;

public class CartAdapter extends BaseAdapter {
    List<Cart> cartList;

    public CartAdapter(List<Cart> cartList) {
        this.cartList = cartList;
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Object getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cart_item,parent, false);
        Cart cart = cartList.get(position);

        TextView ten, gia;
        ten = view.findViewById(R.id.txt_tenhang_cart);
        gia = view.findViewById(R.id.txt_gia_cart);
        ten.setText(cart.getTenhang()+" ("+cart.getSoluong()+")");
        gia.setText(String.format("%,d", cart.getGia())+view.getResources().getString(R.string.vnd));


        return view;
    }
}
