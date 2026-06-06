package com.example.gestorproductos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "gestorproductos.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_PRODUCT = "product";

    // Columnas
    private static final String COL_ID = "id";
    private static final String COL_NOMBRE = "p_nombre";
    private static final String COL_PRECIO = "p_precio";
    private static final String COL_IMAGEN = "p_imagen";
    private static final String COL_TIENDA = "tienda_id"; // 1 = XFit, 2 = SportHouse

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PRODUCT + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NOMBRE + " TEXT, " +
                COL_PRECIO + " REAL, " +
                COL_IMAGEN + " TEXT, " +
                COL_TIENDA + " INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCT);
        onCreate(db);
    }

    // CREATE
    public boolean insertProducto(String nombre, double precio, String imagen, int tiendaId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, nombre);
        values.put(COL_PRECIO, precio);
        values.put(COL_IMAGEN, imagen);
        values.put(COL_TIENDA, tiendaId);
        long result = db.insert(TABLE_PRODUCT, null, values);
        db.close();
        return result != -1;
    }

    // READ - Obtener productos por tienda
    public List<Product> getProductosByTienda(int tiendaId) {
        List<Product> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PRODUCT, null,
                COL_TIENDA + "=?",
                new String[]{String.valueOf(tiendaId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Product p = new Product(
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_NOMBRE)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_PRECIO)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_IMAGEN)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_TIENDA))
                );
                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    // UPDATE
    public boolean updateProducto(int id, String nombre, double precio, String imagen) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_NOMBRE, nombre);
        values.put(COL_PRECIO, precio);
        values.put(COL_IMAGEN, imagen);
        int result = db.update(TABLE_PRODUCT, values, COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }

    // DELETE
    public boolean deleteProducto(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PRODUCT, COL_ID + "=?",
                new String[]{String.valueOf(id)});
        db.close();
        return result > 0;
    }
}
