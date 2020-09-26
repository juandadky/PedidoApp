package com.warsoft.pedidoapp.Service.Model;

public class RespuestaAnular {
    private int code;
    private String status;

    public RespuestaAnular(int code, String status) {
        this.code = code;
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
