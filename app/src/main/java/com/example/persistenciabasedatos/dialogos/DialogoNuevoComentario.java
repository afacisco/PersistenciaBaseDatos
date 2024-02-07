package com.example.persistenciabasedatos.dialogos;

import static com.example.persistenciabasedatos.actividades.MainActivity.datos;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.persistenciabasedatos.R;
import com.example.persistenciabasedatos.adaptadores.AdaptadorReciclador;
import com.example.persistenciabasedatos.controladores.MyOpenHelper;
import com.example.persistenciabasedatos.modelos.DatosReciclador;

import java.util.List;

/*
Autor: Juan Francisco Sánchez González
Fecha: 22/01/2024
Clase: Fragment para el diálogo de nuevo elemento
*/

public class DialogoNuevoComentario extends DialogFragment {

    private EditText usuario;
    private EditText comentario;
    AdaptadorReciclador adaptador;
    DatosReciclador itemLista;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Diálogo nuevo elemento
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        // Inflamos vista del diálogo
        View view = inflater.inflate(R.layout.dialogo_nuevo_comentario, null);

        // Instanciamos controles interfaz
        usuario = (EditText) view.findViewById(R.id.usuario);
        comentario = (EditText) view.findViewById(R.id.comentario);

        // Seteamos diálogo
        builder.setTitle(getString(R.string.dialogo_nuevo_comentario))
                .setIcon(R.mipmap.ic_launcher_round)
                .setView(view)
                .setCancelable(false)
                .setPositiveButton(R.string.btn_positivo_dialogo, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        MyOpenHelper dbHelper = new MyOpenHelper(getActivity());
                        SQLiteDatabase db = dbHelper.getWritableDatabase();
                        if (db != null) {
                            if (usuario.getText().toString().isEmpty() || comentario.getText().toString().isEmpty()) {
                                Toast.makeText(getActivity(), getString(R.string.txt_campos_oblig), Toast.LENGTH_SHORT).show();
                            } else {
                                // Insert BD
                                ContentValues nuevoComent = new ContentValues();
                                nuevoComent.put("usuario", usuario.getText().toString());
                                nuevoComent.put("comentario", comentario.getText().toString());
                                db.insert("comentarios", null, nuevoComent);

                                // Actualizamos adaptador listado
                                Cursor cursor = db.rawQuery("SELECT id FROM comentarios WHERE usuario='" + usuario.getText().toString() + "' AND comentario='" + comentario.getText().toString() + "'", null);
                                cursor.moveToFirst();
                                Integer idItem = cursor.getInt(0);
                                itemLista = new DatosReciclador(usuario.getText().toString(), comentario.getText().toString(), idItem);
                                adaptador = new AdaptadorReciclador(datos);
                                adaptador.addItem(itemLista, adaptador.getItemCount() + 1);
                            }
                        }
                    }
                })
                .setNegativeButton(R.string.btn_negativo_dialogo, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DialogoNuevoComentario.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

