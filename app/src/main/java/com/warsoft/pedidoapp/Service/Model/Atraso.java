package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;

public class Atraso {
    @SerializedName("idcliente")
    private int idCliente;
    private int dias;

    public Atraso(int idCliente, int dias) {
        this.idCliente = idCliente;
        this.dias = dias;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public int getDias() {
        return dias;
    }
}
