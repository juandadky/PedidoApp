package com.warsoft.pedidoapp.Local.Entities;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DetallePedido implements Serializable {

    @SerializedName("idpedido")
    private int idPedido;
    @SerializedName("idproducto")
    private int idProducto;
    @SerializedName("nombreproducto")
    private String nombreProducto;
    @SerializedName("cantidadporunidad")
    private String cantidadPorUnidad;
    @SerializedName("factor")
    private int factor;
    @SerializedName("tipoprecio")
    private String tipoPrecio;
    @SerializedName("almacen")
    private int almacen;
    @SerializedName("cantidad")
    private double cantidad;
    @SerializedName("entregado")
    private int entregado;
    @SerializedName("preciounidad")
    private double precioUnidad;
    @SerializedName("importe")
    private double importe;
    @SerializedName("descuento")
    private double descuento;
    @SerializedName("total")
    private double total;
    @SerializedName("por_descu")
    private double porDescu;
    @SerializedName("porc_igv")
    private int porIgv;
    @SerializedName("unidades")
    private double unidades;
    @SerializedName("por_percep")
    private int porPercep;
    @SerializedName("costo")
    private double costo;
    @SerializedName("registro")
    private int registro;
    @SerializedName("idcombo")
    private int idCombo;
    @SerializedName("combo")
    private int combo;
    private boolean surtido;
    @SerializedName("cantidad_soles")
    private int cantidadSoles;
    private int tipoprecioventa;
    private int tipocombo;

    private int cantidadPedido;

    private int unidadesPedido;

    private int totalunidadesPedido;

    private double totalPedido;

    public DetallePedido(int idPedido, int idProducto, String nombreProducto, String cantidadPorUnidad, int factor, String tipoPrecio, int almacen,
                         double cantidad, int entregado, double precioUnidad, double importe, double descuento, double total, double porDescu,
                         int porIgv, double unidades, int porPercep, double costo, int registro, int idCombo, int combo, boolean surtido, int tipocombo,
                         int cantidadSoles,int tipoprecioventa) {
        this.idPedido = idPedido;
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadPorUnidad = cantidadPorUnidad;
        this.factor = factor;
        this.tipoPrecio = tipoPrecio;
        this.almacen = almacen;
        this.cantidad = cantidad;
        this.entregado = entregado;
        this.precioUnidad = precioUnidad;
        this.importe = importe;
        this.descuento = descuento;
        this.total = total;
        this.porDescu = porDescu;
        this.porIgv = porIgv;
        this.unidades = unidades;
        this.porPercep = porPercep;
        this.costo = costo;
        this.registro = registro;
        this.idCombo = idCombo;
        this.combo = combo;
        this.surtido = surtido;
        this.tipocombo = tipocombo;
        this.cantidadSoles = cantidadSoles;
        this.tipoprecioventa = tipoprecioventa;
        this.cantidadPedido = 0;
        this.unidadesPedido = 0;
        this.totalunidadesPedido = 0;
        this.totalPedido = 0;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getCantidadPorUnidad() {
        return cantidadPorUnidad;
    }

    public int getFactor() {
        return factor;
    }

    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public int getAlmacen() {
        return almacen;
    }

    public double getCantidad() {
        return cantidad;
    }

    public int getEntregado() {
        return entregado;
    }

    public double getPrecioUnidad() {
        return precioUnidad;
    }

    public double getImporte() {
        return importe;
    }

    public double getDescuento() {
        return descuento;
    }

    public double getTotal() {
        return total;
    }

    public double getPorDescu() {
        return porDescu;
    }

    public int getPorIgv() {
        return porIgv;
    }

    public double getUnidades() {
        return unidades;
    }

    public int getPorPercep() {
        return porPercep;
    }

    public double getCosto() {
        return costo;
    }

    public int getRegistro() {
        return registro;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public int getCombo() {
        return combo;
    }

    public boolean isSurtido() {
        return surtido;
    }

    public void setSurtido(boolean surtido) {
        this.surtido = surtido;
    }

    public int getCantidadSoles() {
        return cantidadSoles;
    }

    public void setCantidadSoles(int cantidadSoles) {
        this.cantidadSoles = cantidadSoles;
    }

    public int getTipoprecioventa() {
        return tipoprecioventa;
    }

    public void setTipoprecioventa(int tipoprecioventa) {
        this.tipoprecioventa = tipoprecioventa;
    }

    public int getTipocombo() {
        return tipocombo;
    }

    public void setTipocombo(int tipocombo) {
        this.tipocombo = tipocombo;
    }

    public int getCantidadPedido() {
        return cantidadPedido;
    }

    public void setCantidadPedido(int cantidadPedido) {
        this.cantidadPedido = cantidadPedido;
    }

    public int getUnidadesPedido() {
        return unidadesPedido;
    }

    public void setUnidadesPedido(int unidadesPedido) {
        this.unidadesPedido = unidadesPedido;
    }

    public int getTotalunidadesPedido() {
        return totalunidadesPedido;
    }

    public void setTotalunidadesPedido(int totalunidadesPedido) {
        this.totalunidadesPedido = totalunidadesPedido;
    }

    public double getTotalPedido() {
        return totalPedido;
    }

    public void setTotalPedido(double totalPedido) {
        this.totalPedido = totalPedido;
    }
}
