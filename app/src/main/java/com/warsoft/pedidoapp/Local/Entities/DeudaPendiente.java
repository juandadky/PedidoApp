package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

public class DeudaPendiente {

    private String fecha;
    private String abreviatura;
    @SerializedName("iddocorden")
    private String idDocOrden;
    private String nombre;
    private double total;
    @SerializedName("diascredito")
    private String diasCredito;
    private int dias;
    @SerializedName("tot_pago")
    private double totPago;
    private int percepcion;
    private String empresa;
    @SerializedName("iddocumento")
    private int idDocumento;
    @SerializedName("SaldoSoles")
    private double saldoSoles;
    @SerializedName("SaldoDolares")
    private double saldoDolares;
    @SerializedName("idcliente")
    private int idCliente;

    public DeudaPendiente() {
    }

    public DeudaPendiente(String fecha, String abreviatura, String idDocOrden, String nombre, double total, String diasCredito, int dias,
                          double totPago, int percepcion, String empresa, int idDocumento, double saldoSoles, double saldoDolares, int idCliente) {
        this.fecha = fecha;
        this.abreviatura = abreviatura;
        this.idDocOrden = idDocOrden;
        this.nombre = nombre;
        this.total = total;
        this.diasCredito = diasCredito;
        this.dias = dias;
        this.totPago = totPago;
        this.percepcion = percepcion;
        this.empresa = empresa;
        this.idDocumento = idDocumento;
        this.saldoSoles = saldoSoles;
        this.saldoDolares = saldoDolares;
        this.idCliente = idCliente;
    }

    public String getFecha() {
        return fecha;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public String getIdDocOrden() {
        return idDocOrden;
    }

    public String getNombre() {
        return nombre;
    }

    public double getTotal() {
        return total;
    }

    public String getDiasCredito() {
        return diasCredito;
    }

    public int getDias() {
        return dias;
    }

    public double getTotPago() {
        return totPago;
    }

    public int getPercepcion() {
        return percepcion;
    }

    public String getEmpresa() {
        return empresa;
    }

    public int getIdDocumento() {
        return idDocumento;
    }

    public double getSaldoSoles() {
        return saldoSoles;
    }

    public double getSaldoDolares() {
        return saldoDolares;
    }

    public int getIdCliente() {
        return idCliente;
    }
}
