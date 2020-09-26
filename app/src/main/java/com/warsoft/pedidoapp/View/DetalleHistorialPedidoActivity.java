package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.HistorialDetallePedidoAdapter;
import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.warsoft.pedidoapp.Local.Entities.DetallePedido;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaAnular;
import com.warsoft.pedidoapp.Service.Model.RespuestaDetallePedido;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleHistorialPedidoActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int EDITAR_PEDIDO_CODE = 99;

    private CabeceraPedido cabecera;
    private TextView txtIdPedido,txtFechaPedido,txtClientePedido,txtTipoPago,txtTipoDocumento,txtTotal,txtClienteDireccion;
    private Button btnAnularPedido;
    private ImageButton btnBack,btnEditar;
    private RecyclerView recyclerDetallePedido;
    private HistorialDetallePedidoAdapter adapter;
    private IServiceApi serviceApi;
    private AlertDialog dialogCarga;
    private List<DetallePedido> detallePedidoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_historial_pedido);

        iniciarVista();

        serviceApi = Service.getServiceApi();
        detallePedidoList = new ArrayList<>();

        if(getIntent().getExtras() != null && getIntent().hasExtra(Util.CABECERA_PEDIDO)){
            cabecera = (CabeceraPedido) getIntent().getSerializableExtra(Util.CABECERA_PEDIDO);
            Util.cabeceraSeleccionado = cabecera;
            setearVista();
            cargarProductos();
        }

        if(TextUtils.equals(cabecera.getEstado(),"AN")){
            btnAnularPedido.setVisibility(View.GONE);

        }

        btnBack.setOnClickListener(this);
        btnEditar.setOnClickListener(this);
        btnAnularPedido.setOnClickListener(this);

    }


    //Metodos usados

    private void iniciarVista() {
        txtIdPedido = findViewById(R.id.txt_id_pedido);
        txtFechaPedido = findViewById(R.id.txt_fecha_pedido);
        txtClientePedido = findViewById(R.id.txt_cliente_pedido);
        txtTipoPago = findViewById(R.id.txt_tipoPago);
        txtTipoDocumento = findViewById(R.id.txt_tipoDocumento);
        txtTotal = findViewById(R.id.txt_total);
        txtClienteDireccion = findViewById(R.id.txt_cliente_direccion);
        recyclerDetallePedido = findViewById(R.id.recycler_productos);
        btnAnularPedido = findViewById(R.id.btn_anularPedido);
        btnBack = findViewById(R.id.btn_back);
        btnEditar = findViewById(R.id.btn_editar);
    }

    private void setearVista() {
        txtIdPedido.setText("ID: " + cabecera.getIdPedido());
        txtFechaPedido.setText("Fecha Pedido: " +Util.setearFecha(cabecera.getFechaPedido()));
        txtClientePedido.setText("Cliente: " + cabecera.getNombreCliente());
        txtClienteDireccion.setText("Dirección: " + cabecera.getDireccion());
        txtTipoPago.setText("Tipo Pago: " + (TextUtils.equals(cabecera.getTipoVenta(),"1")? "Contado" : "Crédito"));
        txtTipoDocumento.setText("Documento: " + (cabecera.getFacBolNP() == 1 ? "Factura" : "Boleta"));
        txtTotal.setText("Total: S/. 00.00");
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando el pedido...").setCancelable(false).build();
        if(cabecera.getEstado() != null){
            btnEditar.setVisibility(View.GONE);
        }else{
            btnEditar.setVisibility(View.VISIBLE);
        }
    }

    private void cargarProductos() {
        dialogCarga.show();
        serviceApi.obtenerDetallePedido(cabecera.getIdPedido()).enqueue(new Callback<RespuestaDetallePedido>() {
            @Override
            public void onResponse(Call<RespuestaDetallePedido> call, Response<RespuestaDetallePedido> response) {
                try {
                    RespuestaDetallePedido respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        detallePedidoList = respuesta.getResult();
                        mostrarResultado();
                        dialogCarga.dismiss();
                    } else {
                        Toast.makeText(DetalleHistorialPedidoActivity.this, "No se pudo obtener los detalles del pedido", Toast.LENGTH_SHORT).show();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(DetalleHistorialPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDetallePedido> call, Throwable t) {
                Util.mensajeErrorConexion(DetalleHistorialPedidoActivity.this,true);
                dialogCarga.dismiss();
            }
        });
    }

    private void mostrarResultado() {
        adapter = new HistorialDetallePedidoAdapter(this, detallePedidoList);
        recyclerDetallePedido.setLayoutManager(new LinearLayoutManager(this));
        recyclerDetallePedido.setHasFixedSize(true);
        recyclerDetallePedido.setAdapter(adapter);
        setearTotal();
    }

    private void setearTotal(){
        double total = 0;
        for(DetallePedido detalle : detallePedidoList){
            total += detalle.getTotal();
        }
        txtTotal.setText(String.format("Total: S/.%.2f", total));
    }

    private void anularPedido() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Anular Pedido");
        builder.setMessage("¿Está seguro de anular el pedido?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                servicioAnular();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }

    private int compararFechaConActual(String fechaPedido) {
        try {
            DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
            Calendar cal  = Calendar.getInstance();
            cal.setTime(df.parse(fechaPedido));
            long msDiff = Calendar.getInstance().getTimeInMillis() - cal.getTimeInMillis();
            long daysDiff = TimeUnit.MILLISECONDS.toDays(msDiff);
            return (int) daysDiff;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 3;

    }


    //Metodos de consumo de api

    private void servicioAnular() {
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Anulando el pedido...").setCancelable(false).build();
        dialogCarga.show();
        serviceApi.anularPedido(cabecera.getIdPedido()).enqueue(new Callback<RespuestaAnular>() {
            @Override
            public void onResponse(Call<RespuestaAnular> call, Response<RespuestaAnular> response) {
                try {
                    RespuestaAnular respuesta = response.body();
                    if (respuesta.getCode() == 100) {

                        dialogCarga.dismiss();
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(DetalleHistorialPedidoActivity.this);
                        builder.setTitle("Mensaje");
                        builder.setMessage("Se anuló el pedido");
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Util.ESTA_ANULADO  = true;
                                finish();
                            }
                        });
                        builder.create();
                        builder.show();

                    }else{
                        Toast.makeText(DetalleHistorialPedidoActivity.this, "No se pudo anular el documento,intentelo de nuevo", Toast.LENGTH_LONG).show();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(DetalleHistorialPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAnular> call, Throwable t) {
                Toast.makeText(DetalleHistorialPedidoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialogCarga.dismiss();
            }
        });
    }


    //Metodos Overrides

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_anularPedido:
                anularPedido();
                break;
            case R.id.btn_editar:
                if(compararFechaConActual(Util.setearFecha(cabecera.getFechaPedido())) <= 2) {
                    Intent intent = new Intent(this, EditarPedidoActivity.class);
                    intent.putExtra(Util.CABECERA_PEDIDO, cabecera);
                    Bundle data = new Bundle();
                    data.putSerializable(Util.DETALLE_PEDIDO, (Serializable) detallePedidoList);
                    intent.putExtras(data);
                    startActivityForResult(intent, EDITAR_PEDIDO_CODE);
                }else{
                    Util.generarAlerta("Error","No se puede editar este pedido,debido a que tiene mas de 2 dias de realizado",DetalleHistorialPedidoActivity.this);
                }
                break;
            default:
                Toast.makeText(this, "No existe ese boton", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onResume() {
        super.onResume();
        cabecera = Util.cabeceraSeleccionado;
        cargarProductos();
    }
}
