package com.dadky.pedidoapp.View;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.dadky.pedidoapp.Adapter.BuscarProductoAdapter;
import com.dadky.pedidoapp.Local.Producto;
import com.dadky.pedidoapp.R;
import com.dadky.pedidoapp.Service.IServiceApi;
import com.dadky.pedidoapp.Service.Model.RespuestaProducto;
import com.dadky.pedidoapp.Service.Service;
import com.dadky.pedidoapp.Utils.Conexion;
import com.dadky.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuscarProductoActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton btnBack,btnBuscarProducto;
    private RadioButton rdbCodigo,rdbNombreProducto;
    private RadioGroup rdbOpcionesBusqueda;
    private EditText txtDatoBusqueda;
    private RecyclerView recyclerBuscarProducto;
    private TextView txtSinDatos;
    private IServiceApi serviceApi;
    private String pantalla;
    private List<Producto> listaProductos;
    private BuscarProductoAdapter adapter;
    private AlertDialog dialogCarga;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buscar_producto);

        cargarVista();

        serviceApi = Service.getServiceApi();
        listaProductos = new ArrayList<>();

        if(getIntent().getExtras()!= null){
            pantalla = getIntent().getStringExtra(Util.PANTALLA_ORIGEN);
        }

        btnBack.setOnClickListener(this);
        btnBuscarProducto.setOnClickListener(this);

        txtDatoBusqueda.setOnClickListener(this);

    }

    private void cargarVista() {
        rdbOpcionesBusqueda = findViewById(R.id.rdb_opciones_busqueda);
        rdbCodigo = findViewById(R.id.rdb_codigo);
        rdbCodigo.setChecked(true);
        rdbNombreProducto = findViewById(R.id.rdb_nombre_producto);
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
                regresar();
                break;
            case R.id.btn_buscar_producto:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                buscarProducto();
                break;
            case R.id.txt_dato_busqueda:
                setearEditText();
                break;
            default:
                Toast.makeText(this, "Presione en botón válido", Toast.LENGTH_SHORT).show();
        }
    }

    private void setearEditText() {
        if(rdbCodigo.isChecked()) {
            txtDatoBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
    }

    private void buscarProducto() {
        String dato = txtDatoBusqueda.getText().toString();
        Conexion conexion = new Conexion(this);
        try {
            //Comprobar si esta vacio
            if (!TextUtils.isEmpty(dato)) {
                //Comprobar conexion a internet
                if (conexion.execute().get()) {
                    //Comprobar si el radioButton de Codigo esta seleccionado
                    if (rdbCodigo.isChecked()) {
                        //Comprobar si solo son numeros
                        if (!TextUtils.isDigitsOnly(dato)) {
                            int codigo = Integer.parseInt(dato);
                            buscarPorCodigo(codigo);
                        } else {
                            txtDatoBusqueda.setError("Debe ingresar solo numeros");
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
            }
        }catch (Exception e){
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarPorNombre(String dato) {
        serviceApi.obtenerProductoPorNombre(dato).enqueue(new Callback<RespuestaProducto>() {
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
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaProducto> call, Throwable t) {
                Toast.makeText(BuscarProductoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                dialogCarga.dismiss();
            }
        });
    }

    private void buscarPorCodigo(int dato) {
        serviceApi.obtenerProductoPorCodigo(dato).enqueue(new Callback<RespuestaProducto>() {
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
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaProducto> call, Throwable t) {
                Toast.makeText(BuscarProductoActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
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
        }
    }

    private void iniciarAdapter(List<Producto> productos) {
        adapter = new BuscarProductoAdapter(this, productos, new BuscarProductoAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Producto producto, int position) {
                //Todo Implementar lo que se va a ser dependiendo de la pantalla
                Toast.makeText(BuscarProductoActivity.this, "Seleccionado desde " + pantalla, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerBuscarProducto.setLayoutManager(new LinearLayoutManager(this));
        recyclerBuscarProducto.setHasFixedSize(true);
        recyclerBuscarProducto.setAdapter(adapter);
    }

    private void regresar() {
        if(TextUtils.equals(pantalla,Util.PANTALLA_REGISTRO_PEDIDO)) {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
            builder.setTitle("Salir de pantalla");
            builder.setMessage("¿Desea regresar a la pantalla anterior?, se perderá todo lo hecho");
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    onBackPressed();
                }
            });
            builder.setNegativeButton("No", null);
            builder.create();
            builder.show();
        }
    }



}
