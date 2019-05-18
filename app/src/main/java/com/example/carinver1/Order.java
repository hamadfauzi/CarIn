package com.example.carinver1;

public class Order {

    private String alamatorder,date,deskripsiorder,gambarorder,
            judulorder,notelephoneorder,pemilikorder,tglkembali,tglpinjam,time,hargaorder;

    public Order()
    {

    }
    public Order(String alamatorder,String date,String deskripsiorder,String gambarorder,String judulorder,String notelephoneorder
        ,String pemilikorder,String tglkembali,String tglpinjam,String time,String hargaorder)
    {
        this.alamatorder = alamatorder;
        this.date = date;
        this.deskripsiorder = deskripsiorder;
        this.gambarorder = gambarorder;
        this.judulorder = judulorder;
        this.notelephoneorder = notelephoneorder;
        this.pemilikorder = pemilikorder;
        this.tglkembali = tglkembali;
        this.tglpinjam = tglpinjam;
        this.time = time;
        this.hargaorder = hargaorder;
    }

    public String getHargaorder() {
        return hargaorder;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getAlamatorder() {
        return alamatorder;
    }

    public String getDeskripsiorder() {
        return deskripsiorder;
    }

    public String getGambarorder() {
        return gambarorder;
    }

    public String getJudulorder() {
        return judulorder;
    }

    public String getNotelephoneorder() {
        return notelephoneorder;
    }

    public String getPemilikorder() {
        return pemilikorder;
    }

    public String getTglkembali() {
        return tglkembali;
    }

    public String getTglpinjam() {
        return tglpinjam;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAlamatorder(String alamatorder) {
        this.alamatorder = alamatorder;
    }

    public void setDeskripsiorder(String deskripsiorder) {
        this.deskripsiorder = deskripsiorder;
    }

    public void setGambarorder(String gambarorder) {
        this.gambarorder = gambarorder;
    }

    public void setJudulorder(String judulorder) {
        this.judulorder = judulorder;
    }

    public void setNotelephoneorder(String notelephoneorder) {
        this.notelephoneorder = notelephoneorder;
    }

    public void setPemilikorder(String pemilikorder) {
        this.pemilikorder = pemilikorder;
    }

    public void setTglkembali(String tglkembali) {
        this.tglkembali = tglkembali;
    }

    public void setTglpinjam(String tglpinjam) {
        this.tglpinjam = tglpinjam;
    }
}
