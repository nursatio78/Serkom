package com.example.serkom.model;

import android.graphics.Bitmap;

public class ModelClass {
    private String nama, lokasi_terkini, alamat, nomor, gender;
    private Bitmap image;

    public ModelClass(String nama, String lokasi_terkini, String alamat, String nomor, String gender, Bitmap image) {
        this.nama = nama;
        this.lokasi_terkini = lokasi_terkini;
        this.alamat = alamat;
        this.nomor = nomor;
        this.gender = gender;
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
