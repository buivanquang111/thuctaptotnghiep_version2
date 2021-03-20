package com.example.thuctaptotnghiep.Object;

import java.io.Serializable;

public class TinTuc implements Serializable {
    int id;
    String ten;
    String hinhanh;
    String noidung;

    public TinTuc(int id, String ten, String hinhanh, String noidung) {
        this.id = id;
        this.ten = ten;
        this.hinhanh = hinhanh;
        this.noidung = noidung;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getHinhanh() {
        return hinhanh;
    }

    public void setHinhanh(String hinhanh) {
        this.hinhanh = hinhanh;
    }

    public String getNoidung() {
        return noidung;
    }

    public void setNoidung(String noidung) {
        this.noidung = noidung;
    }
}
