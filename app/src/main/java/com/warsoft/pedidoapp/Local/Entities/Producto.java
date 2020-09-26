package com.warsoft.pedidoapp.Local.Entities;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class Producto implements Serializable {
        private int idProducto;

        @SerializedName("NombreProducto")
        private String nombreProducto;

        @SerializedName("IdProveedor")
        private int idProveedor;

        @SerializedName("IdCategoria")
        private int idCategoria;

        @SerializedName("CantidadPorUnidad")
        private String cantidadPorUnidad;

        @SerializedName("Almacen")
        private int almacen;

        @SerializedName("Cantidad")
        private int cantidad;

        private int unidades;

        private int saldoconta;

        private int saldoreal;

        @SerializedName("UnidadesEnPedido")
        private int unidadesEnPedido;

        private double preciocon;

        private double preciocre;

        @SerializedName("PrecioVol")
        private double precioVol;

        @SerializedName("PrecioMay")
        private double precioMay;

        @SerializedName("PrecioMay2")
        private double precioMay2;

        @SerializedName("por_perce")
        private int porPerce;

        private double precio5;

        private double precio6;

        private double precio7;

        @SerializedName("Preciod1")
        private double preciod1;

        @SerializedName("Preciod2")
        private double preciod2;

    @SerializedName("Preciod3")
    private double preciod3;

    @SerializedName("Preciod4")
        private double preciod4;

        private double costo;

    @SerializedName("Preciod5")
        private double preciod5;

    @SerializedName("Preciod6")
        private double preciod6;

    @SerializedName("Preciod7")
        private double preciod7;

        private int factor;

        @SerializedName("Lote_pedido")
        private String lotePedido;

        @SerializedName("fec_vcmto")
        private String fecVcmto;

        @SerializedName("Afecto_Igv")
        private boolean afectoIgv;

        private List<String> codprov;

        @SerializedName("Categoria")
        private  String categoria;

        @SerializedName("Proveedor")
        private String proveedor;

        @SerializedName("Suspendido")
        private boolean suspendido;

        @SerializedName("Minimo")
        private int minimo;

        @SerializedName("Percepcion")
        private boolean percepcion;

        private double peso;

        private double litros;

        private String nombremarca;

        private int idmarca;

        @SerializedName("Linea")
        private String linea;

        @SerializedName("SubLinea")
        private String subLinea;

        @SerializedName("CostoProv")
        private double costoProv;


        private int idConta;

        @SerializedName("UniMin")
        private String uniMin;

        @SerializedName("Grupo")
        private String grupo;

        @SerializedName("subGrupo")
        private String subGrupo;

        private int unidadesenpedido;

        private String ean;

        private String unicolgate;

        private int faccolgate;

        private boolean controlaboni;

        private boolean combo;

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

        private int cantidadvol;

        private String vendedores;

        private String zonas;

        @SerializedName("tiponegocio")
        private String tipoNegocio;

        @SerializedName("canti_max")
        private double cantiMax;

        private double pordes;

        private boolean surtido;

        private String tipoPrecio;

        private String desde;

        private String hasta;

        private int nroitems;

        private int tipocombo;

        private double preciovol;

        private double preciomay;

        private double cantidadmay;

        private double preciomay2;

        private double cantidadmay2;

        private int cantidadPedido;

        private int unidadesPedido;

        private int totalunidadesPedido;

        private double total;

        private int idCombo;

        private double descuento;

        private boolean bonificacion;

        private int cantidadSoles;

        private int tipoprecioventa;

    public Producto(int idProducto, String nombreProducto, int idProveedor, int idCategoria, String cantidadPorUnidad, int almacen, int cantidad,
                    int unidades, int saldoconta, int saldoreal, int unidadesEnPedido, double preciocon, double preciocre, double precioVol,
                    double precioMay, double precioMay2, int porPerce, double precio5, double precio6, double precio7, double preciod1, double preciod2,
                    double preciod3, double preciod4, double costo, double preciod5, double preciod6, double preciod7, int factor, String lotePedido,
                    String fecVcmto, boolean afectoIgv, List<String> codprov, String categoria, String proveedor, boolean suspendido, int minimo,
                    boolean percepcion, double peso, double litros, String nombremarca, int idmarca, String linea, String subLinea, double costoProv,
                    int idConta, String uniMin, String grupo, String subGrupo, int unidadesenpedido, String ean, String unicolgate, int faccolgate,
                    boolean controlaboni, boolean combo, double precio8, double precio9, double precio10, double precio11, double precio12,
                    double preciod8, double preciod9, double preciod10, double preciod11, double preciod12, int canvolmer, int canvolmay, int cantidadvol,
                    String vendedores, String zonas, String tipoNegocio, double cantiMax, double pordes, boolean surtido, String desde,
                    String hasta, int nroitems, int tipocombo, double preciovol, double preciomay, double cantidadmay, double preciomay2,
                    double cantidadmay2) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.idProveedor = idProveedor;
        this.idCategoria = idCategoria;
        this.cantidadPorUnidad = cantidadPorUnidad;
        this.almacen = almacen;
        this.cantidad = cantidad;
        this.unidades = unidades;
        this.saldoconta = saldoconta;
        this.saldoreal = saldoreal;
        this.unidadesEnPedido = unidadesEnPedido;
        this.preciocon = preciocon;
        this.preciocre = preciocre;
        this.precioVol = precioVol;
        this.precioMay = precioMay;
        this.precioMay2 = precioMay2;
        this.porPerce = porPerce;
        this.precio5 = precio5;
        this.precio6 = precio6;
        this.precio7 = precio7;
        this.preciod1 = preciod1;
        this.preciod2 = preciod2;
        this.preciod3 = preciod3;
        this.preciod4 = preciod4;
        this.costo = costo;
        this.preciod5 = preciod5;
        this.preciod6 = preciod6;
        this.preciod7 = preciod7;
        this.factor = factor;
        this.lotePedido = lotePedido;
        this.fecVcmto = fecVcmto;
        this.afectoIgv = afectoIgv;
        this.codprov = codprov;
        this.categoria = categoria;
        this.proveedor = proveedor;
        this.suspendido = suspendido;
        this.minimo = minimo;
        this.percepcion = percepcion;
        this.peso = peso;
        this.litros = litros;
        this.nombremarca = nombremarca;
        this.idmarca = idmarca;
        this.linea = linea;
        this.subLinea = subLinea;
        this.costoProv = costoProv;
        this.idConta = idConta;
        this.uniMin = uniMin;
        this.grupo = grupo;
        this.subGrupo = subGrupo;
        this.unidadesenpedido = unidadesenpedido;
        this.ean = ean;
        this.unicolgate = unicolgate;
        this.faccolgate = faccolgate;
        this.controlaboni = controlaboni;
        this.combo = combo;
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
        this.cantidadvol = cantidadvol;
        this.vendedores = vendedores;
        this.zonas = zonas;
        this.tipoNegocio = tipoNegocio;
        this.cantiMax = cantiMax;
        this.pordes = pordes;
        this.surtido = surtido;
        this.preciovol = preciovol;
        this.preciomay = preciomay;
        this.cantidadmay = cantidadmay;
        this.preciomay2 = preciomay2;
        this.cantidadmay2 = cantidadmay2;
        this.desde = desde;
        this.hasta = hasta;
        this.nroitems = nroitems;
        this.tipocombo = tipocombo;
        this.tipoPrecio = " ";
        this.cantidadPedido = 0;
        this.unidadesPedido = 0;
        this.totalunidadesPedido = 0;
        this.total = 0;
        this.idCombo = 0;
        this.descuento = 0;
        this.bonificacion = false;
        this.cantidadSoles = 0;
        this.tipoprecioventa = 0;
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

        public int getCantidad() {
            return cantidad;
        }

        public int getUnidades() {
            return unidades;
        }

        public int getSaldoconta() {
            return saldoconta;
        }

        public int getSaldoreal() {
            return saldoreal;
        }

        public int getUnidadesEnPedido() {
            return unidadesEnPedido;
        }

        public double getPreciocon() {
            return preciocon;
        }

        public double getPreciocre() {
            return preciocre;
        }

    public double getPrecioVol() {
        return precioVol;
    }

    public double getPrecioMay() {
            return precioMay;
        }

        public double getPrecioMay2() {
            return precioMay2;
        }

        public int getPorPerce() {
            return porPerce;
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

        public double getCosto() {
            return costo;
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

        public int getFactor() {
            return factor;
        }

        public String getLotePedido() {
            return lotePedido;
        }

        public String getFecVcmto() {
            return fecVcmto;
        }

        public boolean isAfectoIgv() {
            return afectoIgv;
        }

        public List<String> getCodprov() {
            return codprov;
        }

        public String getCategoria() {
            return categoria;
        }

        public String getProveedor() {
            return proveedor;
        }

        public boolean isSuspendido() {
            return suspendido;
        }

        public int getMinimo() {
            return minimo;
        }

        public boolean isPercepcion() {
            return percepcion;
        }

        public double getPeso() {
            return peso;
        }

        public double getLitros() {
            return litros;
        }

        public String getNombremarca() {
            return nombremarca;
        }

        public int getIdmarca() {
            return idmarca;
        }

        public String getLinea() {
            return linea;
        }

        public String getSubLinea() {
            return subLinea;
        }

        public double getCostoProv() {
            return costoProv;
        }

        public int getIdConta() {
            return idConta;
        }

        public String getUniMin() {
            return uniMin;
        }

        public String getGrupo() {
            return grupo;
        }

        public String getSubGrupo() {
            return subGrupo;
        }

        public int getUnidadesenpedido() {
            return unidadesenpedido;
        }

        public String getEan() {
            return ean;
        }

        public String getUnicolgate() {
            return unicolgate;
        }

        public int getFaccolgate() {
            return faccolgate;
        }

        public boolean isControlaboni() {
            return controlaboni;
        }
        public boolean isCombo() {
            return combo;
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

    public int getCantidadvol() {
        return cantidadvol;
    }

    public String getVendedores() {
        return vendedores;
    }

    public String getZonas() {
        return zonas;
    }

    public String getTipoNegocio() {
        return tipoNegocio;
    }

    public double getCantiMax() {
        return cantiMax;
    }

    public double getPordes() {
        return pordes;
    }

    public String getDesde() {
        return desde;
    }

    public String getHasta() {
        return hasta;
    }

    public int getNroitems() {
        return nroitems;
    }

    public int getTipocombo() {
        return tipocombo;
    }

    public boolean isSurtido() {
        return surtido;
    }

    public int getCantidadPedido() {
        return cantidadPedido;
    }

    public void setCantidadPedido(int cantidadPedido) {
        this.cantidadPedido = cantidadPedido;
    }

    public int getUnidadesPedido() {
        return unidadesPedido;
    }

    public void setUnidadesPedido(int unidadesPedido) {
        this.unidadesPedido = unidadesPedido;
    }

    public int getTotalunidadesPedido() {
        return totalunidadesPedido;
    }

    public void setTotalunidadesPedido(int totalunidadesPedido) {
        this.totalunidadesPedido = totalunidadesPedido;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getIdCombo() {
        return idCombo;
    }

    public void setIdCombo(int idCombo) {
        this.idCombo = idCombo;
    }

    public void setCombo(boolean combo) {
        this.combo = combo;
    }

    public double getDescuento() {
        return descuento;
    }

    public void setDescuento(double descuento) {
        this.descuento = descuento;
    }

    public void setCantidadPorUnidad(String cantidadPorUnidad) {
        this.cantidadPorUnidad = cantidadPorUnidad;
    }

    public void setAlmacen(int almacen) {
        this.almacen = almacen;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public void setUnidades(int unidades) {
        this.unidades = unidades;
    }

    public void setSaldoconta(int saldoconta) {
        this.saldoconta = saldoconta;
    }

    public void setSaldoreal(int saldoreal) {
        this.saldoreal = saldoreal;
    }

    public void setUnidadesEnPedido(int unidadesEnPedido) {
        this.unidadesEnPedido = unidadesEnPedido;
    }

    public void setPreciocon(double preciocon) {
        this.preciocon = preciocon;
    }

    public void setPreciocre(double preciocre) {
        this.preciocre = preciocre;
    }

    public void setPrecioVol(double precioVol) {
        this.precioVol = precioVol;
    }

    public void setPrecioMay(double precioMay) {
        this.precioMay = precioMay;
    }

    public void setPrecioMay2(double precioMay2) {
        this.precioMay2 = precioMay2;
    }

    public void setPorPerce(int porPerce) {
        this.porPerce = porPerce;
    }

    public void setPrecio5(double precio5) {
        this.precio5 = precio5;
    }

    public void setPrecio6(double precio6) {
        this.precio6 = precio6;
    }

    public void setPrecio7(double precio7) {
        this.precio7 = precio7;
    }

    public void setPreciod1(double preciod1) {
        this.preciod1 = preciod1;
    }

    public void setPreciod2(double preciod2) {
        this.preciod2 = preciod2;
    }

    public void setPreciod3(double preciod3) {
        this.preciod3 = preciod3;
    }

    public void setPreciod4(double preciod4) {
        this.preciod4 = preciod4;
    }

    public void setCosto(double costo) {
        this.costo = costo;
    }

    public void setPreciod5(double preciod5) {
        this.preciod5 = preciod5;
    }

    public void setPreciod6(double preciod6) {
        this.preciod6 = preciod6;
    }

    public void setPreciod7(double preciod7) {
        this.preciod7 = preciod7;
    }

    public void setFactor(int factor) {
        this.factor = factor;
    }

    public void setLotePedido(String lotePedido) {
        this.lotePedido = lotePedido;
    }

    public void setFecVcmto(String fecVcmto) {
        this.fecVcmto = fecVcmto;
    }

    public void setAfectoIgv(boolean afectoIgv) {
        this.afectoIgv = afectoIgv;
    }

    public void setCodprov(List<String> codprov) {
        this.codprov = codprov;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public void setSuspendido(boolean suspendido) {
        this.suspendido = suspendido;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public void setPercepcion(boolean percepcion) {
        this.percepcion = percepcion;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public void setLitros(double litros) {
        this.litros = litros;
    }

    public void setNombremarca(String nombremarca) {
        this.nombremarca = nombremarca;
    }

    public void setIdmarca(int idmarca) {
        this.idmarca = idmarca;
    }

    public void setLinea(String linea) {
        this.linea = linea;
    }

    public void setSubLinea(String subLinea) {
        this.subLinea = subLinea;
    }

    public void setCostoProv(double costoProv) {
        this.costoProv = costoProv;
    }

    public void setIdConta(int idConta) {
        this.idConta = idConta;
    }

    public void setUniMin(String uniMin) {
        this.uniMin = uniMin;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public void setSubGrupo(String subGrupo) {
        this.subGrupo = subGrupo;
    }

    public void setUnidadesenpedido(int unidadesenpedido) {
        this.unidadesenpedido = unidadesenpedido;
    }

    public void setEan(String ean) {
        this.ean = ean;
    }

    public void setUnicolgate(String unicolgate) {
        this.unicolgate = unicolgate;
    }

    public void setFaccolgate(int faccolgate) {
        this.faccolgate = faccolgate;
    }

    public void setControlaboni(boolean controlaboni) {
        this.controlaboni = controlaboni;
    }

    public void setPrecio8(double precio8) {
        this.precio8 = precio8;
    }

    public void setPrecio9(double precio9) {
        this.precio9 = precio9;
    }

    public void setPrecio10(double precio10) {
        this.precio10 = precio10;
    }

    public void setPrecio11(double precio11) {
        this.precio11 = precio11;
    }

    public void setPrecio12(double precio12) {
        this.precio12 = precio12;
    }

    public void setPreciod8(double preciod8) {
        this.preciod8 = preciod8;
    }

    public void setPreciod9(double preciod9) {
        this.preciod9 = preciod9;
    }

    public void setPreciod10(double preciod10) {
        this.preciod10 = preciod10;
    }

    public void setPreciod11(double preciod11) {
        this.preciod11 = preciod11;
    }

    public void setPreciod12(double preciod12) {
        this.preciod12 = preciod12;
    }

    public void setCanvolmer(int canvolmer) {
        this.canvolmer = canvolmer;
    }

    public void setCanvolmay(int canvolmay) {
        this.canvolmay = canvolmay;
    }

    public void setCantidadvol(int cantidadvol) {
        this.cantidadvol = cantidadvol;
    }

    public void setVendedores(String vendedores) {
        this.vendedores = vendedores;
    }

    public void setZonas(String zonas) {
        this.zonas = zonas;
    }

    public void setTipoNegocio(String tipoNegocio) {
        this.tipoNegocio = tipoNegocio;
    }

    public void setCantiMax(double cantiMax) {
        this.cantiMax = cantiMax;
    }

    public void setPordes(double pordes) {
        this.pordes = pordes;
    }

    public void setSurtido(boolean surtido) {
        this.surtido = surtido;
    }

    public void setDesde(String desde) {
        this.desde = desde;
    }

    public void setHasta(String hasta) {
        this.hasta = hasta;
    }

    public void setNroitems(int nroitems) {
        this.nroitems = nroitems;
    }

    public void setTipocombo(int tipocombo) {
        this.tipocombo = tipocombo;
    }



    public String getTipoPrecio() {
        return tipoPrecio;
    }

    public void setTipoPrecio(String tipoPrecio) {
        this.tipoPrecio = tipoPrecio;
    }

    public boolean isBonificacion() {
        return bonificacion;
    }

    public void setBonificacion(boolean bonificacion) {
        this.bonificacion = bonificacion;
    }

    public double getPreciovol() {
        return preciovol;
    }

    public void setPreciovol(double preciovol) {
        this.preciovol = preciovol;
    }

    public double getPreciomay() {
        return preciomay;
    }

    public void setPreciomay(double preciomay) {
        this.preciomay = preciomay;
    }

    public double getCantidadmay() {
        return cantidadmay;
    }

    public void setCantidadmay(double cantidadmay) {
        this.cantidadmay = cantidadmay;
    }

    public double getPreciomay2() {
        return preciomay2;
    }

    public void setPreciomay2(double preciomay2) {
        this.preciomay2 = preciomay2;
    }

    public double getCantidadmay2() {
        return cantidadmay2;
    }

    public void setCantidadmay2(double cantidadmay2) {
        this.cantidadmay2 = cantidadmay2;
    }

    public int getCantidadSoles() {
        return cantidadSoles;
    }

    public void setCantidadSoles(int cantidadSoles) {
        this.cantidadSoles = cantidadSoles;
    }

    public int getTipoprecioventa() {
        return tipoprecioventa;
    }

    public void setTipoprecioventa(int tipoprecioventa) {
        this.tipoprecioventa = tipoprecioventa;
    }

    @NonNull
    @Override
    public String toString() {
        return "Producto: " + nombreProducto + "Bonificacion" + (bonificacion ? "SI" : "NO");
    }
}
