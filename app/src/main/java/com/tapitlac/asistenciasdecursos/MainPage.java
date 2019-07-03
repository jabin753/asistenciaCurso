package com.tapitlac.asistenciasdecursos;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.view.ActionMode;
import android.view.MenuInflater;
import android.view.View.OnClickListener;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainPage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listaAsistencia;
    List<String[]> data;
    protected Object mActionMode;
    public int selectedItem=-1;
    String arreglo[];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Toast.makeText(this,"Mantenga presionado para eliminar un elemento",Toast.LENGTH_SHORT).show();
        listaAsistencia = (ListView)findViewById(R.id.ListaAsistencia);
        listaAsistencia.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listaAsistencia.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mActionMode!=null){
                    return false;

                }
                selectedItem=i;
                Borrar();
                view.setSelected(true);
                return true;
            }
        });

        try {
            cargarListaAsistencia();
        }catch(Exception e){ Toast.makeText(this,e.getMessage(),
                Toast.LENGTH_LONG).show();}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        displayView(item.getItemId());
        return true;
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
                exportDBLista();
                return true;
            case R.id.importarDatos:
                importDBLista();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //From stackoverflow's answer
    public void displayView(int viewId) {
        switch (viewId) {
            case R.id.nav_camera:
                cargarListaAsistencia();
                startActivity(new Intent(getApplicationContext(),alumnos_agregar.class));
                break;
            case R.id.nav_gallery:
                cargarListaAsistencia();
                startActivity(new Intent(getApplicationContext(),alumnos_eliminar.class));
                break;
            case R.id.nav_slideshow:
                cargarListaAsistencia();
                startActivity(new Intent(getApplicationContext(),alumnos_lista.class));
                break;
            case R.id.nav_asistencia:
                cargarListaAsistencia();
                startActivity(new Intent(getApplicationContext(),asistencia_agregar.class));
                break;
            case R.id.nav_refrescar:
                cargarListaAsistencia();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
    protected  void cargarListaAsistencia(){
        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        /*SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
        builder.setTables("INNER JOIN Personas ON Asistencia.IdAlumno = Personas.Id");
        builder.buildQuery(new String[]{"Id","Fecha","IdAlumno","esAsistencia","EsRetardo"},"select * from Asistencia",null,null,null,null);
        */
        if(db!=null) {
            Cursor c = db.rawQuery("select * from Asistencia a INNER JOIN Personas b ON a.IdAlumno = b.Id", null);
            int i = 0;
            int cantidad = c.getCount();

            arreglo= new String[cantidad];
            data = new ArrayList<String[]>();
            if (c.moveToFirst()) {
                do {
                    String asistencia,retardo;
                    if(c.getInt(3)==1){asistencia="Asistencia";}else{asistencia="Falta";}
                    if(c.getInt(4)==1){retardo="Retardo";}else{retardo="";}
                    data.add(new String[]{c.getString(1),String.valueOf(c.getInt(2)),asistencia,retardo});
                    String linea =c.getInt(0)+"    "+c.getString(1)+"     "+c.getString(6)+" "+c.getString(7)+"     "+c.getString(8)+"     "+asistencia+"     "+retardo;
                    arreglo[i] = linea;
                    i++;

                } while (c.moveToNext());
            }
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arreglo);
            listaAsistencia.setAdapter(adapter);

        }
    }

    public void exportDBLista(){
        cargarListaAsistencia();
        try
        {
            File tarjeta = Environment.getExternalStorageDirectory();
            File file = new File(tarjeta.getAbsolutePath(), "ListaAsistenciaAlumnos.csv");
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
    public void importDBLista() {

        File tarjeta = Environment.getExternalStorageDirectory();
        File file = new File(tarjeta.getAbsolutePath(), "ListaAsistenciaAlumnos.csv");
        try {
            FileReader fr = new FileReader(file.getAbsolutePath());
            CSVReader lector = new CSVReader(fr, ',');
            data = lector.readAll();
            lector.close();

            BaseHelper baseHelper = new BaseHelper(this, "DEMODB", null, 1);
            SQLiteDatabase db = baseHelper.getWritableDatabase();
            ContentValues values = new ContentValues();
            int i;
            for (i = 0; i < data.size(); i++) {
                String registro[] = data.get(i);
                values.put("Fecha", registro[0]);
                values.put("IdAlumno", Integer.parseInt(registro[1]));
                if ("Asistencia".equals(registro[2])) {
                    values.put("esAsistencia", 1);
                } else if("Falta".equals(registro[2])){
                    values.put("esAsistencia", 0);
                }
                if ("Retardo".equals(registro[3])) {
                    values.put("esRetardo", 1);
                } else if("".equals(registro[3])) {
                    values.put("esRetardo", 0);
                }
                long result = db.insert("Asistencia", null, values);
            }
            cargarListaAsistencia();
            if (i > 0) {
                Toast.makeText(this, lector.getLinesRead() + " registros restaurados",
                        Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }
    private void    Borrar(){
        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        int id= Integer.parseInt(arreglo[selectedItem].split(" ")[0]);
        if(db!=null) {
            long res=db.delete("Asistencia","id="+id,null);
            if (res>0){
                Toast.makeText(this,"Elemento Eliminado",Toast.LENGTH_SHORT).show();
                cargarListaAsistencia();
            }

        }
    }

}
    
