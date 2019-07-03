package com.tapitlac.asistenciasdecursos;

import android.content.ContentValues;

import java.util.UUID;

/**
 * Created by el_pr on 26/06/2017.
 */

public class Alumno {
    private String id;
    private String nombre;
    private String apellido;
    private String numControl;
    private String avatarUri;
    public Alumno(String nombre, String apellido, String numControl, String avatarUri){
        this.id = UUID.randomUUID().toString();
        this.nombre = nombre;
        this.apellido = apellido;
        this.numControl = numControl;
        this.avatarUri = avatarUri;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getNumControl() {
        return numControl;
    }

    public String getAvatarUri() {
        return avatarUri;
    }
    public ContentValues toContentValues(){
        ContentValues values = new ContentValues();
        values.put(AlumnosContract.AlumnosEntry.ID, id);
        values.put(AlumnosContract.AlumnosEntry.NOMBRE, nombre);
        values.put(AlumnosContract.AlumnosEntry.APELLIDO,apellido);
        values.put(AlumnosContract.AlumnosEntry.NCONTROL,numControl);
        values.put(AlumnosContract.AlumnosEntry.AVATAR_URI,avatarUri);
        return values;
    }
}
