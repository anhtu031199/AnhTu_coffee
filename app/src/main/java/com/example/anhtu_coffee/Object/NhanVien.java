package com.example.anhtu_coffee.Object;

public class NhanVien {
    String hoTen, sdt, user, pass;
    int per;

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public NhanVien(String hoTen, String sdt, String user, String pass, int per) {
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.user = user;
        this.pass = pass;
        this.per = per;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


}
