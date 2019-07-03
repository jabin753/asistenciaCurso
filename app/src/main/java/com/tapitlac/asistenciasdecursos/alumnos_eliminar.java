package com.tapitlac.asistenciasdecursos;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.view.ActionMode.Callback;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
public class alumnos_eliminar extends AppCompatActivity {
    String[] arreglo;
    ListView lista;
    protected Object mActionMode;
    public int selectedItem=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alumnos_eliminar);

        cargar();
        lista=(ListView) findViewById(R.id.Lista);
        lista.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        lista.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (mActionMode!=null){
                    return false;

                }
                selectedItem=position;
                mActionMode =alumnos_eliminar.this.startSupportActionMode(amc);
                view.setSelected(true);
                return true;

            }
        });

    }
    private Callback amc=new Callback(){

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater= mode.getMenuInflater();
            inflater.inflate(R.menu.eliminar,menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()){
                case R.id.delete_v2:
                    Borrar();
                    mode.finish();
                    return true;
                default:
                    return false;

            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode=null;
            selectedItem=-1;
        }
    };
    public void cargar  (){

        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        if(db!=null) {

            Cursor c = db.rawQuery("select * from Personas", null);
            int i = 0;
            int cantidad = c.getCount();
            arreglo = new String[cantidad];
            if (c.moveToFirst()) {
                do {
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
    private void    Borrar(){
        BaseHelper baseHelper=new BaseHelper(this,"DEMODB",null,1);
        SQLiteDatabase db = baseHelper.getReadableDatabase();
        int id= Integer.parseInt(arreglo[selectedItem].split(" ")[0]);
        if(db!=null) {
            long res=db.delete("Personas","id="+id,null);
            if (res>0){
                Toast.makeText(this,"Alumno Eliminado",Toast.LENGTH_SHORT).show();
                cargar();
            }

        }
    }
}
