package com.warsoft.pedidoapp.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.warsoft.pedidoapp.Local.Entities.CabeceraDescuento;
import com.warsoft.pedidoapp.Local.Entities.CabeceraPedido;
import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.Local.Entities.Producto;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Util {

    public static final String USUARIO = "usuario";
    public static final String CONTRASEÑA = "contraseña";
    public static final String ID_USUARIO_LOGIN = "idUsuario";
    public static final String NOMBRE_USUARIO = "nombreUsuario";
    public static final String COD_VENDEDOR = "codVendedor";
    public static final String TIPO_USUARIO = "tipoUsuario";
    public static final int TIPO_PEDIDO = 1;
    public static final int TIPO_DESCUENTO = 2;
    public static final String LOCALHOST = "http://192.168.0.11:5000/";
    public static final String PANTALLA_ORIGEN = "pantallaOrigen";
    public static final String PANTALLA_PRINCIPAL = "MainActivity";
    public static final String PANTALLA_REGISTRO_PEDIDO="RegistrarPedidoActivity";
    public static final String PANTALLA_EDITAR_PEDIDO = "EditarPedidoActivity";
    public static final String CABECERA_PEDIDO = "cabeceraPedido";
    public static final String DETALLE_PEDIDO = "detallePedido";
    public static final String RECORDAR = "recordar";
    public static final String TIPO = "TIPO" ;
    public static final String HORA_PEDIDO = "horaPedido";
    public static final String CREDITO = "credito";
    public static final String EDITAR = "editar";
    public static boolean actualizar = false;

    public static boolean ESTA_ANULADO = false;
    public static final String CLIENTE_SELECCIONADO = "clienteSeleccionado";
    public static final String TIPO_CLIENTE = "tipoCliente";
    public static final String PRODUCTO_SELECCIONADO = "productoSeleccionado";
    public static Cliente clienteSeleccionado = new Cliente();
    public static List<Producto> productosSeleccionados = new ArrayList<>();
    public static List<CabeceraDescuento> descuentosSeleccionados = new ArrayList<>();
    public static List<CabeceraDescuento> detalleDescuentosSeleccionados = new ArrayList<>();
    public static CabeceraPedido cabeceraSeleccionado = new CabeceraPedido();

    public static boolean hostAvailable() {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress("www.google.com", 80), 2000);
            return true;
        } catch (IOException e) {
            // Either we have a timeout or unreachable host or failed DNS lookup
            //System.out.println(e);
            return false;
        }
    }

    public static boolean isConnected(Context context){
        boolean connected = false;
        if(context!= null) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                //we are connected to a network
                connected = true;
            } else
                connected = false;
        }

        return connected;
    }

    public static String setearFecha(String fecha) {
        if(fecha != null && fecha.trim() !="") {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH);
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyy", Locale.ENGLISH);
                LocalDate date = LocalDate.parse(fecha, inputFormatter);
                return outputFormatter.format(date);
            } else {
                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                try {
                    Date date = inputFormat.parse(fecha);
                    return outputFormat.format(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }
        return "";
    }

    public static String textoCapitalizado(String texto) {
        if(texto != null && texto.trim() != "") {
            String[] partes = texto.split(" ");
            String textoNuevo = "";
            for (int i = 0; i < partes.length; i++) {
                String parte = partes[i];
                parte = parte.substring(0, 1).toUpperCase() + parte.substring(1).toLowerCase();
                if (i == partes.length - 1) {
                    textoNuevo += parte;
                } else {
                    textoNuevo += parte + " ";
                }
            }
            return textoNuevo;
        }
        return "";
    }

    public static void mensajeErrorConexion(final Context context, final boolean salir){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage("Se terminó el tiempo de carga, revise su conexión e inténtelo otra vez");
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if(salir){
                    ((Activity) context).finish();
                }
            }
        });
        builder.create();
        builder.show();
    }

    public static void generarAlerta(String titulo,String mensaje,Context context){
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
        builder.setTitle(titulo);
        builder.setMessage(mensaje);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
    }
}
