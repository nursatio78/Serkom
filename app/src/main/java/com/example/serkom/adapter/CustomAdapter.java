package com.example.serkom.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serkom.R;
import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.model.Member;
import com.example.serkom.model.ModelClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    List<ModelClass> objectModelClass;

    public CustomAdapter(Context context, List<ModelClass> objectModelClass) {
        this.context = context;
        this.objectModelClass = objectModelClass;
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new CustomAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        ModelClass modelClass = objectModelClass.get(position);
        holder.tvNama.setText(modelClass.getNama());
        holder.tvAlamat.setText(modelClass.getAlamat());
        holder.tvGender.setText(modelClass.getGender());
        holder.tvLokasi.setText(modelClass.getLokasi_terkini());
        holder.tvNomor.setText(modelClass.getNomor());

        holder.imageView.setImageBitmap(modelClass.getImage());
    }

    @Override
    public int getItemCount() {
        return objectModelClass.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNama, tvAlamat, tvGender, tvLokasi, tvNomor;
        Button btHapus, btUpdate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView= itemView.findViewById(R.id.imageProfile);
            tvNama= itemView.findViewById(R.id.tvNama);
            tvAlamat= itemView.findViewById(R.id.tvAlamatProfile);
            tvGender= itemView.findViewById(R.id.tvGender);
            tvLokasi= itemView.findViewById(R.id.tvLokasiPendaftaran);
            tvNomor= itemView.findViewById(R.id.tvNomorProfile);

            btHapus= itemView.findViewById(R.id.btHapus);
            btUpdate= itemView.findViewById(R.id.btUpdate);
        }
    }
}
