package com.example.thuctaptotnghiep.Object;

import java.io.Serializable;

public class GioHang implements Serializable {
    public int id;
    public String idproduct;
    public String tendangnhap;
    public String title;
    public double price;
    public String image;
    public int soluong_mua;

    public GioHang(int id,String idproduct, String tendangnhap, String title, double price, String image, int soluong_mua) {
        this.id = id;
        this.idproduct=idproduct;
        this.tendangnhap = tendangnhap;
        this.title = title;
        this.price = price;
        this.image = image;
        this.soluong_mua = soluong_mua;
    }

    public String getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(String idproduct) {
        this.idproduct = idproduct;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTendangnhap() {
        return tendangnhap;
    }

    public void setTendangnhap(String tendangnhap) {
        this.tendangnhap = tendangnhap;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getSoluong_mua() {
        return soluong_mua;
    }

    public void setSoluong_mua(int soluong_mua) {
        this.soluong_mua = soluong_mua;
    }
}
