package com.warsoft.pedidoapp.Service.Model;

import com.warsoft.pedidoapp.Local.Entities.DetallePedido;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaDetallePedido {

    private int code;
    @SerializedName("status")
    private String message;
    private List<DetallePedido> result;

    public RespuestaDetallePedido(int code, String message, List<DetallePedido> result) {
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

    public List<DetallePedido> getResult() {
        return result;
    }
}
