package com.tapitlac.asistenciasdecursos;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class alumnos_lista extends AppCompatActivity {
    String[] arreglo;
    ListView lista;
    List<String[]> data;
    protected Object mActionMode;
    public int selectedItem=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_lista);
        cargar();
        lista=(ListView) findViewById(R.id.Lista);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.accion_setting:
                exportDB();
                return true;
            case R.id.importarDatos:
                importDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void cargar  (){
        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        if(db!=null) {
            Cursor c = db.rawQuery("select * from Personas", null);
            int i = 0;
            int cantidad = c.getCount();
            arreglo = new String[cantidad];
            data = new ArrayList<String[]>();
            if (c.moveToFirst()) {
                do {
                    data.add(new String[]{c.getString(1),c.getString(2),c.getString(3)});
                    String linea = c.getInt(0) + "      " + c.getString(1) + "  " + c.getString(2) + "    "+ c.getInt(3);
                    arreglo[i] = linea;
                    i++;

                } while (c.moveToNext());

            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
            ListView lista=(ListView)findViewById(R.id.Lista);
            lista.setAdapter(adapter);
        }
    }
    private void exportDB() {
        String csv="";
        try
        {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), "ListaAlumnos.csv");
            FileWriter fw = new FileWriter(file.getAbsolutePath());
            CSVWriter escritor = new CSVWriter(fw);
            escritor.writeAll(data);

            escritor.close();
            Toast.makeText(this,"Los datos fueron grabados correctamente en"+file.getAbsolutePath(),
                    Toast.LENGTH_SHORT).show();

        }
        catch(Exception sqlEx)
        {
            Toast.makeText(this,sqlEx.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void importDB(){
        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), "ListaAlumnos.csv");
        try {
            FileReader fr = new FileReader(file.getAbsolutePath());
            CSVReader lector = new CSVReader(fr,',');
            data = lector.readAll();
            lector.close();

            BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            int i;
            for(i=0;i<data.size();i++){
                String registro[] = data.get(i);
                values.put("Nombre",registro[0]);
                values.put("Apellido",registro[1]);
                values.put("N°Control",registro[2]);
                long result = db.insert("Personas",null,values);
            }
            cargar();
            if(i>0){Toast.makeText(this,lector.getLinesRead()+" registros restaurados",
                    Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {Toast.makeText(this,e.getMessage(),
                Toast.LENGTH_SHORT).show();
        }
    }
}
