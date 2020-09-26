package com.warsoft.pedidoapp.Service;

import com.warsoft.pedidoapp.Service.Model.RespuestaAnular;
import com.warsoft.pedidoapp.Service.Model.RespuestaAtrasoCredito;
import com.warsoft.pedidoapp.Service.Model.RespuestaAvanceVentas;
import com.warsoft.pedidoapp.Service.Model.RespuestaCabeceraDescuento;
import com.warsoft.pedidoapp.Service.Model.RespuestaCabeceraPedido;
import com.warsoft.pedidoapp.Service.Model.RespuestaCategoria;
import com.warsoft.pedidoapp.Service.Model.RespuestaCliente;
import com.warsoft.pedidoapp.Service.Model.RespuestaDetallePedido;
import com.warsoft.pedidoapp.Service.Model.RespuestaDeudasPendientes;
import com.warsoft.pedidoapp.Service.Model.RespuestaGeneral;
import com.warsoft.pedidoapp.Service.Model.RespuestaLogin;
import com.warsoft.pedidoapp.Service.Model.RespuestaProducto;
import com.warsoft.pedidoapp.Service.Model.RespuestaTipoCliente;
import com.warsoft.pedidoapp.Service.Model.RespuestaTipoNegocio;
import com.warsoft.pedidoapp.Service.Model.RespuestaTipoPrecio;
import com.warsoft.pedidoapp.Service.Model.RespuestaZona;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IServiceApi {

    @FormUrlEncoded
    @POST("usuario/login")
    Call<RespuestaLogin> login(@Field("usuario")String usuario,
                               @Field("password") String password);

    @GET("productos/nombre/{nombProducto}/{tipoCliente}")
    Call<RespuestaProducto>obtenerProductoPorNombre(@Path("nombProducto")String nombProducto,@Path("tipoCliente")int tipoCliente);

    @GET("productos/id/{idProducto}/{tipoCliente}")
    Call<RespuestaProducto>obtenerProductoPorCodigo(@Path("idProducto")int idProducto,@Path("tipoCliente")int tipoCliente);

    @GET("clientes/nombre/{nomBuscar}")
    Call<RespuestaCliente>obtenerClientePorNombre(@Path("nomBuscar")String nomBuscar);

    @GET("clientes/nombre/{dniRuc}")
    Call<RespuestaCliente>obtenerClientePorDniRuc(@Path("dniRuc")String dniRuc);

    @PUT("anularPedido/{idPedido}")
    Call<RespuestaAnular>anularPedido(@Path("idPedido") int idPedido);

    @GET("obtenerCabeceraPedidoPorFacturar/idvendedor/{idVendedor}")
    Call<RespuestaCabeceraPedido>obtenerCabeceraPedidoSegunVendedorPorFacturar(@Path("idVendedor") int idVendedor);

    @GET("obtenerCabeceraPedidoFacturadoAnulado/idvendedor/{idVendedor}/{estado}")
    Call<RespuestaCabeceraPedido>obtenerCabeceraPedidoSegunVendedorFacturadoAnulado(@Path("idVendedor") int idVendedor,@Path("estado") String estado);

    @GET("obtenerDetallePedido/idPedido/{idPedido}")
    Call<RespuestaDetallePedido>obtenerDetallePedido(@Path("idPedido") int idPedido);

    @FormUrlEncoded
    @POST("registroCabeceraPedido")
    Call<RespuestaGeneral> registroCabeceraPedido(@Field("idCliente") int idCliente, @Field("idVendedor") int idVendedor, @Field("fechaPedido") String fechaPedido,
                                                  @Field("tipoVenta") int tipoVenta, @Field("idTransporte") int idTransporte, @Field("idZona") int idZona,
                                                  @Field("nroDias")int nroDias, @Field("porceIgv") double porceIgv, @Field("facBolNP") int facBolNp,
                                                  @Field("total") double total, @Field("idUsu") int idUsu, @Field("clave") int clave);

    @FormUrlEncoded
    @POST("registroDetallePedido")
    Call<RespuestaGeneral> registroDetallePedido(@Field("idPedido") int idPedido,@Field("idProducto") int idProducto, @Field("precioUnidad") double precioUnidad,
                                                 @Field("cantidad") double cantidad, @Field("entregado") int entregado, @Field("unidades") int unidades,
                                                 @Field("almacen") int almacen,@Field("descuento") double descuento,@Field("importe") double importe, @Field("subtotal") double subtotal,
                                                 @Field("porDescu") double porDescu,@Field("factor") int factor, @Field("tipoPrecio") String tipoPrecio, @Field("porcIgv") double porcIgv,
                                                 @Field("valorVenta") double valorVenta,@Field("costo") double costo, @Field("idUsu") int idUsu,
                                                 @Field("combo") int combo, @Field("idCombo") int idCombo, @Field("tipoCombo") int tipoCombo, @Field("surtido") boolean surtido,
                                                 @Field("cantidadSoles") int cantidadSoles, @Field("tipoPrecioVol") int tipoPrecioVol);

    @GET("obtenerPagoCredito/{idCliente}")
    Call<RespuestaAtrasoCredito> obtenerMaxPagoCredito(@Path("idCliente") int idCliente);

    @GET("obtenerAtraso/{idCliente}")
    Call<RespuestaAtrasoCredito> obtenerAtrasoCliente(@Path("idCliente") int idCliente);

    @GET("obtenerPagosPendientes/{idCliente}/{fechaInicio}/{fechaFinal}")
    Call<RespuestaDeudasPendientes> obtenerDeudasPendientes(@Path("idCliente") int idCliente,@Path("fechaInicio") String fechaInicio,
                                                            @Path("fechaFinal") String fechaFinal);

    @FormUrlEncoded
    @POST("registrarPagoPendiente")
    Call<RespuestaGeneral> registrarPago(@Field("fecha") String fecha, @Field("idCliente") int idCliente, @Field("idDocumento") int idDocumento,
                                         @Field("serie") String serie, @Field("numero") int numero, @Field("tipoDoc") int tipoDoc,
                                         @Field("importe") double importe, @Field("idUsuario") int idUsuario);

    @FormUrlEncoded
    @POST("registrarCliente")
    Call<RespuestaGeneral> registrarCliente(@Field("nombre") String nombre, @Field("direccion") String direccion, @Field("docIden") String docIden,
                                            @Field("ruc") String ruc, @Field("telefono") String telefono, @Field("fPago") String fPago,
                                            @Field("idZona") int idZona, @Field("tipNeg") int tipNeg, @Field("referencia") String referencia,
                                            @Field("boletear") String boletear, @Field("tipoCliente") String tipoCliente, @Field("celular") String celular,
                                            @Field("visita") int visita, @Field("lCredito") double lCredito, @Field("ctaDetra") String ctaDetra,
                                            @Field("idDistrito") int idDistrito, @Field("idVendedor") int idVendedor, @Field("cliProv") int cliProv,
                                            @Field("idVendedor2") int idVendedor2, @Field("idZona2") int idZona2, @Field("visita2") int visita2,
                                            @Field("lCredito2") double lCredito2, @Field("zona") String zona, @Field("email") String email,
                                            @Field("lista") String lista,@Field("preciovolumen") boolean preciovolumen);

    @GET("obtenerZona")
    Call<RespuestaZona> obtenerZona();

    @GET("obtenerTipoNegocio")
    Call<RespuestaTipoNegocio> obtenerTipoNegocio();

    @GET("obtenerTipodePrecio")
    Call<RespuestaTipoPrecio> obtenerTipoPrecio();

    @PUT("actualizarCabeceraPedido/{idPedido}/{total}")
    Call<RespuestaGeneral> actualizarCabeceraPedido(@Path("idPedido") int idPedido, @Path("total") double total);


    @PUT("actualizarDetallePedido/{idPedido}/{idProducto}/{cantidad}/{unidades}/{subtotal}/{importe}")
    Call<RespuestaGeneral> actualizarDetallePedido(@Path("idPedido") int idPedido,@Path("idProducto") int idProducto, @Path("cantidad") double cantidad,
                                                   @Path("unidades") int unidades,@Path("subtotal") double subtotal,@Path("importe") double importe);

    @DELETE("eliminarDetallePedido/{idPedido}/{idProducto}")
    Call<RespuestaGeneral> eliminarDetallePedido(@Path("idPedido") int idPedido, @Path("idProducto") int idProducto);

    @GET("tipoCliente/{idCliente}")
    Call<RespuestaCliente>obtenerTipoCliente(@Path("idCliente") int idCliente);

    @GET("obtenerCategoria")
    Call<RespuestaCategoria> obtenerCategoria();

    @GET("obtenerAvanceVentas/{idVendedor}/{fechaInicio}/{fechaFinal}/{tipo}/{linea}/{sublinea}/{fuerza}")
    Call<RespuestaAvanceVentas> obtenerAvanceVentas(@Path("idVendedor")int idVendedor,@Path("fechaInicio")String fechaInicio,@Path("fechaFinal") String fechaFinal,
                                                    @Path("tipo") int tipo, @Path("linea") String linea,@Path("sublinea") String sublinea,@Path("fuerza") int fuerza);

    @GET("obtenerCabeceraDescuento/{idProducto}/{fecha}/{tipo}")
    Call<RespuestaCabeceraDescuento> obtenerCabeceraDescuento(@Path("idProducto") int producto, @Path("fecha") String fecha, @Path("tipo") int tipo);

    @GET("obtenerPromoDescuento/id/{idProducto}/{tipoCliente}")
    Call<RespuestaProducto> obtenerPromoDescuentoPorId(@Path("idProducto") int producto, @Path("tipoCliente") int tipo);

    @GET("obtenerPromoDescuento/nombre/{nombProducto}/{tipoCliente}")
    Call<RespuestaProducto> obtenerPromoDescuentoPorNombre(@Path("nombProducto") String nombProducto, @Path("tipoCliente") int tipo);

    @PUT("actualizarCliente/{idCliente}/{direccion}/{referencia}/{telefono}/{celular}")
    Call<RespuestaGeneral> actualizarCliente(@Path("idCliente") int idCliente,@Path("direccion") String direccion,@Path("referencia") String referencia,
                                             @Path("telefono") String telefono, @Path("celular") String celular);

}
