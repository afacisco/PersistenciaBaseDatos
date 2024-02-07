package com.example.persistenciabasedatos.actividades;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.persistenciabasedatos.R;
import com.example.persistenciabasedatos.adaptadores.AdaptadorReciclador;
import com.example.persistenciabasedatos.controladores.MyOpenHelper;
import com.example.persistenciabasedatos.dialogos.DialogoNuevoComentario;
import com.example.persistenciabasedatos.modelos.DatosReciclador;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/*
Autor: Juan Francisco Sánchez González
Fecha: 22/01/2024
Clase: Actividad que muestra un listado de elementos (Cards con 2 textos) en un RecyclerView. Para la
persistencia de esos datos se utiliza una BD local (SQLite) con 1 tabla. Existe la posibilidad de añadir
y eliminar elementos al listado. También contiene una Toolbar con la opción de salir de la aplicación.
*/

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private Toolbar toolbar;
    private RecyclerView reciclador;
    private RecyclerView.Adapter adaptadorReciclador;
    private RecyclerView.LayoutManager layManagerReciclador;

    // Listado de elementos del Recycler
    public static List<DatosReciclador> datos;
    // Objeto elemento del listado
    DatosReciclador itemLista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Componente Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Carga de datos Reciclador
        datos = new ArrayList<>();
        MyOpenHelper dbHelper = new MyOpenHelper(MainActivity.this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT id, usuario, comentario FROM comentarios", null);
        if (cursor.moveToFirst()) {
            do {
                Integer id = cursor.getInt(0);
                String usu = cursor.getString(1);
                String com = cursor.getString(2);
                itemLista = new DatosReciclador(usu, com, id);
                datos.add(itemLista);
            } while (cursor.moveToNext());
        }

        // Componente Reciclador
        reciclador = (RecyclerView) findViewById(R.id.reciclador);
        reciclador.setHasFixedSize(true);
        layManagerReciclador = new LinearLayoutManager(this);
        reciclador.setLayoutManager(layManagerReciclador);

        adaptadorReciclador = new AdaptadorReciclador(datos);
        reciclador.setAdapter(adaptadorReciclador);

        // Componente Floating Action Button (FAB)
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Diálogo nuevo elemento listado
                DialogFragment dialogo = new DialogoNuevoComentario();
                dialogo.show(getSupportFragmentManager(), "DialogoNuevoComentario");
            }
        });
    }

    // OptionsMenu de la ToolBar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    // Listener OptionsMenu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Salir
        if (item.getItemId() == R.id.opcionSalir) {
            // Diálogo salir aplicativo
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage(getResources().getString(R.string.dialogo_salir_mensaje))
                    .setTitle(getResources().getString(R.string.options_menu_salir))
                    .setIcon(R.mipmap.ic_launcher_round)
                    .setPositiveButton(getResources().getString(R.string.btn_positivo_dialogo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.btn_negativo_dialogo), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            AlertDialog dialogo = builder.create();
            dialogo.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}