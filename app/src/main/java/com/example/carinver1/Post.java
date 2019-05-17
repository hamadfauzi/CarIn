package com.example.carinver1;

public class Post {

    public String judul,deskripsi,postimage,harga;

    public Post()
    {

    }
    public Post(String judul,String deskripsi,String postimage,String harga)
    {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.postimage = postimage;
        this.harga = harga;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public String getPostimage() {
        return postimage;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setPhoto(String postimage) {
        this.postimage = postimage;
    }

}
