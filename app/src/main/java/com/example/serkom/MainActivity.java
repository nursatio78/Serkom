package com.example.serkom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.serkom.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        binding.btnDaftar.setOnClickListener(this::onViewClicked);
    }
    public boolean validasidata() {
        return !TextUtils.isEmpty(binding.etNamaDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etAlamatDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etNomorDaftar.getText().toString());
    }

    private void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_hasil:
                startActivity(new Intent(this, HasilPendaftaranActivity.class));
                break;

            case R.id.btn_daftar:
                if (validasidata()){

                }else {
                    Snackbar.make(binding.getRoot(), "Isian harus diisi!", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(R.id.btn_daftar).show();
                }
            case R.id.btn_pilih_foto:
                pilihPhoto();
                break;
        }
    }

    private void pilihPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            binding.ivFoto.setImageURI(data.getData());
        }
    }
}