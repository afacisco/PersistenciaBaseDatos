package com.example.persistenciabasedatos.adaptadores;

import static com.example.persistenciabasedatos.actividades.MainActivity.datos;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.persistenciabasedatos.R;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.persistenciabasedatos.actividades.MainActivity;
import com.example.persistenciabasedatos.controladores.MyOpenHelper;
import com.example.persistenciabasedatos.modelos.DatosReciclador;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

/*
Autor: Juan Francisco Sánchez González
Fecha: 22/01/2024
Clase: Adaptador para el RecyclerView del listado que se muestra en el MainActivity
*/

public class AdaptadorReciclador extends RecyclerView.Adapter<AdaptadorReciclador.ViewHolder> {

    private Context ctx;
    private List<DatosReciclador> items;
    private DatosReciclador itemLista;

    public AdaptadorReciclador(List<DatosReciclador> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView usuario;
        private final TextView comentario;
        private final ImageButton btnEliminar;

        public ViewHolder(View vista) {
            super (vista);

            // Instanciamos los 3 controles de cada item del listado
            usuario = (TextView) vista.findViewById(R.id.textViewUsuario);
            comentario = (TextView) vista.findViewById(R.id.textViewComentario);
            btnEliminar = (ImageButton) vista.findViewById(R.id.buttonEliminar);
        }

        public TextView getUsuario() {
            return usuario;
        }

        public TextView getComentario() {
            return comentario;
        }

    }

    @Override
    public int getItemCount () {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_listado, viewGroup, false);
        ctx = viewGroup.getContext();
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.getUsuario().setText(items.get(i).getUsuario());
        viewHolder.getComentario().setText(items.get(i).getComentario());

        // Listener botón eliminar elemento
        viewHolder.btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Diálogo confirmar borrado
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage(ctx.getString(R.string.dialogo_eliminar_mensaje))
                        .setTitle(ctx.getString(R.string.dialogo_eliminar_titulo))
                        .setIcon(R.mipmap.ic_launcher_round)
                        .setPositiveButton(ctx.getString(R.string.btn_positivo_dialogo), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyOpenHelper dbHelper = new MyOpenHelper(ctx);
                                SQLiteDatabase db = dbHelper.getWritableDatabase();
                                itemLista = datos.get(i);
                                if (db != null) {
                                    db.delete("comentarios", "id=" + itemLista.getId(), null);
                                    deleteItem(i);
                                }
                            }
                        })
                        .setNegativeButton(ctx.getString(R.string.btn_negativo_dialogo), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialogo = builder.create();
                dialogo.show();
            }
        });
    }

    // Método para actualizar el adaptador después de añadir un nuevo item
    public void addItem(DatosReciclador item, int index) {
        items.add(item);
        notifyItemInserted(index);
    }

    // Método para actualizar el adaptador después de eliminar un nuevo item
    void deleteItem(int index) {
        items.remove(index);
        notifyItemRemoved(index);
    }
}
