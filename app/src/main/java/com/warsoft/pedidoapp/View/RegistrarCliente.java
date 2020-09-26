package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Interface.CargaSpinner;
import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.Local.Entities.TipoNegocio;
import com.warsoft.pedidoapp.Local.Entities.TipoPrecio;
import com.warsoft.pedidoapp.Local.Entities.Zona;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaGeneral;
import com.warsoft.pedidoapp.Service.Model.RespuestaTipoNegocio;
import com.warsoft.pedidoapp.Service.Model.RespuestaTipoPrecio;
import com.warsoft.pedidoapp.Service.Model.RespuestaZona;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistrarCliente extends AppCompatActivity implements View.OnClickListener, CargaSpinner {

    private ImageButton btn_back;
    private TextView txt_titulo_bar;
    private EditText txt_nombre_cliente_registro,txt_dniRuc_cliente_registro,txt_direccion_cliente_registro,txt_referencia_registro,
            txt_telefono_cliente_registro,txt_celular_cliente_registro,txt_email_cliente_registro,txt_linea_credito;
    private Spinner spin_tipo_doc_iden,spin_forma_pago,spin_tipo_negocio,spin_visita,spin_zona,spin_tipo_precio,spin_tipo_cliente;
    private Button btn_registrar_cliente;
    private AlertDialog dialogCarga;
    private IServiceApi serviceApi;
    private List<Zona> zonaList;
    private List<TipoNegocio> tipoNegocioList;
    private List<TipoPrecio> tipoPrecioList;
    private int conteoSpinner = 0;
    private int zona,distrito,visita,tipoNegocio;
    private String documentoIdentidad,formaPago,tipoPrecio,tipoCliente;
    private SharedPreferences preferences;
    private boolean isEditar;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_cliente);

        zonaList = new ArrayList<>();
        tipoNegocioList = new ArrayList<>();
        tipoPrecioList = new ArrayList<>();
        if(getIntent().getExtras() != null){
            cliente = (Cliente) getIntent().getSerializableExtra(Util.CLIENTE_SELECCIONADO);
            isEditar = getIntent().getBooleanExtra(Util.EDITAR,false);
        }

        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Cargando datos").setCancelable(false).build();
        dialogCarga.show();

        serviceApi = Service.getServiceApi();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        iniciarVista();
        obtenerZona();
        obtenerTipoNegocio();
        obtenerTipoPrecio();

        btn_back.setOnClickListener(this);
        btn_registrar_cliente.setOnClickListener(this);

        spin_forma_pago.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    formaPago = String.valueOf(position);
                }
                if(position == 2){
                    txt_linea_credito.setVisibility(View.VISIBLE);
                }
                if(position == 1 || position == 0){
                    txt_linea_credito.setText("");
                    txt_linea_credito.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_tipo_negocio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    TipoNegocio tipoNegocioSeleccionada = tipoNegocioList.get(position-1);
                    tipoNegocio = tipoNegocioSeleccionada.getIdTipoNeg();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_zona.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    Zona zonaSeleccionada = zonaList.get(position-1);
                    zona = zonaSeleccionada.getIdZona();
                    distrito = zonaSeleccionada.getIdDistri();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_visita.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    visita = position;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_tipo_precio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    TipoPrecio tipoPrecioSeleccionada = tipoPrecioList.get(position-1);
                    tipoPrecio = String.valueOf(tipoPrecioSeleccionada.getIdPrecio());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_tipo_doc_iden.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    documentoIdentidad = (String)parent.getSelectedItem();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_tipo_cliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position != 0){
                    tipoCliente = String.valueOf(position);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //Metodos usados

    private void iniciarVista() {
        btn_back = findViewById(R.id.btn_back);
        txt_titulo_bar = findViewById(R.id.txt_titulo_bar);
        txt_nombre_cliente_registro = findViewById(R.id.txt_nombre_cliente_registro);
        txt_dniRuc_cliente_registro = findViewById(R.id.txt_dniRuc_cliente_registro);
        txt_direccion_cliente_registro = findViewById(R.id.txt_direccion_cliente_registro);
        txt_referencia_registro = findViewById(R.id.txt_referencia_registro);
        txt_telefono_cliente_registro = findViewById(R.id.txt_telefono_cliente_registro);
        txt_celular_cliente_registro = findViewById(R.id.txt_celular_cliente_registro);
        txt_email_cliente_registro = findViewById(R.id.txt_email_cliente_registro);
        txt_linea_credito = findViewById(R.id.txt_linea_credito);

        //Seteando Spinner tipoDoc
        spin_tipo_doc_iden = findViewById(R.id.spin_tipo_doc_iden);
        ArrayAdapter adapterTipoDocumento = ArrayAdapter.createFromResource(this,R.array.tipoDocumento,R.layout.spinner_properties);
        adapterTipoDocumento.setDropDownViewResource(R.layout.spinner_properties);
        spin_tipo_doc_iden.setAdapter(adapterTipoDocumento);

        //Seteando Spinner formaPago
        spin_forma_pago = findViewById(R.id.spin_forma_pago);
        ArrayAdapter adapterFormaPago = ArrayAdapter.createFromResource(this,R.array.formaPago,R.layout.spinner_properties);
        adapterFormaPago.setDropDownViewResource(R.layout.spinner_properties);
        spin_forma_pago.setAdapter(adapterFormaPago);

        //Seteando Spinner visita
        spin_visita = findViewById(R.id.spin_visita);
        ArrayAdapter adapterVisita = ArrayAdapter.createFromResource(this,R.array.visita,R.layout.spinner_properties);
        adapterVisita.setDropDownViewResource(R.layout.spinner_properties);
        spin_visita.setAdapter(adapterVisita);

        //Seteando Spinner tipo cliente
        spin_tipo_cliente = findViewById(R.id.spin_tipo_cliente);
        ArrayAdapter adapterTipoCliente = ArrayAdapter.createFromResource(this,R.array.tipoCliente,R.layout.spinner_properties);
        adapterTipoCliente.setDropDownViewResource(R.layout.spinner_properties);
        spin_tipo_cliente.setAdapter(adapterTipoCliente);


        spin_tipo_negocio = findViewById(R.id.spin_tipo_negocio);
        spin_tipo_precio = findViewById(R.id.spin_tipo_precio);
        spin_zona = findViewById(R.id.spin_zona);

        btn_registrar_cliente = findViewById(R.id.btn_registrar_cliente);

        //Validar si es editar o no y bloquear vistas
        if(isEditar) {
            txt_titulo_bar.setText("Editar Cliente");
            txt_nombre_cliente_registro.setText(cliente.getNombre());
            txt_nombre_cliente_registro.setEnabled(false);
            txt_dniRuc_cliente_registro.setText(cliente.getRuc());
            txt_dniRuc_cliente_registro.setEnabled(false);
            txt_email_cliente_registro.setText("Información no disponible");
            txt_email_cliente_registro.setEnabled(false);
            txt_linea_credito.setText(""+cliente.getLinCredito());
            txt_linea_credito.setEnabled(false);
            txt_direccion_cliente_registro.setText(cliente.getDireccion());
            txt_referencia_registro.setText(cliente.getReferencia());
            txt_telefono_cliente_registro.setText(cliente.getTelefono1().trim());
            txt_celular_cliente_registro.setText(cliente.getCelular().trim());
            spin_tipo_doc_iden.setEnabled(false);
            spin_forma_pago.setEnabled(false);
            spin_tipo_cliente.setEnabled(false);
            spin_tipo_negocio.setEnabled(false);
            spin_tipo_precio.setEnabled(false);
            spin_zona.setEnabled(false);
            spin_visita.setEnabled(false);
            btn_registrar_cliente.setText("EDITAR");
        }
    }

    private void registrarCliente() {
        if(verificarCamposVacios() && verificarRucDNIValido() && verificarSpinner() && verificarCelularValido() && verificarCorreoValido()){
            //datos
            registrarNuevoCliente(txt_nombre_cliente_registro.getText().toString(),txt_dniRuc_cliente_registro.getText().toString(),
                    txt_direccion_cliente_registro.getText().toString(),txt_referencia_registro.getText().toString(),
                    txt_linea_credito.getText().toString(),txt_celular_cliente_registro.getText().toString(),
                    txt_telefono_cliente_registro.getText().toString(),txt_email_cliente_registro.getText().toString());
        }else{
            Toast.makeText(this, "Faltan datos", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean verificarCamposVacios() {
        if(TextUtils.isEmpty(txt_nombre_cliente_registro.getText().toString().trim())){
            txt_nombre_cliente_registro.setError("Ingrese nombre");
            return false;
        }

        if(TextUtils.isEmpty(txt_dniRuc_cliente_registro.getText().toString().trim())){
            txt_dniRuc_cliente_registro.setError("Ingrese DNI o RUC");
            return false;
        }

        if(TextUtils.isEmpty(txt_direccion_cliente_registro.getText().toString().trim())){
            txt_direccion_cliente_registro.setError("Ingrese dirección");
            return false;
        }

        if(TextUtils.isEmpty(txt_referencia_registro.getText().toString().trim())){
            txt_referencia_registro.setError("Ingrese referencia");
            return false;
        }

        if(spin_forma_pago.getSelectedItemPosition() == 2){
            if(TextUtils.isEmpty(txt_linea_credito.getText().toString())){
                txt_linea_credito.setError("Igrese línea de crédito");
                return false;
            }
        }


        return true;
    }

    private boolean verificarRucDNIValido() {
        if(txt_dniRuc_cliente_registro.getText().toString().length() == 8 || txt_dniRuc_cliente_registro.getText().toString().length() == 11){
            return true;
        }else{
            txt_dniRuc_cliente_registro.setError("Ingrese un DNI o RUC válido");
            return false;
        }
    }

    private boolean verificarCorreoValido() {
        if(!TextUtils.isEmpty(txt_email_cliente_registro.getText().toString())) {
            if (!Patterns.EMAIL_ADDRESS.matcher(txt_email_cliente_registro.getText().toString()).matches()) {
                txt_email_cliente_registro.setError("El correo no es válido");
                return false;
            }
        }
        return true;
    }

    private boolean verificarSpinner() {
        if(spin_tipo_doc_iden.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Selecciona un tipo de documento de identidad", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spin_forma_pago.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Selecciona la forma de pago", Toast.LENGTH_SHORT).show();
        }

        if(spin_tipo_negocio.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Selecciona el tipo de negocio", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spin_visita.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Selecciona la visita", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spin_zona.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Selecciona la zona a la que pertenece", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(spin_tipo_precio.getSelectedItemPosition() == 0){
            Toast.makeText(this, "Selecciona el tipo de precio", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private boolean verificarCelularValido() {
        if(!TextUtils.isEmpty(txt_celular_cliente_registro.getText().toString())) {
            if (!txt_celular_cliente_registro.getText().toString().startsWith("9") && txt_celular_cliente_registro.getText().toString().length() == 9) {
                txt_celular_cliente_registro.setError("Ingresar número de celular válido");
                return false;
            }
        }
        return true;
    }

    private void llenarSpinnerZona() {
        ArrayList<String> datito = new ArrayList<>();
        for(Zona data: zonaList){
            datito.add(data.getDetalle());
        }
        datito.add(0,"Seleccione Zona");

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.spinner_properties,datito);
        adapter.setDropDownViewResource(R.layout.spinner_properties);
        spin_zona.setAdapter(adapter);
        terminarCarga();
    }

    private void llenarSpinnerTipoNegocio() {
        ArrayList<String> datito = new ArrayList<>();
        for(TipoNegocio data: tipoNegocioList){
            datito.add(data.getDetalle());
        }
        datito.add(0,"Seleccione Tipo de negocio");

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.spinner_properties,datito);
        adapter.setDropDownViewResource(R.layout.spinner_properties);
        spin_tipo_negocio.setAdapter(adapter);
        terminarCarga();
    }

    private void llenarSpinnerTipoPrecio() {
        ArrayList<String> datito = new ArrayList<>();
        for(TipoPrecio data: tipoPrecioList){
            datito.add(data.getNombrePrecio());
        }
        datito.add(0,"Seleccione Tipo de precio");

        ArrayAdapter adapter = new ArrayAdapter(this,R.layout.spinner_properties,datito);
        adapter.setDropDownViewResource(R.layout.spinner_properties);
        spin_tipo_precio.setAdapter(adapter);
        terminarCarga();
    }

    //Peticiones a Backend
    private void obtenerZona(){
        serviceApi.obtenerZona().enqueue(new Callback<RespuestaZona>() {
            @Override
            public void onResponse(Call<RespuestaZona> call, Response<RespuestaZona> response) {
                try{
                    RespuestaZona respuesta = response.body();
                    if(respuesta.getCode() == 100){
                        contarCarga();
                        zonaList.addAll(respuesta.getResult());
                        llenarSpinnerZona();
                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(RegistrarCliente.this, "No se pudo obtener las zonas,vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                    }

                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(RegistrarCliente.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<RespuestaZona> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(RegistrarCliente.this,true);
            }
        });
    }

    private void obtenerTipoNegocio(){
        serviceApi.obtenerTipoNegocio().enqueue(new Callback<RespuestaTipoNegocio>() {
            @Override
            public void onResponse(Call<RespuestaTipoNegocio> call, Response<RespuestaTipoNegocio> response) {
                try{
                    RespuestaTipoNegocio respuesta = response.body();
                    if (respuesta.getCode() == 100){
                        contarCarga();
                        tipoNegocioList.addAll(respuesta.getResult());
                        llenarSpinnerTipoNegocio();
                    }else{
                        dialogCarga.dismiss();
                        Toast.makeText(RegistrarCliente.this, "No se pudo obtener los tipos de Negocio, vuelva a intentarlo", Toast.LENGTH_SHORT).show();

                    }
                }catch (Exception e){
                    dialogCarga.dismiss();
                    Toast.makeText(RegistrarCliente.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RespuestaTipoNegocio> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(RegistrarCliente.this,true);
            }
        });
    }

    private void obtenerTipoPrecio(){
        serviceApi.obtenerTipoPrecio().enqueue(new Callback<RespuestaTipoPrecio>() {
            @Override
            public void onResponse(Call<RespuestaTipoPrecio> call, Response<RespuestaTipoPrecio> response) {
                try {
                    RespuestaTipoPrecio respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        contarCarga();
                        tipoPrecioList.addAll(respuesta.getResult());
                        llenarSpinnerTipoPrecio();
                    } else {
                        Toast.makeText(RegistrarCliente.this, "No se pudo obtener los tipos de Precio, vuelva a intentarlo", Toast.LENGTH_SHORT).show();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(RegistrarCliente.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaTipoPrecio> call, Throwable t) {
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(RegistrarCliente.this,true);
            }
        });
    }


    private void registrarNuevoCliente(String nombre,String dniRuc,String direccion,String referencia,String lineaCredito,String celular,String telefono,String email) {
        int idVendedor = preferences.getInt(Util.COD_VENDEDOR,-1);
        dialogCarga = new SpotsDialog.Builder().setContext(this).setMessage("Registrando nuevo cliente").setCancelable(false).build();
        dialogCarga.show();
        double lCredito = 0;
        if(TextUtils.equals(formaPago,"2")){
            lCredito = Double.parseDouble(lineaCredito);
        }

        //TODO definir cada paramentro del servicio
        serviceApi.registrarCliente(nombre,direccion,documentoIdentidad,dniRuc,telefono,formaPago,zona,tipoNegocio,referencia,"",
                tipoCliente, celular,visita,lCredito,"",distrito,idVendedor,2,
                idVendedor,zona,visita,lCredito,"00",email,tipoPrecio,false).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try {
                    RespuestaGeneral respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        dialogCarga.dismiss();
                        Toast.makeText(RegistrarCliente.this, "Se registro el cliente", Toast.LENGTH_SHORT).show();
                        registroClienteExitoso();
                    } else {
                        Toast.makeText(RegistrarCliente.this, "No se pudo registrar el cliente,volver a intentarlo", Toast.LENGTH_LONG).show();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Toast.makeText(RegistrarCliente.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                Log.e("RegistrarCliente:", t.getMessage());
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(RegistrarCliente.this,false);
            }
        });
    }

    private void editarCliente(){
        if(TextUtils.isEmpty(txt_direccion_cliente_registro.getText().toString().trim())){
            txt_direccion_cliente_registro.setError("Ingrese dirección");
            return;
        }

        if(TextUtils.isEmpty(txt_referencia_registro.getText().toString().trim())){
            txt_referencia_registro.setError("Ingrese referencia");
            return;
        }
        String telefono =txt_telefono_cliente_registro.getText().toString();
        String celular = txt_celular_cliente_registro.getText().toString();
        serviceApi.actualizarCliente(cliente.getIdCliente(),txt_direccion_cliente_registro.getText().toString(),txt_referencia_registro.getText().toString(),
                TextUtils.equals(telefono.trim(),"") ? "Nodata" : telefono,TextUtils.equals(celular.trim(),"") ? "Nodata" : celular).enqueue(new Callback<RespuestaGeneral>() {
            @Override
            public void onResponse(Call<RespuestaGeneral> call, Response<RespuestaGeneral> response) {
                try {
                    RespuestaGeneral respuesta = response.body();
                    if (respuesta.getCode() == 100) {
                        dialogCarga.dismiss();
                        Toast.makeText(RegistrarCliente.this, "Se editó el cliente", Toast.LENGTH_SHORT).show();
                        Util.actualizar = true;
                        finish();
                    } else {
                        Toast.makeText(RegistrarCliente.this, "No se pudo editar el cliente,volver a intentarlo", Toast.LENGTH_LONG).show();
                        dialogCarga.dismiss();
                    }
                }catch (Exception e){
                    Log.e("EditarCliente",e.getMessage());
                    Toast.makeText(RegistrarCliente.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialogCarga.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaGeneral> call, Throwable t) {
                Log.e("EditarCliente:", t.getMessage());
                dialogCarga.dismiss();
                Util.mensajeErrorConexion(RegistrarCliente.this,false);
            }
        });
    }


    //Estados de pantalla

    private void registroClienteExitoso(){
        txt_nombre_cliente_registro.setText("");
        txt_celular_cliente_registro.setText("");
        txt_direccion_cliente_registro.setText("");
        txt_dniRuc_cliente_registro.setText("");
        txt_email_cliente_registro.setText("");
        txt_referencia_registro.setText("");
        txt_telefono_cliente_registro.setText("");
        spin_tipo_doc_iden.setSelection(0);
        spin_forma_pago.setSelection(0);
        spin_tipo_cliente.setSelection(0);
        spin_tipo_precio.setSelection(0);
        spin_zona.setSelection(0);
        spin_tipo_precio.setSelection(0);
        spin_visita.setSelection(0);
    }



    //Overrides

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_back:
                onBackPressed();
                break;
            case R.id.btn_registrar_cliente:
                if(isEditar){
                    editarCliente();
                }else {
                    registrarCliente();
                }
                break;
            default:
                Toast.makeText(this, "No existe este control en la interfaz,revisar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void contarCarga() {
        conteoSpinner++;
    }

    @Override
    public void terminarCarga() {
        if(conteoSpinner == 3){
            dialogCarga.dismiss();
        }
    }
}
