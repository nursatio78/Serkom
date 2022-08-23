package com.example.serkom;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.serkom.callback.ActionListener;
import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.databinding.ActivityMainBinding;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.normal.TedPermission;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

@SuppressLint("MissingPermission")
public class MainActivity extends AppCompatActivity {
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
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        binding.btnDaftar.setOnClickListener(this::onViewClicked);
        binding.ivBarang.setOnClickListener(this::onViewClicked);
        binding.btnHasil.setOnClickListener(this::onViewClicked);
        binding.btLocation.setOnClickListener(this::onViewClicked);

        getLocations();
    }

    private void getLocations() {
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(MainActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(MainActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }
        };
        TedPermission.create()
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .check();
    }

    public boolean validasidata() {
        return !TextUtils.isEmpty(binding.etNamaDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etAlamatDaftar.getText().toString()) &&
                !TextUtils.isEmpty(binding.etNomorDaftar.getText().toString());
    }

    private void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_daftar:
                if (validasidata()) {
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
                if (ActivityCompat.checkSelfPermission(this,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                    Location();
                } else {
                    ActivityCompat.requestPermissions(this, 
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 
                            PERMISSION_LOCATION_REQUEST_CODE);
                }
                break;
        }
    }

    private void Location() {

        LocationRequest request = com.google.android.gms.location.LocationRequest.create();
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        request.setInterval(5000);
        request.setFastestInterval(2000);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(request);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(this).checkLocationSettings(builder.build());
        result.addOnCompleteListener(this, new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    // All location settings are satisfied. The client can initialize location requests here.
                    getLocations();
                    ambilLokasi();
                } catch (ApiException exception) {
                    switch (exception.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            // Location settings are not satisfied. But could be fixed by showing the user a dialog.
                            try {
                                // Cast to a resolvable exception.
                                ResolvableApiException resolvable = (ResolvableApiException) exception;
                                // Show the dialog by calling startResolutionForResult(),
                                // and check the result in onActivityResult().
                                resolvable.startResolutionForResult(MainActivity.this,
                                        PERMISSION_LOCATION_REQUEST_CODE);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            } catch (ClassCastException e) {
                                // Ignore, should be an impossible error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                            break;
                    }
                }
            }
        });
    }

    private void ambilLokasi() {
        fusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                Location location = task.getResult();
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> addresses = null;
                if (location != null){
                    try {
                        addresses  = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    String address = addresses.get(0).getAddressLine(0);
                    String postalCode = addresses.get(0).getPostalCode();
                    binding.etAlamatDaftar.setText(address + ", " + postalCode);
                    binding.tvLocation.setText(address + ", " + postalCode);
                }
            }
        });
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
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    PERMISSION_STORAGE_REQUEST_CODE);
        }
//        Intent i = new Intent();
//        i.setType("image/*");
//        i.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data!=null && data.getData()!=null) {
            imageUri = data.getData();
            binding.ivBarang.setImageURI(imageUri);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork == null || !activeNetwork.isConnectedOrConnecting()) {
            Toast.makeText(this, "Tidak ada koneksi internet!", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Ada koneksi internet!", Toast.LENGTH_LONG).show();
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