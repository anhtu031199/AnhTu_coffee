package com.example.anhtu_coffee.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anhtu_coffee.Activity.TableActivity;
import com.example.anhtu_coffee.Interface.ITable;
import com.example.anhtu_coffee.Object.Table;
import com.example.anhtu_coffee.R;
import com.example.anhtu_coffee.databinding.TableItemBinding;

import java.util.List;

public class TableAdaper extends BaseAdapter {
    List<Table> tableList;

    public TableAdaper(List<Table> tableList) {
        this.tableList = tableList;
    }

    @Override
    public int getCount() {
        return tableList.size();
    }

    @Override
    public Object getItem(int position) {
        return tableList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.table_item,parent, false);
        Table table = tableList.get(position);
        TextView id, floor, empty;
        ImageView img;
        id = view.findViewById(R.id.txt_id_table);
        floor = view.findViewById(R.id.txt_floor_table);
        empty = view.findViewById(R.id.txt_empty_table);
        img = view.findViewById(R.id.img_item_table);
        id.setText(view.getResources().getString( R.string.table)+" "+table.getId_table());
        floor.setText(view.getResources().getString( R.string.floor1)+" "+table.getFloor());

        if(table.getEmpty()==0)
        {
           empty.setText(view.getResources().getString( R.string.is_empty1));
           empty.setTextColor(view.getResources().getColor(R.color.green));
            img.setImageResource(R.drawable.table_0);
        }
        else
        {
            empty.setText(view.getResources().getString( R.string.is_not_empty));
            empty.setTextColor(view.getResources().getColor(R.color.red));
            img.setImageResource(R.drawable.table_1);
        }
        return view;
    }
}
