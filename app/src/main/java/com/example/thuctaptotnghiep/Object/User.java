package com.example.thuctaptotnghiep.Object;

import java.io.Serializable;

public class User implements Serializable {
    public int id;
    public String name;
    public String email;
    public String password;
    public String tendangnhap;
    public String sdt;

public  User(){}
    public User(int id, String name, String email, String password, String tendangnhap, String sdt) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.tendangnhap = tendangnhap;
        this.sdt = sdt;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTendangnhap() {
        return tendangnhap;
    }

    public void setTendangnhap(String tendangnhap) {
        this.tendangnhap = tendangnhap;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }
}
