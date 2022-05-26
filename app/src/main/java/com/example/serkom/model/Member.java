package com.example.serkom.model;

public class Member {
    private String name, lokasi, alamat, nomor, gender, image;

    public Member(String nama, String alamat, String nomor, String image) {
        this.name = nama;
        this.alamat = alamat;
        this.nomor = nomor;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }
}
