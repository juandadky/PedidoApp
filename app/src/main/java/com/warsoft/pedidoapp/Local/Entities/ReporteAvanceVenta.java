package com.warsoft.pedidoapp.Local.Entities;

import java.util.Calendar;

public class ReporteAvanceVenta {
    private String titulo;
    private Calendar fechaInicio;
    private Calendar fechaFinal;
    private String categoria;
    private AvanceVenta detalle;

    public ReporteAvanceVenta(String titulo, Calendar fechaInicio, Calendar fechaFinal, String categoria, AvanceVenta detalle) {
        this.titulo = titulo;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.categoria = categoria;
        this.detalle = detalle;
    }

    public String getTitulo() {
        return titulo;
    }

    public Calendar getFechaInicio() {
        return fechaInicio;
    }

    public Calendar getFechaFinal() {
        return fechaFinal;
    }

    public String getCategoria() {
        return categoria;
    }

    public AvanceVenta getDetalle() {
        return detalle;
    }
}
