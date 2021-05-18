package com.example.anhtu_coffee.Interface;

import com.example.anhtu_coffee.Object.Table;

public interface ITable {
    void onID(int id);
    void onFloor(int id);
    void onImg(Table table);
    void onEmpty(int id, int floor);
}
