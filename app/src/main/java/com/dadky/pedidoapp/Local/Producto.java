package com.dadky.pedidoapp.Local;

import com.google.gson.annotations.SerializedName;

public class Producto {

    @SerializedName("idproducto")
    private int idProducto;
    @SerializedName("NombreProducto")
    private String nombreProducto;
    @SerializedName("idproveedor")
    private int idProveedor;
    @SerializedName("idcategoria")
    private int idCategoria;
    @SerializedName("CantidadPorUnidad")
    private String cantidadPorUnidad;
    @SerializedName("Almacen")
    private int almacen;
    private int factor;
    @SerializedName("saldoreal")
    private int saldoReal;
    @SerializedName("Cantidad")
    private int cantidad;
    private int unidades;
    @SerializedName("Saldo")
    private int saldo;
    @SerializedName("saldoconta")
    private int saldoConta;
    @SerializedName("UnidadesEnPedido")
    private int unidadesEnPedido;
    private double precio1;
    private double precio2;
    private double precio3;
    private double precio4;
    private double precio5;
    private double precio6;
    private double precio7;
    private double costo;
    @SerializedName("Preciod1")
    private double preciod1;
    @SerializedName("Preciod2")
    private double preciod2;
    @SerializedName("Preciod3")
    private double preciod3;
    @SerializedName("Preciod4")
    private double preciod4;
    @SerializedName("Preciod5")
    private double preciod5;
    @SerializedName("Preciod6")
    private double preciod6;
    @SerializedName("Preciod7")
    private double preciod7;
    @SerializedName("Lote_pedido")
    private String lotePedido;
    @SerializedName("Afecto_Igv")
    private boolean afectoIgv;
    @SerializedName("por_perce")
    private double porPerce;
    @SerializedName("Categoria")
    private String categoria;
    @SerializedName("Proveedor")
    private String proveedor;
    @SerializedName("Minimo")
    private int minimo;
    private double peso;
    private double litros;
    @SerializedName("nombremarca")
    private String nombreMarca;
    @SerializedName("idmarca")
    private int idMarca;
    @SerializedName("Linea")
    private String linea;
    @SerializedName("SubLinea")
    private String subLinea;
    @SerializedName("CostoProv")
    private String costoProv;
    private int idConta;
    @SerializedName("Precio8")
    private double precio8;
    private double precio9;
    private double precio10;
    private double precio11;
    private double precio12;
    private double preciod8;
    private double preciod9;
    private double preciod10;
    private double preciod11;
    private double preciod12;
    private int canvolmer;
    private int canvolmay;

    public Producto(int idProducto, String nombreProducto, int idProveedor, int idCategoria, String cantidadPorUnidad, int almacen, int factor, int saldoReal, int cantidad, int unidades, int saldo, int saldoConta, int unidadesEnPedido, double precio1, double precio2, double precio3, double precio4, double precio5, double precio6, double precio7, double costo, double preciod1, double preciod2, double preciod3, double preciod4, double preciod5, double preciod6, double preciod7, String lotePedido, boolean afectoIgv, double porPerce, String categoria, String proveedor, int minimo, double peso, double litros, String nombreMarca, int idMarca, String linea, String subLinea, String costoProv, int idConta, double precio8, double precio9, double precio10, double precio11, double precio12, double preciod8, double preciod9, double preciod10, double preciod11, double preciod12, int canvolmer, int canvolmay) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.idProveedor = idProveedor;
        this.idCategoria = idCategoria;
        this.cantidadPorUnidad = cantidadPorUnidad;
        this.almacen = almacen;
        this.factor = factor;
        this.saldoReal = saldoReal;
        this.cantidad = cantidad;
        this.unidades = unidades;
        this.saldo = saldo;
        this.saldoConta = saldoConta;
        this.unidadesEnPedido = unidadesEnPedido;
        this.precio1 = precio1;
        this.precio2 = precio2;
        this.precio3 = precio3;
        this.precio4 = precio4;
        this.precio5 = precio5;
        this.precio6 = precio6;
        this.precio7 = precio7;
        this.costo = costo;
        this.preciod1 = preciod1;
        this.preciod2 = preciod2;
        this.preciod3 = preciod3;
        this.preciod4 = preciod4;
        this.preciod5 = preciod5;
        this.preciod6 = preciod6;
        this.preciod7 = preciod7;
        this.lotePedido = lotePedido;
        this.afectoIgv = afectoIgv;
        this.porPerce = porPerce;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.minimo = minimo;
        this.peso = peso;
        this.litros = litros;
        this.nombreMarca = nombreMarca;
        this.idMarca = idMarca;
        this.linea = linea;
        this.subLinea = subLinea;
        this.costoProv = costoProv;
        this.idConta = idConta;
        this.precio8 = precio8;
        this.precio9 = precio9;
        this.precio10 = precio10;
        this.precio11 = precio11;
        this.precio12 = precio12;
        this.preciod8 = preciod8;
        this.preciod9 = preciod9;
        this.preciod10 = preciod10;
        this.preciod11 = preciod11;
        this.preciod12 = preciod12;
        this.canvolmer = canvolmer;
        this.canvolmay = canvolmay;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public int getIdProveedor() {
        return idProveedor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public String getCantidadPorUnidad() {
        return cantidadPorUnidad;
    }

    public int getAlmacen() {
        return almacen;
    }

    public int getFactor() {
        return factor;
    }

    public int getSaldoReal() {
        return saldoReal;
    }

    public int getCantidad() {
        return cantidad;
    }

    public int getUnidades() {
        return unidades;
    }

    public int getSaldo() {
        return saldo;
    }

    public int getSaldoConta() {
        return saldoConta;
    }

    public int getUnidadesEnPedido() {
        return unidadesEnPedido;
    }

    public double getPrecio1() {
        return precio1;
    }

    public double getPrecio2() {
        return precio2;
    }

    public double getPrecio3() {
        return precio3;
    }

    public double getPrecio4() {
        return precio4;
    }

    public double getPrecio5() {
        return precio5;
    }

    public double getPrecio6() {
        return precio6;
    }

    public double getPrecio7() {
        return precio7;
    }

    public double getCosto() {
        return costo;
    }

    public double getPreciod1() {
        return preciod1;
    }

    public double getPreciod2() {
        return preciod2;
    }

    public double getPreciod3() {
        return preciod3;
    }

    public double getPreciod4() {
        return preciod4;
    }

    public double getPreciod5() {
        return preciod5;
    }

    public double getPreciod6() {
        return preciod6;
    }

    public double getPreciod7() {
        return preciod7;
    }

    public String getLotePedido() {
        return lotePedido;
    }

    public boolean isAfectoIgv() {
        return afectoIgv;
    }

    public double getPorPerce() {
        return porPerce;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getProveedor() {
        return proveedor;
    }

    public int getMinimo() {
        return minimo;
    }

    public double getPeso() {
        return peso;
    }

    public double getLitros() {
        return litros;
    }

    public String getNombreMarca() {
        return nombreMarca;
    }

    public int getIdMarca() {
        return idMarca;
    }

    public String getLinea() {
        return linea;
    }

    public String getSubLinea() {
        return subLinea;
    }

    public String getCostoProv() {
        return costoProv;
    }

    public int getIdConta() {
        return idConta;
    }

    public double getPrecio8() {
        return precio8;
    }

    public double getPrecio9() {
        return precio9;
    }

    public double getPrecio10() {
        return precio10;
    }

    public double getPrecio11() {
        return precio11;
    }

    public double getPrecio12() {
        return precio12;
    }

    public double getPreciod8() {
        return preciod8;
    }

    public double getPreciod9() {
        return preciod9;
    }

    public double getPreciod10() {
        return preciod10;
    }

    public double getPreciod11() {
        return preciod11;
    }

    public double getPreciod12() {
        return preciod12;
    }

    public int getCanvolmer() {
        return canvolmer;
    }

    public int getCanvolmay() {
        return canvolmay;
    }
}
