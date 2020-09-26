package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;
import com.warsoft.pedidoapp.Local.Entities.TipoPrecio;

import java.util.List;

public class RespuestaTipoPrecio {
    private int code;
    @SerializedName("status")
    private String message;
    private List<TipoPrecio> result;

    public RespuestaTipoPrecio(int code, String message, List<TipoPrecio> result) {
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

    public List<TipoPrecio> getResult() {
        return result;
    }
}
