package com.warsoft.pedidoapp.View;

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
import android.widget.Toast;

import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Utils.Util;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView card_historial_pedido,card_registrar_pedido,card_buscar_cliente,card_buscar_producto,card_avance_venta,card_logout;
    private TextView txt_saludo;
    private SharedPreferences preferences;
    private Date horaPedido;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        cargarVista();

        if(preferences.contains(Util.NOMBRE_USUARIO)){
            String nombres = preferences.getString(Util.NOMBRE_USUARIO,"");
            nombres = Util.textoCapitalizado(nombres);
            String[] nombre = nombres.split(" ");
            txt_saludo.setText("Bienvenido, " + nombre[0] + "!");
        }



        card_historial_pedido.setOnClickListener(this);
        card_registrar_pedido.setOnClickListener(this);
        card_buscar_cliente.setOnClickListener(this);
        card_buscar_producto.setOnClickListener(this);
        card_avance_venta.setOnClickListener(this);
        card_logout.setOnClickListener(this);

    }

    private void cargarVista() {
        card_historial_pedido = findViewById(R.id.card_historial_pedido);
        card_registrar_pedido = findViewById(R.id.card_registrar_pedido);
        card_buscar_cliente = findViewById(R.id.card_buscar_cliente);
        card_buscar_producto = findViewById(R.id.card_buscar_producto);
        card_avance_venta = findViewById(R.id.card_avance_venta);
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
                Util.clienteSeleccionado = new Cliente();
                Util.productosSeleccionados = new ArrayList<>();
                Util.descuentosSeleccionados = new ArrayList<>();
                Util.detalleDescuentosSeleccionados = new ArrayList<>();
                Util.cabeceraSeleccionado = new CabeceraPedido();
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
                String hora = preferences.getString(Util.HORA_PEDIDO,"");
                if(hora != "") {
                    String hour = hora.substring(11, 13);
                    String minute = hora.substring(14, 16);

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                        LocalTime open = LocalTime.of(6, 00);
                        LocalTime closed = LocalTime.of(Integer.parseInt(hour), Integer.parseInt(minute));
//                        LocalTime open = LocalTime.of(0, 00);
//                        LocalTime closed = LocalTime.of(23, 59);

                        LocalTime currentTime = LocalTime.now();
                        if (currentTime.isBefore(open) || currentTime.isAfter(closed)) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setTitle("Error");
                            builder.setMessage("No se encuentra en el horario disponible para realizar pedidos");
                            builder.setPositiveButton("Ok", null);
                            builder.create();
                            builder.show();
                        } else {
                            irActividad(RegistrarPedidoActivity.class);
                        }
                    } else {
                        irActividad(RegistrarPedidoActivity.class);
                    }
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Error");
                    builder.setMessage("No se guardaron los datos del usuario correctamente,por favor salir de session y volver a iniciarlo,gracias");
                    builder.setPositiveButton("Ok",null);
                    builder.create();
                    builder.show();
                }
                break;
            case R.id.card_buscar_cliente:
                irActividad(BuscarClienteActivity.class);
                break;
            case R.id.card_avance_venta:
                irActividad(AvanceVentaActivity.class);
                break;
            case R.id.card_buscar_producto:
                irActividad(BuscarProductoActivity.class);
                break;
            default:
                Toast.makeText(this, "Error,no existe esa pantalla", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Salir de la aplicacion");
        if(Util.clienteSeleccionado != null || Util.productosSeleccionados.size()>0) {
            builder.setMessage("¿Desea salir de la aplicación?,Tiene un pedido sin terminar,se perdera todo");

        }else{
            builder.setMessage("¿Desea salir de la aplicación?");
        }
        builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Util.clienteSeleccionado = null;
                Util.productosSeleccionados = null;
                finish();
            }
        });
        builder.setNegativeButton("No", null);
        builder.create();
        builder.show();
    }
}
