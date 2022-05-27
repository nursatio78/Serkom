package com.example.serkom;

import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.example.serkom.callback.ActionListener;
import com.example.serkom.database.MyDatabaseHelper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainViewModel extends ViewModel {
    private StorageReference storageReference;
    private DatabaseReference myRef;
    private ActionListener actionListener;

    public MainViewModel(ActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void pushData(String nama, String alamat, String nomor, String lokasi_terkini, String gender, Uri imageUri){
        myRef = FirebaseDatabase.getInstance().getReference().child("Users").child(nama);
        storageReference = FirebaseStorage.getInstance().getReference().child("imagePost").child(imageUri.getLastPathSegment());
        storageReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        String t=task.getResult().toString();

                        Map<String, Object> profileHashMap = new HashMap<>();
                        profileHashMap.put("nama", nama);
                        profileHashMap.put("alamat", alamat);
                        profileHashMap.put("nomor", nomor);
                        profileHashMap.put("lokasi_terkini", lokasi_terkini);
                        profileHashMap.put("gender", gender);
                        profileHashMap.put("image", t);

                        myRef.updateChildren(profileHashMap)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            actionListener.onSuccess("Push data berhasil");
                                        } else {
                                            actionListener.onError("Push data gagal");
                                        }
                                    }
                                });
                    }
                });
            }
        });
    }

    public void setGender(String gender) {
    }
}
