package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Adapter.AvanceVentasAdapter;
import com.warsoft.pedidoapp.Local.Entities.AvanceVenta;
import com.warsoft.pedidoapp.Local.Entities.Categoria;
import com.warsoft.pedidoapp.Local.Entities.ReporteAvanceVenta;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaAvanceVentas;
import com.warsoft.pedidoapp.Service.Model.RespuestaCategoria;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class AvanceVentaActivity extends AppCompatActivity implements View.OnClickListener {

    public static final int FECHA_INICIAL = 1;
    public static final int FECHA_FINAL = 2;
    public static final int VENTA_GENERAL = 1;
    public static final int VENTA_LINEA = 2;
    public static final int VENTA_SUBLINEA = 3;
    public static final String TITULO_VENTA_GENERAL = "Búsqueda de Venta General";
    public static final String TITULO_VENTA_LINEA = "Búsqueda de Venta por Línea";
    public static final String TITULO_VENTA_SUBLINEA = "Búsqueda de Venta por Línea y Sublínea";


    private Button btnFechaInicio,btnFechaFinal,btnObtenerAvanceVenta;
    private ImageButton btnBack;
    private Spinner spinCategoria,spinFuerza;
    private RadioGroup rbdOpcionesBusqueda;
    private RadioButton rdbVentaGeneral,rdbVentaLinea,rdbVentaLineaSublinea;
    private TextView txtResultado,txtSinDato;
    private RecyclerView recyclerResultadoAvanceVenta;
    private IServiceApi serviceApi;
    private SharedPreferences preferences;
    private List<ReporteAvanceVenta> reporteAvanceVentaList;
    private List<Categoria> categoriaList;
    private AlertDialog dialogCarga;
    private Calendar fechaInicio,fechaFinal;
    private int tipoBusqueda,fuerza;
    private String linea,sublinea;
    private AvanceVentasAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avance_venta);

        serviceApi = Service.getServiceApi();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        reporteAvanceVentaList = new ArrayList<>();
        categoriaList = new ArrayList<>();
        fechaInicio = Calendar.getInstance();
        fechaFinal = Calendar.getInstance();
        tipoBusqueda = VENTA_GENERAL;
        linea="0";
        sublinea="0";
        fuerza = 0;

        setearVista();
        obtenerCategoria();

        btnBack.setOnClickListener(this);
        btnFechaInicio.setOnClickListener(this);
        btnFechaFinal.setOnClickListener(this);
        btnObtenerAvanceVenta.setOnClickListener(this);

        rbdOpcionesBusqueda.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdb_venta_general:
                        estadoVentaGeneral();
                        tipoBusqueda = VENTA_GENERAL;
                        break;
                    case R.id.rdb_venta_linea:
                        estadoVentaLinea();
                        tipoBusqueda = VENTA_LINEA;
                        break;
                    case R.id.rdb_venta_linea_sublinea:
                        tipoBusqueda = VENTA_SUBLINEA;
                        estadoVentaSublinea();
                        break;
                    default:
                        Toast.makeText(AvanceVentaActivity.this, "No existe esa opción", Toast.LENGTH_SHORT).show();
                }
            }
        });

        spinCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0) {
                    if (rdbVentaLinea.isChecked()) {
                        linea = categoriaList.get(position-1).getCodigo().substring(0,2);
                    }else if(rdbVentaLineaSublinea.isChecked()){
                        String categoria = (String)parent.getItemAtPosition(position);
                        Categoria categoriaSeleccionada = null;
                        for(Categoria dato: categoriaList){
                            if(TextUtils.equals(dato.getNombreCategoria(),categoria)){
                                categoriaSeleccionada = dato;
                            }
                        }
                        if(categoriaSeleccionada != null){
                            linea = categoriaSeleccionada.getCodigo().substring(0,2);
                            sublinea = categoriaSeleccionada.getCodigo();
                        }else{
                            Toast.makeText(AvanceVentaActivity.this, "No se encontró la categoria!", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        linea = "0";
                        sublinea = "0";
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinFuerza.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position!= 0){
                    fuerza = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    //Métodos utilizados

    private void setearVista() {
        btnFechaInicio = findViewById(R.id.btn_fecha_inicio);
        btnFechaFinal = findViewById(R.id.btn_fecha_final);
        btnObtenerAvanceVenta = findViewById(R.id.btn_obtener_avance_venta);
        btnBack = findViewById(R.id.btn_back);
        rbdOpcionesBusqueda = findViewById(R.id.rdb_opciones_busqueda);
        rdbVentaGeneral = findViewById(R.id.rdb_venta_general);
        rdbVentaGeneral.setChecked(true);
        rdbVentaLinea = findViewById(R.id.rdb_venta_linea);
        rdbVentaLineaSublinea = findViewById(R.id.rdb_venta_linea_sublinea);
        txtResultado = findViewById(R.id.txt_resultado);
        txtSinDato = findViewById(R.id.txt_sin_datos);
        recyclerResultadoAvanceVenta = findViewById(R.id.recycler_resultado_avance_venta);

        //Seteando spinFuerza
        spinFuerza = findViewById(R.id.spin_fuerza);
        ArrayAdapter adapterFuerza = ArrayAdapter.createFromResource(this,R.array.fuerza,R.layout.spinner_properties);
        adapterFuerza.setDropDownViewResource(R.layout.spinner_properties);
        spinFuerza.setAdapter(adapterFuerza);

        spinCategoria = findViewById(R.id.spin_categoria);

        adapter = new AvanceVentasAdapter();
        recyclerResultadoAvanceVenta.setLayoutManager(new LinearLayoutManager(this));
        recyclerResultadoAvanceVenta.setHasFixedSize(true);
        recyclerResultadoAvanceVenta.setAdapter(adapter);
    }

    private void llenarSpinnerLineaSubLinea() {
        ArrayList<String> datito = new ArrayList<>();
        if(rdbVentaGeneral.isChecked() || rdbVentaLinea.isChecked()) {
            for (Categoria data : categoriaList) {
                datito.add(data.getNombreCategoria());
            }
        }else if(rdbVentaLineaSublinea.isChecked()){
            for (Categoria data : categoriaList) {
                if(data.getCodigo().length() == 4) {
                    datito.add(data.getNombreCategoria());
                }
            }
        }
        datito.add(0,"Seleccione Categoria");

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.spinner_properties,datito);
        adapter.setDropDownViewResource(R.layout.spinner_properties);
        spinCategoria.setAdapter(adapter);
    }

    private void elegirFecha(int tipoFecha) {
        switch (tipoFecha){
            case FECHA_INICIAL:
                obtenerFechaInicial();
                break;
            case FECHA_FINAL:
                obtenerFechaFinal();
                break;
            default:
                Toast.makeText(this, "No existe esa configuración", Toast.LENGTH_SHORT).show();
        }
    }

    private void obtenerFechaInicial() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String fecha = agregarCeroAFecha(dayOfMonth) + "/" + agregarCeroAFecha((month+1)) + "/" + year;
                fechaInicio.set(year,month,dayOfMonth);
                btnFechaInicio.setText(fecha);
            }
        },
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
        );
        datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
        datePickerDialog.show();
    }

    private void obtenerFechaFinal() {
        if (!btnFechaInicio.getText().toString().toLowerCase().equals("fecha inicial")) {
            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String fecha = agregarCeroAFecha(dayOfMonth) + "/" + agregarCeroAFecha((month+1)) + "/" + year;
                            btnFechaFinal.setText(fecha);
                            fechaFinal.set(year,month,dayOfMonth);
                        }
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(Calendar.DATE)
            );
            datePickerDialog.getDatePicker().setMaxDate(Calendar.getInstance().getTimeInMillis());
            datePickerDialog.show();
        } else {
           Util.generarAlerta("Error","Debe seleccionar una fecha de inicio!",this);
        }
    }

    private void obtenerCategoria(){
        if(categoriaList.size() == 0)
            serviceObtenerCategoria();
    }

    private void obtenerAvanceVenta() {
        if(validarFecha() && validarSpinner()) {
            serviceObtenerAvanceVenta();
        }
    }

    private boolean validarSpinner() {
        if(spinFuerza.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Seleccione una fuerza por favor", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(rdbVentaLinea.isChecked() || rdbVentaLineaSublinea.isChecked()){
            if(spinCategoria.getSelectedItemPosition() == 0){
                Toast.makeText(this, "Seleccione una categoría por favor", Toast.LENGTH_SHORT).show();
                return false;
            }
        }
        return true;
    }

    private boolean validarFecha() {
        if(TextUtils.equals(btnFechaInicio.getText().toString(),"Fecha Inicial")){
            Toast.makeText(this, "Falta que agregue fecha inicial", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(TextUtils.equals(btnFechaFinal.getText().toString(),"Fecha Final")){
            Toast.makeText(this, "Falta que agregue fecha final", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String agregarCeroAFecha(int numeroFecha){
        return numeroFecha<10? "0"+numeroFecha: ""+numeroFecha;
    }

    private void llenarAdapter(List<AvanceVenta> result,int idVendedor) {
        AvanceVenta avanceVenta = null;
        for(AvanceVenta dato: result){
            if(dato.getIdVendedor() == idVendedor){
                avanceVenta = dato;
            }
        }

        adapter.setReporteAvanceVentas(mapearReporteAvanceVentas(avanceVenta));
    }

    private ReporteAvanceVenta mapearReporteAvanceVentas(AvanceVenta avanceVenta) {
        String titulo = rdbVentaGeneral.isChecked()?TITULO_VENTA_GENERAL: (rdbVentaLinea.isChecked()? TITULO_VENTA_LINEA : TITULO_VENTA_SUBLINEA);
        return new ReporteAvanceVenta(titulo,fechaInicio,fechaFinal,linea=="0"?"":(String)spinCategoria.getSelectedItem(),avanceVenta);
    }

    //Servicios consumidos
    private void serviceObtenerCategoria(){
        categoriaList.clear();
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Obteniendo  datos").setCancelable(false).build();
        dialogCarga.show();

        serviceApi.obtenerCategoria().enqueue(new Callback<RespuestaCategoria>() {
            @Override
            public void onResponse(Call<RespuestaCategoria> call, Response<RespuestaCategoria> response) {
                try{
                    RespuestaCategoria respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        categoriaList.addAll(respuesta.getResult());
                        dialogCarga.dismiss();
                        llenarSpinnerLineaSubLinea();
                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(AvanceVentaActivity.this, "No se pudo obtener la informacion,intentelo otra vez presionando en la opcion Venta General", Toast.LENGTH_LONG).show();
                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(AvanceVentaActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaCategoria> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(AvanceVentaActivity.this,true);
            }
        });
    }

    private void serviceObtenerAvanceVenta(){
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Obteniendo  datos").setCancelable(false).build();
        dialogCarga.show();
        final int idVendedor = preferences.getInt(Util.COD_VENDEDOR,-1);

        serviceApi.obtenerAvanceVentas(idVendedor,
                fechaInicio.get(Calendar.YEAR) + "" + agregarCeroAFecha((fechaInicio.get(Calendar.MONTH)+1)) + "" + agregarCeroAFecha(fechaInicio.get(Calendar.DATE)),
                fechaFinal.get(Calendar.YEAR) + "" + agregarCeroAFecha((fechaFinal.get(Calendar.MONTH)+1)) + "" + agregarCeroAFecha(fechaFinal.get(Calendar.DATE)),
                tipoBusqueda,linea,sublinea,fuerza).enqueue(new Callback<RespuestaAvanceVentas>() {
            @Override
            public void onResponse(Call<RespuestaAvanceVentas> call, Response<RespuestaAvanceVentas> response) {
                try{
                    RespuestaAvanceVentas respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        llenarAdapter(respuesta.getResult(),idVendedor);
                        estadoBusquedaExitosa();
                        dialogCarga.dismiss();
                    }else{
                        estadoBusquedaFallida();
                        dialogCarga.dismiss();
                        Toast.makeText(AvanceVentaActivity.this, "No se pude obtener el avance de ventas,intentelo otra vez", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    estadoBusquedaFallida();
                    dialogCarga.dismiss();
                    Toast.makeText(AvanceVentaActivity.this, "Excepcion: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaAvanceVentas> call, Throwable t) {
                dialogCarga.dismiss();
                estadoBusquedaFallida();
                Util.mensajeErrorConexion(AvanceVentaActivity.this,false);
            }
        });
    }


    //Estados de pantalla
    private void estadoVentaSublinea(){
        spinCategoria.setVisibility(View.VISIBLE);
    }

    private void estadoVentaLinea(){
        spinCategoria.setVisibility(View.VISIBLE);
    }

    private void estadoVentaGeneral(){
        spinCategoria.setVisibility(View.GONE);
    }

    private void estadoBusquedaExitosa(){
        rdbVentaGeneral.setChecked(true);
        spinCategoria.setSelection(0);
        spinFuerza.setSelection(0);
        btnFechaInicio.setText("Fecha Inicial");
        btnFechaFinal.setText("Fecha Final");
        recyclerResultadoAvanceVenta.setVisibility(View.VISIBLE);
        txtResultado.setVisibility(View.VISIBLE);
        txtSinDato.setVisibility(View.GONE);
    }

    private void estadoBusquedaFallida(){
        txtSinDato.setText("No se pudo encontrar el resultado,intentelo de nuevo");
    }



    //Metodos overrides

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_fecha_inicio:
                elegirFecha(FECHA_INICIAL);
                break;
            case R.id.btn_fecha_final:
                elegirFecha(FECHA_FINAL);
                break;
            case R.id.btn_obtener_avance_venta:
                obtenerAvanceVenta();
                break;
            default:
                Toast.makeText(this, "No existe ese boton", Toast.LENGTH_SHORT).show();
        }
    }



}
