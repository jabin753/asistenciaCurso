package com.tapitlac.asistenciasdecursos;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class asistencia_agregar extends AppCompatActivity {
    int idAlumno;
    TextView editfecha;
    CheckBox esAsistencia,esRetardo;
    Button mostrarMenuFecha;
    private int dia,mes,ano;
    Spinner alumnosChooser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.asistencia_agregar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        idAlumno=0;
        esAsistencia = (CheckBox)findViewById(R.id.checkBoxAsistencia);
        esRetardo = (CheckBox)findViewById(R.id.checkBoxRetardo);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarAsistencia();
            }
        });
        editfecha = (TextView)findViewById(R.id.ResultFecha);
        alumnosChooser = (Spinner)findViewById(R.id.spinner);
        mostrarMenuFecha = (Button)findViewById(R.id.menuFecha);
        mostrarMenuFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                final Calendar c = Calendar.getInstance();
                dia = c.get(Calendar.DAY_OF_MONTH);
                mes = c.get(Calendar.MONTH);
                ano = c.get(Calendar.YEAR);
                DatePickerDialog datePickerDialog = new DatePickerDialog(view.getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    editfecha.setText(i + "/" + (i1 + 1) + "/" + i2);

                    }
                },ano,mes,dia);

                    datePickerDialog.show();
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }

        });
        try {
            ArrayAdapter<String> adapterAlumnos = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, cargarDatos());

        adapterAlumnos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alumnosChooser.setAdapter(adapterAlumnos);
        }catch(Exception e){Toast.makeText(getApplicationContext(),"No hay alumnos registrados",Toast.LENGTH_SHORT).show();}
        alumnosChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idAlumno = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    protected String[] cargarDatos(){

        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        if(db!=null) {
            Cursor c = db.rawQuery("select * from Personas", null);
            int i = 0;
            int cantidad = c.getCount();
            String[] arreglo = new String[cantidad];
            arreglo[0]="Seleccionar alumno";
            if (c.moveToFirst()) {
                do {
                    String linea = c.getString(1) + "      " + c.getString(2) + "      "+ c.getInt(3);
                    arreglo[i] = linea;
                    i++;

                } while (c.moveToNext());
            }
            c.close();
            return arreglo;
        }
        return null;
    }
    protected void guardarAsistencia(){
        try {
            BaseHelper baseHelper = new BaseHelper(this, "DEMODB", null, 1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();
            String Fecha = editfecha.getText().toString();
            int Alumno = idAlumno;
            int valorAsistencia = 0, valorRetardo = 0;
            if (esAsistencia.isChecked()) valorAsistencia = 1;
            if (esRetardo.isChecked()) valorRetardo = 1;
            if (db != null) {
                ContentValues registronuevo = new ContentValues();
                registronuevo.put("Fecha", Fecha);
                registronuevo.put("IdAlumno", Alumno);
                registronuevo.put("esAsistencia", valorAsistencia);
                registronuevo.put("esRetardo", valorRetardo);
                long i = db.insert("Asistencia", null, registronuevo);
                if (i > 0) {
                    Toast.makeText(getApplicationContext(), "Guardado con exito", Toast.LENGTH_LONG)
                            .show();
                    esAsistencia.setChecked(true);
                    esRetardo.setChecked(false);
                    esRetardo.setEnabled(true);
                    alumnosChooser.setSelection(alumnosChooser.getFirstVisiblePosition());
                }

            }
        }catch(Exception e){Toast.makeText(this,e.getMessage()+e.getCause().toString(),Toast.LENGTH_SHORT).show();}
    }
    public void CheckBoxBeaviour(View view){
        if(esAsistencia.isChecked())esRetardo.setEnabled(true);
        else{esRetardo.setEnabled(false);esRetardo.setChecked(false);}
    }


}
