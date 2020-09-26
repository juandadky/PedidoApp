package com.warsoft.pedidoapp.Utils;

import android.content.Context;
import android.os.AsyncTask;

public class Conexion extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    public Conexion(Context context){
        this.context = context;
    }
    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        if (Util.isConnected(context)) {
            return Util.hostAvailable();
        }
        return false;
    }

    @Override
    protected void onPostExecute(Boolean hasInternet) {
        if (hasInternet) {
            //Toast.makeText(getActivity(), "Iniciando Request!", Toast.LENGTH_SHORT).show();
        } else {
            //Toast.makeText(getActivity(), "No tienes conexion", Toast.LENGTH_SHORT).show();
        }
    }
}
