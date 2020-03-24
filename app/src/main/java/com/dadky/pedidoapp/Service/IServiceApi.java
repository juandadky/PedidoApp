package com.dadky.pedidoapp.Service;

import com.dadky.pedidoapp.Service.Model.RespuestaCliente;
import com.dadky.pedidoapp.Service.Model.RespuestaGeneral;
import com.dadky.pedidoapp.Service.Model.RespuestaLogin;
import com.dadky.pedidoapp.Service.Model.RespuestaProducto;

import retrofit2.Call;
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

    @GET("productos/nombre/{nombProducto}")
    Call<RespuestaProducto>obtenerProductoPorNombre(@Path("nombProducto")String nombProducto);

    @GET("productos/id/{idProducto}")
    Call<RespuestaProducto>obtenerProductoPorCodigo(@Path("idProducto")int idProducto);

    @GET("clientes/nombre/{nomBuscar}")
    Call<RespuestaCliente>obtenerClientePorNombre(@Path("nomBuscar")String nomBuscar);

    @GET("clientes/nombre/{dniRuc}")
    Call<RespuestaCliente>obtenerClientePorDniRuc(@Path("dniRuc")String dniRuc);

    @PUT("anularPedido")
    Call<RespuestaGeneral>anularPedido(@Field("idPedido")int idPedido);

}
