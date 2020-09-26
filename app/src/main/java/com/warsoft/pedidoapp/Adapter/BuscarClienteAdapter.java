package com.warsoft.pedidoapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.warsoft.pedidoapp.Local.Entities.Cliente;
import com.warsoft.pedidoapp.R;
import com.warsoft.pedidoapp.Utils.Util;

import java.util.List;

public class BuscarClienteAdapter extends RecyclerView.Adapter<BuscarClienteAdapter.ViewHolder> {

    private List<Cliente> clientes;
    private Context context;
    private OnItemClickListener listener;
    private String pantalla;

    public BuscarClienteAdapter(Context context, List<Cliente> clientes, String pantalla, OnItemClickListener listener) {
        this.clientes = clientes;
        this.context = context;
        this.pantalla = pantalla;
        this.listener = listener;
    }


    @Override
    public BuscarClienteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_buscar_cliente, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuscarClienteAdapter.ViewHolder viewHolder, final int position) {
        try {
            final Cliente cliente = clientes.get(position);
            viewHolder.txt_nombre_cliente.setText(cliente.getNombre());
            viewHolder.txt_tipo_cliente.setText("Cliente Tipo " + cliente.getTipoCliente());
            if (listener != null) {
                if (TextUtils.equals(Util.PANTALLA_REGISTRO_PEDIDO, pantalla)) {
                    viewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(cliente, position);
                        }
                    });
                }
                if (TextUtils.equals(Util.PANTALLA_PRINCIPAL, pantalla)) {
                    viewHolder.btn_ver_mas.setVisibility(View.VISIBLE);
                    viewHolder.btn_ver_mas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onItemClick(cliente, position);
                        }
                    });
                    viewHolder.btn_editar_cliente.setVisibility(View.VISIBLE);
                    viewHolder.btn_editar_cliente.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listener.onEditClick(cliente,position);
                        }
                    });
                }
            }
        } catch (Exception e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e("Error", "onBindViewHolder: " + e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return clientes.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView txt_nombre_cliente;
        private TextView txt_tipo_cliente;
        private Button btn_ver_mas;
        private ImageButton btn_editar_cliente;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview);
            txt_nombre_cliente = itemView.findViewById(R.id.txt_nombre_cliente);
            txt_tipo_cliente = itemView.findViewById(R.id.txt_tipo_cliente);
            btn_ver_mas = itemView.findViewById(R.id.btn_ver_mas);
            btn_editar_cliente = itemView.findViewById(R.id.btn_editar_cliente);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Cliente cliente, int position);
        void onEditClick(Cliente cliente, int position);
    }
}

