package com.tapitlac.asistenciasdecursos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by el_pr on 26/06/2017.
 */

public class AlumnosDBHelper extends SQLiteOpenHelper {
    public static final int BD_VERSION = 1;
    public static final String BD_NOMBRE = "Alumnos.db";
    public AlumnosDBHelper(Context context){
        super(context, BD_NOMBRE, null, BD_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // Comandos SQL
        sqLiteDatabase.execSQL("CREATE TABLE "+ AlumnosContract.AlumnosEntry.NOMBRE_TABLA + "("
        + AlumnosContract.AlumnosEntry._ID              + " INTEGER PRIMARY KEY AUTOINCREMENT,"
        + AlumnosContract.AlumnosEntry.ID               + " TEXT NOT NULL,"
        + AlumnosContract.AlumnosEntry.NOMBRE           + " TEXT NOT NULL,"
        + AlumnosContract.AlumnosEntry.APELLIDO         + " TEXT NOT NULL,"
        + AlumnosContract.AlumnosEntry.NCONTROL         + " TEXT NOT NULL,"
        + AlumnosContract.AlumnosEntry.AVATAR_URI       + " TEXT,"
        + "UNIQUE ("+ AlumnosContract.AlumnosEntry.ID   +"))");
        //Datos de Prueba
        saveAlumno(new Alumno("Joseph Jabin", "Cuevas", "15560509", "15560509.jpg"));
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No hay operaciones
    }
    public long saveAlumno(Alumno alumno){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();

        return sqLiteDatabase.insert(
                AlumnosContract.AlumnosEntry.NOMBRE_TABLA,
                null,
                alumno.toContentValues());
    }
    public Cursor getAllAlumnos(){
        return getReadableDatabase()
                .query(
                        AlumnosContract.AlumnosEntry.NOMBRE_TABLA,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);
    }
    public Cursor getAlumnoById(String alumnoId){
        Cursor c = getReadableDatabase().query(
                AlumnosContract.AlumnosEntry.NOMBRE_TABLA,
                null,
                AlumnosContract.AlumnosEntry.ID + "LIKE ?",
                new String[]{alumnoId},
                null,
                null,
                null);
        return c;
    }

}
