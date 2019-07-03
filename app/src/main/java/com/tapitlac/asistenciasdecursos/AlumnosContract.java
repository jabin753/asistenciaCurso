package com.tapitlac.asistenciasdecursos;

import android.provider.BaseColumns;

/**
 * Created by el_pr on 26/06/2017.
 */

public class AlumnosContract {
    public  static abstract class  AlumnosEntry implements BaseColumns{
        public static final String NOMBRE_TABLA = "ALUMNO";

        public static final String ID = "id";
        public static final String NOMBRE = "nombre";
        public static final String APELLIDO = "apellido";
        public static final String NCONTROL = "numControl";
        public static final String AVATAR_URI = "avatarUri";
    }
}
