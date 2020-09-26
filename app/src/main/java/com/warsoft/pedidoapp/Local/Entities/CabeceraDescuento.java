package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CabeceraDescuento implements Serializable {
    @SerializedName("idproducto")
    private int idProducto;
    @SerializedName("idprodcomponente")
    private int idProdComponente;
    @SerializedName("nombreproducto")
    private String nombreProducto;
    @SerializedName("vendedores")
    private String vendedores;
    @SerializedName("zonas")
    private String zonas;
    @SerializedName("tiponegocio")
    private String tipoNegocio;
    @SerializedName("desde")
    private String fechaInicio;
    @SerializedName("hasta")
    private String fechaFinal;
    @SerializedName("tipocombo")
    private int tipoCombo;
    @SerializedName("nombredescuento")
    private String nombreDescuento;
    @SerializedName("canti_max")
    private double catiMax;
    @SerializedName("pordes")
    private double porDes;
    @SerializedName("nroitems")
    private int nroItems;
    @SerializedName("cantidad")
    private double cantidad;
    @SerializedName("criterio")
    private String criterio;
    private boolean surtido;
    private int unidades;
    @SerializedName("unimed")
    private String uniMed;
    private int factor;
    @SerializedName("cantidad_soles")
    private int cantidadSoles;


    public CabeceraDescuento(int idProducto, int idProdComponente, String nombreProducto, String vendedores, String zonas, String tipoNegocio,
                             String fechaInicio, String fechaFinal, int tipoCombo, String nombreDescuento, double catiMax, double porDes, int nroItems,
                             double cantidad, String criterio, boolean surtido, int unidades, String uniMed, int factor, int cantidadSoles) {
        this.idProducto = idProducto;
        this.idProdComponente = idProdComponente;
        this.nombreProducto = nombreProducto;
        this.vendedores = vendedores;
        this.zonas = zonas;
        this.tipoNegocio = tipoNegocio;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.tipoCombo = tipoCombo;
        this.nombreDescuento = nombreDescuento;
        this.catiMax = catiMax;
        this.porDes = porDes;
        this.nroItems = nroItems;
        this.cantidad = cantidad;
        this.criterio = criterio;
        this.surtido = surtido;
        this.unidades = unidades;
        this.uniMed = uniMed;
        this.factor = factor;
        this.cantidadSoles = cantidadSoles;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public int getIdProdComponente() {
        return idProdComponente;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public String getVendedores() {
        return vendedores;
    }

    public String getZonas() {
        return zonas;
    }

    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public int getTipoCombo() {
        return tipoCombo;
    }

    public String getNombreDescuento() {
        return nombreDescuento;
    }

    public double getCatiMax() {
        return catiMax;
    }

    public double getPorDes() {
        return porDes;
    }

    public int getNroItems() {
        return nroItems;
    }

    public double getCantidad() {
        return cantidad;
    }

    public String getCriterio() {
        return criterio;
    }

    public boolean isSurtido() {
        return surtido;
    }

    public int getUnidades() {
        return unidades;
    }

    public String getUniMed() {
        return uniMed;
    }

    public int getFactor() {
        return factor;
    }

    public int getCantidadSoles() {
        return cantidadSoles;
    }
}
