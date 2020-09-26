package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;
import com.warsoft.pedidoapp.Local.Entities.TipoNegocio;

import java.util.List;

public class RespuestaTipoNegocio {

    private int code;
    @SerializedName("status")
    private String message;
    private List<TipoNegocio> result;

    public RespuestaTipoNegocio(int code, String message, List<TipoNegocio> result) {
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

    public List<TipoNegocio> getResult() {
        return result;
    }
}
