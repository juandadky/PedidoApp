package com.warsoft.pedidoapp.Service.Model;

import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaCliente {
    private int code;
    @SerializedName("status")
    private String message;
    private List<Cliente> result;

    public RespuestaCliente(int code, String message, List<Cliente> result) {
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

    public List<Cliente> getResult() {
        return result;
    }
}
