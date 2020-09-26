package com.warsoft.pedidoapp.Local.Entities;

public class TipoNegocio {

    private int idTipoNeg;
    private String codigo;
    private String detalle;
    private int tipo;

    public TipoNegocio(int idTipoNeg, String codigo, String detalle, int tipo) {
        this.idTipoNeg = idTipoNeg;
        this.codigo = codigo;
        this.detalle = detalle;
        this.tipo = tipo;
    }

    public int getIdTipoNeg() {
        return idTipoNeg;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDetalle() {
        return detalle;
    }

    public int getTipo() {
        return tipo;
    }
}
