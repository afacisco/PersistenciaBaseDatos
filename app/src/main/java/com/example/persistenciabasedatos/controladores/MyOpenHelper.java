package com.example.persistenciabasedatos.controladores;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
Autor: Juan Francisco Sánchez González
Fecha: 22/01/2024
Clase: Controlador para la gestión de la BD SQLite
*/

public class MyOpenHelper extends SQLiteOpenHelper {
    private String sqlCreate = "CREATE TABLE comentarios(id INTEGER PRIMARY KEY AUTOINCREMENT, usuario TEXT, comentario TEXT)";
    private static final String DB_NOMBRE = "comentarios.sqlite";
    private static final int DB_VERSION = 1;
    public MyOpenHelper(Context context) {
        super(context, DB_NOMBRE, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(sqlCreate);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

