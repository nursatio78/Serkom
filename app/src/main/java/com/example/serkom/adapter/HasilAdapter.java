package com.example.serkom.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serkom.R;
import com.example.serkom.model.Member;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class HasilAdapter extends RecyclerView.Adapter<HasilAdapter.Viewholder> {

    Context context;
    List<Member> hasilModelsList;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference("Users");

    public HasilAdapter(Context context, List<Member> hasilModelsList) {
        this.context = context;
        this.hasilModelsList = hasilModelsList;
    }

    @NonNull
    @Override
    public HasilAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_f, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HasilAdapter.Viewholder holder, int position) {

        Member hasilModels = hasilModelsList.get(position);
        holder.tvNama.setText(hasilModels.getNama());
        holder.tvAlamat.setText(hasilModels.getAlamat());
        holder.tvGender.setText(hasilModels.getGender());
        holder.tvLokasi.setText(hasilModels.getLokasi_terkini());
        holder.tvNomor.setText(hasilModels.getNomor());

        String imageUri=null;
        imageUri=hasilModels.getImage();
        Picasso.get().load(imageUri).into(holder.imageView);

        holder.btHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Hapus Data");
                builder.setMessage("Apakah anda yakin ingin menghapus data ini?");
                builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.child(hasilModels.getNama()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Snackbar.make(view, "Data berhasil dihapus", Snackbar.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(view, "Data gagal dihapus", Snackbar.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return hasilModelsList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView tvNama, tvAlamat, tvGender, tvLokasi, tvNomor;
        Button btHapus, btUpdate;
        public Viewholder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.imageProfile);
            tvNama= itemView.findViewById(R.id.tvNama);
            tvAlamat= itemView.findViewById(R.id.tvAlamatProfile);
            tvGender= itemView.findViewById(R.id.tvGender);
            tvLokasi= itemView.findViewById(R.id.tvLokasiPendaftaran);
            tvNomor= itemView.findViewById(R.id.tvNomorProfile);

            btHapus= itemView.findViewById(R.id.btDelete);
            btUpdate= itemView.findViewById(R.id.btUpdate);
        }
    }
}
