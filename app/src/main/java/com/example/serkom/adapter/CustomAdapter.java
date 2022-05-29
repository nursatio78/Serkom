package com.example.serkom.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.serkom.R;
import com.example.serkom.UpdateActivity;
import com.example.serkom.database.MyDatabaseHelper;
import com.example.serkom.model.Member;
import com.example.serkom.model.ModelClass;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    Context context;
    Activity activity;
    List<ModelClass> objectModelClass;

    public CustomAdapter(Activity activity, Context context, List<ModelClass> objectModelClass) {
        this.activity = activity;
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

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("nama", modelClass.getNama());
                intent.putExtra("alamat", modelClass.getAlamat());
                intent.putExtra("nomor", modelClass.getNomor());
                activity.startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objectModelClass.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView tvNama, tvAlamat, tvGender, tvLokasi, tvNomor;
        LinearLayout mainLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainLayout = itemView.findViewById(R.id.mainLayout);
            imageView= itemView.findViewById(R.id.imageProfile);
            tvNama= itemView.findViewById(R.id.tvNama);
            tvAlamat= itemView.findViewById(R.id.tvAlamatProfile);
            tvGender= itemView.findViewById(R.id.tvGender);
            tvLokasi= itemView.findViewById(R.id.tvLokasiPendaftaran);
            tvNomor= itemView.findViewById(R.id.tvNomorProfile);
        }
    }
}
