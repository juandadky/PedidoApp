package com.warsoft.pedidoapp.Local.Entities;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Usuario implements Serializable {

    @SerializedName("idUsuario")
    private int id;
    @SerializedName("Usuario")
    private String usuario;
    @SerializedName("Clave")
    private String clave;
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
    private String pagos;
    private String horapedido;
    private boolean credito;

    public Usuario() {
    }

    public Usuario(int id, String usuario, String clave, String nombre, String tipo, String abreviatura, int codVendedor, int empresa, String anulacion,
                   String pagos, String horapedido, boolean credito) {
        this.id = id;
        this.usuario = usuario;
        this.clave = clave;
        this.nombre = nombre;
        this.tipo = tipo;
        this.abreviatura = abreviatura;
        this.codVendedor = codVendedor;
        this.empresa = empresa;
        this.anulacion = anulacion;
        this.pagos = pagos;
        this.horapedido = horapedido;
        this.credito = credito;
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

    public String getPagos() {
        return pagos;
    }

    public void setPagos(String pagos) {
        this.pagos = pagos;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getHorapedido() {
        return horapedido;
    }

    public void setHorapedido(String horapedido) {
        this.horapedido = horapedido;
    }

    public boolean isCredito() {
        return credito;
    }

    public void setCredito(boolean credito) {
        this.credito = credito;
    }
}
