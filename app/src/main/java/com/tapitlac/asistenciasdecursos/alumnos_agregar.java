package com.tapitlac.asistenciasdecursos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class alumnos_agregar extends AppCompatActivity {
    EditText ET_Nombre,ET_Apellido ,ET_Con,sim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_agregar);

        ET_Nombre=(EditText) findViewById(R.id.ET_Nombre);
        ET_Apellido=(EditText) findViewById(R.id.ET_Apellido);
        ET_Con=(EditText) findViewById(R.id.ET_Con);
    }
    public void GuardarDatos(View view){
        String nombre=ET_Nombre.getText().toString();
        String apellido=ET_Apellido.getText().toString();
        int control= Integer.parseInt(ET_Con.getText().toString());

        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getWritableDatabase();
        if(db!=null){
            ContentValues registronuevo=new ContentValues();
            registronuevo.put("Nombre ",nombre);
            registronuevo.put("Apellido  ",apellido);
            registronuevo.put("N°Control ",control);
            long i= db.insert("Personas  ",null,registronuevo);
            if(i>0){
                Toast.makeText(this,"Persona Agregada Exitosamente",Toast.LENGTH_SHORT).show();
                ET_Nombre.setText("");
                ET_Apellido.setText("");
                ET_Con.setText("");


            }

        }

    }

}
