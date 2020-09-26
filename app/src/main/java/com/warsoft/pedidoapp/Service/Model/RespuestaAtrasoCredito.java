package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaAtrasoCredito {
    private int code;
    @SerializedName("status")
    private String message;
    private List<Atraso> result;

    public RespuestaAtrasoCredito(int code, String message, List<Atraso> result) {
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

    public List<Atraso> getResult() {
        return result;
    }
}
