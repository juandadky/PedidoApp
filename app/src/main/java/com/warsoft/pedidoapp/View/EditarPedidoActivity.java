package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.warsoft.pedidoapp.Adapter.EditarPedidoAdapter;
import com.warsoft.pedidoapp.Local.Entities.CabeceraDescuento;
import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.warsoft.pedidoapp.Local.Entities.DetallePedido;
import com.warsoft.pedidoapp.Local.Entities.Producto;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaCliente;
import com.warsoft.pedidoapp.Service.Model.RespuestaGeneral;
import com.warsoft.pedidoapp.Service.Model.RespuestaProducto;
import com.warsoft.pedidoapp.Service.Model.RespuestaTipoCliente;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditarPedidoActivity extends AppCompatActivity implements EditarPedidoAdapter.OnItemClickListener, View.OnClickListener {

    public static final int CODE_REQUEST_PRODUCTO = 125;

    private CabeceraPedido cabecera;
    private List<DetallePedido> detallePedidos;
    private TextView txtIdPedido,txtFechaPedido,txtClientePedido,txtTipoPago,txtTipoDocumento,txtTotal,txtClienteDireccion;
    private ImageButton btnBack,btnAgregarProducto;
    private Button btnActualizarPedido;
    private RecyclerView recyclerProducto;
    private EditarPedidoAdapter adapter;
    private IServiceApi serviceApi;
    private AlertDialog dialogCarga;
    private double total;
    private SharedPreferences preferences;
    private boolean seActualizoPedido,seHizoAlgunCambio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_pedido);

        serviceApi = Service.getServiceApi();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cabecera = new CabeceraPedido();
        detallePedidos = new ArrayList<>();
        seActualizoPedido = false;
        seHizoAlgunCambio = false;

        iniciarVista();

        if(getIntent().getExtras() != null){
            cabecera = (CabeceraPedido) getIntent().getSerializableExtra(Util.CABECERA_PEDIDO);
            Bundle bundle = getIntent().getExtras();
            detallePedidos = (ArrayList<DetallePedido>) bundle.getSerializable(Util.DETALLE_PEDIDO);
            setearVista();
            obtenerPromocionesDescuentos();
        }

        btnBack.setOnClickListener(this);
        btnAgregarProducto.setOnClickListener(this);
        btnActualizarPedido.setOnClickListener(this);

    }


    //Metodos Usados

    private void iniciarVista() {
        txtIdPedido = findViewById(R.id.txt_id_pedido);
        txtFechaPedido = findViewById(R.id.txt_fecha_pedido);
        txtClientePedido = findViewById(R.id.txt_cliente_pedido);
        txtTipoPago = findViewById(R.id.txt_tipoPago);
        txtTipoDocumento = findViewById(R.id.txt_tipoDocumento);
        txtTotal = findViewById(R.id.txt_total);
        txtClienteDireccion = findViewById(R.id.txt_cliente_direccion);
        recyclerProducto = findViewById(R.id.recycler_productos);
        btnActualizarPedido = findViewById(R.id.btn_actualizar_pedido);
        btnBack = findViewById(R.id.btn_back);
        btnAgregarProducto = findViewById(R.id.btn_agregar_producto);
    }

    private void setearVista() {
        txtIdPedido.setText("ID: " + cabecera.getIdPedido());
        txtFechaPedido.setText("Fecha Pedido: " +Util.setearFecha(cabecera.getFechaPedido()));
        txtClientePedido.setText("Cliente: " + cabecera.getNombreCliente());
        txtClienteDireccion.setText("Dirección: " + cabecera.getDireccion());
        txtTipoPago.setText("Tipo Pago: " + (TextUtils.equals(cabecera.getTipoVenta(),"1")? "Contado" : "Crédito"));
        txtTipoDocumento.setText("Documento: " + (cabecera.getFacBolNP() == 1 ? "Factura" : "Boleta"));
        txtTotal.setText("Total: S/. 00.00");
        adapter = new EditarPedidoAdapter();
        recyclerProducto.setHasFixedSize(true);
        recyclerProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerProducto.setAdapter(adapter);
        adapter.setListener(this);
        adapter.setProductoList(detallePedidos);
        setearTotal();
    }

    private void setearTotal(){
        total = 0;
        for(DetallePedido detalle : detallePedidos){
            if(detalle.getTotalunidadesPedido() != 0) {
                total += detalle.getTotalunidadesPedido() / (double) detalle.getFactor() * detalle.getPrecioUnidad();
            }else{
                total += detalle.getTotal();
            }
        }
        txtTotal.setText(String.format("Total: S/.%.2f", total));
    }

    private void mostrarDialogEditarCantidad(final DetallePedido detallePedido, final Producto producto, final int position){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Editar cantidad de productos");
        final View view_cantidad = LayoutInflater.from(this).inflate(R.layout.layout_cantidad_producto,null);
        final EditText txt_cantidad = view_cantidad.findViewById(R.id.txt_cantidad);
        final LinearLayout root_unidades = view_cantidad.findViewById(R.id.root_unidades);
        final TextView txt_cantidad_tipo = view_cantidad.findViewById(R.id.txt_cantidad_tipo);
        final EditText txt_unidades = view_cantidad.findViewById(R.id.txt_unidades);
        txt_cantidad.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        txt_unidades.setInputType(InputType.TYPE_NUMBER_FLAG_SIGNED);
        if(producto.getFactor()>1){
            root_unidades.setVisibility(View.VISIBLE);
        }
        txt_cantidad_tipo.setText(producto.getCantidadPorUnidad());
        txt_unidades.setHint("Máximo " + producto.getFactor() + " unidades");
        builder.setView(view_cantidad);
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                validarDatos(txt_cantidad,txt_unidades,detallePedido,view_cantidad,position,producto);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }

    private void validarDatos(TextView txt_cantidad,TextView txt_unidades, DetallePedido detallePedido, View view_cantidad, int position, Producto producto) {
        String cantidad = txt_cantidad.getText().toString().trim();
        String unidades = txt_unidades.getText().toString().trim();

        if(TextUtils.isEmpty(cantidad) && TextUtils.isEmpty(unidades)){
            alertaCantidadInvalida(view_cantidad);
        }else {
            if (!TextUtils.isEmpty(cantidad) && !TextUtils.isEmpty(unidades)) {
                if (TextUtils.isDigitsOnly(cantidad) && TextUtils.isDigitsOnly(unidades)) {
                    detallePedido.setCantidadPedido(Integer.parseInt(cantidad));
                    detallePedido.setUnidadesPedido(Integer.parseInt(unidades));
                    detallePedido.setTotalunidadesPedido((detallePedido.getCantidadPedido() * detallePedido.getFactor() + detallePedido.getUnidadesPedido()));
                    if (detallePedido.getTotalunidadesPedido() > producto.getSaldoreal()) {
                        alertaExceso(producto, view_cantidad);
                    } else {
                        actualizarDatosProducto(detallePedido,position);
                    }
                } else {
                    alertaCantidadInvalida(view_cantidad);
                }
            } else {
                if (!TextUtils.isEmpty(cantidad) && TextUtils.isDigitsOnly(cantidad)) {
                    detallePedido.setCantidadPedido(Integer.parseInt(cantidad));
                    detallePedido.setTotalunidadesPedido(producto.getCantidadPedido() * producto.getFactor());
                    if (detallePedido.getTotalunidadesPedido() > producto.getSaldoreal()) {
                        alertaExceso(producto, view_cantidad);
                    } else {
                        actualizarDatosProducto(detallePedido,position);
                    }
                } else {
                    alertaCantidadInvalida(view_cantidad);
                }
                if (!TextUtils.isEmpty(unidades) && TextUtils.isDigitsOnly(unidades)) {
                    detallePedido.setUnidadesPedido(Integer.parseInt(unidades));
                    detallePedido.setTotalunidadesPedido(producto.getUnidadesPedido());
                    if (detallePedido.getTotalunidadesPedido() > producto.getSaldoreal()) {
                        alertaExceso(producto, view_cantidad);
                    } else {
                        actualizarDatosProducto(detallePedido,position);
                    }
                } else {
                    alertaCantidadInvalida(view_cantidad);
                }
            }
        }
    }

    private void obtenerPromocionesDescuentos(){
        for(DetallePedido dato : detallePedidos){
            if(!verSiEstaDescuento(dato.getIdCombo())){
                
            }
        }
    }

    private boolean verSiEstaDescuento(int codPromo){
        for(int i = 0; i<Util.descuentosSeleccionados.size();i++){
            if(codPromo == Util.descuentosSeleccionados.get(i).getIdProducto()){
                return true;
            }
        }
        return false;
    }

    private void actualizarDatosProducto(DetallePedido detallePedido,int position){
        double precio = detallePedido.getTotalunidadesPedido()/(double)detallePedido.getFactor() * detallePedido.getPrecioUnidad();
        detallePedido.setTotalPedido(precio);
        serviceActualizarProducto(detallePedido,position);
    }

    private void alertaCantidadInvalida(View view_cantidad){
        Toast.makeText(this, "¡Ingresa una cantidad válida!", Toast.LENGTH_SHORT).show();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view_cantidad.getWindowToken(), 0);
    }

    private void alertaExceso(Producto producto,View view_cantidad) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Exceso de producto");
        builder.setMessage(String.format("No hay esa cantidad disponible,solo se cuenta con %.2f %s",producto.getSaldoreal()/(double)producto.getFactor(),producto.getCantidadPorUnidad().toLowerCase()));
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

    private void mensajeTerminadoActualizacion(){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Mensaje");
        builder.setMessage("Se actualizó el pedido");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.create();
        builder.show();
    }

    private void actualizarPedido() {
        serviceActualizarCabecera();
    }


    private void agregarProducto() {
        serviceBuscarClientePorId(cabecera.getIdCliente());
    }

    private void mapearValorYAgregar(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            try{
                Producto producto = (Producto) data.getExtras().getSerializable(Util.PRODUCTO_SELECCIONADO);
                String totalConUnidades = producto.getCantidadPedido()+"."+producto.getUnidadesPedido();
                double precio = cabecera.getTipoVenta()=="1"?producto.getPreciocon():producto.getPreciocre();
                double total = producto.getTotalunidadesPedido()/(double)producto.getFactor() * precio;
                DetallePedido detallePedidoAgregado = new DetallePedido(cabecera.getIdPedido(),producto.getIdProducto(),producto.getNombreProducto(),
                        producto.getCantidadPorUnidad(),producto.getFactor(),"",producto.getAlmacen(),producto.getUnidadesPedido()==0?producto.getCantidadPedido(): Double.parseDouble(totalConUnidades),0,
                        cabecera.getTipoVenta()=="1"?producto.getPreciocon():producto.getPreciocre(),total,0,total,
                        0,18,producto.getTotalunidadesPedido(),0,producto.getCosto(),
                        0,producto.isCombo()?1:0,0,producto.isSurtido(),producto.getTipocombo(),producto.getCantidadSoles(),producto.getTipoprecioventa());
                serviceAgregarProducto(detallePedidoAgregado);

            }catch (Exception e){
                Toast.makeText(this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "No agrego un nuevo producto,intentelo de nuevo", Toast.LENGTH_SHORT).show();
        }
    }

    //Consumo de API

    private void serviceCargarDatoProducto(final DetallePedido detallePedido, final int position){

        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Registrando pedido...").setCancelable(false).build();
        dialogCarga.show();

        serviceApi.obtenerProductoPorNombre(detallePedido.getNombreProducto(),1).enqueue(new Callback<RespuestaProducto>() {
            @Override
            public void onResponse(Call<RespuestaProducto> call, Response<RespuestaProducto> response) {
                try {
                    RespuestaProducto respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        Producto producto = respuesta.getResult().get(0);
                        if(producto != null) {
                            dialogCarga.dismiss();
                            mostrarDialogEditarCantidad(detallePedido, producto, position);
                        }else{
                            Toast.makeText(EditarPedidoActivity.this, "No se puede hacer cambios en este producto", Toast.LENGTH_LONG).show();
                            dialogCarga.dismiss();
                        }
                    } else {
                        Toast.makeText(EditarPedidoActivity.this, "No se pudo traer datos dle producto,intentelo otra vez", Toast.LENGTH_SHORT).show();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(EditarPedidoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaProducto> call, Throwable t) {
                Util.mensajeErrorConexion(EditarPedidoActivity.this,false);
                dialogCarga.dismiss();
            }
        });

    }

    private void serviceActualizarProducto(final DetallePedido pedidoActual, final int position){
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Actualizando...").setCancelable(false).build();
        dialogCarga.show();

        String totalConUnidades = pedidoActual.getCantidadPedido()+"."+pedidoActual.getUnidadesPedido();

        serviceApi.actualizarDetallePedido(pedidoActual.getIdPedido(),pedidoActual.getIdProducto(),
                pedidoActual.getUnidadesPedido()==0?pedidoActual.getCantidadPedido(): Double.parseDouble(totalConUnidades),
                pedidoActual.getTotalunidadesPedido(), pedidoActual.getTotalPedido(),pedidoActual.getTotalPedido())
                .enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try {
                    RespuestaGeneral respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        pedidoActual.setTotalPedido(pedidoActual.getTotalunidadesPedido() / (double) pedidoActual.getFactor() * pedidoActual.getPrecioUnidad());
                        adapter.notifyItemChanged(position);
                        setearTotal();
                        seHizoAlgunCambio = true;
                        seActualizoPedido = false;
                        dialogCarga.dismiss();
                        Util.generarAlerta("Mensaje","Se actualizó el producto",EditarPedidoActivity.this);
                    } else {
                        dialogCarga.dismiss();
                        Toast.makeText(EditarPedidoActivity.this, "No se pudo actualizar el producto,intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(EditarPedidoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(EditarPedidoActivity.this,false);
            }
        });
    }

    private void serviceActualizarCabecera(){
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Actualizando el pedido...").setCancelable(false).build();
        dialogCarga.show();

        serviceApi.actualizarCabeceraPedido(cabecera.getIdPedido(),total).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try {
                    RespuestaGeneral respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        seActualizoPedido = true;
                        seHizoAlgunCambio = false;
                        dialogCarga.dismiss();
                        mensajeTerminadoActualizacion();
                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(EditarPedidoActivity.this, "No se pudo actualizar el pedido,intentelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(EditarPedidoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(EditarPedidoActivity.this,false);
            }
        });
    }

    private void serviceEliminarProducto(final int position){
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Eliminando...").setCancelable(false).build();
        dialogCarga.show();

        DetallePedido productoActual = detallePedidos.get(position);

        serviceApi.eliminarDetallePedido(productoActual.getIdPedido(),productoActual.getIdProducto()).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try{
                    RespuestaGeneral respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        detallePedidos.remove(position);
                        adapter.notifyItemRemoved(position);
                        setearTotal();
                        seHizoAlgunCambio = true;
                        seActualizoPedido = false;
                        dialogCarga.dismiss();
                        Util.generarAlerta("Mensaje","Se eliminó el producto",EditarPedidoActivity.this);
                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(EditarPedidoActivity.this, "No se pudo eliminar el producto,,intentelo de nuevo", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(EditarPedidoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(EditarPedidoActivity.this,false);
            }
        });
    }

    private void serviceAgregarProducto(final DetallePedido detallePedido){
        final int idUsuario = preferences.getInt(Util.ID_USUARIO_LOGIN, -1);
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Agregando...").setCancelable(false).build();
        dialogCarga.show();

        serviceApi.registroDetallePedido(detallePedido.getIdPedido(),detallePedido.getIdProducto(),detallePedido.getPrecioUnidad(),detallePedido.getCantidad(),
                detallePedido.getEntregado(),(int)detallePedido.getUnidades(),detallePedido.getAlmacen(),0,detallePedido.getImporte(),detallePedido.getTotal(),
                0,detallePedido.getFactor(),detallePedido.getTipoPrecio(),detallePedido.getPorIgv(),detallePedido.getPorIgv()!=0? detallePedido.getTotal()/(1.18) : 0 ,detallePedido.getCosto(),
                idUsuario,detallePedido.getCombo(),detallePedido.getIdCombo(),detallePedido.getTipocombo(),detallePedido.isSurtido(),detallePedido.getCantidadSoles(),detallePedido.getTipoprecioventa()).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try {
                    RespuestaGeneral respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        detallePedidos.add(detallePedido);
                        adapter.notifyItemInserted(detallePedidos.size()-1);
                        setearTotal();
                        seHizoAlgunCambio = true;
                        seActualizoPedido = false;
                        dialogCarga.dismiss();
                        Util.generarAlerta("Mensaje","Se agregó el producto al pedido",EditarPedidoActivity.this);
                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(EditarPedidoActivity.this, "No se pudo insertar el nuevo producto al pedido,intentelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(EditarPedidoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(EditarPedidoActivity.this,false);
            }
        });
    }

    private void serviceBuscarClientePorId(int idCliente){
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Buscando datos necesarios...").setCancelable(false).build();
        dialogCarga.show();

        serviceApi.obtenerTipoCliente(idCliente).enqueue(new Callback<RespuestaCliente>() {
            @Override
            public void onResponse(Call<RespuestaCliente> call, Response<RespuestaCliente> response) {
                try {
                    RespuestaCliente respuesta = response.body();
                    if (respuesta.getCode() == 100){
                        dialogCarga.dismiss();
                        Intent intent = new Intent(EditarPedidoActivity.this,BuscarProductoActivity.class);
                        intent.putExtra(Util.PANTALLA_ORIGEN,Util.PANTALLA_EDITAR_PEDIDO);
                        intent.putExtra(Util.TIPO_CLIENTE,Integer.parseInt(respuesta.getResult().get(0).getListaPrecio()));
                        startActivityForResult(intent,CODE_REQUEST_PRODUCTO);

                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(EditarPedidoActivity.this, "No se pudo encontrar al cliente,intentelo otra vez", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(EditarPedidoActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCliente> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(EditarPedidoActivity.this,false);
            }
        });
    }

    //Metodos Override

    @Override
    public void onBackPressed() {
        if(seHizoAlgunCambio){
            if(!seActualizoPedido){
                final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
                builder.setTitle("Alerta");
                builder.setMessage("No puede salir hasta que presione en el boton actualizar pedido");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create();
                builder.show();
            }else{
                finish();
            }
        }else{
            finish();
        }
    }

    @Override
    public void onDeleteItem(final int position) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Eliminar Producto");
        builder.setMessage("¿Desea eliminar el producto seleccionado?,no se podrá recuperar");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serviceEliminarProducto(position);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }



    @Override
    public void onChangeDataItem(final DetallePedido detallePedido, final int position) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Actualizar Producto");
        builder.setMessage("¿Desea realizar algun cambio en el producto?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                serviceCargarDatoProducto(detallePedido,position);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_agregar_producto:
                agregarProducto();
                break;
            case R.id.btn_actualizar_pedido:
                actualizarPedido();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_REQUEST_PRODUCTO:
                mapearValorYAgregar(resultCode,data);
                break;
            default:
                Toast.makeText(this, "No existe esa petición", Toast.LENGTH_SHORT).show();
        }
    }


}
