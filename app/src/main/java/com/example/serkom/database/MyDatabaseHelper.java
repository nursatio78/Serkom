package com.example.serkom.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.serkom.callback.ActionListener;

public class MyDatabaseHelper extends android.database.sqlite.SQLiteOpenHelper {
    private final Context context;
    private static final String DATABASE_NAME = "Pendaftaran.db";
    private static final int DATABASE_VERSION = 1;
    private ActionListener actionListener;

    private static final String TABLE_NAME = "pendaftaran";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAMA = "nama";
    private static final String COLUMN_ALAMAT = "alamat";
    private static final String COLUMN_NOMOR = "nomor";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_LOKASI = "lokasi_terkini";
    private static final String COLUMN_PHOTO = "url_photo";

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

    public boolean addPendaftaran(String nama, String alamat, String nomor, String gender, byte[] img) {
        android.database.sqlite.SQLiteDatabase db = this.getWritableDatabase();
        android.content.ContentValues cv = new android.content.ContentValues();

        cv.put(COLUMN_NAMA, nama);
        cv.put(COLUMN_ALAMAT, alamat);
        cv.put(COLUMN_NOMOR, nomor);
        cv.put(COLUMN_GENDER, gender);
//        cv.put(COLUMN_LOKASI, lokasi_terkini);
        cv.put(COLUMN_PHOTO, img);
        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "FAILED", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Added Successfully!", Toast.LENGTH_SHORT).show();
        }
        return result != -1;
    }

    Cursor readAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
            cursor.moveToFirst();
        }
        return cursor;
    }
}

