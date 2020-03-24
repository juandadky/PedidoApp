package com.dadky.pedidoapp.Local;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {
    @SerializedName("idUsuario")
    private int id;
    @SerializedName("Nombre")
    private String nombre;
    @SerializedName("Tipo")
    private String tipo;
    @SerializedName("Abreviatura")
    private String abreviatura;
    @SerializedName("CodVendedor")
    private int codVendedor;
    @SerializedName("Empresa")
    private int empresa;
    @SerializedName("anulacion")
    private String anulacion;
    @SerializedName("pagos")
    private int pagos;

    public Usuario(int id, String nombre, String tipo, String abreviatura, int codVendedor, int empresa, String anulacion, int pagos) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.abreviatura = abreviatura;
        this.codVendedor = codVendedor;
        this.empresa = empresa;
        this.anulacion = anulacion;
        this.pagos = pagos;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    public void setAbreviatura(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public int getCodVendedor() {
        return codVendedor;
    }

    public void setCodVendedor(int codVendedor) {
        this.codVendedor = codVendedor;
    }

    public int getEmpresa() {
        return empresa;
    }

    public void setEmpresa(int empresa) {
        this.empresa = empresa;
    }

    public String getAnulacion() {
        return anulacion;
    }

    public void setAnulacion(String anulacion) {
        this.anulacion = anulacion;
    }

    public int getPagos() {
        return pagos;
    }

    public void setPagos(int pagos) {
        this.pagos = pagos;
    }
}
