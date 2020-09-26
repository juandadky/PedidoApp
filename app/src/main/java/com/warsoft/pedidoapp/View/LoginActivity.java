package com.warsoft.pedidoapp.View;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.warsoft.pedidoapp.Local.Entities.Usuario;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Service.IServiceApi;
import com.warsoft.pedidoapp.Service.Model.RespuestaLogin;
import com.warsoft.pedidoapp.Service.Service;
import com.warsoft.pedidoapp.Utils.Conexion;
import com.warsoft.pedidoapp.Utils.Util;

import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText txt_usuario,txt_contraseña;
    private CheckBox cbx_recordar;
    private Button btn_login;
    private SharedPreferences preferences;
    private IServiceApi serviceApi;
    private Usuario vendedor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        serviceApi = Service.getServiceApi();
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        cargarVista();


        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

    }


    private void cargarVista() {
        txt_usuario = findViewById(R.id.txt_usuario);
        txt_contraseña = findViewById(R.id.txt_contraseña);
        cbx_recordar = findViewById(R.id.cbx_recordar);
        btn_login = findViewById(R.id.btn_login);
    }

    private Boolean comprobarDatos(String usuario, String contraseña){
        if(TextUtils.isEmpty(usuario.trim()) && TextUtils.isEmpty(contraseña.trim())){
            return false;
        }
        return true;
    }

    private void iniciarSesion() {
        String usuario = txt_usuario.getText().toString();
        String contraseña = txt_contraseña.getText().toString();
        Conexion conexion = new Conexion(LoginActivity.this);
        final AlertDialog dialog = new SpotsDialog.Builder().setContext(this).setMessage("Cargando...").setCancelable(false).build();
        dialog.show();

        try {
            if (comprobarDatos(usuario, contraseña)) {
                if (conexion.execute().get()) {
                    enviarPeticionLogin(usuario,contraseña,dialog);
                } else {
                    Toast.makeText(this, "No tienes conexión a internet,verifica tu conexión", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            } else {
                Toast.makeText(this, "Verifica que tus campos esten con datos", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }catch (Exception e){
            Log.e("Errorcomprobarinternet",e.getMessage().toString());
            dialog.dismiss();
        }
    }

    private void enviarPeticionLogin(final String usuario, final String contraseña, final AlertDialog dialog) {
        serviceApi.login(usuario,contraseña).enqueue(new Callback<RespuestaLogin>() {
            @Override
            public void onResponse(Call<RespuestaLogin> call, Response<RespuestaLogin> response) {
                try{
                    RespuestaLogin data = response.body();
                    if(data != null){
                        if(data.getCode() == 100){
                            vendedor = data.getResult().get(0);
                            guardarDatos(vendedor);
                            irMenuPrincipal();
                            dialog.dismiss();
                        }else if(data.getCode() == 200 || data.getCode() == 400) {
                            Toast.makeText(LoginActivity.this, data.getMessage(), Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }else{
                        Toast.makeText(LoginActivity.this, "No llegan datos!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }catch (Exception e){
                    Log.e("enviarPeticionLogin",e.getMessage());
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<RespuestaLogin> call, Throwable t) {
                Util.mensajeErrorConexion(LoginActivity.this,false);
                dialog.dismiss();
            }
        });
    }



    private void irMenuPrincipal(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void guardarDatos(Usuario usuario) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Util.RECORDAR,cbx_recordar.isChecked()?1:0);
        editor.putString(Util.USUARIO,usuario.getUsuario());
        editor.putString(Util.CONTRASEÑA,usuario.getClave());
        editor.putInt(Util.ID_USUARIO_LOGIN,usuario.getId());
        editor.putString(Util.NOMBRE_USUARIO,usuario.getNombre());
        editor.putInt(Util.COD_VENDEDOR, usuario.getCodVendedor());
        editor.putString(Util.TIPO_USUARIO, usuario.getTipo());
        editor.putString(Util.HORA_PEDIDO, usuario.getHorapedido());
        editor.putBoolean(Util.CREDITO, usuario.isCredito());
        editor.commit();
    }


}
