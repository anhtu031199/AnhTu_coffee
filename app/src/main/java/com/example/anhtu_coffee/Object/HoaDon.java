package com.example.anhtu_coffee.Object;

public class HoaDon {
    int thanhtien, thanhtoan, ngay;
    String ban, ghichu;

    public String getBan() {
        return ban;
    }

    public void setBan(String ban) {
        this.ban = ban;
    }

    public int getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(int thanhtien) {
        this.thanhtien = thanhtien;
    }

    public int getThanhtoan() {
        return thanhtoan;
    }

    public void setThanhtoan(int thanhtoan) {
        this.thanhtoan = thanhtoan;
    }

    public int getNgay() {
        return ngay;
    }

    public void setNgay(int ngay) {
        this.ngay = ngay;
    }

    public String getGhichu() {
        return ghichu;
    }

    public void setGhichu(String ghichu) {
        this.ghichu = ghichu;
    }

    public HoaDon(String ban, int thanhtien, int thanhtoan, int ngay, String ghichu) {
        this.ban = ban;
        this.thanhtien = thanhtien;
        this.thanhtoan = thanhtoan;
        this.ngay = ngay;
        this.ghichu = ghichu;
    }
}
