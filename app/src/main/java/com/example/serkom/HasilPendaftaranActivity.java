package com.example.serkom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.serkom.adapter.CustomAdapter;
import com.example.serkom.adapter.HasilAdapter;
import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.databinding.ActivityHasilPendaftaranBinding;
import com.example.serkom.model.Member;
import com.example.serkom.model.ModelClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HasilPendaftaranActivity extends AppCompatActivity {
    private ActivityHasilPendaftaranBinding binding;
    private FirebaseDatabase mDatabase;
    private MyDatabaseHelper myDB;
    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private List<Member> hasilPendaftaranList;
    private List<ModelClass> modelClassList;
    private HasilAdapter hasilAdapter;
    private CustomAdapter customAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hasil_pendaftaran);

        myDB = new MyDatabaseHelper(this);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Users");
        recyclerView = binding.recyclerView;

        hasilPendaftaranList = new ArrayList<Member>();
        hasilAdapter=new HasilAdapter(HasilPendaftaranActivity.this, hasilPendaftaranList);

        getAllData();
        modelClassList = new ArrayList<ModelClass>();
    }

    private void getAllData() {
        customAdapter = new CustomAdapter(this, myDB.readAllData());
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(customAdapter);
//        myRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Member member=snapshot.getValue(Member.class);
//                hasilPendaftaranList.add(member);
//                hasilAdapter.notifyDataSetChanged();
//                recyclerView.setAdapter(hasilAdapter);
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
//
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });
    }
}