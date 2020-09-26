package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;
import com.warsoft.pedidoapp.Local.Entities.AvanceVenta;

import java.util.List;

public class RespuestaAvanceVentas {
    private int code;
    @SerializedName("status")
    private String message;
    private List<AvanceVenta> result;

    public RespuestaAvanceVentas(int code, String message, List<AvanceVenta> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<AvanceVenta> getResult() {
        return result;
    }
}
