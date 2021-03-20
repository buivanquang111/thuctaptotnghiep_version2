package com.example.thuctaptotnghiep.Object;

import java.io.Serializable;

public class ThuongHieu implements Serializable {
    public int id;
    public String name;
    public String img;

    public ThuongHieu(int id, String name, String img) {
        this.id = id;
        this.name = name;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
