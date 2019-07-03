package com.tapitlac.asistenciasdecursos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.Nullable;

import java.util.ArrayList;

/**
 * Created by Mario on 22/06/2017.
 */

public class BaseHelper extends SQLiteOpenHelper {

    String tabla="CREATE TABLE Personas(Id INTEGER PRIMARY KEY AUTOINCREMENT, Nombre Text, Apellido Text, N°Control INTEGER)";
    String tablaAsistencia="CREATE TABLE Asistencia(Id INTEGER PRIMARY KEY AUTOINCREMENT,Fecha TEXT NOT NULL,IdAlumno INTEGER NOT NULL REFERENCES Personas(Id),esAsistencia INTEGER, esRetardo INTEGER)";

    public BaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(tabla);
        db.execSQL(tablaAsistencia);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                db.setForeignKeyConstraintsEnabled(true);
            } else {
                db.execSQL("PRAGMA foreign_keys=ON");
            }
        }
    }
    //Metodos auxiliares para consultas
    public ArrayList<String[]> getListFromTable(Context context, String dataBaseName, SQLiteDatabase.CursorFactory factory, int version, String tableName){
    ArrayList<String[]>data = new ArrayList<String[]>();
        BaseHelper baseHelper=new BaseHelper(context,dataBaseName,factory,version);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        Cursor c = db.rawQuery(String.format("select * from %s",tableName),null);
        String[] toInsert = new String[c.getColumnCount()];
        if(c.getCount()>0){

            do{


            }while(c.moveToNext());

        }

        return data;
    }
}
