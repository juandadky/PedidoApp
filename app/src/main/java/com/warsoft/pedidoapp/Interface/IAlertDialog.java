package com.warsoft.pedidoapp.Interface;

import com.warsoft.pedidoapp.Local.Entities.Producto;

public interface IAlertDialog {
    void aceptarVol(final Producto producto, final int tipoPrecio, final int posicion);
    void negarVol(final Producto producto,int posicion);
}
