package com.example.serkom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.serkom.callback.ActionListener;
import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.databinding.ActivityMainBinding;
import com.example.serkom.model.Member;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private SQLiteDatabase db;
    private MyDatabaseHelper myDB;
    private MainViewModel viewModel;
    public Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ProfileVMFactory(actionListener)).get(MainViewModel.class);

        myDB = new MyDatabaseHelper(this);

//        member = new Member(binding.etNamaDaftar.getText().toString(), binding.etAlamatDaftar.getText().toString(), binding.etNomorDaftar.getText().toString(), );
        binding.btnDaftar.setOnClickListener(this::onViewClicked);
        binding.ivBarang.setOnClickListener(this::onViewClicked);
        binding.btnHasil.setOnClickListener(this::onViewClicked);
    }

//    public void pushData() {
//        String gender = "";
//        if (binding.rbLakiDaftar.isChecked()) {
//            gender = "Pria";
//        } else {
//            gender = "Wanita";
//        }
//        viewModel.pushData("" + binding.etNamaDaftar.getText().toString(),
//                "" + binding.etAlamatDaftar.getText().toString(),
//                "" + binding.etNomorDaftar.getText().toString(),
//                "" + gender,
//                imageUri);
//    }

    public boolean validasidata() {
        return !TextUtils.isEmpty(binding.etNamaDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etAlamatDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etNomorDaftar.getText().toString());
    }

    private void onViewClicked(View view) {
        String m1 = binding.rbLakiDaftar.getText().toString();
        String m2 = binding.rbPerempuanDaftar.getText().toString();

        switch (view.getId()){
            case R.id.btn_daftar:
                if (validasidata()){
                    insertData();
                } else {
                    Snackbar.make(binding.getRoot(), "Isian harus diisi!", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(R.id.btn_daftar).show();
                }
                break;
            case R.id.ivBarang:
                pilihPhoto();
                break;

            case R.id.btn_hasil:
                startActivity(new Intent(MainActivity.this, HasilPendaftaranActivity.class));
                break;
        }
    }

    private void insertData() {
        String gender = "";
        if (binding.rbLakiDaftar.isChecked()) {
            gender = "Pria";
        } else {
            gender = "Wanita";
        }
        myDB.addPendaftaran(binding.etNamaDaftar.getText().toString(),
                binding.etAlamatDaftar.getText().toString(),
                binding.etNomorDaftar.getText().toString(),
                gender,
                ImageViewToByte(binding.ivBarang));
    }

    private byte[] ImageViewToByte(ImageView ivBarang) {
        Bitmap bitmap = ((BitmapDrawable) ivBarang.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void pilihPhoto() {
        pickFromGallery();
    }

    private void pickFromGallery() {
        Intent i = new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            binding.ivBarang.setImageURI(imageUri);
        }
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onStart() {
            Snackbar.make(binding.getRoot(), "Proses...", BaseTransientBottomBar.LENGTH_INDEFINITE).setAnchorView(binding.etNamaDaftar).show();
            binding.btnDaftar.setEnabled(false);
        }

        @Override
        public void onSuccess(String message) {
            Snackbar.make(binding.getRoot(), message, BaseTransientBottomBar.LENGTH_LONG).setAnchorView(binding.etNamaDaftar).show();
            binding.btnDaftar.setEnabled(true);
            new Handler().postDelayed(() -> {
                startActivity(new Intent(MainActivity.this, HasilPendaftaranActivity.class));
            },1000);
        }

        @Override
        public void onError(String message) {
            Snackbar.make(binding.getRoot(), message, BaseTransientBottomBar.LENGTH_LONG).setAnchorView(binding.etNamaDaftar).show();
            binding.btnDaftar.setEnabled(true);
        }
    };
}