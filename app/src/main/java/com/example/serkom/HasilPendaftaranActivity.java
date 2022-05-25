package com.example.serkom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.example.serkom.databinding.ActivityHasilPendaftaranBinding;

public class HasilPendaftaranActivity extends AppCompatActivity {
    private ActivityHasilPendaftaranBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hasil_pendaftaran);

    }
}