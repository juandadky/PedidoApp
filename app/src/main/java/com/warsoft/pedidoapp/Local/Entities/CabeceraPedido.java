package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CabeceraPedido implements Serializable {
    @SerializedName("idPedido")
    private int idPedido;
    @SerializedName("fechapedido")
    private String fechaPedido;
    @SerializedName("fechaentrega")
    private String fechaEntrega;
    @SerializedName("nro_dias")
    private int nroDias;
    @SerializedName("tarjeta")
    private boolean tarjeta;
    @SerializedName("por_tarjeta")
    private double porTarjeta;
    @SerializedName("estado")
    private String estado;
    @SerializedName("idtransporte")
    private int idTranporte;
    @SerializedName("idzona")
    private int idZona;
    @SerializedName("idcliente")
    private int idCliente;
    @SerializedName("nombrecliente")
    private String nombreCliente;
    @SerializedName("nombrezona")
    private String nombreZona;
    @SerializedName("tipneg")
    private int tipNeg;
    @SerializedName("Detalle")
    private String detalle;
    @SerializedName("tipoventa")
    private String tipoVenta;
    @SerializedName("idVendedor")
    private int idVendedor;
    @SerializedName("referencia")
    private String referencia;
    @SerializedName("direccion")
    private String direccion;
    @SerializedName("ruc")
    private String ruc;
    @SerializedName("doc_iden")
    private String doc_iden;
    @SerializedName("nombre")
    private String nombre;
    @SerializedName("moneda")
    private int moneda;
    @SerializedName("facBolNP")
    private int facBolNP;
    @SerializedName("idusuario")
    private int idUsuario;
    @SerializedName("redespacho")
    private int redespacho;
    private double total;

    public CabeceraPedido() {
    }

    public CabeceraPedido(int idPedido, String fechaPedido, String fechaEntrega, int nroDias, boolean tarjeta, double porTarjeta, String estado,
                          int idTranporte, int idZona, int idCliente, String nombreCliente, String nombreZona, int tipNeg, String detalle,
                          String tipoVenta, int idVendedor, String referencia, String direccion, String ruc, String doc_iden, String nombre,
                          int moneda, int facBolNP, int idUsuario, int redespacho, double total) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.fechaEntrega = fechaEntrega;
        this.nroDias = nroDias;
        this.tarjeta = tarjeta;
        this.porTarjeta = porTarjeta;
        this.estado = estado;
        this.idTranporte = idTranporte;
        this.idZona = idZona;
        this.idCliente = idCliente;
        this.nombreCliente = nombreCliente;
        this.nombreZona = nombreZona;
        this.tipNeg = tipNeg;
        this.detalle = detalle;
        this.tipoVenta = tipoVenta;
        this.idVendedor = idVendedor;
        this.referencia = referencia;
        this.direccion = direccion;
        this.ruc = ruc;
        this.doc_iden = doc_iden;
        this.nombre = nombre;
        this.moneda = moneda;
        this.facBolNP = facBolNP;
        this.idUsuario = idUsuario;
        this.redespacho = redespacho;
        this.total = total;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public String getFechaEntrega() {
        return fechaEntrega;
    }

    public int getNroDias() {
        return nroDias;
    }

    public boolean isTarjeta() {
        return tarjeta;
    }

    public double getPorTarjeta() {
        return porTarjeta;
    }

    public String getEstado() {
        return estado;
    }

    public int getIdTranporte() {
        return idTranporte;
    }

    public int getIdZona() {
        return idZona;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public int getTipNeg() {
        return tipNeg;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getTipoVenta() {
        return tipoVenta;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public String getReferencia() {
        return referencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getRuc() {
        return ruc;
    }

    public String getDoc_iden() {
        return doc_iden;
    }

    public String getNombre() {
        return nombre;
    }

    public int getMoneda() {
        return moneda;
    }

    public int getFacBolNP() {
        return facBolNP;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public int getRedespacho() {
        return redespacho;
    }

    public double getTotal() {
        return total;
    }
}
