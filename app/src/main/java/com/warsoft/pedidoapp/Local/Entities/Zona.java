package com.warsoft.pedidoapp.Local.Entities;

import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class Zona {
    @SerializedName("CODIGO")
    private String codigo;
    private int idZona;
    @SerializedName("DETALLE")
    private String detalle;
    @SerializedName("REPORTES")
    private String reportes;
    @SerializedName("UNIDO")
    private String unido;
    @SerializedName("UBIGEO")
    private String ubigeo;
    @SerializedName("F_UPDATE")
    private String fUpdate;
    @SerializedName("LUNES")
    private String lunes;
    @SerializedName("MARTES")
    private String martes;
    @SerializedName("MIERCOLES")
    private String miercoles;
    @SerializedName("JUEVES")
    private String jueves;
    @SerializedName("VIERNES")
    private String viernes;
    @SerializedName("SABADO")
    private String sabado;
    @SerializedName("VENDEDOR")
    private String vendedor;
    @SerializedName("COD")
    private String cod;
    @SerializedName("SI")
    private String si;
    @SerializedName("iddistri")
    private int idDistri;

    public Zona(String codigo, int idZona, String detalle, String reportes, String unido, String ubigeo, String fUpdate, String lunes, String martes, String miercoles, String jueves, String viernes, String sabado, String vendedor, String cod, String si, int idDistri) {
        this.codigo = codigo;
        this.idZona = idZona;
        this.detalle = detalle;
        this.reportes = reportes;
        this.unido = unido;
        this.ubigeo = ubigeo;
        this.fUpdate = fUpdate;
        this.lunes = lunes;
        this.martes = martes;
        this.miercoles = miercoles;
        this.jueves = jueves;
        this.viernes = viernes;
        this.sabado = sabado;
        this.vendedor = vendedor;
        this.cod = cod;
        this.si = si;
        this.idDistri = idDistri;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getIdZona() {
        return idZona;
    }

    public String getDetalle() {
        return detalle;
    }

    public String getReportes() {
        return reportes;
    }

    public String getUnido() {
        return unido;
    }

    public String getUbigeo() {
        return ubigeo;
    }

    public String getfUpdate() {
        return fUpdate;
    }

    public String getLunes() {
        return lunes;
    }

    public String getMartes() {
        return martes;
    }

    public String getMiercoles() {
        return miercoles;
    }

    public String getJueves() {
        return jueves;
    }

    public String getViernes() {
        return viernes;
    }

    public String getSabado() {
        return sabado;
    }

    public String getVendedor() {
        return vendedor;
    }

    public String getCod() {
        return cod;
    }

    public String getSi() {
        return si;
    }

    public int getIdDistri() {
        return idDistri;
    }

}
