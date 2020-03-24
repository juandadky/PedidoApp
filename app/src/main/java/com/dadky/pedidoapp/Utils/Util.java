package com.dadky.pedidoapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;


public class Util {

    public static final String USUARIO = "usuario";
    public static final String CONTRASEÑA = "contraseña";
    public static final String USUARIO_ACTUAL = "usuarioActual";
    public static final String PANTALLA_ORIGEN = "pantallaOrigen";
    public static final String PANTALLA_PRINCIPAL = "MainActivity";
    public static final String PANTALLA_REGISTRO_PEDIDO="RegistrarPedidoActivity";

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

}
