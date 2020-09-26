package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.DeudaPendienteAdapter;
import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.Local.Entities.DeudaPendiente;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaDeudasPendientes;
import com.warsoft.pedidoapp.Service.Model.RespuestaGeneral;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DocumentosPendientesActivity extends AppCompatActivity {

    private TextView txtSinDatos,txtIdDocumento,txtFechaActual,txtTipoDocumento,txtIdOrden,txtImporte,txtSaldoPagar;
    private EditText txtImportePagar;
    private Button btnPagar;
    private ImageButton btnBack;
    private RecyclerView recycler_deudas_pendientes;
    private DeudaPendienteAdapter adapter;
    private IServiceApi serviceApi;
    private CardView cardViewPago;
    private AlertDialog dialogCarga;
    private Cliente cliente;
    private List<DeudaPendiente> deudaPendienteList;
    private DeudaPendiente deudaSeleccionada;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documentos_pendientes);

        serviceApi = Service.getServiceApi();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cliente = new Cliente();
        deudaPendienteList = new ArrayList<>();

        setearVista();

        if(getIntent().getExtras() != null){
            cliente = (Cliente) getIntent().getSerializableExtra(Util.CLIENTE_SELECCIONADO);
            cargarDeudas();
        }

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btnPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                if(validarCantidadPago()) {
                    registrarPago();
                }else{
                    Toast.makeText(DocumentosPendientesActivity.this, "Inserte un monto válido", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }


    //Mostrar resultado en Vista
    private void setearVista() {
        txtSinDatos = findViewById(R.id.txt_sin_datos);
        txtIdDocumento = findViewById(R.id.txt_id_documento);
        txtFechaActual = findViewById(R.id.txt_fecha_actual);
        txtTipoDocumento = findViewById(R.id.txt_tipo_documento);
        txtIdOrden = findViewById(R.id.txt_id_orden);
        txtImporte = findViewById(R.id.txt_importe);
        txtSaldoPagar = findViewById(R.id.txt_saldo_pagar);
        txtImportePagar = findViewById(R.id.txt_importe_pagar);
        btnPagar = findViewById(R.id.btn_pagar);
        btnBack = findViewById(R.id.btn_back);
        recycler_deudas_pendientes = findViewById(R.id.recycler_deudas_pendientes);
        cardViewPago = findViewById(R.id.card_view_pago);
    }

    private void mostrarResultados() {
        adapter = new DeudaPendienteAdapter(this, deudaPendienteList, new DeudaPendienteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DeudaPendiente deudaActual,int position) {
                deudaSeleccionada = deudaActual;
                estadoPago(deudaActual);
            }
        });
        recycler_deudas_pendientes.setLayoutManager(new LinearLayoutManager(this));
        recycler_deudas_pendientes.setHasFixedSize(true);
        recycler_deudas_pendientes.setAdapter(adapter);
    }


    //Consulta de las deudas del cliente
    private void cargarDeudas() {
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando Deudas Pendientes").setCancelable(false).build();
        dialogCarga.show();
        Calendar fechaActual = Calendar.getInstance();
        serviceApi.obtenerDeudasPendientes(cliente.getIdCliente(),(fechaActual.get(Calendar.YEAR)-2) + "0101", obtenerFechaActual())
                .enqueue(new Callback<RespuestaDeudasPendientes>() {
            @Override
            public void onResponse(Call<RespuestaDeudasPendientes> call, Response<RespuestaDeudasPendientes> response) {
                RespuestaDeudasPendientes respuesta = response.body();
                try {
                    if (respuesta.getCode() == 100) {
                        if (respuesta.getResult().size() > 0) {
                            deudaPendienteList = respuesta.getResult();
                            estadoHayDeudas();
                            mostrarResultados();
                            dialogCarga.dismiss();
                        } else {
                            estadoNoHayDeudas();
                            dialogCarga.dismiss();
                        }
                    } else {
                        Toast.makeText(DocumentosPendientesActivity.this, "No se pudo traer los datos del servidor", Toast.LENGTH_SHORT).show();
                        estadoNoHayDeudas();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    estadoNoHayDeudas();
                    Toast.makeText(DocumentosPendientesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaDeudasPendientes> call, Throwable t) {
                Util.mensajeErrorConexion(DocumentosPendientesActivity.this,true);
                estadoHayDeudas();
                dialogCarga.dismiss();
            }
        });
    }

    //Insertar el pago a la bd
    private void registrarPago() {
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Registrando el pago").setCancelable(false).build();
        dialogCarga.show();

        serviceApi.registrarPago(obtenerFechaActual(),cliente.getIdCliente(),deudaSeleccionada.getIdDocumento(),deudaSeleccionada.getIdDocOrden().substring(0,4),
                Integer.parseInt(deudaSeleccionada.getIdDocOrden().substring(4)),obtenerTipoDocumento(deudaSeleccionada.getAbreviatura()),
                Double.parseDouble(txtImportePagar.getText().toString()),preferences.getInt(Util.ID_USUARIO_LOGIN,-1)).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try {
                    RespuestaGeneral respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        dialogCarga.dismiss();
                        estadoPagoRealizado();
                    }else{
                        dialogCarga.dismiss();
                        estadoPagoNoRealizado();
                    }
                }catch (Exception e){
                    Toast.makeText(DocumentosPendientesActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                    estadoPagoNoRealizado();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                Util.mensajeErrorConexion(DocumentosPendientesActivity.this,false);
                dialogCarga.dismiss();
                estadoPagoNoRealizado();
            }
        });
    }



    //Estados de pantalla
    private void estadoHayDeudas(){
        cardViewPago.setVisibility(View.GONE);
        recycler_deudas_pendientes.setVisibility(View.VISIBLE);
        txtSinDatos.setVisibility(View.GONE);
    }

    private void estadoNoHayDeudas(){
        cardViewPago.setVisibility(View.GONE);
        recycler_deudas_pendientes.setVisibility(View.GONE);
        txtSinDatos.setVisibility(View.VISIBLE);
        txtSinDatos.setText("Este cliente no cuenta con deudas pendientes");
    }

    private void estadoPago(DeudaPendiente deudaSeleccionada){
        Calendar fechaActual = Calendar.getInstance();
        cardViewPago.setVisibility(View.VISIBLE);
        recycler_deudas_pendientes.setVisibility(View.VISIBLE);
        txtSinDatos.setVisibility(View.GONE);
        txtIdDocumento.setText("Nro: " + deudaSeleccionada.getIdDocumento());
        txtFechaActual.setText(agregarCeroAFecha(fechaActual.get(Calendar.DATE))+ "/"+agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))+"/"+fechaActual.get(Calendar.YEAR));
        txtTipoDocumento.setText("Documento: " + setearTipoDocumento(deudaSeleccionada.getAbreviatura()));
        txtIdOrden.setText("Serie: " + deudaSeleccionada.getIdDocOrden().substring(0,4) +"-"+deudaSeleccionada.getIdDocOrden().substring(4));
        txtImporte.setText(String.format("Importe: S/.%.2f",deudaSeleccionada.getTotal()));
        txtSaldoPagar.setText(String.format("Saldo: S/.%.2f",deudaSeleccionada.getSaldoSoles()));
        btnPagar.setEnabled(true);
        txtImportePagar.setEnabled(true);
    }


    private void estadoPagoRealizado(){
        cardViewPago.setVisibility(View.GONE);
        recycler_deudas_pendientes.setVisibility(View.VISIBLE);
        txtSinDatos.setVisibility(View.GONE);
        txtIdDocumento.setText("");
        txtFechaActual.setText("");
        txtTipoDocumento.setText("");
        txtIdOrden.setText("");
        txtImporte.setText("");
        txtSaldoPagar.setText("");
        btnPagar.setEnabled(false);
        txtImportePagar.setEnabled(false);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Mensaje");
        builder.setMessage("Se realizó el pago con exito");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cargarDeudas();
            }
        });
        builder.create();
        builder.show();
    }

    private void estadoPagoNoRealizado(){
        cardViewPago.setVisibility(View.VISIBLE);
        recycler_deudas_pendientes.setVisibility(View.VISIBLE);
        txtSinDatos.setVisibility(View.GONE);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("No se pudo realizar el pago,intentelo otra vez");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtImportePagar.setText("");
            }
        });
        builder.create();
        builder.show();
    }



    //Metodos usados comunmente

    private boolean validarCantidadPago() {
        String importe = txtImportePagar.getText().toString();
        if(TextUtils.isEmpty(importe.trim())){
            Toast.makeText(this, "No puede estar vacio el saldo a pagar", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(noEsNumero(importe)){
            Toast.makeText(this, "Ingrese un numero válido", Toast.LENGTH_SHORT).show();
            return false;
        }
        if(Double.parseDouble(importe) > deudaSeleccionada.getSaldoSoles() || Double.parseDouble(importe) <= 0 ){
            Toast.makeText(this, "Ingrese un saldo válido en rango de 0 a " + deudaSeleccionada.getSaldoSoles(), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean noEsNumero(String importe) {
        try {
            String[] datos = null;
            if (importe.contains(".")) {
                datos = importe.split("[.]");
                if (TextUtils.isDigitsOnly(datos[0]) || TextUtils.isDigitsOnly(datos[1])) {
                    return false;
                }
            } else if (importe.contains(",")) {
                datos = importe.split(",");
                if (TextUtils.isDigitsOnly(datos[0]) || TextUtils.isDigitsOnly(datos[1])) {
                    return false;
                }
            } else {
                if (TextUtils.isDigitsOnly(importe)) {
                    return false;
                }
            }
            return true;
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private String setearTipoDocumento(String abreviaturaDocumento) {
        switch (abreviaturaDocumento){
            case "FV":
                return "Factura";
            case "BV":
                return "Boleta";
            case "NP":
                return "Nota de Pedido";
            default:
                return "Desconocido";
        }
    }

    private int obtenerTipoDocumento(String abreviaturaDocumento) {
        switch (abreviaturaDocumento){
            case "FV":
                return 1;
            case "BV":
                return 3;
            case "NP":
                return 11;
            default:
                return -1;
        }
    }

    private String obtenerFechaActual(){
        Calendar fechaActual = Calendar.getInstance();
        return fechaActual.get(Calendar.YEAR)+ agregarCeroAFecha((fechaActual.get(Calendar.MONTH)+1))
                +agregarCeroAFecha(fechaActual.get(Calendar.DATE));
    }

    private String agregarCeroAFecha(int numeroFecha){
        return numeroFecha<10? "0"+numeroFecha: ""+numeroFecha;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
