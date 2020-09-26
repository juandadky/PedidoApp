package com.warsoft.pedidoapp.Service.Model;

import java.util.List;

public class RespuestaGeneral {
    private List<FilaPedido> result;
    private int code;
    private String status;


    public RespuestaGeneral(List<FilaPedido> result, int code, String status) {
        this.result = result;
        this.code = code;
        this.status = status;
    }

    public RespuestaGeneral(int code, String status) {
        result = null;
        this.code = code;
        this.status = status;
    }

    public List<FilaPedido> getResult() {
        return result;
    }

    public void setResult(List<FilaPedido> result) {
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
