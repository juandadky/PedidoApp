package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;
import com.warsoft.pedidoapp.Local.Entities.Zona;

import java.util.List;

public class RespuestaZona {
    private int code;
    @SerializedName("status")
    private String message;
    private List<Zona> result;

    public RespuestaZona(int code, String message, List<Zona> result) {
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

    public List<Zona> getResult() {
        return result;
    }
}
