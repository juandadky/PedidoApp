package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.HistorialPedidoAdapter;
import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaCabeceraPedido;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistorialPedidoActivity extends AppCompatActivity {

    private ImageButton btnBack;
    private RadioGroup rdbOpcionesBusqueda;
    private RadioButton rdbPorFacturar,rdbFacturado,rdbAnulado;
    private RecyclerView recyclerHistorialPedido;
    private TextView txtSinDatos,txt_numero_pedidos,txt_total;
    private LinearLayout root_datos;
    private IServiceApi serviceApi;
    private AlertDialog dialogCarga;
    private List<CabeceraPedido> cabeceraPedidoList;
    private HistorialPedidoAdapter adapter;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_pedido);

        serviceApi = Service.getServiceApi();
        cabeceraPedidoList = new ArrayList<>();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        setearVista();

        rdbOpcionesBusqueda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                cabeceraPedidoList.clear();
                cargarDatos();
            }
        });

        cargarDatos();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Util.ESTA_ANULADO){
            cabeceraPedidoList.clear();
            cargarDatos();
            Util.ESTA_ANULADO = false;
        }
    }

    private void setearVista() {
        btnBack = findViewById(R.id.btn_back);
        rdbOpcionesBusqueda = findViewById(R.id.rbd_opciones_busqueda_historial);
        rdbPorFacturar = findViewById(R.id.rdb_por_facturar);
        rdbPorFacturar.setChecked(true);
        rdbFacturado = findViewById(R.id.rdb_facturado);
        rdbAnulado = findViewById(R.id.rdb_anulado);
        recyclerHistorialPedido = findViewById(R.id.recycler_historial_pedido);
        txtSinDatos = findViewById(R.id.txt_sin_datos);
        txt_numero_pedidos = findViewById(R.id.txt_numero_pedidos);
        txt_total = findViewById(R.id.txt_total);
        root_datos = findViewById(R.id.root_datos);
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando Historial de Pedidos").setCancelable(false).build();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
    }

    private void cargarDatos() {
        dialogCarga.show();
        int idVendedor = preferences.getInt(Util.COD_VENDEDOR,0);
        if(idVendedor != 0) {
            if(rdbPorFacturar.isChecked()){
                obtenerPorFacturar(idVendedor);
            }else{
                if(rdbFacturado.isChecked())
                    obtenerFacturadoAnulado(idVendedor,"PP");
                else
                    obtenerFacturadoAnulado(idVendedor,"AN");
            }

        }else{
            Toast.makeText(this, "Hubo un error al asignar el idVendedor,por favor, salga de sesión y vuelva a loguearse!", Toast.LENGTH_LONG).show();
            dialogCarga.dismiss();
        }
    }

    private void obtenerFacturadoAnulado(int idVendedor,String estado) {
        serviceApi.obtenerCabeceraPedidoSegunVendedorFacturadoAnulado(idVendedor,estado).enqueue(new Callback<RespuestaCabeceraPedido>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraPedido> call, Response<RespuestaCabeceraPedido> response) {
                try {
                    RespuestaCabeceraPedido respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        cabeceraPedidoList = respuesta.getResult();
                        mostrarResultado();
                        dialogCarga.dismiss();
                    } else {
                        Toast.makeText(HistorialPedidoActivity.this, "No se pudo traer los datos", Toast.LENGTH_SHORT).show();
                        dialogCarga.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(HistorialPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "onResponse: " + e.getMessage());
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCabeceraPedido> call, Throwable t) {
                Util.mensajeErrorConexion(HistorialPedidoActivity.this,true);
            }
        });
    }


    private void obtenerPorFacturar(int idVendedor) {
        serviceApi.obtenerCabeceraPedidoSegunVendedorPorFacturar(idVendedor).enqueue(new Callback<RespuestaCabeceraPedido>() {
            @Override
            public void onResponse(Call<RespuestaCabeceraPedido> call, Response<RespuestaCabeceraPedido> response) {
                try {
                    RespuestaCabeceraPedido respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        cabeceraPedidoList = respuesta.getResult();
                        mostrarResultado();
                        dialogCarga.dismiss();
                    } else {
                        Toast.makeText(HistorialPedidoActivity.this, "No se pudo traer los datos", Toast.LENGTH_SHORT).show();
                        dialogCarga.dismiss();
                    }
                } catch (Exception e) {
                    Toast.makeText(HistorialPedidoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "onResponse: " + e.getMessage());
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCabeceraPedido> call, Throwable t) {
                Util.mensajeErrorConexion(HistorialPedidoActivity.this,true);
                dialogCarga.dismiss();
            }
        });
    }

    private void mostrarResultado() {
        try{
            if(cabeceraPedidoList.size()>0) {
                txtSinDatos.setVisibility(View.GONE);
                recyclerHistorialPedido.setVisibility(View.VISIBLE);
                root_datos.setVisibility(View.VISIBLE);
                txt_numero_pedidos.setText("N° Pedidos: " + cabeceraPedidoList.size());
                txt_total.setText(String.format("Total: S/.%.2f",obtenerTotal()));
                iniciarAdapter();
            }else{
                recyclerHistorialPedido.setVisibility(View.GONE);
                root_datos.setVisibility(View.GONE);
                txtSinDatos.setVisibility(View.VISIBLE);
                txtSinDatos.setText("No se encontraron resultados");
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", "mostrarResultado: " + e.getMessage());
            dialogCarga.dismiss();
        }
    }

    private double obtenerTotal() {
        double total = 0;
        for(CabeceraPedido dato: cabeceraPedidoList){
            total += dato.getTotal();
        }
        return total;
    }

    private void iniciarAdapter() {
        adapter = new HistorialPedidoAdapter(this);
        recyclerHistorialPedido.setLayoutManager(new LinearLayoutManager(this));
        recyclerHistorialPedido.setHasFixedSize(true);
        recyclerHistorialPedido.setAdapter(adapter);
        adapter.setPedidos(cabeceraPedidoList);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
