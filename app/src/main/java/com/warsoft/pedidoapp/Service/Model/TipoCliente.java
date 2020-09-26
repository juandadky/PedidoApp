package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;

public class TipoCliente {

    @SerializedName("tipocliente")
    private int tipoCliente;

    public TipoCliente(int tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public int getTipoCliente() {
        return tipoCliente;
    }
}
