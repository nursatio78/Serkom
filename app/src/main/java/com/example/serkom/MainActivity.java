package com.example.serkom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.serkom.callback.ActionListener;
import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private ActivityMainBinding binding;
    private MyDatabaseHelper myDB;
    private MainViewModel viewModel;
    public Uri imageUri;
    private FusedLocationProviderClient fusedLocationClient;

    final int PERMISSION_LOCATION_REQUEST_CODE = 1;
    final int PERMISSION_STORAGE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this, new ProfileVMFactory(actionListener)).get(MainViewModel.class);

        myDB = new MyDatabaseHelper(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);

        binding.btnDaftar.setOnClickListener(this::onViewClicked);
        binding.ivBarang.setOnClickListener(this::onViewClicked);
        binding.btnHasil.setOnClickListener(this::onViewClicked);
        binding.btLocation.setOnClickListener(this::onViewClicked);
    }

    private boolean hasLocationPermission() {
        EasyPermissions.hasPermissions(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return true;
    }

    private void requestLocationPermission() {
        EasyPermissions.requestPermissions(MainActivity.this, "Izin Lokasi",
                PERMISSION_LOCATION_REQUEST_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    public boolean validasidata() {
        return !TextUtils.isEmpty(binding.etNamaDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etAlamatDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etNomorDaftar.getText().toString());
    }

    @SuppressLint("MissingPermission")
    private void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.btn_daftar:
                if (validasidata()){
                    insertData();
                    pushData();
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

            case R.id.btLocation:
                if (hasLocationPermission()) {
                    requestLocationPermission();
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(this, location -> {
                                Geocoder geoCoder = new Geocoder(this);
                                try {
                                    List<Address> currentLocation = geoCoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                                    binding.tvLocation.setText(currentLocation.get(0).getAddressLine(0));
                                    Log.d("Location", currentLocation.get(0).getAddressLine(0));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                } else {
                    Snackbar.make(binding.getRoot(), "Izin Lokasi tidak diperbolehkan!", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(R.id.btLocation).show();
                }
                break;
        }
    }

    private void pushData() {
        String gender = "";
        if (binding.rbLakiDaftar.isChecked()) {
            gender = "Pria";
        } else {
            gender = "Wanita";
        }
        viewModel.pushData(binding.etNamaDaftar.getText().toString(),
                binding.etAlamatDaftar.getText().toString(),
                binding.etNomorDaftar.getText().toString(),
                binding.tvLocation.getText().toString(),
                gender,
                imageUri);
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
                binding.tvLocation.getText().toString(),
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

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Snackbar.make(binding.getRoot(), "Izin diterima", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(binding.etNamaDaftar).show();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Snackbar.make(binding.getRoot(), "Izin ditolak", BaseTransientBottomBar.LENGTH_LONG).setAnchorView(binding.etNamaDaftar).show();
    }
}