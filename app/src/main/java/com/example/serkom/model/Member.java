package com.example.serkom.model;

public class Member {
    private String nama, lokasi_terkini, alamat, nomor, gender, image;

    public Member() {
    }

    public Member(String nama, String alamat, String nomor, String lokasi_terkini, String image) {
        this.nama = nama;
        this.alamat = alamat;
        this.lokasi_terkini = lokasi_terkini;
        this.nomor = nomor;
        this.image = image;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getLokasi_terkini() {
        return lokasi_terkini;
    }

    public void setLokasi_terkini(String lokasi_terkini) {
        this.lokasi_terkini = lokasi_terkini;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNomor() {
        return nomor;
    }

    public void setNomor(String nomor) {
        this.nomor = nomor;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
