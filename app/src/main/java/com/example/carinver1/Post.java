package com.example.carinver1;

public class Post {

    private String judul,deskripsi,photo;

    public Post()
    {

    }
    public Post(String judul,String deskripsi,String photo)
    {
        this.judul = judul;
        this.deskripsi = deskripsi;
        this.photo = photo;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public String getJudul() {
        return judul;
    }

    public String getPhoto() {
        return photo;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

}
