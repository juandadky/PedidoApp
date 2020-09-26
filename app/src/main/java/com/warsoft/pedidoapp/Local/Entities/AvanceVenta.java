package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

public class AvanceVenta {

    @SerializedName("idvendedor")
    private int idVendedor;
    private String nombre;
    @SerializedName("cliaten")
    private int cliAten;
    @SerializedName("clientes")
    private int clientes;
    private double contado;
    private double credito;


    public AvanceVenta(int idVendedor, String nombre, int cliAten, int clientes, double contado, double credito) {
        this.idVendedor = idVendedor;
        this.nombre = nombre;
        this.cliAten = cliAten;
        this.clientes = clientes;
        this.contado = contado;
        this.credito = credito;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCliAten() {
        return cliAten;
    }

    public int getClientes() {
        return clientes;
    }

    public double getContado() {
        return contado;
    }

    public double getCredito() {
        return credito;
    }
}
