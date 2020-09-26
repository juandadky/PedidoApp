package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

public class Categoria {

    private int idCategoria;
    private String codigo;
    @SerializedName("NombreCategoria")
    private String nombreCategoria;
    private String descripcion;
    private String imagen;
    private boolean activo;

    public Categoria(int idCategoria, String codigo, String nombreCategoria, String descripcion, String imagen, boolean activo) {
        this.idCategoria = idCategoria;
        this.codigo = codigo;
        this.nombreCategoria = nombreCategoria;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.activo = activo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombreCategoria() {
        return nombreCategoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getImagen() {
        return imagen;
    }

    public boolean isActivo() {
        return activo;
    }
}
