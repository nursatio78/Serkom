package com.example.serkom.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.serkom.callback.ActionListener;
import com.example.serkom.model.Member;
import com.example.serkom.model.ModelClass;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "Pendaftaran.db";
    private static final int DATABASE_VERSION = 1;
    private ActionListener actionListener;
    private DatabaseReference mDatabase;

    private static final String TABLE_NAME = "pendaftaran";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_ALAMAT = "alamat";
    private static final String COLUMN_NOMOR = "nomor";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_LOKASI = "lokasi_terkini";
    private static final String COLUMN_PHOTO = "url_photo";

    private ByteArrayOutputStream objectByteArrayOutputStream;

    public MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAMA + " TEXT, " +
                COLUMN_ALAMAT + " TEXT, " +
                COLUMN_NOMOR + " INTEGER, " +
                COLUMN_GENDER + " TEXT, " +
                COLUMN_LOKASI + " TEXT, " +
                COLUMN_PHOTO + " BLOB) ";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addPendaftaran(String nama, String alamat, String nomor, String lokasi_terkini, String gender, byte[] img) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues cv = new android.content.ContentValues();

        cv.put(COLUMN_NAMA, nama);
        cv.put(COLUMN_ALAMAT, alamat);
        cv.put(COLUMN_NOMOR, nomor);
        cv.put(COLUMN_GENDER, gender);
        cv.put(COLUMN_LOKASI, lokasi_terkini);
        cv.put(COLUMN_PHOTO, img);
        long result = db.insertWithOnConflict(TABLE_NAME, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        if (result == -1) {
            Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
        db.close();
        return result != -1;
    }

//    public JSONArray getArray() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        JSONArray jsonArray = new JSONArray();
//        String query = "SELECT * FROM " + TABLE_NAME;
//        Cursor cursor = db.rawQuery(query, null);
//        if (cursor.moveToFirst()) {
//            do {
//                JSONObject jsonObject = new JSONObject();
//                try {
//                    jsonObject.put("id", cursor.getString(0));
//                    jsonObject.put("nama", cursor.getString(1));
//                    jsonObject.put("alamat", cursor.getString(2));
//                    jsonObject.put("nomor", cursor.getString(3));
//                    jsonObject.put("gender", cursor.getString(4));
//                    jsonObject.put("url_photo", cursor.getBlob(4));
//
//                    jsonArray.put(jsonObject);
//                }catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        db.close();
//        return jsonArray;
//    }

    public List<ModelClass> readAllData() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            List<ModelClass> modelClassList = new ArrayList<>();
            Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            if (cursor.getCount()!=0){
                while (cursor.moveToNext()) {
                    String nama = cursor.getString(1);
                    String alamat = cursor.getString(5);
                    String nomor = cursor.getString(4);
                    String gender = cursor.getString(2);
                    String lokasi_terkini = cursor.getString(3);
                    byte[] url_photo = cursor.getBlob(6);

                    Bitmap bitmap = BitmapFactory.decodeByteArray(url_photo, 0, url_photo.length);
                    modelClassList.add(new ModelClass(nama, alamat, gender, lokasi_terkini, nomor, bitmap));
                }
                return modelClassList;
            } else {

            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateData(String row_id, String nama, String alamat, String nomor){
        SQLiteDatabase db = this.getWritableDatabase();
        String sQuery = "UPDATE " + TABLE_NAME + " SET " + COLUMN_NAMA + " = '" + nama + "', " + COLUMN_ALAMAT + " = '" + alamat + "', " + COLUMN_NOMOR + " = '" + nomor + "' WHERE " + COLUMN_ID + " = '" + row_id + "'";
        db.execSQL(sQuery);
        db.close();
    }

    public void deleteData(String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String sQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = '" + id + "'";
        db.execSQL(sQuery);
        db.close();
    }

    public void truncateData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String sQuery1 = "DELETE FROM " + TABLE_NAME;
        String sQuery2 = "DELETE FROM sqlite_squence where nama='" + TABLE_NAME + "'";
        db.execSQL(sQuery1);
        db.execSQL(sQuery2);
        db.close();
    }
}

