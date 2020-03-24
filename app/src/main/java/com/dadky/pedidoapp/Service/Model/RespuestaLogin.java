package com.dadky.pedidoapp.Service.Model;

import com.dadky.pedidoapp.Local.Usuario;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaLogin {
    private int code;
    @SerializedName("status")
    private String message;
    private List<Usuario> result;

    public RespuestaLogin(int code, String message, List<Usuario> result) {
        this.code = code;
        this.message = message;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Usuario> getResult() {
        return result;
    }

    public void setResult(List<Usuario> result) {
        this.result = result;
    }
}
