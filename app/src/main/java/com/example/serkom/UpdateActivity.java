package com.example.serkom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.databinding.ActivityUpdateBinding;
import com.google.android.material.snackbar.Snackbar;

public class UpdateActivity extends AppCompatActivity {
    ActivityUpdateBinding binding;
    String id, nama, alamat, nomor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_update);
        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);

        getIntentData();
        binding.btUpdateForm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myDB.updateData(id, nama, alamat, nomor);
            }
        });
    }

    void getIntentData(){
        if (getIntent().hasExtra("id") && getIntent().hasExtra("nama") && getIntent().hasExtra("alamat") && getIntent().hasExtra("nomor")){
            //Getting Data from Intent
            id = getIntent().getStringExtra("id");
            nama = getIntent().getStringExtra("nama");
            alamat = getIntent().getStringExtra("alamat");
            nomor = getIntent().getStringExtra("nomor");

            //Setting Intent Data
            binding.etNamaUpdate.setText(nama);
            binding.etAlamatUpdate.setText(alamat);
            binding.etNomorUpdate.setText(nomor);
        } else {
            Snackbar.make(binding.getRoot(), "Data tidak ditemukan", Snackbar.LENGTH_SHORT).show();
        }
    }
}