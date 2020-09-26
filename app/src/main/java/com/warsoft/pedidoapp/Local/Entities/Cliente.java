package com.warsoft.pedidoapp.Local.Entities;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Cliente implements Serializable {
    @SerializedName("idCliente")
    private int idCliente;
    @SerializedName("NOMBRE")
    private String nombre;
    @SerializedName("DIRECCION")
    private String direccion;
    @SerializedName("DOC_IDEN")
    private String docIden;
    @SerializedName("RUC")
    private String ruc;
    @SerializedName("TELEFONO1")
    private String telefono1;
    @SerializedName("Celular")
    private String celular;
    @SerializedName("FPAGO")
    private int fPago;
    @SerializedName("idZona")
    private int idZona;
    @SerializedName("TipoCliente")
    private int tipoCliente;
    @SerializedName("TIPNEG")
    private int tipNeg;
    @SerializedName("lincredito")
    private double linCredito;
    @SerializedName("Nextel")
    private String nextel;
    @SerializedName("idvendedor")
    private int idVendedor;
    @SerializedName("iddistrito")
    private int idDistrito;
    @SerializedName("SALDO")
    private double saldo;
    @SerializedName("REFERENCIA")
    private String referencia;
    @SerializedName("BOLETEAR")
    private String boletear;
    @SerializedName("NombreZona")
    private String nombreZona;
    @SerializedName("NombreTipNeg")
    private String nombreTipNeg;
    @SerializedName("Activo")
    private boolean activo;
    @SerializedName("idvendedor2")
    private int idVendedor2;
    @SerializedName("idzona2")
    private int idZona2;
    private int visita2;
    private String zona;
    private String distrito;
    @SerializedName("listaprecio")
    private String listaPrecio;
    @SerializedName("preciovolumen")
    private boolean precioVolumen;
    private int atraso;
    private int maxPago;

    public Cliente() {
        zona = "";
    }

    public Cliente(int idCliente, String nombre, String direccion, String docIden, String ruc, int fPago, int idZona, int tipoCliente, int tipNeg, double linCredito, String nextel, int idVendedor, int idDistrito, double saldo, String referencia, String boletear, String nombreZona, String nombreTipNeg, boolean activo, int idVendedor2, int idZona2, int visita2, String zona, String distrito, String listaPrecio,boolean precioVolumen) {
        this.idCliente = idCliente;
        this.nombre = nombre;
        this.direccion = direccion;
        this.docIden = docIden;
        this.ruc = ruc;
        this.fPago = fPago;
        this.idZona = idZona;
        this.tipoCliente = tipoCliente;
        this.tipNeg = tipNeg;
        this.linCredito = linCredito;
        this.nextel = nextel;
        this.idVendedor = idVendedor;
        this.idDistrito = idDistrito;
        this.saldo = saldo;
        this.referencia = referencia;
        this.boletear = boletear;
        this.nombreZona = nombreZona;
        this.nombreTipNeg = nombreTipNeg;
        this.activo = activo;
        this.idVendedor2 = idVendedor2;
        this.idZona2 = idZona2;
        this.visita2 = visita2;
        this.zona = zona;
        this.distrito = distrito;
        this.listaPrecio = listaPrecio;
        this.precioVolumen = precioVolumen;
        this.atraso = 0;
        this.maxPago = 0;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getDocIden() {
        return docIden;
    }

    public void setDocIden(String docIden) {
        this.docIden = docIden;
    }

    public String getRuc() {
        return ruc;
    }

    public void setRuc(String ruc) {
        this.ruc = ruc;
    }

    public int getfPago() {
        return fPago;
    }

    public void setfPago(int fPago) {
        this.fPago = fPago;
    }

    public int getIdZona() {
        return idZona;
    }

    public void setIdZona(int idZona) {
        this.idZona = idZona;
    }

    public int getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(int tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public int getTipNeg() {
        return tipNeg;
    }

    public void setTipNeg(int tipNeg) {
        this.tipNeg = tipNeg;
    }

    public double getLinCredito() {
        return linCredito;
    }

    public void setLinCredito(double linCredito) {
        this.linCredito = linCredito;
    }

    public int getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(int idVendedor) {
        this.idVendedor = idVendedor;
    }

    public int getIdDistrito() {
        return idDistrito;
    }

    public void setIdDistrito(int idDistrito) {
        this.idDistrito = idDistrito;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getNombreZona() {
        return nombreZona;
    }

    public void setNombreZona(String nombreZona) {
        this.nombreZona = nombreZona;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public int getIdVendedor2() {
        return idVendedor2;
    }

    public void setIdVendedor2(int idVendedor2) {
        this.idVendedor2 = idVendedor2;
    }

    public int getIdZona2() {
        return idZona2;
    }

    public void setIdZona2(int idZona2) {
        this.idZona2 = idZona2;
    }

    public String getZona() {
        return zona;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public String getDistrito() {
        return distrito;
    }

    public void setDistrito(String distrito) {
        this.distrito = distrito;
    }

    public String getListaPrecio() {
        return listaPrecio;
    }

    public void setListaPrecio(String listaPrecio) {
        this.listaPrecio = listaPrecio;
    }

    public String getNextel() {
        return nextel;
    }

    public void setNextel(String nextel) {
        this.nextel = nextel;
    }

    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getBoletear() {
        return boletear;
    }

    public void setBoletear(String boletear) {
        this.boletear = boletear;
    }

    public String getNombreTipNeg() {
        return nombreTipNeg;
    }

    public void setNombreTipNeg(String nombreTipNeg) {
        this.nombreTipNeg = nombreTipNeg;
    }

    public int getVisita2() {
        return visita2;
    }

    public void setVisita2(int visita2) {
        this.visita2 = visita2;
    }

    public String getTelefono1() {
        return telefono1;
    }

    public void setTelefono1(String telefono1) {
        this.telefono1 = telefono1;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public boolean isPrecioVolumen() {
        return precioVolumen;
    }

    public int getAtraso() {
        return atraso;
    }

    public void setAtraso(int atraso) {
        this.atraso = atraso;
    }

    public int getMaxPago() {
        return maxPago;
    }

    public void setMaxPago(int maxPago) {
        this.maxPago = maxPago;
    }
}
