package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.app.Dialog;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.AgregarProductoAdapter;
import com.warsoft.pedidoapp.Adapter.AgregarPromoDctoAdapter;
import com.warsoft.pedidoapp.Interface.CargaDetallePedido;
import com.warsoft.pedidoapp.Interface.IAlertDialog;
import com.warsoft.pedidoapp.Local.Entities.CabeceraDescuento;
import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.Local.Entities.DetallePedido;
import com.warsoft.pedidoapp.Local.Entities.Producto;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaAtrasoCredito;
import com.warsoft.pedidoapp.Service.Model.RespuestaCabeceraDescuento;
import com.warsoft.pedidoapp.Service.Model.RespuestaGeneral;
import com.warsoft.pedidoapp.Service.Model.RespuestaProducto;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarPedidoActivity extends AppCompatActivity implements View.OnClickListener, CargaDetallePedido, AgregarProductoAdapter.OnItemClickListener,AgregarPromoDctoAdapter.OnItemClickListener, IAlertDialog {

    public static final int ACTIVAR_DESCUENTO = 1;
    public static final int DESACTIVAR_DDESCUENTO = 2;
    public static final int BUSCAR_BONO = 0;
    public static final int BUSCAR_PRODUCTO = 1;
    private static final int REQUEST_BUSCAR_CLIENTE = 1;
    private static final int REQUEST_AGREGAR_PRODUCTO = 2;
    public static final int CONTADO_CHECK = 0;
    public static final int CREDITO_CHECK = 1;
    public static final int PRECIO_MAY_2 = 2;
    public static final int PRECIO_MAY = 3;
    public static final int PRECIO_VOL = 4;

    private ImageButton btn_back,btn_buscar_cliente,btn_agregarProducto;
    private TextView txt_dato_cliente,txt_sin_datos,txt_total_pedido,txt_direccion_cliente;
    private EditText txt_dias_crédito;
    private RecyclerView recycler_registrar_producto,recycler_promociones_descuentos;
    private RadioButton rdb_credito,rdb_contado;
    private RadioGroup rbd_opciones_pago;
    private Button btn_registrar_pedido;
    private LinearLayout layout_dias_credito,root_promociones;
    private Cliente cliente;
    private List<Producto> listaProductosPedido;
    private SharedPreferences preferences;
    private AgregarProductoAdapter adapter;
    private AgregarPromoDctoAdapter adapterPromo;
    private IServiceApi serviceApi;
    private double total;
    private int productosEnviadosaBD;
    private AlertDialog dialog;
    private boolean cargandoBonoficacion = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_pedido);

        setearVista();
        comprobarData();

        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        listaProductosPedido = new ArrayList<>();
        serviceApi = Service.getServiceApi();

        btn_back.setOnClickListener(this);
        btn_buscar_cliente.setOnClickListener(this);
        btn_registrar_pedido.setOnClickListener(this);
        btn_agregarProducto.setOnClickListener(this);

        if(!preferences.getBoolean(Util.CREDITO,false)){
            rdb_credito.setVisibility(View.GONE);
        }

        rbd_opciones_pago.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                setearTotal();
                if(R.id.rdb_contado == checkedId){
                    txt_dias_crédito.setVisibility(View.GONE);
                    layout_dias_credito.setVisibility(View.GONE);
                    adapter.setProductoList(Util.productosSeleccionados,CONTADO_CHECK);
                }else if(R.id.rdb_credito == checkedId){
                    txt_dias_crédito.setVisibility(View.VISIBLE);
                    layout_dias_credito.setVisibility(View.VISIBLE);
                    adapter.setProductoList(Util.productosSeleccionados,CREDITO_CHECK);
                }
            }
        });

    }

    private void comprobarData() {
        if(Util.clienteSeleccionado!= null){
            if(Util.clienteSeleccionado.getTipoCliente() != 0) {
                txt_dato_cliente.setText(Util.textoCapitalizado(Util.clienteSeleccionado.getNombre()) + "\n Tipo: " + Util.clienteSeleccionado.getTipoCliente()+ "        " + Util.clienteSeleccionado.getReferencia());
                txt_direccion_cliente.setText("" + Util.clienteSeleccionado.getDireccion());
                setearEstadoVistaCliente();
            }else{
                txt_dato_cliente.setText("");
                txt_direccion_cliente.setText("");
            }
        }
        if(Util.productosSeleccionados != null) {
            if (Util.productosSeleccionados.size() > 0) {
                setearEstadoVista();
                if (rdb_contado.isChecked()) {
                    txt_dias_crédito.setVisibility(View.GONE);
                    layout_dias_credito.setVisibility(View.GONE);
                    adapter.setProductoList(Util.productosSeleccionados, CONTADO_CHECK);
                    setearTotal();
                } else if (rdb_credito.isChecked()) {
                    layout_dias_credito.setVisibility(View.VISIBLE);
                    txt_dias_crédito.setVisibility(View.VISIBLE);
                    adapter.setProductoList(Util.productosSeleccionados, CREDITO_CHECK);
                    setearTotal();
                }
            }
        }
    }

    private void setearVista(){
        btn_back = findViewById(R.id.btn_back);
        btn_buscar_cliente = findViewById(R.id.btn_buscar_cliente);
        btn_agregarProducto = findViewById(R.id.btn_agregar_producto);
        txt_dato_cliente = findViewById(R.id.txt_dato_cliente);
        txt_total_pedido = findViewById(R.id.txt_total_pedido);
        txt_direccion_cliente = findViewById(R.id.txt_direccion_cliente);
        txt_dias_crédito = findViewById(R.id.txt_dias_crédito);
        recycler_registrar_producto = findViewById(R.id.recycler_registrar_producto);
        recycler_promociones_descuentos = findViewById(R.id.recycler_promociones_descuentos);
        txt_sin_datos = findViewById(R.id.txt_sin_datos);
        rbd_opciones_pago = findViewById(R.id.rbd_opciones_pago);
        rdb_credito = findViewById(R.id.rdb_credito);
        rdb_contado = findViewById(R.id.rdb_contado);
        rdb_contado.setChecked(true);
        btn_registrar_pedido = findViewById(R.id.btn_registrar_pedido);
        layout_dias_credito = findViewById(R.id.layout_dias_credito);
        root_promociones = findViewById(R.id.root_promociones);
        //Recycler Agregar Producto
        adapter = new AgregarProductoAdapter(this);
        recycler_registrar_producto.setLayoutManager(new LinearLayoutManager(this));
        recycler_registrar_producto.setHasFixedSize(true);
        recycler_registrar_producto.setAdapter(adapter);
        adapter.setListener(this);
        // Recycler promos y descuentos
        adapterPromo = new AgregarPromoDctoAdapter();
        recycler_promociones_descuentos.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recycler_promociones_descuentos.setHasFixedSize(true);
        recycler_promociones_descuentos.setAdapter(adapterPromo);
        adapterPromo.setListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_buscar_cliente:
                buscarCliente();
                break;
            case R.id.btn_agregar_producto:
                agregarProducto();
                break;
            case R.id.btn_registrar_pedido:
                registrarPedido();
                break;
            default:
                Toast.makeText(this, "No existe ese botón", Toast.LENGTH_SHORT).show();
        }
    }


    private void registrarPedido() {
        if(Util.clienteSeleccionado != null){
            if(Util.productosSeleccionados.size() > 0){
                if((rdb_credito.isChecked() && !TextUtils.isEmpty(txt_dias_crédito.getText().toString().trim()) || rdb_contado.isChecked())) {
                    if(comprobarSinVentas0()) {
                        registrarCabeceraPedido();
                    }else{
                        Toast.makeText(this, "No se puede realizar ventas con productos con precio 0", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(this, "Añada el número de dias del crédito", Toast.LENGTH_SHORT).show();
                }
            }else{
                Toast.makeText(this, "Añada productos al pedido", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(this, "Registre un cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean comprobarSinVentas0() {
        for (Producto item : Util.productosSeleccionados){
            if(item.getTotal() == 0 && !item.isBonificacion()){
                return false;
            }
        }
        return true;
    }

    private void registrarCabeceraPedido(){
        //Obteniendo datos
        int codVendedor = preferences.getInt(Util.COD_VENDEDOR,-1);
        final int idUsuario = preferences.getInt(Util.ID_USUARIO_LOGIN, -1);
        int numeroDiasCredito = Integer.parseInt(txt_dias_crédito.getText().toString().trim());
        Calendar fechaActual = Calendar.getInstance();

        //Inicio del pedido
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Registrando pedido...").setCancelable(false).build();
        dialog.show();
        serviceApi.registroCabeceraPedido(Util.clienteSeleccionado.getIdCliente(),codVendedor,
                fechaActual.get(Calendar.YEAR)+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+agregarCeroAFecha(fechaActual.get(Calendar.DATE)),
                rdb_contado.isChecked()?1:2,0,Util.clienteSeleccionado.getIdZona(),rdb_credito.isChecked()?numeroDiasCredito:0,18.00,(TextUtils.equals(Util.clienteSeleccionado.getDocIden(),"R")?1:3),total,idUsuario,
                Util.clienteSeleccionado.getMaxPago() > 60 ? 50 : 0).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try{
                    RespuestaGeneral respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        if(respuesta.getResult() != null){
                            registrarDetallePedido(respuesta.getResult().get(0).getId(),idUsuario);
                        }
                    }else{
                        Toast.makeText(RegistrarPedidoActivity.this, "Error al registrar los productos", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }

                }catch (Exception e){
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                dialog.dismiss();
            }
        });
    }


    private void registrarDetallePedido(int idPedido,int idUsuario) {
        for (int i = 0;i < Util.productosSeleccionados.size();i++){
            Producto productoActual = Util.productosSeleccionados.get(i);
            String totalConUnidades = productoActual.getCantidadPedido()+"."+productoActual.getUnidadesPedido();
            serviceApi.registroDetallePedido(idPedido,productoActual.getIdProducto(),rdb_contado.isChecked()? productoActual.getPreciocon():productoActual.getPreciocre(),
                    productoActual.getUnidadesPedido()==0?productoActual.getCantidadPedido(): Double.parseDouble(totalConUnidades),0, productoActual.getTotalunidadesPedido(), productoActual.getAlmacen(),productoActual.getTotal()*productoActual.getDescuento()/100.0,productoActual.getTotal(),
                    productoActual.getTotal(),productoActual.getDescuento(),productoActual.getFactor(),
                    productoActual.getTipoPrecio(),18.00, productoActual.isAfectoIgv()? productoActual.getTotal()/(1.18) : 0 ,productoActual.getCosto(),
                    idUsuario,productoActual.isCombo()?1:0,productoActual.getIdCombo(), productoActual.getTipocombo(),productoActual.isSurtido(),productoActual.getCantidadSoles(),productoActual.getTipoprecioventa()).enqueue(new Callback<RespuestaGeneral>() {
                @Override
                public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                    RespuestaGeneral respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        contarProducto();
                        terminarProducto();
                    }else{
                        falloDeProducto();
                    }
                }

                @Override
                public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                    Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                    dialog.dismiss();
                }
            });
        }
    }

    private void agregarProducto() {
        if(Util.CLIENTE_SELECCIONADO != null){
            Intent intent = new Intent(this,BuscarProductoActivity.class);
            intent.putExtra(Util.PANTALLA_ORIGEN,Util.PANTALLA_REGISTRO_PEDIDO);
            intent.putExtra(Util.TIPO_CLIENTE,Integer.parseInt(cliente.getListaPrecio()));
            startActivityForResult(intent,REQUEST_AGREGAR_PRODUCTO);
        }else{
            Toast.makeText(this, "Tiene que seleccionar un cliente por favor", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarCliente() {
        Intent intent = new Intent(this,BuscarClienteActivity.class);
        intent.putExtra(Util.PANTALLA_ORIGEN,Util.PANTALLA_REGISTRO_PEDIDO);
        startActivityForResult(intent,REQUEST_BUSCAR_CLIENTE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_BUSCAR_CLIENTE:
                setearCliente(resultCode,data);
                break;
            case REQUEST_AGREGAR_PRODUCTO:
                setearProducto(resultCode,data);
                break;
            default:
                Toast.makeText(this, "No existe esa solicitud", Toast.LENGTH_SHORT).show();
        }
    }

    private void setearCliente(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            try {
                cliente = (Cliente) data.getExtras().getSerializable(Util.CLIENTE_SELECCIONADO);
                Util.clienteSeleccionado = cliente;
                txt_dato_cliente.setText(Util.textoCapitalizado(Util.clienteSeleccionado.getNombre()) + "\n Tipo: " + Util.clienteSeleccionado.getTipoCliente() + "        " + Util.clienteSeleccionado.getReferencia());
                txt_direccion_cliente.setText("" + Util.clienteSeleccionado.getDireccion());
                consultarAtrasoCliente(cliente.getIdCliente());
                listaProductosPedido = new ArrayList<>();
                Util.productosSeleccionados = new ArrayList<>();
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void consultarAtrasoCliente(final int idCliente) {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Cargando datos del cliente...").setCancelable(false).build();
        dialog.show();
        serviceApi.obtenerAtrasoCliente(idCliente).enqueue(new Callback<RespuestaAtrasoCredito>() {
            @Override
            public void onResponse(Call<RespuestaAtrasoCredito> call, Response<RespuestaAtrasoCredito> response) {
                try{
                    RespuestaAtrasoCredito respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        if(respuesta.getResult().size() > 0) {
                            Util.clienteSeleccionado.setAtraso(respuesta.getResult().get(0).getDias());
                            obtenerMaximoPagoCliente(idCliente,dialog);
                        }else{
                            mensajeInformativo("Mensaje","El cliente no tiene deudas pendientes");
                            obtenerMaximoPagoCliente(idCliente,dialog);
                            btn_agregarProducto.setEnabled(true);
                            btn_registrar_pedido.setEnabled(true);
                            rdb_credito.setEnabled(true);
                        }
                    }else{
                        Toast.makeText(RegistrarPedidoActivity.this, "No se puso traer los datos, intente de nuevo con otro cliente", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAtrasoCredito> call, Throwable t) {
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                dialog.dismiss();
            }
        });
    }

    private void obtenerMaximoPagoCliente(int idCliente, final AlertDialog dialog) {
        serviceApi.obtenerMaxPagoCredito(idCliente).enqueue(new Callback<RespuestaAtrasoCredito>() {
            @Override
            public void onResponse(Call<RespuestaAtrasoCredito> call, Response<RespuestaAtrasoCredito> response) {
                try {
                    RespuestaAtrasoCredito respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        if(respuesta.getResult().size() > 0){
                            Util.clienteSeleccionado.setMaxPago(respuesta.getResult().get(0).getDias());
                            setearEstadoVistaCliente();
                            dialog.dismiss();
                        }else{
                            dialog.dismiss();
                            mensajeInformativo("Mensaje","No tiene máximo día de pago");
                        }
                    }else{
                        Toast.makeText(RegistrarPedidoActivity.this, "Error al traer los datos del clientes", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAtrasoCredito> call, Throwable t) {
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                dialog.dismiss();
            }
        });
    }


    private void setearProducto(int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            try{
                if(data.hasExtra(Util.TIPO)){
                    CabeceraDescuento cabeceraDescuento = (CabeceraDescuento) data.getExtras().getSerializable(Util.TIPO);
                    adapterPromo.setPromoDsctoList(Util.descuentosSeleccionados);
                    setearEstadoVista();
                    realizarCambiosSegunTipo(cabeceraDescuento,ACTIVAR_DESCUENTO);
                }else {
                    Producto producto = (Producto) data.getExtras().getSerializable(Util.PRODUCTO_SELECCIONADO);
                    listaProductosPedido.add(producto);
                    Util.productosSeleccionados.add(producto);
                    setearEstadoVista();
                    if(!cumpleConPrecioMayor(producto)){
                        buscarPromocionDescuento(producto);
                    }else {
                        if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay2()) {
                            preguntarUsuario(producto,PRECIO_MAY_2,Util.productosSeleccionados.size()-1);
                        }else if(Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay()){
                            preguntarUsuario(producto,PRECIO_MAY, Util.productosSeleccionados.size()-1);
                        }else if(Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= producto.getCantidadvol()){
                            preguntarUsuario(producto,PRECIO_VOL,Util.productosSeleccionados.size()-1);
                        }
                    }
                }
            }catch (Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void buscarPromocionDescuento(final Producto producto) {
        setearTotal();
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Buscando promociones o descuentos...").setCancelable(false).build();
        dialog.show();
        Calendar fechaActual = Calendar.getInstance();
        serviceApi.obtenerCabeceraDescuento(
                producto.getIdProducto(),
                fechaActual.get(Calendar.YEAR)+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+agregarCeroAFecha(fechaActual.get(Calendar.DATE)),
                1
        ).enqueue(new Callback<RespuestaCabeceraDescuento>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraDescuento> call, Response<RespuestaCabeceraDescuento> response) {
                try {
                    final RespuestaCabeceraDescuento respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        if (respuesta.getResult().size() > 0) {
                            dialog.dismiss();
                            if(respuesta.getResult().size() == 1) {
                                obtenerCabeceraPromocionDescuento(respuesta.getResult().get(0).getIdProducto());
                            }else{
                                final List<String> promoDescuentosString=new ArrayList<>();  // here is list
                                for(int i=0;i<respuesta.getResult().size();i++) {
                                    promoDescuentosString.add(respuesta.getResult().get(i).getNombreDescuento());
//                                    if(producto.getTotalunidadesPedido() >= respuesta.getResult().get(i).getUnidades() && producto.getCantidadSoles() == 1) {
//                                        promoDescuentosString.add(respuesta.getResult().get(i).getNombreDescuento());
//                                    }else if(producto.getTotal() >= respuesta.getResult().get(i).getUnidades()){
//                                        promoDescuentosString.add(respuesta.getResult().get(i).getNombreDescuento());
//                                    }
                                }
                                AlertDialog.Builder alt_bld = new AlertDialog.Builder(RegistrarPedidoActivity.this);
                                alt_bld.setTitle("Seleccionar una opción");
                                ArrayAdapter<String> promoDescuentos = new ArrayAdapter<String>(RegistrarPedidoActivity.this, android.R.layout.simple_list_item_single_choice,promoDescuentosString);
                                alt_bld.setSingleChoiceItems(promoDescuentos, -1, new DialogInterface
                                        .OnClickListener() {
                                    public void onClick(DialogInterface dialog, int item) {
                                        obtenerCabeceraPromocionDescuento(respuesta.getResult().get(item).getIdProducto());
                                        dialog.dismiss();// dismiss the alertbox after chose option
                                    }
                                });
                                AlertDialog alert = alt_bld.create();
                                alert.show();


                            }

                        } else {
                            dialog.dismiss();
                            setearTotal();
                            Toast.makeText(RegistrarPedidoActivity.this, "No hay promociones o descuentos con ese producto", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegistrarPedidoActivity.this, "No se pudo traer los datos,intentelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch(Exception e){
                    dialog.dismiss();
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RespuestaCabeceraDescuento> call, Throwable t) {
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                dialog.dismiss();
            }
        });
    }

    private void obtenerCabeceraPromocionDescuento(int idProducto) {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Cargando promocion o descuento...").setCancelable(false).build();
        dialog.show();
        Calendar fechaActual = Calendar.getInstance();

        serviceApi.obtenerCabeceraDescuento(idProducto,
                fechaActual.get(Calendar.YEAR)+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+agregarCeroAFecha(fechaActual.get(Calendar.DATE)),
                3).enqueue(new Callback<RespuestaCabeceraDescuento>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraDescuento> call, Response<RespuestaCabeceraDescuento> response) {
                try {
                    RespuestaCabeceraDescuento respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        boolean existe = false;
                        CabeceraDescuento cabeceraDescuento = respuesta.getResult().get(0);
                        for(CabeceraDescuento dato : Util.descuentosSeleccionados){
                            if(dato.getIdProducto() == cabeceraDescuento.getIdProducto()){
                                existe = true;
                            }
                        }
                        if(!existe) {
                            serviceObtenerDetallePedido(cabeceraDescuento);
                        }else{
                            dialog.dismiss();
                            realizarCambiosSegunTipo(cabeceraDescuento,ACTIVAR_DESCUENTO);
                        }
                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegistrarPedidoActivity.this, "No se pudo traer los datos,intentelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialog.dismiss();
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCabeceraDescuento> call, Throwable t) {
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                dialog.dismiss();
            }
        });
    }

    private void serviceObtenerDetallePedido(final CabeceraDescuento cabeceraDescuento) {
        Calendar fechaActual = Calendar.getInstance();
        serviceApi.obtenerCabeceraDescuento(cabeceraDescuento.getIdProducto(),
                fechaActual.get(Calendar.YEAR)+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+agregarCeroAFecha(fechaActual.get(Calendar.DATE)),
                2).enqueue(new Callback<RespuestaCabeceraDescuento>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraDescuento> call, Response<RespuestaCabeceraDescuento> response) {
                try {
                    RespuestaCabeceraDescuento respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        Util.detalleDescuentosSeleccionados.addAll(respuesta.getResult());
                        Util.descuentosSeleccionados.add(cabeceraDescuento);
                        dialog.dismiss();
                        adapterPromo.setPromoDsctoList(Util.descuentosSeleccionados);
                        setearEstadoVista();
                        realizarCambiosSegunTipo(cabeceraDescuento,ACTIVAR_DESCUENTO);
                    } else {
                        dialog.dismiss();
                        Toast.makeText(RegistrarPedidoActivity.this, "No se pudo traer los datos,intentelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialog.dismiss();
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCabeceraDescuento> call, Throwable t) {
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
                dialog.dismiss();
            }
        });
    }


    private void setearEstadoVista(){
        if(Util.productosSeleccionados.size()>0){
            txt_sin_datos.setVisibility(View.GONE);
            recycler_registrar_producto.setVisibility(View.VISIBLE);
            if(Util.descuentosSeleccionados.size()>0){
                root_promociones.setVisibility(View.VISIBLE);
                recycler_promociones_descuentos.setVisibility(View.VISIBLE);
            }else{
                root_promociones.setVisibility(View.GONE);
                recycler_promociones_descuentos.setVisibility(View.GONE);
            }
        }else{
            txt_sin_datos.setVisibility(View.VISIBLE);
            recycler_registrar_producto.setVisibility(View.GONE);
            root_promociones.setVisibility(View.GONE);
            recycler_promociones_descuentos.setVisibility(View.GONE);
        }
    }

    private void setearEstadoVistaCliente(){
        if(Util.clienteSeleccionado.getAtraso() != 0){
            int dias= 0;
            switch (preferences.getString(Util.TIPO_USUARIO,"")){
                case "X":
                    dias = 0;
                    break;
                case "M":
                    dias = 30;
                    break;
                default:
                    dias = -1;
            }
            definirSituacionCliente(dias);
        }
    }

    private void definirSituacionCliente(int dias) {
        if(dias != -1) {
            int atraso = Util.clienteSeleccionado.getAtraso();
            if (atraso >= 45 && atraso <= (59 + dias)) {
                mensajeInformativo("Mensaje","El cliente solo puede realizar el pedido al contado");
                rdb_credito.setEnabled(false);
            } else if (atraso >= (60 + dias)) {
                mensajeInformativo("Mensaje","El cliente no puede ser atendido por tener una deuda pendiente");
                btn_registrar_pedido.setEnabled(false);
                btn_agregarProducto.setEnabled(false);
            }
        }else{
            Toast.makeText(this, "No se pudo obtener los dias de atraso del cliente", Toast.LENGTH_SHORT).show();
        }
    }

    private void mensajeInformativo(String titulo, String mensaje) {
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


    public void setearTotal(){
        total = 0;
        for (int i=0; i< Util.productosSeleccionados.size();i++) {
            Producto producto = Util.productosSeleccionados.get(i);
            if(!producto.isBonificacion()) {
                if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay2()) {
                    if(producto.getTipoprecioventa() > 1) {
                        calcularPrecio(producto, PRECIO_MAY_2, i);
                    }else{
                        if (rdb_contado.isChecked()) {
                            calcularPrecio(producto,CONTADO_CHECK,i);
                        }else{
                            calcularPrecio(producto,CREDITO_CHECK,i);
                        }
                    }
                } else if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay()) {
                    if(producto.getTipoprecioventa() > 1) {
                        calcularPrecio(producto, PRECIO_MAY, i);
                    }else{
                        if (rdb_contado.isChecked()) {
                            calcularPrecio(producto,CONTADO_CHECK,i);
                        }else{
                            calcularPrecio(producto,CREDITO_CHECK,i);
                        }
                    }
                } else if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadvol()) {
                    if(producto.getTipoprecioventa() > 1) {
                        calcularPrecio(producto, PRECIO_VOL, i);
                    }else{
                        if (rdb_contado.isChecked()) {
                            calcularPrecio(producto,CONTADO_CHECK,i);
                        }else{
                            calcularPrecio(producto,CREDITO_CHECK,i);
                        }
                    }
                } else if (rdb_contado.isChecked()) {
                    calcularPrecio(producto,CONTADO_CHECK,i);
                } else if (rdb_credito.isChecked()) {
                    calcularPrecio(producto,CREDITO_CHECK,i);
                }
            }
            txt_total_pedido.setText(String.format("S/.%.2f", total));
        }
        txt_total_pedido.setText(String.format("S/.%.2f", total));

    }

    private boolean cumpleConPrecioMayor(Producto producto){
        if(Util.clienteSeleccionado.isPrecioVolumen() && producto.getCantidadmay2() > 0 && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay2()){
            return true;
        }else if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getCantidadmay() > 0 && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay()){
            return true;
        }else if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getCantidadvol() > 0 && producto.getTotalunidadesPedido() >= (int) producto.getCantidadvol()){
            return true;
        }
        return false;
    }

    private void preguntarUsuario(final Producto producto, final int tipoPrecio, final int posicion){

        double precioProd;

        switch (tipoPrecio){
            case CONTADO_CHECK :
                precioProd = producto.getPreciocon();
                break;
            case CREDITO_CHECK:
                precioProd = producto.getPreciocre();
                break;
            case PRECIO_VOL:
                precioProd = producto.getPreciovol();
                break;
            case PRECIO_MAY:
                precioProd = producto.getPreciomay();
                break;
            case PRECIO_MAY_2:
                precioProd = producto.getPreciomay2();
                break;
            default:
                precioProd = 0;
        }

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Precio por mayor");
        builder.setMessage(String.format("¿Desea aplicar precio por volumen de S/.%.2f a S/.%.2f ?",producto.getPreciocon(),precioProd) );
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                aceptarVol(producto,tipoPrecio,posicion);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                negarVol(producto,posicion);
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }

    private void calcularPrecio(Producto producto, int tipoPrecio,int posicion){
        double precioProd = 0;
        switch (tipoPrecio){
            case CONTADO_CHECK :
                precioProd = producto.getPreciocon();
                Util.productosSeleccionados.get(posicion).setTipoprecioventa(1);
                break;
            case CREDITO_CHECK:
                precioProd = producto.getPreciocre();
                Util.productosSeleccionados.get(posicion).setTipoprecioventa(1);
                break;
            case PRECIO_VOL:
                precioProd = producto.getPreciovol();
                Util.productosSeleccionados.get(posicion).setTipoprecioventa(2);
                break;
            case PRECIO_MAY:
                precioProd = producto.getPreciomay();
                Util.productosSeleccionados.get(posicion).setTipoprecioventa(3);
                break;
            case PRECIO_MAY_2:
                precioProd = producto.getPreciomay2();
                Util.productosSeleccionados.get(posicion).setTipoprecioventa(4);
                break;
        }
        if(!producto.isBonificacion()) {
            double precio = (producto.getTotalunidadesPedido() / (double) producto.getFactor() * precioProd) * (100 - producto.getDescuento()) / (double) 100;
            Util.productosSeleccionados.get(posicion).setTotal(precio);
            adapter.setProductoList(Util.productosSeleccionados, tipoPrecio);
            total += precio;
        }
    }

    private String agregarCeroAFecha(int numeroFecha){
        return numeroFecha<10? "0"+numeroFecha: ""+numeroFecha;
    }


    @Override
    public void onBackPressed() {
        if(!txt_dato_cliente.getText().toString().isEmpty()) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Salir de pantalla");
            builder.setMessage("¿Desea regresar a la pantalla principal?, podria perder todo el progreso");
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
        }else{
            finish();
        }
    }

    @Override
    public void contarProducto() {
        productosEnviadosaBD ++;
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Pedido");
        builder.setMessage("Se registro el pedido de " + Util.clienteSeleccionado.getNombre());
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
    public void terminarProducto() {
        if(productosEnviadosaBD == Util.productosSeleccionados.size()){
            dialog.dismiss();
            Util.productosSeleccionados.clear();
            Util.clienteSeleccionado = new Cliente();
            Util.descuentosSeleccionados.clear();
            Util.detalleDescuentosSeleccionados.clear();
            setearEstadoVista();
            txt_total_pedido.setText("S/.00.00");
            txt_dato_cliente.setText("");
            txt_direccion_cliente.setText("");
            txt_dias_crédito.setText("");
            layout_dias_credito.setVisibility(View.GONE);
            txt_dias_crédito.setVisibility(View.GONE);
            root_promociones.setVisibility(View.GONE);
            rdb_credito.setEnabled(true);
            btn_registrar_pedido.setEnabled(true);
            btn_agregarProducto.setEnabled(false);
        }
    }

    @Override
    public void falloDeProducto() {
        Toast.makeText(this, "Fallo en enviar el producto ", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
    }

    @Override
    public void cargando() {

    }

    @Override
    public void finalizado() {

    }


    //Eliminar Producto
    @Override
    public void onItemClick(final int position) {
        final android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(RegistrarPedidoActivity.this);
            builder.setTitle("Eliminar Producto");
            builder.setMessage("¿Desea eliminar el producto seleccionado?");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Identificar si el item se encuentra en algun detalle de un descuento o bonificacion
                    int obtenerIdCabecera = -1;
                    for(CabeceraDescuento dato : Util.detalleDescuentosSeleccionados){
                        if(dato.getIdProdComponente() == Util.productosSeleccionados.get(position).getIdProducto()){
                            obtenerIdCabecera = dato.getIdProducto();
                        }
                    }
                    //Eliminar el producto y setear el total
                    Util.productosSeleccionados.remove(position);
                    adapter.notifyItemRemoved(position);
                    setearTotal();
                    //Si encontro entonces la cabecera y veo si se activa o desactiva
                    if(obtenerIdCabecera >=0) {
                        for (CabeceraDescuento dato : Util.descuentosSeleccionados) {
                            if (obtenerIdCabecera == dato.getIdProducto()) {
                                realizarCambiosSegunTipo(dato, ACTIVAR_DESCUENTO);
                                break;
                            }
                        }
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("No", null);
            builder.create();
            builder.show();
    }

    //Cambiar Cantidad de Producto
    @Override
    public void onViewIntemClick(final Producto producto, final int position) {

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
                validarDatos(txt_cantidad,txt_unidades, producto,view_cantidad,position);
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();

    }

     private void validarDatos(TextView txt_cantidad,TextView txt_unidades, Producto producto,View view_cantidad,int position) {

         String cantidad = txt_cantidad.getText().toString().trim();
         String unidades = txt_unidades.getText().toString().trim();

         if(TextUtils.isEmpty(cantidad) && TextUtils.isEmpty(unidades)){
             alertaCantidadInvalida(view_cantidad);
         }else {
             if (!TextUtils.isEmpty(cantidad) && !TextUtils.isEmpty(unidades)) {
                 if (TextUtils.isDigitsOnly(cantidad) && TextUtils.isDigitsOnly(unidades)) {
                     producto.setCantidadPedido(Integer.parseInt(cantidad));
                     producto.setUnidadesPedido(Integer.parseInt(unidades));
                     producto.setTotalunidadesPedido((producto.getCantidadPedido() * producto.getFactor() + producto.getUnidadesPedido()));
                     if (producto.getTotalunidadesPedido() > producto.getSaldoreal()) {
                         alertaExceso(producto, view_cantidad);
                     } else {
                         actualizarDatosProducto(position);
                     }
                 } else {
                     alertaCantidadInvalida(view_cantidad);
                 }
             } else {
                 if (!TextUtils.isEmpty(cantidad) && TextUtils.isDigitsOnly(cantidad)) {
                     producto.setCantidadPedido(Integer.parseInt(cantidad));
                     producto.setUnidadesPedido(0);
                     producto.setTotalunidadesPedido(producto.getCantidadPedido() * producto.getFactor());
                     if (producto.getTotalunidadesPedido() > producto.getSaldoreal()) {
                         alertaExceso(producto, view_cantidad);
                     } else {
                         actualizarDatosProducto(position);
                     }
                 } else {
                     alertaCantidadInvalida(view_cantidad);
                 }
                 if (!TextUtils.isEmpty(unidades) && TextUtils.isDigitsOnly(unidades)) {
                     producto.setUnidadesPedido(Integer.parseInt(unidades));
                     producto.setCantidadPedido(0);
                     producto.setTotalunidadesPedido(producto.getUnidadesPedido());
                     if (producto.getTotalunidadesPedido() > producto.getSaldoreal()) {
                         alertaExceso(producto, view_cantidad);
                     } else {
                         actualizarDatosProducto(position);
                     }
                 } else {
                     alertaCantidadInvalida(view_cantidad);
                 }
             }
        }
    }

    private void actualizarDatosProducto(int position){
        adapter.notifyItemChanged(position);
        Producto producto = Util.productosSeleccionados.get(position);
        if(!cumpleConPrecioMayor(producto)) {
            setearTotal();
            buscarPromocionDescuento(producto);
        }else {
            setearTotal();
            if (Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay2()) {
                calcularPrecio(producto,PRECIO_MAY_2,Util.productosSeleccionados.size()-1);
            }else if(Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= (int) producto.getCantidadmay()){
                calcularPrecio(producto,PRECIO_MAY,Util.productosSeleccionados.size()-1);
            }else if(Util.clienteSeleccionado.isPrecioVolumen() && producto.getTotalunidadesPedido() >= producto.getCantidadvol()){
                calcularPrecio(producto,PRECIO_VOL,Util.productosSeleccionados.size()-1);
            }
            //Ver si hay Promo y descuento actual

//            int obtenerIdCabecera = -1;
//            for (CabeceraDescuento dato : Util.detalleDescuentosSeleccionados) {
//                if (dato.getIdProdComponente() == producto.getIdProducto()) {
//                    obtenerIdCabecera = dato.getIdProducto();
//                }
//            }
//            for (CabeceraDescuento dato : Util.descuentosSeleccionados) {
//                if (obtenerIdCabecera == dato.getIdProducto()) {
//                    realizarCambiosSegunTipo(dato, ACTIVAR_DESCUENTO);
//                }
//            }
        }
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

    private void realizarCambiosSegunTipo(CabeceraDescuento cabeceraDescuento,int tipoEvento) {
        if(cabeceraDescuento.getTipoCombo() > 0 && cabeceraDescuento.getTipoCombo() <= 3){
            List<CabeceraDescuento> detalleDescuento = new ArrayList<>();
            obtenerDetalleDescuento(cabeceraDescuento.getIdProducto(),detalleDescuento);
//            if(detalleDescuento.size()>0) {
//                if (TextUtils.equals(detalleDescuento.get(0).getCriterio().trim(),"O")) {
//                    activarCriterioO(detalleDescuento,cabeceraDescuento,tipoEvento);
//                } else if (TextUtils.equals(detalleDescuento.get(0).getCriterio().trim(),"Y")) {
//                    activarCriterioIgual(detalleDescuento,cabeceraDescuento,tipoEvento);
//                }else if(TextUtils.equals(detalleDescuento.get(0).getCriterio().trim(),"=")){
//                    activarCriterioIgual(detalleDescuento,cabeceraDescuento,tipoEvento);
//                }
//            }
            if(detalleDescuento.size() > 0){
                List<CabeceraDescuento> criterioO = new ArrayList<>();
                List<CabeceraDescuento> criterioY = new ArrayList<>();
                for (CabeceraDescuento dato : detalleDescuento){
                    if(TextUtils.equals(dato.getCriterio().trim(),"O")) {
                        criterioO.add(dato);
                    }
                    if(TextUtils.equals(dato.getCriterio().trim(),"Y")){
                        criterioY.add(dato);
                    };
                }
                aplicarDescuentoPromo(criterioO,criterioY,cabeceraDescuento,tipoEvento);
            }
        }else{
            Toast.makeText(this, "No es un descuento,no se aplicará ningun cambio,por favor,borrelo", Toast.LENGTH_SHORT).show();
        }

    }

    private void aplicarDescuentoPromo(List<CabeceraDescuento> criterioO, List<CabeceraDescuento> criterioY, CabeceraDescuento cabeceraDescuento, int tipoEvento) {
        //Calcular promedio
        int suma = 0;
        double promedio;
        for(CabeceraDescuento dato : criterioO){
            if(dato.getCantidadSoles() == 1) {
                suma += dato.getUnidades();
            }else if(dato.getCantidadSoles() == 2){
                suma += dato.getCantidad();
            }
        }
        promedio = suma/(double) criterioO.size();

        //Obtengo productos que hay en pedido que se aplican descuento criterio O
        List<Producto> productosReconocidosO = obtenerProductosReconocidos(criterioO);
        //Obtengo productos que hay en pedido que se aplican descuento criterio Y
        List<Producto> productosReconocidosY = obtenerProductosReconocidos(criterioY);

        if(productosReconocidosO.size() == 0 && productosReconocidosY.size() == 0){
            if(tipoEvento == ACTIVAR_DESCUENTO) {
                Toast.makeText(this, "No se encuentra ningun producto que aplique " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                if(cabeceraDescuento.getTipoCombo() == 1 || cabeceraDescuento.getTipoCombo() == 2){
                    int posicion = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                    if(posicion >= 0) {
                        Util.productosSeleccionados.remove(posicion);
                        adapter.notifyItemRemoved(posicion);
                        setearTotal();
                    }
                }
                for(int i = 0;i<Util.detalleDescuentosSeleccionados.size();i++){
                    if(Util.detalleDescuentosSeleccionados.get(i).getIdProducto() == cabeceraDescuento.getIdProducto()){
                        Util.detalleDescuentosSeleccionados.remove(i);
                        adapterPromo.notifyDataSetChanged();
                    }
                }
                for(int i=0;i<Util.descuentosSeleccionados.size();i++){
                    if(Util.descuentosSeleccionados.get(i).getIdProducto() == cabeceraDescuento.getIdProducto()){
                        Util.descuentosSeleccionados.remove(i);
                        adapterPromo.notifyDataSetChanged();
                    }
                }
            }else{
                if(cabeceraDescuento.getTipoCombo() == 1 || cabeceraDescuento.getTipoCombo() == 2){
                    int posicion = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                    if(posicion >= 0) {
                        Util.productosSeleccionados.remove(posicion);
                        adapter.notifyItemRemoved(posicion);
                        setearTotal();
                    }
                }
                for(int i = 0;i<Util.detalleDescuentosSeleccionados.size();i++){
                    if(Util.detalleDescuentosSeleccionados.get(i).getIdProducto() == cabeceraDescuento.getIdProducto()){
                        Util.detalleDescuentosSeleccionados.remove(i);
                        adapterPromo.notifyDataSetChanged();
                    }
                }
            }
        }else{

            //Saber si los productos del criterio Y estan hechos
            boolean esTodoCorrecto = true;
            for(CabeceraDescuento detalle : criterioY){
                for (Producto dato : productosReconocidosY) {
                    int posicion = versiEstaEnPedido(dato, BUSCAR_PRODUCTO);
                    if(posicion >= 0) {
                        if(cabeceraDescuento.getCantidadSoles() == 1) {
                            if (Util.productosSeleccionados.get(posicion).getTotalunidadesPedido() < detalle.getUnidades()) {
                                esTodoCorrecto = false;
                                break;
                            }
                        }else{
                            if(Util.productosSeleccionados.get(posicion).getTotal() < detalle.getCantidad()){
                                esTodoCorrecto = false;
                                break;
                            }
                        }
                    }
                }
            }

            //Ver si es surtido o no, si es surtido entonces promedio sino entonces cualquiera
            if(cabeceraDescuento.isSurtido()){
                //Obtener suma de cantidad segun criterio O
                double sumaCantidad = 0;
                for (Producto dato : productosReconocidosO) {
                    if(cabeceraDescuento.getCantidadSoles() == 1) {
                        sumaCantidad += dato.getTotalunidadesPedido();
                    }else if(cabeceraDescuento.getCantidadSoles() == 2){
                        sumaCantidad += dato.getTotal();
                    }
                }

                if(sumaCantidad >= promedio && esTodoCorrecto){
                    //Agregando productosReconocidos lo que se tiene de cada condicion
                    List<Producto> productosReconocidos = new ArrayList<>();
                    productosReconocidos.addAll(productosReconocidosO);
                    productosReconocidos.addAll(productosReconocidosY);

                    double descuento = cabeceraDescuento.getPorDes();
                    int posicionProducto = -1;
                    int cantidad = (int) Math.floor(sumaCantidad / promedio) * cabeceraDescuento.getUnidades();
                    int cantidadBonificacion  = (cantidad > cabeceraDescuento.getCatiMax() * cabeceraDescuento.getUnidades()) ? (int) cabeceraDescuento.getCatiMax() * cabeceraDescuento.getUnidades() : cantidad;
                    Toast.makeText(this, "Se aplica " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_LONG).show();
                    for (Producto dato : productosReconocidos) {
                        int posicion = versiEstaEnPedido(dato,BUSCAR_PRODUCTO);
                        posicionProducto = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                        if(posicion >= 0) {
                            if (cabeceraDescuento.getTipoCombo() == 3) {
                                if (tipoEvento == ACTIVAR_DESCUENTO) {
                                    Util.productosSeleccionados.get(posicion).setCombo(true);
                                    Util.productosSeleccionados.get(posicion).setIdCombo(cabeceraDescuento.getIdProducto());
                                    Util.productosSeleccionados.get(posicion).setDescuento(descuento);
                                    Util.productosSeleccionados.get(posicion).setTipocombo(3);
                                    Util.productosSeleccionados.get(posicion).setSurtido(cabeceraDescuento.isSurtido());
                                    Util.productosSeleccionados.get(posicion).setCantidadSoles(cabeceraDescuento.getCantidadSoles());
                                } else {
                                    Util.productosSeleccionados.get(posicion).setCombo(false);
                                    Util.productosSeleccionados.get(posicion).setIdCombo(0);
                                    Util.productosSeleccionados.get(posicion).setDescuento(0);
                                    Util.productosSeleccionados.get(posicion).setTipocombo(0);
                                    Util.productosSeleccionados.get(posicion).setSurtido(false);
                                    Util.productosSeleccionados.get(posicion).setCantidadSoles(0);
                                }
                            }else {
                                Util.productosSeleccionados.get(posicion).setTipocombo(cabeceraDescuento.getTipoCombo());
                                Util.productosSeleccionados.get(posicion).setSurtido(cabeceraDescuento.isSurtido());
                                Util.productosSeleccionados.get(posicion).setIdCombo(cabeceraDescuento.getIdProducto());
                                Util.productosSeleccionados.get(posicion).setCantidadSoles(cabeceraDescuento.getCantidadSoles());
                            }
                        }
                        setearTotal();
                        adapter.notifyDataSetChanged();
                    }

                    if(cabeceraDescuento.getTipoCombo() == 2 || cabeceraDescuento.getTipoCombo() == 1){
                        if(tipoEvento == ACTIVAR_DESCUENTO) {
                            if (posicionProducto >= 0) {
                                Producto bonificacion = Util.productosSeleccionados.get(posicionProducto);
                                int cantidadPro = cantidadBonificacion / bonificacion.getFactor();
                                int unidad = cantidadBonificacion % bonificacion.getFactor();
                                bonificacion.setCantidadPedido(cantidadPro);
                                bonificacion.setUnidadesPedido(unidad);
                                bonificacion.setTotalunidadesPedido(cantidadBonificacion);
                                bonificacion.setBonificacion(true);
                                setearTotal();
                                adapter.notifyDataSetChanged();

                            } else {
                                obtenerProductoBonoficacion(cabeceraDescuento, cantidadBonificacion);
                            }
                        }
                    }

                    if(tipoEvento == DESACTIVAR_DDESCUENTO){
                        for (Producto dato : productosReconocidos) {
                            int posicion = versiEstaEnPedido(dato,BUSCAR_PRODUCTO);
                            if(posicion >= 0){
                                Util.productosSeleccionados.get(posicion).setTipocombo(0);
                                Util.productosSeleccionados.get(posicion).setSurtido(false);
                                Util.productosSeleccionados.get(posicion).setIdCombo(0);
                                Util.productosSeleccionados.get(posicion).setCantidadSoles(0);
                            }
                        }
                        if(cabeceraDescuento.getTipoCombo() == 1 || cabeceraDescuento.getTipoCombo() == 2){
                            int posicionP = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                            if(posicionP >= 0) {
                                Util.productosSeleccionados.remove(posicionP);
                                adapter.notifyItemRemoved(posicionP);
                                setearTotal();
                            }
                        }
                        for(int i = 0;i<Util.detalleDescuentosSeleccionados.size();i++){
                            if(Util.detalleDescuentosSeleccionados.get(i).getIdProducto() == cabeceraDescuento.getIdProducto()){
                                Util.detalleDescuentosSeleccionados.remove(i);
                                adapterPromo.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(this, "Se desactivó " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    List<Producto> productosReconocidos = new ArrayList<>();
                    productosReconocidos.addAll(productosReconocidosO);
                    productosReconocidos.addAll(productosReconocidosY);
                    int posicionBon = -1;
                    posicionBon = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                    if(posicionBon >= 0){
                        if(cabeceraDescuento.getTipoCombo() == 1 || cabeceraDescuento.getTipoCombo() == 2){
                            Util.productosSeleccionados.remove(posicionBon);
                            adapter.notifyItemRemoved(posicionBon);
                            setearTotal();
                            Toast.makeText(this, "Se desactivó " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "No cumple requisito " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                        setearTotal();
                    }
                }
            }else{

                boolean esTodoCorrectoO = false;
                for(CabeceraDescuento detalle : criterioO){
                    for (Producto dato : productosReconocidosO) {
                        int posicion = versiEstaEnPedido(dato, BUSCAR_PRODUCTO);
                        if(posicion >= 0) {
                            if(cabeceraDescuento.getCantidadSoles() == 1) {
                                if (Util.productosSeleccionados.get(posicion).getTotalunidadesPedido() >= detalle.getUnidades()) {
                                    esTodoCorrectoO = true;
                                    break;
                                }
                            }else if(cabeceraDescuento.getCantidadSoles() == 2){
                                if(Util.productosSeleccionados.get(posicion).getTotal() >= detalle.getCantidad()){
                                    esTodoCorrectoO = true;
                                    break;
                                }
                            }
                        }
                    }
                }

                if(esTodoCorrecto && esTodoCorrectoO) {
                    int bonificacion = 0;
                    int sumaPedido = 0;
                    int posicionBonificacion = versiEstaEnPedido(cabeceraDescuento.getIdProdComponente(), BUSCAR_BONO);
                    if(posicionBonificacion >= 0){
                        bonificacion = Util.productosSeleccionados.get(posicionBonificacion).getTotalunidadesPedido();
                    }


                    for(CabeceraDescuento detalle : criterioO) {
                        int position = versiEstaEnPedido(detalle.getIdProdComponente(), BUSCAR_PRODUCTO);
                        if (position >= 0) {
                            Producto producto = Util.productosSeleccionados.get(position);
                            if(cabeceraDescuento.getCantidadSoles() == 1) {
                                if (producto.getTotalunidadesPedido() >= detalle.getUnidades()) {
                                    sumaPedido += producto.getTotalunidadesPedido() / detalle.getUnidades() * cabeceraDescuento.getUnidades();
                                }
                            }else{
                                if(producto.getTotal() >= detalle.getCantidad()){
                                    sumaPedido += producto.getTotal() / detalle.getCantidad() * cabeceraDescuento.getUnidades();
                                }
                            }
                        }
                    }

                    bonificacion =  sumaPedido > cabeceraDescuento.getCatiMax() * cabeceraDescuento.getUnidades() ? (int) cabeceraDescuento.getCatiMax() * cabeceraDescuento.getUnidades() : sumaPedido;

                    List<Producto> productosReconocidos = new ArrayList<>();
                    productosReconocidos.addAll(productosReconocidosO);
                    productosReconocidos.addAll(productosReconocidosY);

                    double descuento = cabeceraDescuento.getPorDes();
                    int posicionProducto = -1;
                    Toast.makeText(this, "Se aplica " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_LONG).show();
                    for (Producto dato : productosReconocidos) {
                        int posicion = versiEstaEnPedido(dato, BUSCAR_PRODUCTO);
                        posicionProducto = verSiEstaEnPedido(cabeceraDescuento, BUSCAR_BONO);
                        if(posicion >= 0) {
                            if (cabeceraDescuento.getTipoCombo() == 3) {
                                if (tipoEvento == ACTIVAR_DESCUENTO) {
                                    Util.productosSeleccionados.get(posicion).setCombo(true);
                                    Util.productosSeleccionados.get(posicion).setIdCombo(cabeceraDescuento.getIdProducto());
                                    Util.productosSeleccionados.get(posicion).setDescuento(descuento);
                                    Util.productosSeleccionados.get(posicion).setTipocombo(3);
                                    Util.productosSeleccionados.get(posicion).setSurtido(cabeceraDescuento.isSurtido());
                                    Util.productosSeleccionados.get(posicion).setCantidadSoles(cabeceraDescuento.getCantidadSoles());
                                } else {
                                    Util.productosSeleccionados.get(posicion).setCombo(false);
                                    Util.productosSeleccionados.get(posicion).setIdCombo(0);
                                    Util.productosSeleccionados.get(posicion).setDescuento(0);
                                    Util.productosSeleccionados.get(posicion).setTipocombo(0);
                                    Util.productosSeleccionados.get(posicion).setSurtido(false);
                                    Util.productosSeleccionados.get(posicion).setCantidadSoles(0);
                                }
                            }else {
                                Util.productosSeleccionados.get(posicion).setTipocombo(cabeceraDescuento.getTipoCombo());
                                Util.productosSeleccionados.get(posicion).setSurtido(cabeceraDescuento.isSurtido());
                                Util.productosSeleccionados.get(posicion).setIdCombo(cabeceraDescuento.getIdProducto());
                                Util.productosSeleccionados.get(posicion).setCantidadSoles(cabeceraDescuento.getCantidadSoles());
                            }
                        }
                        setearTotal();
                        adapter.notifyDataSetChanged();
                    }

                    if(cabeceraDescuento.getTipoCombo() == 2 || cabeceraDescuento.getTipoCombo() == 1){
                        if(tipoEvento == ACTIVAR_DESCUENTO) {
                            if (posicionProducto >= 0) {
                                Producto bonif = Util.productosSeleccionados.get(posicionProducto);
                                int cantidadPro = bonificacion / bonif.getFactor();
                                int unidad = bonificacion % bonif.getFactor();
                                bonif.setCantidadPedido(cantidadPro);
                                bonif.setUnidadesPedido(unidad);
                                bonif.setTotalunidadesPedido(bonificacion);
                                bonif.setBonificacion(true);
                                setearTotal();
                                adapter.notifyDataSetChanged();

                            } else {
                                obtenerProductoBonoficacion(cabeceraDescuento, bonificacion);
                            }
                        }
                    }


                    if(tipoEvento == DESACTIVAR_DDESCUENTO){
                        for (Producto dato : productosReconocidos) {
                            int posicion = versiEstaEnPedido(dato,BUSCAR_PRODUCTO);
                            if(posicion >= 0){
                                Util.productosSeleccionados.get(posicion).setTipocombo(0);
                                Util.productosSeleccionados.get(posicion).setSurtido(false);
                                Util.productosSeleccionados.get(posicion).setIdCombo(0);
                                Util.productosSeleccionados.get(posicion).setCantidadSoles(0);
                                adapter.notifyDataSetChanged();
                                setearTotal();
                            }
                        }
                        if(cabeceraDescuento.getTipoCombo() == 1 || cabeceraDescuento.getTipoCombo() == 2){
                            int posicionP = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                            if(posicionP >= 0) {
                                Util.productosSeleccionados.remove(posicionP);
                                adapter.notifyItemRemoved(posicionP);
                            }
                        }
                        for(int i = 0;i<Util.detalleDescuentosSeleccionados.size();i++){
                            if(Util.detalleDescuentosSeleccionados.get(i).getIdProducto() == cabeceraDescuento.getIdProducto()){
                                Util.detalleDescuentosSeleccionados.remove(i);
                                adapterPromo.notifyDataSetChanged();
                            }
                        }
                        Toast.makeText(this, "Se desactivó " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                    }

                }else{
                    List<Producto> productosReconocidos = new ArrayList<>();
                    productosReconocidos.addAll(productosReconocidosO);
                    productosReconocidos.addAll(productosReconocidosY);
                    int posicionBon = -1;
                    posicionBon = verSiEstaEnPedido(cabeceraDescuento,BUSCAR_BONO);
                    if(posicionBon >= 0){
                        if(cabeceraDescuento.getTipoCombo() == 1 || cabeceraDescuento.getTipoCombo() == 2){
                            Util.productosSeleccionados.remove(posicionBon);
                            adapter.notifyItemRemoved(posicionBon);
                            setearTotal();
                            Toast.makeText(this, "Se desactivó " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "No cumple requisito " + cabeceraDescuento.getNombreDescuento(), Toast.LENGTH_SHORT).show();
                        setearTotal();
                    }
                }
            }
        }

    }

    private void obtenerProductoBonoficacion(final CabeceraDescuento cabeceraDescuento, final int cantidad) {
        dialog = new SpotsDialog.Builder().setContext(this).setMessage("Cargando datos del producto a bonificar...").setCancelable(false).build();
        dialog.show();
        serviceApi.obtenerProductoPorCodigo(cabeceraDescuento.getIdProdComponente(),Integer.parseInt(Util.clienteSeleccionado.getListaPrecio())).enqueue(new Callback<RespuestaProducto>() {
            @Override
            public void onResponse(Call<RespuestaProducto> call, Response<RespuestaProducto> response) {
                try{
                    RespuestaProducto respuesta = response.body();
                    if(respuesta.getCode()==100){
                        if(respuesta.getResult().size() > 0) {
                            dialog.dismiss();
                            Producto bonificacion = respuesta.getResult().get(0);
                            int cantidadPro = cantidad / bonificacion.getFactor();
                            int unidad = cantidad % bonificacion.getFactor();
                            bonificacion.setTotalunidadesPedido(cantidad);
                            bonificacion.setUnidadesPedido(unidad);
                            bonificacion.setCantidadPedido(cantidadPro);
                            bonificacion.setTipoPrecio("B");
                            bonificacion.setPreciocon(0.0);
                            bonificacion.setPreciocre(0.0);
                            bonificacion.setTotal(0.0);
                            bonificacion.setCombo(true);
                            bonificacion.setCosto(0.0);
                            bonificacion.setBonificacion(true);
                            bonificacion.setIdCombo(cabeceraDescuento.getIdProducto());
                            bonificacion.setTipocombo(cabeceraDescuento.getTipoCombo());
                            bonificacion.setSurtido(cabeceraDescuento.isSurtido());
                            bonificacion.setCantidadSoles(cabeceraDescuento.getCantidadSoles());
                            Util.productosSeleccionados.add(bonificacion);
                            adapter.notifyDataSetChanged();
                            setearTotal();

                        }else{
                            dialog.dismiss();
                            Toast.makeText(RegistrarPedidoActivity.this, "No se obtuvo resultados con producto,por favor intentelo otra vez", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        dialog.dismiss();
                        Toast.makeText(RegistrarPedidoActivity.this, "No se pudo traer los datos del servidor", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    dialog.dismiss();
                    Toast.makeText(RegistrarPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "onReponse: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RespuestaProducto> call, Throwable t) {
                dialog.dismiss();
                Log.e("Error failure", "onReponse: " + t.getMessage());
                Util.mensajeErrorConexion(RegistrarPedidoActivity.this,false);
            }
        });
    }

    private List<Producto> obtenerProductosReconocidos(List<CabeceraDescuento> criterio){
        List<Producto> productosReconocidos = new ArrayList<>();

        for(int i=0;i<criterio.size();i++){
            int posicion = verSiEstaEnPedido(criterio.get(i),BUSCAR_PRODUCTO);
            if(posicion>=0){
                productosReconocidos.add(Util.productosSeleccionados.get(posicion));
            }
        }

        return productosReconocidos;
    }

    private void obtenerDetalleDescuento(int idProducto, List<CabeceraDescuento> detalleDescuento ) {
        for(CabeceraDescuento dato : Util.detalleDescuentosSeleccionados){
            if(dato.getIdProducto() == idProducto){
                detalleDescuento.add(dato);
            }
        }
    }
//
//
    private void mostrarProductoNoEncontrado(CabeceraDescuento cabeceraDescuento) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Alerta");
        builder.setMessage("Esta promoción se aplica al producto " + cabeceraDescuento.getNombreProducto().toLowerCase().trim() + ", el cual no se encuentra");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
//
    private int verSiEstaEnPedido(CabeceraDescuento cabeceraDescuento, int tipoBusqueda) {
        for(int i = 0;i<Util.productosSeleccionados.size();i++){
            if(Util.productosSeleccionados.get(i).getIdProducto() == cabeceraDescuento.getIdProdComponente()){
                if(tipoBusqueda == BUSCAR_BONO){
                    if(Util.productosSeleccionados.get(i).isBonificacion()){
                        return i;
                    }
                }else {
                    if(!Util.productosSeleccionados.get(i).isBonificacion()){
                        return i;
                    }
                }
            }
        }
        return -1;
    }
//
    private int versiEstaEnPedido(Producto producto, int tipoBusqueda) {
        for(int i = 0;i<Util.productosSeleccionados.size();i++){
            if(Util.productosSeleccionados.get(i).getIdProducto() == producto.getIdProducto()){
                if(tipoBusqueda == BUSCAR_BONO){
                    if(Util.productosSeleccionados.get(i).isBonificacion()){
                        return i;
                    }
                }else {
                    if(!Util.productosSeleccionados.get(i).isBonificacion()){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

    private int versiEstaEnPedido(int idProducto, int tipoBusqueda) {
        for(int i = 0;i<Util.productosSeleccionados.size();i++){
            if(Util.productosSeleccionados.get(i).getIdProducto() == idProducto){
                if(tipoBusqueda == BUSCAR_BONO){
                    if(Util.productosSeleccionados.get(i).isBonificacion()){
                        return i;
                    }
                }else {
                    if(!Util.productosSeleccionados.get(i).isBonificacion()){
                        return i;
                    }
                }
            }
        }
        return -1;
    }

//
    private void mostrarMensajeInformativo(String titulo, String mensaje) {
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
//
//
    @Override
    public void eliminarPromoDscto(final int position) {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Mensaje");
        builder.setMessage("¿Esta seguro que desea eliminarlo?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                CabeceraDescuento actual = Util.descuentosSeleccionados.get(position);
                realizarCambiosSegunTipo(actual,DESACTIVAR_DDESCUENTO);
                Util.descuentosSeleccionados.remove(position);
                adapterPromo.setPromoDsctoList(Util.descuentosSeleccionados);
                setearEstadoVista();
                Toast.makeText(RegistrarPedidoActivity.this, "Se eliminó la promoción o descuento", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No",null);
        builder.create();
        builder.show();
    }

    @Override
    public void aceptarVol(Producto producto, int tipoPrecio, int posicion) {
        calcularPrecio(producto,tipoPrecio,posicion);

        buscarPromocionDescuento(producto);
    }

    @Override
    public void negarVol(Producto producto,int posicion) {
        if(rdb_contado.isChecked()) {
            calcularPrecio(producto,CONTADO_CHECK,posicion);
        }else if(rdb_credito.isChecked()){
            calcularPrecio(producto,CREDITO_CHECK,posicion);
        }
        buscarPromocionDescuento(producto);
    }
}
