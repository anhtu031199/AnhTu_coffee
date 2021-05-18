package com.example.anhtu_coffee.Object;

public class Cart {
    String tenhang;
    int soluong, gia;

    public String getTenhang() {
        return tenhang;
    }

    public void setTenhang(String tenhang) {
        this.tenhang = tenhang;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public Cart(String tenhang, int soluong, int gia) {
        this.tenhang = tenhang;
        this.soluong = soluong;
        this.gia = gia;
    }
}
