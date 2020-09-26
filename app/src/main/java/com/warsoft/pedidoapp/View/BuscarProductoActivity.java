package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.BuscarProductoAdapter;
import com.warsoft.pedidoapp.Local.Entities.CabeceraDescuento;
import com.warsoft.pedidoapp.Local.Entities.Producto;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaCabeceraDescuento;
import com.warsoft.pedidoapp.Service.Model.RespuestaProducto;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Conexion;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarProductoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int PROMO = 1;
    public static final int DESCUENTO = 3;

    private ImageButton btnBack,btnBuscarProducto;
    private RadioButton rdbCodigo,rdbNombreProducto;
    private RadioGroup rdbOpcionesBusqueda;
    private EditText txtDatoBusqueda;
    private RecyclerView recyclerBuscarProducto;
    private TextView txtSinDatos;
    private IServiceApi serviceApi;
    private String pantalla;
    private int tipoCliente = 1;
    private List<Producto> listaProductos;
    private BuscarProductoAdapter adapter;
    private AlertDialog dialogCarga;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        cargarVista();

        serviceApi = Service.getServiceApi();
        listaProductos = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        if(getIntent().getExtras()!= null){
            pantalla = getIntent().getStringExtra(Util.PANTALLA_ORIGEN);
            if(getIntent().hasExtra(Util.TIPO_CLIENTE)){
                tipoCliente = getIntent().getIntExtra(Util.TIPO_CLIENTE,1);
            }
        }

        btnBack.setOnClickListener(this);
        btnBuscarProducto.setOnClickListener(this);

        //txtDatoBusqueda.setOnClickListener(this);

    }

    private void cargarVista() {
        rdbOpcionesBusqueda = findViewById(R.id.rdb_opciones_busqueda);
        rdbCodigo = findViewById(R.id.rdb_codigo);
        rdbNombreProducto = findViewById(R.id.rdb_nombre_producto);
        rdbNombreProducto.setChecked(true);
        txtDatoBusqueda = findViewById(R.id.txt_dato_busqueda);
        recyclerBuscarProducto = findViewById(R.id.recycler_buscar_producto);
        txtSinDatos = findViewById(R.id.txt_sin_datos);
        btnBack = findViewById(R.id.btn_back);
        btnBuscarProducto = findViewById(R.id.btn_buscar_producto);
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando Productos...").setCancelable(false).build();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_buscar_producto:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                buscarProducto();
                break;
            case R.id.txt_dato_busqueda:
                //setearEditText();
                break;
            default:
                Toast.makeText(this, "Presione en botón válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarProducto() {
        String dato = txtDatoBusqueda.getText().toString();
        dialogCarga.show();
        Conexion conexion = new Conexion(this);
        try {
            //Comprobar si esta vacio
            if (!TextUtils.isEmpty(dato.trim())) {
                //Comprobar conexion a internet
                if (conexion.execute().get()) {
                    //Comprobar si el radioButton de Codigo esta seleccionado
                    if (rdbCodigo.isChecked()) {
                        //Comprobar si solo son numeros
                        if (TextUtils.isDigitsOnly(dato)) {
                            int codigo = Integer.parseInt(dato);
                            buscarPorCodigo(codigo);
                        } else {
                            txtDatoBusqueda.setError("Debe ingresar solo numeros");
                            dialogCarga.dismiss();
                        }
                    } else {
                        buscarPorNombre(dato);
                    }
                }else{
                    Toast.makeText(this, "No tienes conexion a internet", Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            } else {
                txtDatoBusqueda.setError("No puede estar vacio");
                dialogCarga.dismiss();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            dialogCarga.dismiss();
        }
    }

    private void buscarPorNombre(String dato) {
        serviceApi.obtenerProductoPorNombre(dato,tipoCliente).enqueue(new Callback<RespuestaProducto>() {
            @Override
            public void onResponse(Call<RespuestaProducto> call, Response<RespuestaProducto> response) {
                try{
                    RespuestaProducto respuesta = response.body();
                    if(respuesta.getCode()==100){
                        listaProductos = respuesta.getResult();
                        mostrarResultado();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(BuscarProductoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "onResponse: " + e.getMessage());
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaProducto> call, Throwable t) {
                Util.mensajeErrorConexion(BuscarProductoActivity.this,false);
                Log.e("Error", "onResponse: " + t.getMessage());
                dialogCarga.dismiss();
            }
        });
    }

    private void buscarPorCodigo(int dato) {
        serviceApi.obtenerProductoPorCodigo(dato,tipoCliente).enqueue(new Callback<RespuestaProducto>() {
            @Override
            public void onResponse(Call<RespuestaProducto> call, Response<RespuestaProducto> response) {
                try{
                    RespuestaProducto respuesta = response.body();
                    if(respuesta.getCode()==100){
                        listaProductos = respuesta.getResult();
                        mostrarResultado();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(BuscarProductoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "onReponse: " + e.getMessage());
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaProducto> call, Throwable t) {
                Log.e("ErrorProducto", "onFailure: " + t.getMessage() );
                Util.mensajeErrorConexion(BuscarProductoActivity.this,false);
                dialogCarga.dismiss();
            }
        });
    }

    private void mostrarResultado() {
        try{
            List<Producto> productos = new ArrayList<>();
            productos.addAll(listaProductos);
            if(productos.size()>0) {
                txtSinDatos.setVisibility(View.GONE);
                recyclerBuscarProducto.setVisibility(View.VISIBLE);
                iniciarAdapter(productos);
            }else{
                txtSinDatos.setVisibility(View.VISIBLE);
                txtSinDatos.setText("No se encontraron resultados");
                recyclerBuscarProducto.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", "mostrarResultado: " + e.getMessage());
            dialogCarga.dismiss();
        }
    }

    private void iniciarAdapter(final List<Producto> productos) {
        if(TextUtils.equals(pantalla,Util.PANTALLA_REGISTRO_PEDIDO) || TextUtils.equals(pantalla,Util.PANTALLA_EDITAR_PEDIDO)) {
            adapter = new BuscarProductoAdapter(this, productos, new BuscarProductoAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(final Producto producto, int position) {
                    if(producto.getTipocombo() > 0 && producto.getTipocombo() <= 3) {
                        //if (TextUtils.equals(Util.clienteSeleccionado.getZona(),"")) {
                            if (Util.productosSeleccionados.size() > 0) {
                                if(producto.getTipocombo() == 3) {
                                    seleccionarDescuento(producto);
                                }else{
                                    seleccionarPromocion(producto);
                                }
                            } else {
                                mostrarMensaje("Error", "Debe tener un producto como mínimo agregado");
                            }
                        //} else {
                        //    mostrarMensaje("Error", "No se puede obtener descuentos o promociones desde esta pantalla");
                        //}
                   }else{
                       seleccionarProducto(producto);
                    }
                }
            });
        }else if(TextUtils.equals(pantalla,Util.PANTALLA_PRINCIPAL)){
            adapter = new BuscarProductoAdapter(this,productos);
        }
        recyclerBuscarProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerBuscarProducto.setHasFixedSize(true);
        recyclerBuscarProducto.setAdapter(adapter);
    }

    private void seleccionarDescuento(Producto producto) {
        if(esVendedorValido(producto.getVendedores()) && esTipoNegocioValida(producto.getTipoNegocio()) &&
                esZonaValida(producto.getZonas())){
            serviceObtenerDetallePedido(producto,DESCUENTO,null);
        }else{
            mostrarMensaje("Error","No se cumple las condiciones para activar este descuento");
        }
    }

    private void seleccionarPromocion(Producto producto) {
        if(esVendedorValido(producto.getVendedores()) && esTipoNegocioValida(producto.getTipoNegocio()) &&
                esZonaValida(producto.getZonas())){
            serviceObtenerCabeceraPromo(producto);
        }else{
            mostrarMensaje("Error","No se cumple las condiciones para activar esta promocion");
        }
    }

    private void serviceObtenerCabeceraPromo(final Producto producto) {
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando datos importantes...").setCancelable(false).build();
        dialogCarga.show();
        Calendar fechaActual = Calendar.getInstance();

        serviceApi.obtenerCabeceraDescuento(producto.getIdProducto(),
                fechaActual.get(Calendar.YEAR)+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+agregarCeroAFecha(fechaActual.get(Calendar.DATE)),
                3).enqueue(new Callback<RespuestaCabeceraDescuento>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraDescuento> call, Response<RespuestaCabeceraDescuento> response) {
                try {
                    RespuestaCabeceraDescuento respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        if(respuesta.getResult().size()>0){
                            dialogCarga.dismiss();
                            serviceObtenerDetallePedido(producto,PROMO,respuesta.getResult().get(0));
                        }else{
                            Toast.makeText(BuscarProductoActivity.this, "Error,no hay datos relacionados a esa promo,intente con otra", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BuscarProductoActivity.this, "No se pudo traer los datos,intente otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e) {
                    dialogCarga.dismiss();
                    Toast.makeText(BuscarProductoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCabeceraDescuento> call, Throwable t) {
                dialogCarga.dismiss();
                Log.e("ErrorPCD", "onFailure: " + t.getMessage() );
                Util.mensajeErrorConexion(BuscarProductoActivity.this,false);
            }
        });
    }

    private void serviceObtenerDetallePedido(final Producto producto, final int tipo, final CabeceraDescuento dato) {
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando datos importantes...").setCancelable(false).build();
        dialogCarga.show();
        Calendar fechaActual = Calendar.getInstance();

        serviceApi.obtenerCabeceraDescuento(producto.getIdProducto(),
                fechaActual.get(Calendar.YEAR)+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+agregarCeroAFecha(fechaActual.get(Calendar.DATE)),
                2).enqueue(new Callback<RespuestaCabeceraDescuento>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraDescuento> call, Response<RespuestaCabeceraDescuento> response) {
                try {
                    RespuestaCabeceraDescuento respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        if(respuesta.getResult().size()>0){
                            dialogCarga.dismiss();
                            Util.detalleDescuentosSeleccionados.addAll(respuesta.getResult());
                            if(tipo == DESCUENTO) {
                                CabeceraDescuento descuentoActual = new CabeceraDescuento(producto.getIdProducto(), 0,
                                        producto.getNombreProducto(), producto.getVendedores(), producto.getZonas(), producto.getTipoNegocio(),
                                        producto.getDesde(), producto.getHasta(), producto.getTipocombo(), producto.getNombreProducto(),
                                        respuesta.getResult().get(0).getCatiMax(), producto.getPordes(), producto.getNroitems(), producto.getCantiMax(),
                                        respuesta.getResult().get(0).getCriterio(), respuesta.getResult().get(0).isSurtido(),
                                        respuesta.getResult().get(0).getUnidades(), respuesta.getResult().get(0).getUniMed(), respuesta.getResult().get(0).getFactor(),respuesta.getResult().get(0).getCantidadSoles());
                                Util.descuentosSeleccionados.add(descuentoActual);
                                terminarPantalla(producto,Util.TIPO_DESCUENTO,descuentoActual);
                            }
                            terminarPantalla(producto,Util.TIPO_DESCUENTO,dato);
                        }else{
                            Toast.makeText(BuscarProductoActivity.this, "Error,no hay datos relacionados a esa promo", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(BuscarProductoActivity.this, "No se pudo traer los datos,intente otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(BuscarProductoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCabeceraDescuento> call, Throwable t) {
                dialogCarga.dismiss();
                Log.e("ErrorPCD", "onFailure: " + t.getMessage() );
                Util.mensajeErrorConexion(BuscarProductoActivity.this,false);
            }
        });
    }


    private String agregarCeroAFecha(int numeroFecha){
        return numeroFecha<10? "0"+numeroFecha: ""+numeroFecha;
    }

    private void seleccionarProducto(final Producto producto){
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BuscarProductoActivity.this);
            builder.setTitle("Cantidad de productos en el pedido");
            final View view_cantidad = LayoutInflater.from(BuscarProductoActivity.this).inflate(R.layout.layout_cantidad_producto, null);
            final EditText txt_cantidad = view_cantidad.findViewById(R.id.txt_cantidad);
            final LinearLayout root_unidades = view_cantidad.findViewById(R.id.root_unidades);
            final TextView txt_cantidad_tipo = view_cantidad.findViewById(R.id.txt_cantidad_tipo);
            final EditText txt_unidades = view_cantidad.findViewById(R.id.txt_unidades);
            txt_cantidad.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            txt_unidades.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
            if (producto.getFactor() > 1) {
                root_unidades.setVisibility(View.VISIBLE);
            }
            txt_cantidad_tipo.setText(producto.getCantidadPorUnidad());
            txt_unidades.setHint("Máximo " + producto.getFactor() + " unidades");
            builder.setView(view_cantidad);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String cantidad = txt_cantidad.getText().toString().trim();
                    String unidades = txt_unidades.getText().toString().trim();
                    double saldo = producto.getSaldoreal() - producto.getUnidadesenpedido();
                    if(producto.isCombo()){
                        if (TextUtils.isEmpty(cantidad) || !TextUtils.isDigitsOnly(cantidad)) {
                            alertaCantidadInvalida(view_cantidad);
                        } else {
                            if(Integer.parseInt(cantidad) > saldo){
                                alertaExceso(producto,view_cantidad);
                            }else {
                                if(Integer.parseInt(cantidad) > producto.getCantiMax()){
                                    alertaCantidadMaxima(producto,view_cantidad);
                                }else{
                                    if(esVendedorValido(producto.getVendedores()) &&
                                            esTipoNegocioValida(producto.getTipoNegocio()) &&
                                            esZonaValida(producto.getZonas())) {
                                        producto.setCantidadPedido(Integer.parseInt(cantidad));
                                        producto.setTotalunidadesPedido(Integer.parseInt(cantidad));
                                        producto.setCombo(true);
                                        producto.setIdCombo(producto.getIdProducto());
                                        terminarPantalla(producto,Util.TIPO_PEDIDO,null);
                                    }else{
                                        mostrarMensaje("Error","Esta promoción no cumple las condiciones para ser activada");
                                    }
                                }
                            }
                        }
                    }else {
                        if (TextUtils.isEmpty(cantidad) && TextUtils.isEmpty(unidades)) {
                            alertaCantidadInvalida(view_cantidad);
                        } else {
                            if (!TextUtils.isEmpty(cantidad) && !TextUtils.isEmpty(unidades)) {
                                if (TextUtils.isDigitsOnly(cantidad) && TextUtils.isDigitsOnly(unidades)) {
                                    producto.setCantidadPedido(Integer.parseInt(cantidad));
                                    producto.setUnidadesPedido(Integer.parseInt(unidades));
                                    producto.setTotalunidadesPedido((producto.getCantidadPedido() * producto.getFactor() + producto.getUnidadesPedido()));
                                    if (producto.getTotalunidadesPedido() > saldo) {
                                        alertaExceso(producto, view_cantidad);
                                    } else {
                                        terminarPantalla(producto,Util.TIPO_PEDIDO,null);
                                    }
                                } else {
                                    alertaCantidadInvalida(view_cantidad);
                                }
                            } else {
                                if (!TextUtils.isEmpty(cantidad) && TextUtils.isDigitsOnly(cantidad)) {
                                    producto.setCantidadPedido(Integer.parseInt(cantidad));
                                    producto.setTotalunidadesPedido(producto.getCantidadPedido() * producto.getFactor());
                                    if (producto.getTotalunidadesPedido() > saldo) {
                                        alertaExceso(producto, view_cantidad);
                                    } else {
                                        terminarPantalla(producto,Util.TIPO_PEDIDO,null);
                                    }
                                } else {
                                    alertaCantidadInvalida(view_cantidad);
                                }
                                if (!TextUtils.isEmpty(unidades) && TextUtils.isDigitsOnly(unidades)) {
                                    producto.setUnidadesPedido(Integer.parseInt(unidades));
                                    producto.setTotalunidadesPedido(Integer.parseInt(unidades));
                                    if (producto.getTotalunidadesPedido() > saldo) {
                                        alertaExceso(producto, view_cantidad);
                                    } else {
                                        terminarPantalla(producto,Util.TIPO_PEDIDO,null);
                                    }
                                } else {
                                    alertaCantidadInvalida(view_cantidad);
                                }
                            }

                        }
                    }
                }
            });
            builder.setNegativeButton("No", null);
            builder.create();
            builder.show();
    }

    private boolean esVendedorValido(String lista) {
        if(TextUtils.isEmpty(lista.trim())){
            return true;
        }else {
            int codVendedor = preferences.getInt(Util.COD_VENDEDOR, -1);
            String[] listaVendedores = lista.split(",");
            for (String dato : listaVendedores) {
                if (TextUtils.equals(dato, String.valueOf(codVendedor))) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean esZonaValida(String lista) {
        if(TextUtils.isEmpty(lista.trim())){
            return true;
        }else {
            int idZona = Util.clienteSeleccionado.getIdZona();
            String[] listaZonas = lista.split(",");
            for (String dato : listaZonas) {
                if (TextUtils.equals(dato, String.valueOf(idZona))) {
                    return true;
                }
            }
            return false;
        }
    }

    private boolean esTipoNegocioValida(String lista) {
        if(TextUtils.isEmpty(lista.trim())){
            return true;
        }else {
            int tipoNegocio = Util.clienteSeleccionado.getTipNeg();
            String[] listaTipoNegocios = lista.split(",");
            for (String dato : listaTipoNegocios) {
                if (TextUtils.equals(dato, String.valueOf(tipoNegocio))) {
                    return true;
                }
            }
            return false;
        }
    }

    private void terminarPantalla(Producto producto,int tipo,CabeceraDescuento cabeceraDescuento){
        Intent resultIntent = null;
        if (TextUtils.equals(pantalla, Util.PANTALLA_REGISTRO_PEDIDO)) {
            resultIntent = new Intent(BuscarProductoActivity.this, RegistrarPedidoActivity.class);
            if(Util.TIPO_PEDIDO == tipo){
                resultIntent.putExtra(Util.PRODUCTO_SELECCIONADO, producto);
            }else if(Util.TIPO_DESCUENTO == tipo){
                resultIntent.putExtra(Util.TIPO,cabeceraDescuento);
            }
        } else if (TextUtils.equals(pantalla, Util.PANTALLA_EDITAR_PEDIDO)) {
            resultIntent = new Intent(BuscarProductoActivity.this, EditarPedidoActivity.class);
            resultIntent.putExtra(Util.PRODUCTO_SELECCIONADO, producto);
        }
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void alertaCantidadInvalida(View view_cantidad){
        Toast.makeText(BuscarProductoActivity.this, "¡Ingresa una cantidad válida!", Toast.LENGTH_SHORT).show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view_cantidad.getWindowToken(), 0);
    }

    private void alertaExceso(Producto producto,View view_cantidad) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BuscarProductoActivity.this);
        builder.setTitle("Exceso de producto");
        builder.setMessage(String.format("No hay esa cantidad disponible,solo se cuenta con %.2f %s",(producto.getSaldoreal()-producto.getUnidadesenpedido())/(double)producto.getFactor(),producto.getCantidadPorUnidad().toLowerCase()));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create();
        builder.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view_cantidad.getWindowToken(), 0);
    }

    private void alertaCantidadMaxima(Producto producto,View view_cantidad){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(BuscarProductoActivity.this);
        builder.setTitle("Exceso de producto");
        builder.setMessage(String.format("Esa promoción solo se puede aplicar %.2f veces",(double)producto.getCantiMax()));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
        builder.create();
        builder.show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view_cantidad.getWindowToken(), 0);
    }

    private String[] setearCantidad(String cantidad) {
        String[] dividir = {cantidad};
        if(cantidad.contains(".")) {
            dividir = cantidad.split("[.]");
        }else if(cantidad.contains(",")){
            dividir = cantidad.split(",");
        }
        return dividir;
    }

    private void regresar() {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Salir de pantalla");
            builder.setMessage("¿Desea regresar a la pantalla anterior?, se perderá todo lo hecho");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setNegativeButton("No", null);
            builder.create();
            builder.show();
    }

    private void mostrarMensaje(String titulo, String mensaje){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    @Override
    public void onBackPressed() {
        if(TextUtils.equals(pantalla,Util.PANTALLA_REGISTRO_PEDIDO)) {
            regresar();
        }
        super.onBackPressed();
    }
}
