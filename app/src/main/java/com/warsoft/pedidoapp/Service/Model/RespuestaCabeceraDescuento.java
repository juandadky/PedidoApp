package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;
import com.warsoft.pedidoapp.Local.Entities.CabeceraDescuento;

import java.util.List;

public class RespuestaCabeceraDescuento {
    private int code;
    @SerializedName("status")
    private String message;
    private List<CabeceraDescuento> result;

    public RespuestaCabeceraDescuento(int code, String message, List<CabeceraDescuento> result) {
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

    public List<CabeceraDescuento> getResult() {
        return result;
    }
}
