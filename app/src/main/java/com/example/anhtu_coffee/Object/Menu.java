package com.example.anhtu_coffee.Object;

public class Menu {
    String tenHang, thanhPhan, linkAnh;
    int gia;
    float khuyenMai;

    public String getTenHang() {
        return tenHang;
    }

    public void setTenHang(String tenHang) {
        this.tenHang = tenHang;
    }

    public String getThanhPhan() {
        return thanhPhan;
    }

    public void setThanhPhan(String thanhPhan) {
        this.thanhPhan = thanhPhan;
    }



    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public float getKhuyenMai() {
        return khuyenMai;
    }

    public void setKhuyenMai(float khuyenMai) {
        this.khuyenMai = khuyenMai;
    }

    public String getLinkAnh() {
        return linkAnh;
    }

    public void setLinkAnh(String linkAnh) {
        this.linkAnh = linkAnh;
    }

    public Menu(String tenHang, String thanhPhan, String linkAnh, int gia, float khuyenMai) {
        this.tenHang = tenHang;
        this.thanhPhan = thanhPhan;
        this.linkAnh = linkAnh;
        this.gia = gia;
        this.khuyenMai = khuyenMai;
    }
}
