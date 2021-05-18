package com.example.anhtu_coffee.Object;

public class Table {
    int id_table, empty, floor;

    public int getId_table() {
        return id_table;
    }

    public void setId_table(int id_table) {
        this.id_table = id_table;
    }

    public int getEmpty() {
        return empty;
    }

    public void setEmpty(int empty) {
        this.empty = empty;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public Table(int id_table, int floor, int empty) {
        this.id_table = id_table;
        this.empty = empty;
        this.floor = floor;
    }
}
