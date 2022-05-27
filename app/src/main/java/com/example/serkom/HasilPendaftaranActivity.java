package com.example.serkom;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.serkom.adapter.HasilAdapter;
import com.example.serkom.databinding.ActivityHasilPendaftaranBinding;
import com.example.serkom.model.Member;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class HasilPendaftaranActivity extends AppCompatActivity {
    private ActivityHasilPendaftaranBinding binding;
    private FirebaseDatabase mDatabase;
    private DatabaseReference myRef;
    private RecyclerView recyclerView;
    private List<Member> hasilPendaftaranList;
    private HasilAdapter hasilAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_hasil_pendaftaran);

        mDatabase = FirebaseDatabase.getInstance();
        myRef = mDatabase.getReference("Users");
        recyclerView = binding.recyclerView;
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        hasilPendaftaranList = new ArrayList<Member>();
        hasilAdapter=new HasilAdapter(HasilPendaftaranActivity.this, hasilPendaftaranList);

        getAllData();
    }

    private void getAllData() {
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Member member=snapshot.getValue(Member.class);
                hasilPendaftaranList.add(member);
                hasilAdapter.notifyDataSetChanged();
                recyclerView.setAdapter(hasilAdapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}