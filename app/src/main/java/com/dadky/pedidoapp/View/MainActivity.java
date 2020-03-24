package com.dadky.pedidoapp.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.TextView;

import com.dadky.pedidoapp.Local.Usuario;
import com.dadky.pedidoapp.R;
import com.dadky.pedidoapp.Utils.Util;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card_historial_pedido,card_registrar_pedido,card_buscar_cliente,card_buscar_producto,card_logout;
    private TextView txt_saludo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cargarVista();

        if(getIntent().getExtras() != null){
            Usuario usuario = (Usuario) getIntent().getSerializableExtra(Util.USUARIO_ACTUAL);
            String[] cadena = usuario.getNombre().split(" ");
            String nombre = cadena[0].substring(0,1).toUpperCase() + cadena[0].substring(1).toLowerCase();
            txt_saludo.setText("Bienvenido, " + nombre);
        }

        card_historial_pedido.setOnClickListener(this);
        card_registrar_pedido.setOnClickListener(this);
        card_buscar_cliente.setOnClickListener(this);
        card_buscar_producto.setOnClickListener(this);
        card_logout.setOnClickListener(this);

    }

    private void cargarVista() {
        card_historial_pedido = findViewById(R.id.card_historial_pedido);
        card_registrar_pedido = findViewById(R.id.card_registrar_pedido);
        card_buscar_cliente = findViewById(R.id.card_buscar_cliente);
        card_buscar_producto = findViewById(R.id.card_buscar_producto);
        card_logout = findViewById(R.id.card_logout);
        txt_saludo = findViewById(R.id.txt_saludo);
    }

    private void logout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar Sesión");
        builder.setMessage("¿Desea cerrar sesión?");
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No",null);
        builder.create();
        builder.show();
    }

    private void irActividad(Class actividad){
        Intent intent = new Intent(this,actividad);
        intent.putExtra(Util.PANTALLA_ORIGEN,Util.PANTALLA_PRINCIPAL);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.card_logout:
                logout();
                break;
            case R.id.card_historial_pedido:
                irActividad(HistorialPedidoActivity.class);
                break;
            case R.id.card_registrar_pedido:
                irActividad(RegistrarPedidoActivity.class);
                break;
            case R.id.card_buscar_cliente:
                irActividad(BuscarClienteActivity.class);
                break;
            case R.id.card_buscar_producto:
                irActividad(BuscarProductoActivity.class);
                break;
        }

    }



}
