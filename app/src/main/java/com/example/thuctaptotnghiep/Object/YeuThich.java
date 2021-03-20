package com.example.thuctaptotnghiep.Object;

public class YeuThich {
    int idtym;
    int iduser;
    int idproduct;


    public YeuThich(int idtym, int iduser, int idproduct) {
        this.idtym = idtym;
        this.iduser = iduser;
        this.idproduct = idproduct;
    }

    public int getIdtym() {
        return idtym;
    }

    public void setIdtym(int idtym) {
        this.idtym = idtym;
    }

    public int getIduser() {
        return iduser;
    }

    public void setIduser(int iduser) {
        this.iduser = iduser;
    }

    public int getIdproduct() {
        return idproduct;
    }

    public void setIdproduct(int idproduct) {
        this.idproduct = idproduct;
    }
}
