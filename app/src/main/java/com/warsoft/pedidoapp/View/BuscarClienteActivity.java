package com.warsoft.pedidoapp.View;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.BuscarClienteAdapter;
import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaCliente;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Conexion;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarClienteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack,btnBuscarCliente;
    private FloatingActionButton fabAgregarCliente;
    private RadioButton rdbNombreCliente,rdbDni;
    private CheckBox cbxBuscarTodoCliente;
    private EditText txtDatoBusqueda;
    private TextView txtSinDatos;
    private RecyclerView recyclerBuscarCliente;
    private AlertDialog cargaDialog;
    private IServiceApi serviceApi;
    private String pantalla;
    private List<Cliente> clienteList;
    private BuscarClienteAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_cliente);

        serviceApi = Service.getServiceApi();
        clienteList = new ArrayList<>();

        if(getIntent().getExtras()!= null){
            pantalla = getIntent().getStringExtra(Util.PANTALLA_ORIGEN);
        }

        Util.actualizar = false;

        setearVista();



        btnBack.setOnClickListener(this);

        btnBuscarCliente.setOnClickListener(this);

        fabAgregarCliente.setOnClickListener(this);


    }

    private void setearVista() {
        btnBack = findViewById(R.id.btn_back);
        btnBuscarCliente = findViewById(R.id.btn_buscar_cliente);
        fabAgregarCliente = findViewById(R.id.fab_agregar_cliente);
        rdbNombreCliente = findViewById(R.id.rdb_nombre_cliente);
        rdbNombreCliente.setChecked(true);
        rdbDni = findViewById(R.id.rdb_dni);
        cbxBuscarTodoCliente = findViewById(R.id.cbx_buscar_todo_cliente);
        txtDatoBusqueda = findViewById(R.id.txt_dato_busqueda);
        txtSinDatos = findViewById(R.id.txt_sin_datos);
        recyclerBuscarCliente = findViewById(R.id.recycler_buscar_cliente);
        cargaDialog = new SpotsDialog.Builder().setContext(this).setMessage("Cargando Clientes").setCancelable(false).build();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_buscar_cliente:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                buscarCliente();
                break;
            case R.id.fab_agregar_cliente:
                startActivity(new Intent(this, RegistrarCliente.class));
                break;
            default:
                Toast.makeText(this, "Clicka en el boton correcto", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarCliente() {
        String dato = txtDatoBusqueda.getText().toString();
        cargaDialog.show();
        Conexion conexion = new Conexion(this);
        try{
            if(!TextUtils.isEmpty(dato.trim())){
                if(conexion.execute().get()){
                    if(rdbNombreCliente.isChecked()){
                        buscarPorNombre(dato);
                    }else{
                        if(TextUtils.isDigitsOnly(dato)){
                            buscarPorDniRuc(dato);
                        }else{
                            Toast.makeText(this, "Debes ingresar solo numeros", Toast.LENGTH_SHORT).show();
                            cargaDialog.dismiss();
                        }
                    }
                }else{
                    Toast.makeText(this, "No tienes conexion a internet", Toast.LENGTH_SHORT).show();
                    cargaDialog.dismiss();
                }
            }else{
                Toast.makeText(this, "No puede estar vacio", Toast.LENGTH_SHORT).show();
                cargaDialog.dismiss();
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", "buscarCliente: " + e.getMessage());
            cargaDialog.dismiss();
        }

    }

    private void buscarPorDniRuc(String dniRuc) {
        serviceApi.obtenerClientePorDniRuc(dniRuc).enqueue(new Callback<RespuestaCliente>() {
            @Override
            public void onResponse(Call<RespuestaCliente> call, Response<RespuestaCliente> response) {
                try {
                    RespuestaCliente respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        clienteList = respuesta.getResult();
                        mostrarResultado();
                        cargaDialog.dismiss();
                    }else{
                        Toast.makeText(BuscarClienteActivity.this, "No se encontraron clientes con ese DNI/RUC", Toast.LENGTH_SHORT).show();
                        cargaDialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(BuscarClienteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    cargaDialog.dismiss();
                    Log.e("Error", "onResponse: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<RespuestaCliente> call, Throwable t) {
                Util.mensajeErrorConexion(BuscarClienteActivity.this,false);
                cargaDialog.dismiss();
            }
        });
    }

    private void buscarPorNombre(String nombre) {
        serviceApi.obtenerClientePorNombre(nombre).enqueue(new Callback<RespuestaCliente>() {
            @Override
            public void onResponse(Call<RespuestaCliente> call, Response<RespuestaCliente> response) {
                try {
                    RespuestaCliente respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        clienteList = respuesta.getResult();
                        mostrarResultado();
                        cargaDialog.dismiss();
                    }else{
                        Toast.makeText(BuscarClienteActivity.this, "No se encontraron clientes con ese nombre", Toast.LENGTH_SHORT).show();
                        cargaDialog.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(BuscarClienteActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("Error", "onResponse: " + e.getMessage());
                    cargaDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCliente> call, Throwable t) {
                Util.mensajeErrorConexion(BuscarClienteActivity.this,false);
                cargaDialog.dismiss();
            }
        });
    }

    private void mostrarResultado() {
        try{
            List<Cliente> clientes = new ArrayList<>();
            clientes.addAll(clienteList);
            if(clientes.size()>0) {
                txtSinDatos.setVisibility(View.GONE);
                recyclerBuscarCliente.setVisibility(View.VISIBLE);
                iniciarAdapter(clientes);
            }else{
                txtSinDatos.setVisibility(View.VISIBLE);
                txtSinDatos.setText("No se encontraron resultados");
                recyclerBuscarCliente.setVisibility(View.GONE);
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", "mostrarResultado: " + e.getMessage());
            cargaDialog.dismiss();
        }
    }

    private void iniciarAdapter(List<Cliente> clientes) {
        if(TextUtils.equals(Util.PANTALLA_REGISTRO_PEDIDO,pantalla)) {
            adapter = new BuscarClienteAdapter(this, clientes,pantalla, new BuscarClienteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Cliente cliente, int position) {
                    Intent resultIntent = new Intent(BuscarClienteActivity.this,RegistrarPedidoActivity.class);
                    resultIntent.putExtra(Util.CLIENTE_SELECCIONADO,cliente);
                    setResult(RESULT_OK,resultIntent);
                    finish();
                }

                @Override
                public void onEditClick(Cliente cliente, int position) {

                }
            });
        }else if(TextUtils.equals(Util.PANTALLA_PRINCIPAL,pantalla)){
            adapter = new BuscarClienteAdapter(this, clientes, pantalla, new BuscarClienteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Cliente cliente, int position) {
                    Intent intent = new Intent(BuscarClienteActivity.this,DocumentosPendientesActivity.class);
                    intent.putExtra(Util.CLIENTE_SELECCIONADO,cliente);
                    startActivity(intent);
                }

                @Override
                public void onEditClick(Cliente cliente, int position) {
                    Intent intent = new Intent(BuscarClienteActivity.this,RegistrarCliente.class);
                    intent.putExtra(Util.EDITAR,true);
                    intent.putExtra(Util.CLIENTE_SELECCIONADO,cliente);
                    startActivity(intent);
                }
            });
        }
        recyclerBuscarCliente.setLayoutManager(new LinearLayoutManager(this));
        recyclerBuscarCliente.setHasFixedSize(true);
        recyclerBuscarCliente.setAdapter(adapter);
    }

    private void regresar() {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Salir de pantalla");
            builder.setMessage("¿Desea regresar a la pantalla anterior?, se perderá todo lo hecho");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            builder.setNegativeButton("No", null);
            builder.create();
            builder.show();
    }

    @Override
    public void onBackPressed() {
        if(TextUtils.equals(pantalla,Util.PANTALLA_REGISTRO_PEDIDO)) {
            regresar();
        }else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        if(Util.actualizar){
            recyclerBuscarCliente.setVisibility(View.GONE);
            if(rdbNombreCliente.isChecked()) {
                buscarCliente();
            }else{
                buscarPorDniRuc(txtDatoBusqueda.getText().toString());
            }
        }
        super.onResume();
    }
}
