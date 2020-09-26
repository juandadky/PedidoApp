package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

public class TipoPrecio {
    private int idPrecio;
    @SerializedName("NombrePrecio")
    private String nombrePrecio;

    public TipoPrecio(int idPrecio, String nombrePrecio) {
        this.idPrecio = idPrecio;
        this.nombrePrecio = nombrePrecio;
    }

    public int getIdPrecio() {
        return idPrecio;
    }

    public String getNombrePrecio() {
        return nombrePrecio;
    }
}
