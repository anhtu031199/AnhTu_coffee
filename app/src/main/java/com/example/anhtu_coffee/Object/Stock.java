package com.example.anhtu_coffee.Object;

public class Stock {
    String nguyenLieu, ncc, donvitinh;
    int slcon;

    public Stock() {
    }

    public String getNguyenLieu() {
        return nguyenLieu;
    }

    public void setNguyenLieu(String nguyenLieu) {
        this.nguyenLieu = nguyenLieu;
    }

    public String getNcc() {
        return ncc;
    }

    public void setNcc(String ncc) {
        this.ncc = ncc;
    }

    public int getSlcon() {
        return slcon;
    }

    public void setSlcon(int slcon) {
        this.slcon = slcon;
    }

    public String getDonvitinh() {
        return donvitinh;
    }

    public void setDonvitinh(String donvitinh) {
        this.donvitinh = donvitinh;
    }

    public Stock(String nguyenLieu, String ncc, String donvitinh, int slcon) {
        this.nguyenLieu = nguyenLieu;
        this.ncc = ncc;
        this.donvitinh = donvitinh;
        this.slcon = slcon;
    }
}
