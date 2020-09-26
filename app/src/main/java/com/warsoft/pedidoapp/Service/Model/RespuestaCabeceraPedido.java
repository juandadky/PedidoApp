package com.warsoft.pedidoapp.Service.Model;

import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaCabeceraPedido {

    private int code;
    @SerializedName("status")
    private String message;
    private List<CabeceraPedido> result;

    public RespuestaCabeceraPedido(int code, String message, List<CabeceraPedido> result) {
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

    public List<CabeceraPedido> getResult() {
        return result;
    }
}
