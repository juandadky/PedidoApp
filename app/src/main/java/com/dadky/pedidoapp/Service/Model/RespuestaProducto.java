package com.dadky.pedidoapp.Service.Model;

import com.dadky.pedidoapp.Local.Producto;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaProducto {
    private int code;
    @SerializedName("status")
    private String message;
    private List<Producto> result;

    public RespuestaProducto(int code, String message, List<Producto> result) {
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

    public List<Producto> getResult() {
        return result;
    }

}
