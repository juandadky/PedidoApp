package com.warsoft.pedidoapp.Service.Model;

import com.warsoft.pedidoapp.Local.Entities.DeudaPendiente;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RespuestaDeudasPendientes {
    private int code;
    @SerializedName("status")
    private String message;
    private List<DeudaPendiente> result;

    public RespuestaDeudasPendientes(int code, String message, List<DeudaPendiente> result) {
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

    public List<DeudaPendiente> getResult() {
        return result;
    }
}
