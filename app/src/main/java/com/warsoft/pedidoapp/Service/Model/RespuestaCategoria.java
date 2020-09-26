package com.warsoft.pedidoapp.Service.Model;

import com.google.gson.annotations.SerializedName;
import com.warsoft.pedidoapp.Local.Entities.Categoria;

import java.util.List;

public class RespuestaCategoria {

    private int code;
    @SerializedName("status")
    private String message;
    private List<Categoria> result;

    public RespuestaCategoria(int code, String message, List<Categoria> result) {
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

    public List<Categoria> getResult() {
        return result;
    }
}
