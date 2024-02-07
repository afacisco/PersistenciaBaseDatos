package com.example.persistenciabasedatos.modelos;

/*
Autor: Juan Francisco Sánchez González
Fecha: 22/01/2024
Clase: Modelo de datos de los elementos del listado (2 textos y el id de su registro en la BD)
*/

public class DatosReciclador {
    private String usuario;
    private String comentario;
    private Integer id;

    public DatosReciclador(String usu, String com, Integer idItem) {
        usuario = usu;
        comentario = com;
        id = idItem;
    }

    public String getUsuario() {
        return usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public Integer getId() {
        return id;
    }

}


