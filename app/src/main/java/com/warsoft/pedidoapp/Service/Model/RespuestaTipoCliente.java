package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaTipoCliente {

    private int code;
    @SerializedName("status")
    private String message;
    private List<TipoCliente> result;

    public RespuestaTipoCliente(int code, String message, List<TipoCliente> result) {
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

    public List<TipoCliente> getResult() {
        return result;
    }
}
